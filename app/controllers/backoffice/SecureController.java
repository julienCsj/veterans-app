package controllers.backoffice;

import models.Compte;
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

    public static Compte getCompte() {
        Long compteId = new Long(session.get("compte"));
        return Compte.findById(compteId);
    }
}
