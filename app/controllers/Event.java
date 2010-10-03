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
import play.mvc.Before;
import play.mvc.Controller;

/**
 * A controller for JugEvent handling.
 *
 * @author Nicolas Martignole
 * @since 12 sept. 2010 09:59:41
 */
public class Event extends Controller {

    /**
     * The show method is accessible even if we're not authenticated.
     */
    @Before(unless = {"show"})
    static void checkLogin() {
        if (!session.contains("user")) {
            redirect("Application/login");
        }
    }


    /**
     * Shows a specific JugEvent
     *
     * @param jugEventId is the jugEvent to show
     * @param title      this param is not used, but required to create a nice URL, see also 'routes'
     * @param date       this param is not used, but required to create a nice URL, see also 'routes'
     */
    public static void show(final Long jugEventId, final String title, final String date) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        JugUser currentUser = Application.currentUser();
        render(jugEvent, currentUser);
    }

    /**
     * Shows a specific JugEvent not using nice url. This method is usefull only for unregister and unregister
     * methods below. I could not find a way to redirect to the 3 params methods for an unknown reason.
     *
     * @param jugEventId is the jugEvent to show
     */
    public static void show(final Long jugEventId) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        JugUser currentUser = Application.currentUser();
        render(jugEvent, currentUser);
    }


    /**
     * Register the current authenticated user for this event.
     *
     * @param jugEventId is the primaray key
     */
    public static void registerThisEvent(Long jugEventId) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        if (jugEvent == null) {
            flash.error("Event not found");
            Application.index();
            return;
        }
        String userId = session.get("user-id");


        // Should add the user to the attendees list
        // or put the user on waiting list
        String result = jugEvent.book(userId);

        flash.success(result);

        // Play generates the correct Redirect HTTP response
        show(jugEventId);

    }

    /**
     * Cancel a registration for a specific event.
     *
     * @param jugEventId is the PK.
     */
    public static void unregisterThisEvent(Long jugEventId) {
        JugEvent jugEvent = JugEvent.findById(jugEventId);
        if (jugEvent == null) {
            flash.error("Event not found");
            Application.index();
            return;
        }

        String userId = session.get("user-id");

        // Should add the user to the attendees list
        // or put the user on waiting list
        String result = jugEvent.unbook(userId);

        flash.success(result);

        // Play generates the correct Redirect HTTP response
        show(jugEventId);
    }
}
