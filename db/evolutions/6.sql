# --- !Ups
alter table evenement add column deuxJours boolean default false;

# --- !Downs
alter table evenement drop column deuxJours;




