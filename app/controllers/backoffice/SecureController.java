package controllers.backoffice;

import models.Compte;
import models.Track;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.Date;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class SecureController extends Controller {

    @Before
    public void checkConnexion() {
        try {
            Long compteId = Long.parseLong(session.get("compte"));
            if(compteId == null) {
                forbidden("Il faut être connecté");
            } else {
                Compte compte = Compte.findById(compteId);
                if(compte == null) {
                    forbidden("Il faut être connecté");
                } else {
                    compte.dateDerniereConnexion = new Date();
                    compte.save();
                    renderArgs.put("compteConnecte", compte);
                }
            }
        } catch(Exception e) {
            Application.connexion();
        }

    }

    @Before
    public static void track() {
        try {
            Track t = new Track(request);
            t.compte = getCompte();
            t.save();
        } catch(Exception e) {
            if(Play.mode.isDev()) e.printStackTrace();
        }

    }

    public static Compte getCompte() {
        Long compteId = new Long(session.get("compte"));
        return Compte.findById(compteId);
    }
}
