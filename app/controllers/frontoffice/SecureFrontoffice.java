package controllers.frontoffice;

import models.Compte;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Created by juliencustoja on 23/09/2016.
 */
public class SecureFrontoffice extends Controller {

    @Before
    public static void check() {
        String hash = request.params.get("hash").toString();
        notFoundIfNull(hash);
        Compte compte = Compte.find("hash = ?", hash).first();
        notFoundIfNull(compte);
    }
}
