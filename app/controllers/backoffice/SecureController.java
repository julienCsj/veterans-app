package controllers.backoffice;

import models.Compte;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class SecureController extends Controller {

    @Before
    public void checkConnexion() {
        Long compteId = Long.parseLong(session.get("compte"));
        System.out.println(compteId);
        if(compteId == null) {
            forbidden("Il faut être connecté");
        } else {
            Compte compte = Compte.findById(compteId);
            if(compte == null) {
                forbidden("Il faut être connecté");
            } else {
                renderArgs.put("compteConnecte", compte);
            }
        }
    }

    public static Compte getCompte() {
        Long compteId = new Long(session.get("compte"));
        return Compte.findById(compteId);
    }
}
