-- ALTER TABLE IF EXISTS ONLY public.comment DROP CONSTRAINT IF EXISTS pk_comment_id CASCADE;


DROP TABLE IF EXISTS public.users;
DROP SEQUENCE IF EXISTS public.users_id_seq;
CREATE TABLE users (
    id serial NOT NULL,
    email character varying,
    username character varying,
    password character varying,
    register_date timestamp without time zone
);

DROP TABLE IF EXISTS public.blogposts;
DROP SEQUENCE IF EXISTS public.blogposts_id_seq;
CREATE TABLE blogposts (
    id serial NOT NULL,
    title character varying,
    content character varying,
    created_by integer,
    creation_date timestamp without time zone
);

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_user_id PRIMARY KEY (id);

ALTER TABLE ONLY blogposts
    ADD CONSTRAINT pk_blogpost_id PRIMARY KEY (id);

ALTER TABLE ONLY blogposts
    ADD CONSTRAINT fk_blogpost_user_id FOREIGN KEY (created_by) REFERENCES users(id);



-- INSERT INTO users VALUES (0, 'root@user.com', 'root', 'password', 'salt', '2017-01-01 00:00:00');


