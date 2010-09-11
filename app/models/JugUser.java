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
