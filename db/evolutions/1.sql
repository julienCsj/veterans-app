# --- !Ups
create table compte (id bigint not null, pseudo varchar(255), motDePasse varchar(255), groupe varchar(255), hash VARCHAR(255), dateCreation timestamp, dateDerniereConnexion timestamp, primary key (id));
create sequence hibernate_sequence;

create table evenement (id int8, titre varchar(255), description text, dateDebut timestamp, dateFin timestamp, compte_id int8, categorie int8, urlForum varchar(512), primary key (id));
alter table evenement ADD CONSTRAINT FK_EVENEMENT_COMPTE foreign key (Compte_id) references compte;
create table evenement_participant (evenement_id int8 not null, participants_id int8);


# --- !Downs

alter table evenement drop constraint FK_EVENEMENT_COMPTE;
drop table evenement;
drop table evenement_participant;

drop table compte;
drop sequence hibernate_sequence;



