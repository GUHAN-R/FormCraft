DROP TABLE IF EXISTS public.users CASCADE;

CREATE TABLE users
(
   user_id     varchar(36)    NOT NULL,
   email       varchar(255)   NOT NULL,
   password    varchar(25)    NOT NULL,
   created_at  timestamp      DEFAULT CURRENT_TIMESTAMP,
   name        varchar(50)
);

ALTER TABLE users
   ADD CONSTRAINT users_pkey
   PRIMARY KEY (user_id);

ALTER TABLE users
   ADD CONSTRAINT users_email_key UNIQUE (email);

COMMIT;


DROP TABLE IF EXISTS public.schemas CASCADE;

CREATE TABLE schemas
(
   schema_id           varchar(36)    NOT NULL,
   schema_description  varchar(255),
   json_schema         jsonb          NOT NULL,
   created_at          timestamp      DEFAULT CURRENT_TIMESTAMP,
   schema_title        varchar(50),
   user_id             varchar(36)
);

ALTER TABLE schemas
   ADD CONSTRAINT schemas_pkey
   PRIMARY KEY (schema_id);

ALTER TABLE schemas
  ADD CONSTRAINT fk_users_schemas
  FOREIGN KEY (user_id) REFERENCES users(user_id);

COMMIT;

DROP TABLE IF EXISTS public.forms CASCADE;

CREATE TABLE forms
(
   form_id     varchar(36)   NOT NULL,
   schema_id   varchar(36)   NOT NULL,
   form_data   jsonb         NOT NULL,
   created_at  timestamp     DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE forms
   ADD CONSTRAINT forms_pkey
   PRIMARY KEY (form_id);

ALTER TABLE forms
  ADD CONSTRAINT fk_schemas_forms
  FOREIGN KEY (schema_id) REFERENCES schemas(schema_id);

COMMIT;
