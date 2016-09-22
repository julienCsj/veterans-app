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
public class Comptes extends SecureController {

    public static void index() {
        List<Compte> comptes = Compte.find("order by dateDerniereConnexion DESC").fetch();
        render(comptes);
    }

    public static void ajouterComptePost(@Required(message = "Le pseudo est obligatoire") String pseudo) {

        if(Validation.hasErrors()) {
            Validation.keep();
            index();
        }

        Compte compte = new Compte(pseudo);
        compte.save();
        System.out.println(compte.dateDerniereConnexion);
        flash.success("Le compte de "+pseudo+ " est crée.");
        index();
    }

    public static void supprimerCompte(Long id) {
        Compte.delete("id = ?", id);
        flash.success("Le compte est supprimé");
        index();
    }

    public static void resetCompte(Long id) {
        Compte compte = Compte.findById(id);
        notFoundIfNull(compte);
        compte.reset();
        compte.save();
        flash.success("Le compte est re-initialisé");
        index();
    }

    public static void editerCompte(Long id) {
        Compte compte = Compte.findById(id);
        notFoundIfNull(compte);
        List<Compte.Groupe> groupes = Arrays.asList(Compte.Groupe.values());
        render(compte, groupes);
    }

    public static void editerComptePost(Long id, String pseudo, String motDePasse, Compte.Groupe groupe) {
        Compte compte = Compte.findById(id);
        notFoundIfNull(compte);

        compte.pseudo = pseudo;
        if(motDePasse != null && ! "".equals(motDePasse)) {
            compte.motDePasse = DigestUtils.sha1Hex(motDePasse);
        }
        compte.groupe = groupe;
        compte.save();
        flash.success("Le compte est édité");
        index();
    }
}
