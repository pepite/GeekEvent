package controllers;

import models.JugEvent;
import models.JugUser;
import play.mvc.*;

import java.util.List;

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
        JugUser user = JugUser.authenticate(email,password);
        if(user==null){
            flash.error("Erreur: mot de passe non valide ou comtpe inexistant");
            index();
            return;
        }
        Scope.Session.current().put("user",user.email);
        flash.success("Vous êtes authentifié !");
    }

}