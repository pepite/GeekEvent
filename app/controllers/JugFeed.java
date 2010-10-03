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

import models.JugEvent;
import play.mvc.Controller;

import java.util.List;

/**
 * Controller for the RSS feed
 *
 * @author Nicolas Martignole
 * @since 2 oct. 2010 17:57:52
 */
public class JugFeed extends Controller {
    public static void rss(){
        List<JugEvent> jugEvents = JugEvent.findAll();
        render(jugEvents);
    }
}
