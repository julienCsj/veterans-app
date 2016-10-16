package controllers.backoffice;

import models.Compte;
import models.Track;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class Tracks extends SecureController {

    public static void index() {
        List<Track> tracks = Track.find("order by dateCreation DESC").fetch();
        render(tracks);
    }
}
