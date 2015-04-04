--
-- PostgreSQL database dump
--

-- Started on 2015-04-04 00:31:41

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 497 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 141 (class 1259 OID 90542)
-- Dependencies: 3
-- Name: arbol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE arbol (
    id integer NOT NULL,
    id_padre integer
);


ALTER TABLE public.arbol OWNER TO postgres;

--
-- TOC entry 140 (class 1259 OID 90540)
-- Dependencies: 141 3
-- Name: arbol_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE arbol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.arbol_id_seq OWNER TO postgres;

--
-- TOC entry 1887 (class 0 OID 0)
-- Dependencies: 140
-- Name: arbol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE arbol_id_seq OWNED BY arbol.id;


--
-- TOC entry 1888 (class 0 OID 0)
-- Dependencies: 140
-- Name: arbol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('arbol_id_seq', 13, true);


--
-- TOC entry 142 (class 1259 OID 90548)
-- Dependencies: 3
-- Name: articulo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE articulo (
    id integer NOT NULL,
    categoria character varying(50),
    codigo character varying(10),
    costo numeric(1000,0),
    existencia numeric(1000,0),
    nombre character varying(100),
    precio numeric(1000,0)
);


ALTER TABLE public.articulo OWNER TO postgres;

--
-- TOC entry 143 (class 1259 OID 90556)
-- Dependencies: 3
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cliente (
    id_cliente integer NOT NULL
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 144 (class 1259 OID 90561)
-- Dependencies: 3
-- Name: empleado; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE empleado (
    id_empleado integer NOT NULL
);


ALTER TABLE public.empleado OWNER TO postgres;

--
-- TOC entry 146 (class 1259 OID 90568)
-- Dependencies: 3
-- Name: group_members; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE group_members (
    id integer NOT NULL,
    group_id integer NOT NULL,
    username character varying(20) NOT NULL
);


ALTER TABLE public.group_members OWNER TO postgres;

--
-- TOC entry 145 (class 1259 OID 90566)
-- Dependencies: 146 3
-- Name: group_members_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE group_members_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.group_members_id_seq OWNER TO postgres;

--
-- TOC entry 1889 (class 0 OID 0)
-- Dependencies: 145
-- Name: group_members_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE group_members_id_seq OWNED BY group_members.id;


--
-- TOC entry 1890 (class 0 OID 0)
-- Dependencies: 145
-- Name: group_members_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('group_members_id_seq', 1, false);


--
-- TOC entry 147 (class 1259 OID 90574)
-- Dependencies: 3
-- Name: group_menu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE group_menu (
    id_group_menu integer NOT NULL,
    group_id integer,
    menu_id integer
);


ALTER TABLE public.group_menu OWNER TO postgres;

--
-- TOC entry 148 (class 1259 OID 90579)
-- Dependencies: 3
-- Name: groups; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE groups (
    id integer NOT NULL,
    authority character varying(100) NOT NULL,
    group_name character varying(50) NOT NULL
);


ALTER TABLE public.groups OWNER TO postgres;

--
-- TOC entry 156 (class 1259 OID 90669)
-- Dependencies: 3
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 1891 (class 0 OID 0)
-- Dependencies: 156
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 9, true);


--
-- TOC entry 149 (class 1259 OID 90584)
-- Dependencies: 3
-- Name: history_logins; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE history_logins (
    id integer NOT NULL,
    date_login timestamp without time zone,
    date_logout timestamp without time zone,
    username character varying(20) NOT NULL
);


ALTER TABLE public.history_logins OWNER TO postgres;

--
-- TOC entry 150 (class 1259 OID 90589)
-- Dependencies: 3
-- Name: menu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE menu (
    actividad character varying(255),
    icono character varying(255),
    nombre character varying(255),
    ruta character varying(255),
    id_menu integer NOT NULL
);


ALTER TABLE public.menu OWNER TO postgres;

--
-- TOC entry 151 (class 1259 OID 90597)
-- Dependencies: 3
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE persistent_logins (
    series character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL,
    token character varying(64) NOT NULL,
    username character varying(20) NOT NULL
);


ALTER TABLE public.persistent_logins OWNER TO postgres;

--
-- TOC entry 153 (class 1259 OID 90604)
-- Dependencies: 3
-- Name: persona; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE persona (
    id integer NOT NULL,
    apellido character varying(50),
    cedula character varying(255) NOT NULL,
    correo character varying(20),
    direccion character varying(255),
    nombre character varying(50),
    sexo boolean,
    telefono character varying(20)
);


