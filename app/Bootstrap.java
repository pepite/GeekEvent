import play.Play;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if (Play.mode == Play.Mode.DEV) {
            if(JugEvent.count()==0){
                Fixtures.load("initial-data.yml");
            }
        }
    }

}