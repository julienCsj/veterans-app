package controllers.backoffice;

import org.apache.commons.codec.digest.DigestUtils;
import play.*;
import play.mvc.*;

import java.security.MessageDigest;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void connexion() {
        render();
    }


    public static void connexionPost(String login, String password) {
        session.clear();
        Compte compte = Compte.find("pseudo = ?", login).first();
        String hash = DigestUtils.sha1Hex(password);

        if(compte != null && compte.dateDerniereConnexion == null) {
            session.put("compte", compte.id);
            Dashboard.premiereConnexion();
        }

        if(hash.equals(compte.motDePasse)) {
            session.put("compte", compte.id);
            if(compte.dateDerniereConnexion == null) {
                Dashboard.premiereConnexion();
            } else {
                compte.dateDerniereConnexion = new Date();
                compte.save();
                Dashboard.index();
            }
        } else {
            flash.error("Mauvais identifiants");
            connexion();
        }
    }

    public static void connexionHash(String hash) {
        session.clear();
        Compte compte = Compte.find("hash = ?", hash).first();
        if(compte == null) {
            flash.error("Impossible de se connecter");
            connexion();
        } else {
            session.put("compte", compte.id);
            if(compte.dateDerniereConnexion == null) {
                Dashboard.premiereConnexion();
            } else {
                compte.dateDerniereConnexion = new Date();
                compte.save();
                Dashboard.index();
            }
        }
    }

    public static void deconnexion() {
        session.clear();
        flash.success("Vous êtes déconnecté");
        connexion();
    }

    public static void setData() {
        if(Play.mode.isDev()) {
            Compte compte = Compte.find("pseudo = ?", "Julien").first();
            if(compte != null) {
                compte.motDePasse = "b6a703d3439bd18f8f79c5ec515167776997707d";
                compte.save();
            } else {
                compte = new Compte("Julien");
                compte.motDePasse = "b6a703d3439bd18f8f79c5ec515167776997707d";
                compte.save();
            }
            renderText("OK");
        }
    }

}