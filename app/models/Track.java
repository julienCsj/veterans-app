package models;

import play.db.jpa.Model;
import play.mvc.Http;

import javax.persistence.*;
import java.util.*;

/**
 * Created by juliencustoja on 22/09/2016.
 */
@Entity
@Table(name = "track")
public class Track extends Model {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public Compte compte;

    @Column
    public Date dateCreation;

    @Column
    public String params;

    @Column
    public String controller;

    @Column
    public String action;

    @Column
    public String headers;

    @Column
    public String url;

    public Track(Http.Request request) {
        this.dateCreation = new Date();
        this.params = request.params.allSimple().toString();
        this.action = request.action;
        this.controller = request.controller;

        String headers = "";
        for (Map.Entry header : request.headers.entrySet()) {
            headers += header.getKey() + " = " + ((Http.Header)header.getValue()).value().toString();
            headers += "<br>";
        }
        this.headers = headers;

        this.url = request.url;
    }
}