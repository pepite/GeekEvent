package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * A Java User Group Event.
 *
 * @author Nicolas Martignole
 * @since 9 sept. 2010 19:19:50
 */
@Entity
public class JugEvent extends Model {

    public String title;

    public String abstractContent;

    public String description;

    @Temporal(TemporalType.TIMESTAMP)
    public Date date;

    public Integer totalSlots;


    public void bookCurrentUser() {
        if(totalSlots==0){
            return;
        }
        totalSlots--;
        this.save();
    }
}
