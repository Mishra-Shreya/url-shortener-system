
# schema creation
CREATE SCHEMA USS;


#sequence for id generation
CREATE SEQUENCE id_sequence
AS BIGINT
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 999999999
CACHE 100;