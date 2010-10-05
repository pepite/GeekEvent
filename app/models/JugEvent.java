/*
 * Copyright (c) 2010 Nicolas MARTIGNOLE
 *
 * This file is part of GeekEvent.
 *
 * GeekEvent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GeekEvent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with GeekEvent.  If not, see http://www.gnu.org/licenses/.
 *
 * See http://touilleur-express.fr/ for more details.
 */

package models;

import org.hibernate.annotations.GenericGenerator;
import play.data.validation.*;
import play.db.jpa.JPASupport;
import play.db.jpa.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A Java User Group Event.
 *
 * @author Nicolas Martignole
 * @since 9 sept. 2010 19:19:50 in a train to la Rochelle for Jug summer camp
 */
@Entity
public class JugEvent extends Model {
    @Required
    @MaxSize(value = 255)
    public String title;

    @MaxSize(value = 255)
    public String abstractContent;

    @Lob
    @Required
    @MaxSize(10000)
    public String description;

    @Required
    @Temporal(TemporalType.TIMESTAMP)
    @InFuture
    public Date date;

    @Required
    @Min(1)
    public Integer totalSlots;

    @OneToOne
    public JugUser createdBy;

    @ManyToMany(mappedBy = "attendeesEvent")
    public Set<JugUser> participants;

    @ManyToOne
    public JugUser eventOrganizer;

    /**
     * Holds all the logic for booking, this method will try to put the user
     * on the attendees list, or on the waiting list and will return a status string.
     *
     * @param userId is the user to book
     * @return a simple status, cause I don't want business exception here
     */
    public String book(String userId) {
        if (userId == null) {
            return Messages.get("book.error1");
        }

        JugUser user = JugUser.findById(userId);
        if (user == null) {
            return Messages.get("book.error2");
        }


        if (participants.size() < totalSlots) {
            user.attendeesEvent.add(this);
            participants.add(user);
            user.save();
            save();

            return Messages.get("book.success");
        } else {
            // here we should put the user on an orderd list to handle "waiting" status
            return Messages.get("book.waiting");
        }

    }

    /**
     * Removes the specified user from this event, this method also updates the collection in user.
     *
     * @param userId is the user to remove.
     * @return a status string.
     */
    public String unbook(String userId) {
        if (userId == null) {
            return Messages.get("book.error1");
        }

        JugUser user = JugUser.findById(userId);
        if (user == null) {
            return Messages.get("book.error2");
        }

        user.attendeesEvent.remove(this);
        participants.remove(user);
        user.save();
        save();

        return Messages.get("unbook.success");
    }

    /**
     * Returns the number of free slots for this event.
     * @return the number of free slots.
     */
    @Transient
    public Integer getFreeSlots() {
        if (participants == null) {
            return 0;
        }
        return totalSlots - participants.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JugEvent jugEvent = (JugEvent) o;

        if (id != null ? !id.equals(jugEvent.id) : jugEvent.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JugEvent{" +
                "title='" + title + '\'' +
                ", date=" + date +
                '}';
    }

    public void setCreatedBy(JugUser currentUser) {
        this.createdBy=currentUser;
        this.eventOrganizer=currentUser;
        save();
    }
}
