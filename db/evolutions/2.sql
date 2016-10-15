# --- !Ups
alter table evenement add column idTopic int8;

create table evenement_incertain (evenement_id int8 not null, incertains_id int8);
create table evenement_absent (evenement_id int8 not null, absents_id int8);



# --- !Downs

alter table evenement drop column idTopic;
drop table evenement_incertain;
drop table evenement_absent;




