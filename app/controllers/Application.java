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

package controllers;

import models.JugEvent;
import models.JugUser;
import play.mvc.*;

import java.util.List;


/**
 * Main controller, generates the home page.
 *
 * @author Nicolas Martignole
 */
public class Application extends Controller {

    public static void index() {
        List<JugEvent> jugEvents = JugEvent.findAll();

        String userEmail = Scope.Session.current().get("user");
        JugUser user = JugUser.findByEmail(userEmail);

        if (user != null) {
            render(jugEvents, user);
        } else {

            render(jugEvents);
        }

    }

    public static void bookEvent(Long id) {
        JugEvent jugEvent = JugEvent.findById(id);
        notFoundIfNull(jugEvent);


        String userEmail = Scope.Session.current().get("user");
        JugUser user = JugUser.findByEmail(userEmail);

        if (user != null) {
            jugEvent.bookCurrentUser();
            flash.success("Votre demande a été acceptée.");

        } else {
            flash.success("Merci de vous authentifier");
        }

        index();
    }

    public static void register(String firsName, String lastName, String email) {
        index();
    }

    public static void login(String email, String password) {
        JugUser user = JugUser.authenticate(email, password);
        if (user == null) {
            flash.error("Erreur: mot de passe non valide ou comtpe inexistant");
            index();
            return;
        }
        Scope.Session.current().put("user", user.email);
        flash.success("Vous êtes authentifié !");
    }

}