ALTER TABLE public.persona OWNER TO postgres;

--
-- TOC entry 152 (class 1259 OID 90602)
-- Dependencies: 3 153
-- Name: persona_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE persona_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.persona_id_seq OWNER TO postgres;

--
-- TOC entry 1892 (class 0 OID 0)
-- Dependencies: 152
-- Name: persona_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE persona_id_seq OWNED BY persona.id;


--
-- TOC entry 1893 (class 0 OID 0)
-- Dependencies: 152
-- Name: persona_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('persona_id_seq', 1, true);


--
-- TOC entry 155 (class 1259 OID 90615)
-- Dependencies: 3
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id integer NOT NULL,
    activo boolean NOT NULL,
    foto bytea,
    pasword character varying(100),
    username character varying(20),
    persona_id integer
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 154 (class 1259 OID 90613)
-- Dependencies: 155 3
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 1894 (class 0 OID 0)
-- Dependencies: 154
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- TOC entry 1895 (class 0 OID 0)
-- Dependencies: 154
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuario_id_seq', 1, true);


--
-- TOC entry 1828 (class 2604 OID 90545)
-- Dependencies: 141 140 141
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol ALTER COLUMN id SET DEFAULT nextval('arbol_id_seq'::regclass);


--
-- TOC entry 1829 (class 2604 OID 90571)
-- Dependencies: 145 146 146
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_members ALTER COLUMN id SET DEFAULT nextval('group_members_id_seq'::regclass);


--
-- TOC entry 1830 (class 2604 OID 90607)
-- Dependencies: 153 152 153
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY persona ALTER COLUMN id SET DEFAULT nextval('persona_id_seq'::regclass);


--
-- TOC entry 1831 (class 2604 OID 90618)
-- Dependencies: 154 155 155
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- TOC entry 1870 (class 0 OID 90542)
-- Dependencies: 141
-- Data for Name: arbol; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO arbol (id, id_padre) VALUES (1, NULL);
INSERT INTO arbol (id, id_padre) VALUES (2, NULL);
INSERT INTO arbol (id, id_padre) VALUES (3, 2);
INSERT INTO arbol (id, id_padre) VALUES (4, 3);
INSERT INTO arbol (id, id_padre) VALUES (5, 3);
INSERT INTO arbol (id, id_padre) VALUES (6, 3);
INSERT INTO arbol (id, id_padre) VALUES (7, NULL);
INSERT INTO arbol (id, id_padre) VALUES (8, 7);
INSERT INTO arbol (id, id_padre) VALUES (9, NULL);
INSERT INTO arbol (id, id_padre) VALUES (10, 9);
INSERT INTO arbol (id, id_padre) VALUES (11, NULL);
INSERT INTO arbol (id, id_padre) VALUES (12, 11);
INSERT INTO arbol (id, id_padre) VALUES (13, 10);


--
-- TOC entry 1871 (class 0 OID 90548)
-- Dependencies: 142
-- Data for Name: articulo; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1872 (class 0 OID 90556)
-- Dependencies: 143
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1873 (class 0 OID 90561)
-- Dependencies: 144
-- Data for Name: empleado; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO empleado (id_empleado) VALUES (1);


--
-- TOC entry 1874 (class 0 OID 90568)
-- Dependencies: 146
-- Data for Name: group_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO group_members (id, group_id, username) VALUES (9, 6, 'euge');


--
-- TOC entry 1875 (class 0 OID 90574)
-- Dependencies: 147
-- Data for Name: group_menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO group_menu (id_group_menu, group_id, menu_id) VALUES (9, 6, 2);
INSERT INTO group_menu (id_group_menu, group_id, menu_id) VALUES (10, 6, 3);
INSERT INTO group_menu (id_group_menu, group_id, menu_id) VALUES (11, 1, 2);
INSERT INTO group_menu (id_group_menu, group_id, menu_id) VALUES (12, 1, 3);
INSERT INTO group_menu (id_group_menu, group_id, menu_id) VALUES (13, 6, 4);


