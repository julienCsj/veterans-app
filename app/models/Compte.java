package models;

import org.apache.commons.codec.digest.DigestUtils;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by juliencustoja on 21/09/2016.
 */

@Entity
@Table(name = "compte")
public class Compte extends Model {

    @Column(unique = true, nullable = false)
    public String pseudo;

    @Column(nullable = true)
    public String motDePasse;

    @Column(nullable = false)
    public String hash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Groupe groupe;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateDerniereConnexion;

    public Compte(String pseudo) {
        this.pseudo = pseudo;
        this.motDePasse = null;
        this.hash = UUID.randomUUID().toString();
        this.dateCreation = new Date();
        this.groupe = Groupe.MEMBRE;
        this.dateDerniereConnexion = null;
    }

    public void reset() {
        this.hash = UUID.randomUUID().toString();
    }


    public enum Groupe {
        ADMINISTRATEUR,
        MEMBRE,
        RECRUE;
    }
}
