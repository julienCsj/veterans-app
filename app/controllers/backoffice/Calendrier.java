package controllers.backoffice;

import models.Compte;
import models.Evenement;
import play.data.binding.As;

import java.util.*;

/**
 * Created by juliencustoja on 21/09/2016.
 */

public class Calendrier extends SecureController {

    public static void index(Long idEvenementAOuvrir) {

        List<Evenement> evenementNonPlaces = Evenement.getLesEvenementsNonPlaces();
        List<Evenement> evenementPlaces = Evenement.getLesEvenementsPlaces();
        Evenement.CategorieEvenement[] categories = Evenement.CategorieEvenement.values();
        List<Compte> comptes = Compte.findAll();

        Map<String, List<Evenement>> evenementParMois = new LinkedHashMap<>();

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DAY_OF_MONTH, 1);

        Calendar endMonth = Calendar.getInstance();
        endMonth.setTime(now.getTime());
        endMonth.add(Calendar.MONTH, 1);
        endMonth.set(Calendar.DAY_OF_MONTH, 1);
        endMonth.add(Calendar.DAY_OF_YEAR, -1);

        for(int i = 1; i<=12; i++) {
            List<Evenement> listeEvenement =  Evenement.find("(dateDebut BETWEEN ? AND ? AND dateFin IS NULL) OR (dateDebut > ? AND dateFin < ?) order by dateDebut ASC", now.getTime(), endMonth.getTime(), now.getTime(), endMonth.getTime()).fetch();

            evenementParMois.put(now.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE).toString(), listeEvenement);

            now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(Calendar.DAY_OF_MONTH, 1);
            now.add(Calendar.MONTH, i);

            endMonth = Calendar.getInstance();
            endMonth.setTime(now.getTime());
            endMonth.add(Calendar.MONTH, 1);
            endMonth.set(Calendar.DAY_OF_MONTH,1);
            endMonth.add(Calendar.DAY_OF_YEAR, -1);
        }


        render(evenementNonPlaces, evenementPlaces, categories, evenementParMois, idEvenementAOuvrir, comptes);
    }

    public static void ajouterEvenementPost(String titre, String description, Evenement.CategorieEvenement categorie, String urlForum, Boolean valide) {
        Compte compte = getCompte();
        Evenement evenement = new Evenement(titre, description, compte, categorie, urlForum, valide);
        evenement.save();

        flash.success("L'évènement a été créé. Vous pouvez maintenant le placer sur le calendrier (glissez puis déposez à la date souhaitée).");
        index(null);
    }

    public static void traiterEvenementPost(Long id, String titre, String description, Evenement.CategorieEvenement categorie, Long idCompte) {
        if(params.get("modifier") != null) {
            Evenement evenement = Evenement.findById(id);
            evenement.titre = titre;
            evenement.categorie = categorie;
            evenement.description = description;
            evenement.save();
            flash.success("L'évènement a bien été modifié");
            index(evenement.id);
        }

        if(params.get("supprimer") != null) {
            Evenement.delete("id = ?", id);
            flash.success("Evenement supprimé");
            index(null);
        }

        if(params.get("participer") != null) {
            Evenement evenement = Evenement.findById(id);
            if(!evenement.participants.contains(getCompte())) {
                evenement.participants.add(getCompte());
            }
            evenement.save();
            flash.success("Votre participation a bien été enregistrée");
            index(evenement.id);
        }

        if(params.get("nePlusParticiper") != null) {
            Evenement evenement = Evenement.findById(id);
            evenement.participants.remove(getCompte());
            evenement.save();
            index(evenement.id);
        }

        if(params.get("ajouterParticipant") != null) {
            Evenement evenement = Evenement.findById(id);
            Compte compte = Compte.findById(idCompte);
            if(!evenement.participants.contains(compte)) {
                evenement.participants.add(compte);
            }
            evenement.save();
            index(evenement.id);
        }

        index(null);
    }

    public static void getEvenementAjax(Long idEvenement) {
        if(idEvenement != null) {
            Evenement evenement = Evenement.findById(idEvenement);
            Map render = evenement.toMap();
            render.put("participe", evenement.participants.contains(getCompte()));
            renderJSON(render);
        } else {
            Map<String, String> map = new HashMap<>();
            renderJSON(map);
        }
    }

    public static void placerEvenementCalendrierAjax(Long idEvenement, @As("yyyy-MM-dd HH:mm") Date dateDebut, @As("yyyy-MM-dd HH:mm") Date dateFin) {
        Evenement evenement = Evenement.findById(idEvenement);

        evenement.dateDebut = dateDebut;
        if(dateFin != null) {
            evenement.dateFin = dateFin;
        }
        evenement.save();

        Map<String, Object> response = new HashMap<>();
        response.put("id", evenement.id);
        response.put("success", true);

        renderJSON(response);
    }


    public static void supprimerEvenement(Long id) {
        Evenement.delete("id = ?", id);
        flash.success("Evénement supprimé.");
        index(null);
    }
}