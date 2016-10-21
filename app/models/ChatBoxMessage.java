package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by juliencustoja on 21/09/2016.
 */

@Entity
@Table(name = "chatbox_message")
public class ChatBoxMessage extends Model {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public Compte compte;

    @Column
    public String message;

    @Column
    public Boolean silence;

    @Column
    public Date dateCreation;

    public ChatBoxMessage(Compte compte, String message) {
        this.compte = compte;
        this.message = message;
        this.silence = false;
        this.dateCreation = new Date();
    }
}
