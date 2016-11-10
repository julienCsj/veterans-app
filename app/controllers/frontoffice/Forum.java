package controllers.frontoffice;

import models.Compte;
import models.Evenement;
import models.MOTD;
import models.Track;
import org.joda.time.DateTime;
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
        List<Evenement> evenements = Evenement.find("idTopic = ? AND valide  = ? order by dateDebut asc", idTopic, true).fetch();
        Evenement.CategorieEvenement[] categories = Evenement.CategorieEvenement.values();

        System.out.println(evenements.size());
        render(compte, evenements, idTopic, hash, categories);
    }

    public static void participe(String hash, Long idEvenement) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("id = ? AND valide  = ?", idEvenement, true).first();
        evenement.participants.add(compte);
        evenement.absents.remove(compte);
        evenement.incertains.remove(compte);
        evenement.save();
        boxEvenenement(hash, evenement.idTopic);
    }

    public static void incertain(String hash, Long idEvenement) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("id = ? AND valide  = ?", idEvenement, true).first();
        evenement.incertains.add(compte);
        evenement.participants.remove(compte);
        evenement.absents.remove(compte);
        evenement.save();
        boxEvenenement(hash, evenement.idTopic);
    }

    public static void absent(String hash, Long idEvenement) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        Evenement evenement = Evenement.find("id = ? AND valide  = ?", idEvenement, true).first();
        evenement.absents.add(compte);
        evenement.participants.remove(compte);
        evenement.incertains.remove(compte);
        evenement.save();
        boxEvenenement(hash, evenement.idTopic);
    }


    public static void boxProchainEvenement(String hash) {
        Compte compte = Compte.find("hash = ?", hash).first();
        compte.dateDerniereVueBox = new Date();
        compte.save();
        MOTD motd = MOTD.find("afficher = ?", true).first();
        Evenement evenement = Evenement.find("dateDebut > ?  AND valide  = ? order by dateDebut ASC", new Date(), true).first();

        render(motd, evenement, compte);
    }

    public static void ajouterEvenement(String hash, Evenement.CategorieEvenement categorie, String nom, String description, Long idTopic, Boolean deuxJour, Integer jour, Integer mois, Integer annee, Integer heure, Integer minute) {
        Compte compte = Compte.find("hash = ?", hash).first();
        Date date = DateTime.now().withDate(annee, mois, jour).withHourOfDay(heure).withMinuteOfHour(minute).withSecondOfMinute(0).toDate();
        Evenement evenement = new Evenement(nom, description, compte, categorie, "t="+idTopic, true, deuxJour);
        evenement.dateDebut = date;
        evenement.save();

        if(deuxJour != null && deuxJour) {
            Date dateNouvelEvenement = new DateTime(date).plusDays(1).toDate();
            Evenement evenementDemain = new Evenement(nom, description, compte, categorie, "t="+idTopic, true, deuxJour);
            evenementDemain.dateDebut = dateNouvelEvenement;
            evenementDemain.save();
        }
        boxEvenenement(compte.hash, idTopic);
    }

}
