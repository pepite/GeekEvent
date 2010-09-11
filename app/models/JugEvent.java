/*
 * Copyright (c) 2010 Nicolas MARTIGNOLE
 *
 *     This file is part of GeekEvent.
 *
 *     GeekEvent is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     See http://touilleur-express.fr/ for more details.
 */

package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * A Java User Group Event.
 *
 * @author Nicolas Martignole
 * @since 9 sept. 2010 19:19:50
 */
@Entity
public class JugEvent extends Model {

    public String title;

    public String abstractContent;

    public String description;

    @Temporal(TemporalType.TIMESTAMP)
    public Date date;

    public Integer totalSlots;


    public void bookCurrentUser() {
        if(totalSlots==0){
            return;
        }
        totalSlots--;
        this.save();
    }
}
