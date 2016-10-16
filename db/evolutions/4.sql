# --- !Ups
alter table evenement add column valide boolean default true;
create table track (
id int8,
compte_id int8,
dateCreation TIMESTAMP,
controller text,
action text,
headers text,
params text,
url text,
PRIMARY KEY (id)
);
alter table track add CONSTRAINT fk_track_compte_id FOREIGN KEY (compte_id) REFERENCES compte;

alter TABLE compte add column dateDerniereVueBox timestamp DEFAULT null;

# --- !Downs
alter table evenement drop column valide;
drop table track;
alter TABLE compte drop column dateDerniereVueBox;





