package controllers.backoffice;

import models.Compte;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class Dashboard extends SecureController {

    public static void index() {
        Compte compte = getCompte();
        render(compte);
    }

    public static void premiereConnexion() {
        Compte compte = getCompte();
        render(compte);
    }

    public static void choisirMotDePasse(Long id, String motDePasse, String motDePasseConfirmation) {
        Compte compte = Compte.findById(id);
        notFoundIfNull(compte);
        if(motDePasse.equals(motDePasseConfirmation)) {
            compte.motDePasse = DigestUtils.sha1Hex(motDePasse);
            compte.dateDerniereConnexion = new Date();
            compte.save();
            flash.success("Compte initialisé");
            index();
        } else {
            flash.error("Les deux mots de passe sont différents.");
            premiereConnexion();
        }
    }
}
