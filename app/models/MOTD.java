package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by juliencustoja on 21/09/2016.
 */

@Entity
@Table(name = "motd")
public class MOTD extends Model {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public Compte compte;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date dateCreation;

    @Column(nullable = false, updatable = true)
    public String texte;

    @Column
    public Boolean afficher;

    public MOTD(Compte compte, String texte) {
        this.compte = compte;
        this.texte = texte;
        this.dateCreation = new Date();
        this.afficher = false;
    }
}
