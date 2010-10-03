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
import play.data.validation.Valid;
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

    /**
     * Performs the authentication.
     */
    public static void login() {
        render();
    }

    /**
     * Clears the session, delete the encrypted cookie.
     */
    public static void logout() {
        session.clear();
        login();
    }


    /**
     * Main page
     */
    public static void index() {
        List<JugEvent> jugEvents = JugEvent.findAll();
        String userId = session.get("user-id");

        if (userId == null) {
            // the user is not yet authenticated
            render(jugEvents);
            return;
        }

        JugUser currentUser = JugUser.findById(userId);

        render(jugEvents, currentUser);
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

    /**
     * Register the current authenticated user for this event.
     *
     * @param jugEventId is the primaray key
     */
    public static void registerThisEvent(Long jugEventId) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        if (jugEvent == null) {
            render("Event not found");
            return;
        }
        String userId = session.get("user-id");

        if (userId == null) {
            // the user is not yet authenticated
            render("user not found");
            return;
        }

        // Should add the user to the attendees list
        // or put the user on waiting list
        String result = jugEvent.book(userId);

        flash.success(result);

        index();

    }

    /**
     * Cancel a registration for a specific event.
     *
     * @param jugEventId is the PK.
     */
    public static void unregisterThisEvent(Long jugEventId) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        if (jugEvent == null) {
            render("Event not found");
            return;
        }

        String userId = session.get("user-id");

        if (userId == null) {
            // the user is not yet authenticated
            render("user not found");
            return;
        }

        // Should add the user to the attendees list
        // or put the user on waiting list
        String result = jugEvent.unbook(userId);

        flash.success(result);

        index();
    }


    /**
     * Loads the creation form.
     */
    public static void newJugEvent() {
        render();
    }


    /**
     * Edit a jugEvent
     *
     * @param id is the unique id.
     */
    public static void editJugEvent(Long id) {
        JugEvent jugEvent = JugEvent.findById(id);
        if (jugEvent == null) {
            flash.error("Aucun événement ne correspond à cet id");
            index();
        }
        if (jugEvent.createdBy != currentUser()) {
            flash.error("Impossible d'éditer un événement dont vous n'êtes pas le propriétaire");
            index();
        }

        render(jugEvent);
    }

    /**
     * Validates and persists a JugEvent. Please note that Play! Framework is able to turn HTTP params
     * to a Java object directly, so that we can use Play validation on the rich domai object.
     *
     * @param jugEvent is a new event.
     */
    public static void saveJugEvent(JugEvent jugEvent) {
        validation.valid(jugEvent);
        if (validation.hasErrors()) {
            if (jugEvent.id != null) {
                render("@editJugEvent", jugEvent);
            } else {
                render("@newJugEvent", jugEvent);

            }
            return;
        }
        if (jugEvent.id != null) {
            jugEvent.merge();
        } else {
            jugEvent.save();
        }
        JugUser currentUser = currentUser();

        jugEvent.setCreatedBy(currentUser);

        flash.success("Nouvel événement enregistré");
        index();
    }


    /**
     * Updates an existing event.
     * Stuffs that are not usual :
     * - the jugEvent is a template object that is filled automaticall by Play!
     * with params from the view, to perfom the validation
     * - If the form params are not valid we call update, we need to pass
     * again the id. Since the id is extracted on jugEven in the editJugEven view
     * then the id is empty.
     * - the 'edit' is able to extract params from the HTTP request and
     * is used to update the entity loaded from DB.
     * <p/>
     * TODO : the edit pattern with Play! is not very nice
     */
    public static void updateJugEvent(Long id, @Valid JugEvent jugEvent) {
        if (validation.hasErrors()) {
            if (request.isAjax()) error("Invalid value");
            jugEvent.id = id;
            render("@editJugEvent", jugEvent);
        }
        JugEvent jugEventFromDB = JugEvent.findById(id);
        JugUser currentUser = currentUser();
        if (jugEventFromDB.createdBy != currentUser) {
            flash.error("Impossible d'éditer un événement dont vous n'êtes pas le propriétaire");
            index();
        }

        jugEventFromDB.edit("jugEvent", params);
        jugEventFromDB.save();


        flash.success("Evenement mis à jour");

        index();

    }

    /**
     * Retrieves the current JugUser using the session.
     *
     * @return the current authenticated jugUser.
     */
    public static JugUser currentUser() {
        String userId = session.get("user-id");
        if(userId==null) return null;
        JugUser currentUser = JugUser.findById(userId);
        notFoundIfNull(currentUser);
        return currentUser;
    }

}