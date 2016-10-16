package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.*;

/**
 * Created by juliencustoja on 22/09/2016.
 */
@Entity
@Table(name = "evenement")
public class Evenement extends Model implements Comparable<Evenement> {

    @Column(nullable = false, updatable = true)
    public String titre;

    @Column(nullable = true, updatable = true)
    public String description;

    @Column(nullable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateDebut;

    @Column(nullable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public Compte compte;

    @Column(nullable = true, updatable = true)
    @OneToMany
    @ElementCollection
    @CollectionTable(name = "evenement_participant")
    public List<Compte> participants;

    @Column(nullable = true, updatable = true)
    @OneToMany
    @ElementCollection
    @CollectionTable(name = "evenement_incertain")
    public List<Compte> incertains;

    @Column(nullable = true, updatable = true)
    @OneToMany
    @ElementCollection
    @CollectionTable(name = "evenement_absent")
    public List<Compte> absents;

    @Column(nullable = false, updatable = true)
    @Enumerated(EnumType.ORDINAL)
    public CategorieEvenement categorie;

    @Column(nullable = true, updatable = true)
    public String urlForum;

    @Column(nullable = true, updatable = true)
    public Long idTopic;

    @Column(nullable = true, updatable = true)
    public Boolean valide;

    public Evenement(String titre, String description, Compte compte, CategorieEvenement categorie, String urlForum, Boolean valide) {
        this.titre = titre;
        this.description = description;
        this.compte = compte;
        this.categorie = categorie;
        this.urlForum = urlForum;
        this.idTopic = extractIdFromUrl(urlForum);
        this.valide = valide;
    }

    private Long extractIdFromUrl(String urlForum) {
        if(urlForum == null) {
            return null;
        }

        String idAsStr = urlForum.substring(urlForum.lastIndexOf("t=")+2);
        return new Long(idAsStr);
    }

    public static List<Evenement> getLesEvenementsNonPlaces() {
        return Evenement.find("dateDebut IS NULL").fetch();
    }

    public static List<Evenement> getLesEvenementsPlaces() {
        return Evenement.find("dateDebut IS NOT NULL").fetch();
    }

    @Override
    public int compareTo(Evenement o) {
        if (this.dateDebut.before(o.dateDebut)) {
            return -1;
        }
        return 1;
    }

    public enum CategorieEvenement {
        MISSION("Mission", "#00a65a"),
        ENTRAINEMENT("Entrainement", "#00c0ef"),
        REUNION("Réunion", "#f56954"),
        TEST("Test Mission/Addon", "#f39c12"),
        AUTRE("Autre", "#6600FF");

        public String traduction;
        public String couleur;

        CategorieEvenement(String traduction, String couleur) {
            this.traduction = traduction;
            this.couleur = couleur;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("id", this.id);
        toMap.put("titre", this.titre);
        toMap.put("description", this.description);
        toMap.put("categorie", this.categorie);
        toMap.put("dateDebut", this.dateDebut);
        toMap.put("dateFin", this.dateFin);
        toMap.put("compte", this.compte.id);
        toMap.put("urlForum", this.urlForum);

        List<Object> participant = new ArrayList<>();
        for(Compte c : this.participants) {
            Map<String, Object> p = new HashMap<>();
            p.put("pseudo", c.pseudo);
            participant.add(p);
        }
        toMap.put("participants", participant);
        return toMap;
    }

}