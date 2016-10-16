package controllers.frontoffice;

import models.Compte;
import models.Evenement;
import models.MOTD;
import models.Track;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by juliencustoja on 15/10/2016.
 */
public class Forum extends Controller {

    @Before
    public static void track() {
        try {
            Track t = new Track(request);
            t.compte = Compte.find("hash = ?", params.get("hash")).first();
            t.save();
        } catch (Exception e) {
            if(Play.mode.isDev()) e.printStackTrace();
        }

    }

    public static void boxEvenenement(String hash, Long idTopic) {

        // Trouver l'évènement correspondant
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("idTopic = ?", idTopic).first();
        render(compte, evenement);
    }

    public static void participe(String hash, Long idTopic) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("idTopic = ?", idTopic).first();
        evenement.participants.add(compte);
        evenement.absents.remove(compte);
        evenement.incertains.remove(compte);
        evenement.save();
        boxEvenenement(hash, idTopic);
    }

    public static void incertain(String hash, Long idTopic) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("idTopic = ?", idTopic).first();
        evenement.incertains.add(compte);
        evenement.participants.remove(compte);
        evenement.absents.remove(compte);
        evenement.save();
        boxEvenenement(hash, idTopic);
    }

    public static void absent(String hash, Long idTopic) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("idTopic = ?", idTopic).first();
        evenement.absents.add(compte);
        evenement.participants.remove(compte);
        evenement.incertains.remove(compte);
        evenement.save();
        boxEvenenement(hash, idTopic);
    }

    public static void boxProchainsEvenement(String hash) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 14);

        Date dans14jours = cal.getTime();
        List<Evenement> evenements = Evenement.find("dateDebut > ? and dateDebut < ?", new Date(), dans14jours).fetch();
        render(evenements);
    }

    public static void boxProchainEvenement(String hash) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        MOTD motd = MOTD.find("afficher = ?", true).first();
        Evenement evenement = Evenement.find("dateDebut > ? order by dateDebut DESC", new Date()).first();

        render(motd, evenement, compte);
    }

}
