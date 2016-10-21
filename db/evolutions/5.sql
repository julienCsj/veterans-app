# --- !Ups
create table chatbox_message (
id int8,
compte_id int8,
dateCreation TIMESTAMP,
message text,
silence boolean,
PRIMARY KEY (id)
);
alter table chatbox_message add CONSTRAINT fk_chatbox_message_compte_id FOREIGN KEY (compte_id) REFERENCES compte;


# --- !Downs
drop table chatbox_message;





