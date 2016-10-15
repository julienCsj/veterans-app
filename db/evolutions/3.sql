# --- !Ups
create table motd (id int8 not null, compte_id int8, texte text, dateCreation TIMESTAMP, afficher boolean, PRIMARY KEY (id));
alter table motd add CONSTRAINT fk_motd_compte_id FOREIGN KEY (compte_id) REFERENCES compte;

# --- !Downs
drop TABLE motd;




