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
        render(jugEvent);
    }
}
