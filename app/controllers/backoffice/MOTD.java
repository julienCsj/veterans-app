package controllers.backoffice;

import models.Compte;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.validation.Required;
import play.data.validation.Validation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by juliencustoja on 21/09/2016.
 */
public class MOTD extends SecureController {

    public static void index() {
        List<models.MOTD> motds = models.MOTD.find("order by dateCreation DESC").fetch();
        render(motds);
    }

    public static void ajouterMotdPost(String texte) {
        Compte compte = getCompte();
        models.MOTD motd = new models.MOTD(compte, texte);
        motd.save();
        flash.success("MOTD ajouté");
        index();
    }

    public static void toggleMotd(Long id) {
        models.MOTD motd = models.MOTD.findById(id);
        motd.afficher = !motd.afficher;
        motd.save();
        index();
    }

    public static void supprimer(Long id) {
        models.MOTD.delete("id = ?", id);
        index();
    }
}
