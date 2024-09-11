CREATE TYPE user_role AS ENUM (
'ADMIN', 'USER'
);

CREATE SEQUENCE user_data_id_seq
  INCREMENT BY 1
  START WITH 2
  MAXVALUE 999
  MINVALUE 1;

CREATE TABLE user_data (
    id          SMALLINT        PRIMARY KEY,
    username    VARCHAR (20)    UNIQUE    NOT NULL,
    password    VARCHAR(100)     NOT NULL,
    role        user_role       NOT NULL
);


CREATE TYPE task_status AS ENUM (
'TODO', 'IN_PROGRESS', 'COMPLETED'
);


CREATE SEQUENCE task_id_seq
  INCREMENT BY 1
  START WITH 2
  MAXVALUE 999
  MINVALUE 1;

CREATE TABLE task (
    id              SMALLINT        PRIMARY KEY,
    user_id         SMALLINT        NOT NULL,
    title           VARCHAR(50)     NOT NULL,
    description     VARCHAR(100),
    due_date        DATE,
    status          task_status     NOT NULL,
    CONSTRAINT fk_task__user_data FOREIGN KEY (user_id) REFERENCES user_data (id)
);