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

import org.hibernate.validator.NotNull;
import play.data.validation.Email;
import play.db.jpa.JPASupport;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * This entity is the real person that is registered for events.
 *
 * @author Nicolas Martignole
 * @since 9 sept. 2010 20:18:12
 */
@Entity
public class JugUser extends JPASupport {

    @Id
    @NotNull
    @Email
    public String email;

    public String firstName;

    public String lastName;

    public Date registedOn;

    public Boolean confirmed;

    public String password;

    public static JugUser findByEmail(String userEmail) {
        if (userEmail == null) {
            return null;
        }
        if (userEmail.trim().equals("")) {
            return null;
        }
        return JugUser.find("from JugUser ju where userEmail=:pemail").bind("pemail", userEmail).first();
    }

    public static JugUser authenticate(String email, String password) {
        if(password==null){return null;}
        if(email==null){return null;}

        JugUser user = findByEmail(email);
        if (user.password.equals(password)) {
            return user;
        }
        return null;
    }

}