--
-- TOC entry 1876 (class 0 OID 90579)
-- Dependencies: 148
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO groups (id, authority, group_name) VALUES (2, 'ROLE_MEDICOS', 'MEDICOS');
INSERT INTO groups (id, authority, group_name) VALUES (3, 'ROLE_PROGRAMADORES', 'PROGRAMADORES');
INSERT INTO groups (id, authority, group_name) VALUES (4, 'ROLE_SECRETARIAS', 'SECRETARIAS');
INSERT INTO groups (id, authority, group_name) VALUES (5, 'ROLE_ENFERMERAS', 'ENFERMERAS');
INSERT INTO groups (id, authority, group_name) VALUES (6, 'ROLE_ADMINISTRADORES', 'ADMINISTRADORES');
INSERT INTO groups (id, authority, group_name) VALUES (7, 'ROLE_PROFESORES', 'PROFESORES');
INSERT INTO groups (id, authority, group_name) VALUES (8, 'ROLE_GERENTES', 'GERENTES');
INSERT INTO groups (id, authority, group_name) VALUES (1, 'ROLE_PROVEEDORES', 'PROVEEDORES');


--
-- TOC entry 1877 (class 0 OID 90584)
-- Dependencies: 149
-- Data for Name: history_logins; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO history_logins (id, date_login, date_logout, username) VALUES (1, '2015-04-03 19:30:05.348', NULL, 'euge');


--
-- TOC entry 1878 (class 0 OID 90589)
-- Dependencies: 150
-- Data for Name: menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, 'z-icon-book', 'Paso 1', '/WEB-INF/views/sistema/funcionalidades/new_file.zul', 1);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, 'z-icon-cog', 'Configuracion', '/WEB-INF/views/sistema/funcionalidades/new_file.zul', 2);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, 'z-icon-lock', 'Seguridad', NULL, 3);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, NULL, 'Grupos', '/WEB-INF/views/sistema/seguridad/configuracion/grupos/listaGrupos.zul', 4);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, NULL, 'Usuarios', '/WEB-INF/views/sistema/seguridad/configuracion/usuarios/listaUsuarios.zul', 5);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, NULL, 'Accesos', '/WEB-INF/views/sistema/seguridad/configuracion/accesos/listaAccesos.zul', 6);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, NULL, 'Prueba de Conf', NULL, 7);
INSERT INTO menu (actividad, icono, nombre, ruta, id_menu) VALUES (NULL, NULL, 'Paso 3.1', NULL, 8);


--
-- TOC entry 1879 (class 0 OID 90597)
-- Dependencies: 151
-- Data for Name: persistent_logins; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO persistent_logins (series, last_used, token, username) VALUES ('VcuIBtk+LJOOpHfJodYuug==', '2015-04-03 23:54:37.831', '98cN9LMsYmtHH/2E0JxYlA==', 'euge');


--
-- TOC entry 1880 (class 0 OID 90604)
-- Dependencies: 153
-- Data for Name: persona; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO persona (id, apellido, cedula, correo, direccion, nombre, sexo, telefono) VALUES (1, 'Caicedo', '20186243', NULL, NULL, 'Eugenio', NULL, NULL);


--
-- TOC entry 1881 (class 0 OID 90615)
-- Dependencies: 155
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario (id, activo, foto, pasword, username, persona_id) VALUES (1, true, NULL, '147', 'euge', 1);


--
-- TOC entry 1833 (class 2606 OID 90547)
-- Dependencies: 141 141
-- Name: arbol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY arbol
    ADD CONSTRAINT arbol_pkey PRIMARY KEY (id);


--
-- TOC entry 1835 (class 2606 OID 90555)
-- Dependencies: 142 142
-- Name: articulo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY articulo
    ADD CONSTRAINT articulo_pkey PRIMARY KEY (id);


--
-- TOC entry 1837 (class 2606 OID 90560)
-- Dependencies: 143 143
-- Name: cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 1839 (class 2606 OID 90565)
-- Dependencies: 144 144
-- Name: empleado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);


--
-- TOC entry 1841 (class 2606 OID 90573)
-- Dependencies: 146 146
-- Name: group_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY group_members
    ADD CONSTRAINT group_members_pkey PRIMARY KEY (id);


--
-- TOC entry 1843 (class 2606 OID 90578)
-- Dependencies: 147 147
-- Name: group_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY group_menu
    ADD CONSTRAINT group_menu_pkey PRIMARY KEY (id_group_menu);


--
-- TOC entry 1845 (class 2606 OID 90583)
-- Dependencies: 148 148
-- Name: groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- TOC entry 1847 (class 2606 OID 90588)
-- Dependencies: 149 149
-- Name: history_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY history_logins
    ADD CONSTRAINT history_logins_pkey PRIMARY KEY (id);


