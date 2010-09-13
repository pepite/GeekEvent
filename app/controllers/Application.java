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

package controllers;

import models.JugEvent;
import models.JugUser;
import play.data.validation.Required;
import play.mvc.*;

import java.util.List;


/**
 * Main controller, generates the home page.
 *
 * @author Nicolas Martignole
 */
public class Application extends Controller {

    @Before(unless = {"login", "logout", "authenticate", "index"})
    static void checkLogin() {
        if (!session.contains("user")) {
            login();
        }
    }

    public static void login() {
        render();
    }

    public static void logout() {
        session.clear();
        login();
    }


    public static void index() {
        List<JugEvent> jugEvents = JugEvent.findAll();
        String userId=session.get("user-id");

        if(userId==null){
            // the user is not yet authenticated
            render(jugEvents);
            return;
        }

        JugUser currentUser=JugUser.findById(userId);

        render(jugEvents,currentUser);
    }


    public static void login(String email, String password) {
        render();
    }

    /**
     * Authenticate the user on login and password.
     *
     * @param email    received from the view
     * @param password received from the view
     */
    public static void authenticate(String email, String password) {
        validation.required(email);
        validation.email(email);
        validation.required(password);
        if (validation.hasErrors()) {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            login();
        }

        JugUser user = JugUser.connect(email, password);
        if (user != null) {
            session.put("user-f", user.firstName);
            session.put("user", user.email);
            session.put("user-id", user.id);
            flash.success("Vous êtes authentifié !");
            index();
        }
        params.flash();
        flash.error("Email ou mot de passe incorrect. Merci d'essayer à nouveau.");
        login();
    }

    public static void bookThisEvent(Long jugEventId) {
           JugEvent jugEvent = JugEvent.findById(jugEventId);
           notFoundIfNull(jugEvent);

           String userId = session.get("user-id");

           if (userId == null) {
               // the user is not yet authenticated
               redirect("Application/login");
               return;
           }

           // Should add the user to the attendees list
           // or put the user on waiting list
           String result=jugEvent.book(userId);

           flash.success(result);

           index();

       }

       public static void unregisterThisEvent(Long jugEventId){
           System.out.println("Unregister :"+jugEventId);
            JugEvent jugEvent = JugEvent.findById(jugEventId);
           notFoundIfNull(jugEvent);
           System.out.println("Unregister on event found");

           String userId = session.get("user-id");

           if (userId == null) {
               // the user is not yet authenticated
               redirect("Application/login");
               return;
           }

           // Should add the user to the attendees list
           // or put the user on waiting list
           String result=jugEvent.unbook(userId);

           flash.success(result);

           render();
       }
    

}