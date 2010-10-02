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
 * along with GeekEvent.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://touilleur-express.fr/ for more details.
 */

package models;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPASupport;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * This entity is the real person that is registered for events.
 *
 * @author Nicolas Martignole
 * @since 9 sept. 2010 20:18:12
 */
@Entity
public class JugUser extends JPASupport {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String id;

    @Required
    @MaxSize(value = 255, message = "L'adresse email est trop longue")
    @Email
    public String email;

    @MaxSize(value = 255, message = "Le prénom est trop long")
    public String firstName;

    @MaxSize(value = 255, message = "Le nom de famille est trop long")
    public String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    public Date creationDate;

    public Boolean emailConfirmed;

    public String password;

    // Events I'd like to participate to
    @ManyToMany
    public Set<JugEvent> attendeesEvent;

    // Events I created
    @OneToMany(mappedBy = "eventOrganizer")
    public Set<JugEvent> myEvents;

    /**
     * Creates a new user. I prefer to generate a random password instead of asking the user to provide
     * its password. Thus I can store it as plain text.
     *
     * @param email     is the user email.
     * @param firstName is the first name
     * @param lastName  is the last name
     */
    public JugUser(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = RandomStringUtils.randomAlphanumeric(8);
        this.creationDate = new Date();
        this.emailConfirmed = false;
    }

    /**
     * Look-up user by email.
     *
     * @param userEmail is the email we're looking for.
     * @return the JugUser or null if it was not found.
     */
    public static JugUser findByEmail(final String userEmail) {
        if (userEmail == null) {
            return null;
        }
        if (userEmail.trim().equals("")) {
            return null;
        }
        return JugUser.find("from JugUser ju where email=:pemail").bind("pemail", userEmail).first();
    }

    /**
     * Performs the authentication part.
     *
     * @param email    to authenticate the user.
     * @param password a password he provided
     * @return either the JugUser if the tuple was valid, else null.
     */
    public static JugUser connect(final String email, final String password) {
        if (password == null) {
            return null;
        }
        if (email == null) {
            return null;
        }

        return JugUser.find("from JugUser ju where email=:p1 and password=:p2")
                .bind("p1", email)
                .bind("p2", password).first();

    }


    /**
     * An utility method to reset the user password.
     */
    public void changePassword() {
        this.password = RandomStringUtils.randomAlphanumeric(8);
        this.save();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JugUser jugUser = (JugUser) o;

        if (!email.equals(jugUser.email)) return false;
        if (id != null ? !id.equals(jugUser.id) : jugUser.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName ;
    }
}
