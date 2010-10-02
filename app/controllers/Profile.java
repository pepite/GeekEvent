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
 */

package controllers;

import models.JugUser;
import org.apache.commons.codec.digest.DigestUtils;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * A simple controller to manage JugUser
 *
 * @author Nicolas Martignole
 * @since 2 oct. 2010 14:11:26
 */
public class Profile extends Controller {

    @Before()
    static void checkLogin() {
        if (!session.contains("user")) {
            Application.login();
        }
    }

    public static void index() {
        JugUser jugUser = Application.currentUser();
        render(jugUser);
    }


    public static void gravatar(String email) {
        if (email == null) {
            flash.error("Email cannot be null");
            index();
            return;
        }
        String hashtext=DigestUtils.md5Hex(email);


        render(hashtext);
    }


}