--
-- TOC entry 1849 (class 2606 OID 90596)
-- Dependencies: 150 150
-- Name: menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT menu_pkey PRIMARY KEY (id_menu);


--
-- TOC entry 1851 (class 2606 OID 90601)
-- Dependencies: 151 151
-- Name: persistent_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persistent_logins
    ADD CONSTRAINT persistent_logins_pkey PRIMARY KEY (series);


--
-- TOC entry 1853 (class 2606 OID 90612)
-- Dependencies: 153 153
-- Name: persona_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id);


--
-- TOC entry 1855 (class 2606 OID 90623)
-- Dependencies: 155 155
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 1857 (class 2606 OID 90673)
-- Dependencies: 155 155
-- Name: usuario_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_username_key UNIQUE (username);


--
-- TOC entry 1867 (class 2606 OID 90659)
-- Dependencies: 1832 150 141
-- Name: fk33155f49114222; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT fk33155f49114222 FOREIGN KEY (id_menu) REFERENCES arbol(id);


--
-- TOC entry 1859 (class 2606 OID 90629)
-- Dependencies: 153 1852 143
-- Name: fk334b85fa6576c673; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT fk334b85fa6576c673 FOREIGN KEY (id_cliente) REFERENCES persona(id);


--
-- TOC entry 1861 (class 2606 OID 90639)
-- Dependencies: 1844 146 148
-- Name: fk3b9c7759513bd8b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_members
    ADD CONSTRAINT fk3b9c7759513bd8b FOREIGN KEY (group_id) REFERENCES groups(id);


--
-- TOC entry 1862 (class 2606 OID 90674)
-- Dependencies: 155 1856 146
-- Name: fk3b9c7759996f83f5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_members
    ADD CONSTRAINT fk3b9c7759996f83f5 FOREIGN KEY (username) REFERENCES usuario(username);


--
-- TOC entry 1860 (class 2606 OID 90634)
-- Dependencies: 153 1852 144
-- Name: fk471e00cb53c18b0c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY empleado
    ADD CONSTRAINT fk471e00cb53c18b0c FOREIGN KEY (id_empleado) REFERENCES persona(id);


--
-- TOC entry 1865 (class 2606 OID 90654)
-- Dependencies: 147 1832 141
-- Name: fk4c6e0f1f48ff30e2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_menu
    ADD CONSTRAINT fk4c6e0f1f48ff30e2 FOREIGN KEY (id_group_menu) REFERENCES arbol(id);


--
-- TOC entry 1863 (class 2606 OID 90644)
-- Dependencies: 148 147 1844
-- Name: fk4c6e0f1f513bd8b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_menu
    ADD CONSTRAINT fk4c6e0f1f513bd8b FOREIGN KEY (group_id) REFERENCES groups(id);


--
-- TOC entry 1864 (class 2606 OID 90649)
-- Dependencies: 147 1848 150
-- Name: fk4c6e0f1f58e0e709; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_menu
    ADD CONSTRAINT fk4c6e0f1f58e0e709 FOREIGN KEY (menu_id) REFERENCES menu(id_menu);


--
-- TOC entry 1858 (class 2606 OID 90624)
-- Dependencies: 141 141 1832
-- Name: fk58c37eed44f5e01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol
    ADD CONSTRAINT fk58c37eed44f5e01 FOREIGN KEY (id_padre) REFERENCES arbol(id);


--
-- TOC entry 1868 (class 2606 OID 90684)
-- Dependencies: 151 1856 155
-- Name: fkbd224d2996f83f5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY persistent_logins
    ADD CONSTRAINT fkbd224d2996f83f5 FOREIGN KEY (username) REFERENCES usuario(username);


--
-- TOC entry 1866 (class 2606 OID 90679)
-- Dependencies: 149 1856 155
-- Name: fkee0d8835996f83f5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY history_logins
    ADD CONSTRAINT fkee0d8835996f83f5 FOREIGN KEY (username) REFERENCES usuario(username);


--
-- TOC entry 1869 (class 2606 OID 90664)
-- Dependencies: 1852 153 155
-- Name: fkf814f32eb13a4a2b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT fkf814f32eb13a4a2b FOREIGN KEY (persona_id) REFERENCES persona(id);


--
-- TOC entry 1886 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-04-04 00:31:41

--
-- PostgreSQL database dump complete
--

