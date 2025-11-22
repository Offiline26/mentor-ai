-- =======================

-- Mentor-AI (H2) - SCHEMA + SEED (UUID)

-- =======================
DROP TABLE IF EXISTS usuario_rota;

DROP TABLE IF EXISTS rota_curso;

DROP TABLE IF EXISTS curso_habilidade;

DROP TABLE IF EXISTS usuario_habilidade;

DROP TABLE IF EXISTS rotas_requalificacao;

DROP TABLE IF EXISTS cursos;

DROP TABLE IF EXISTS habilidades;

DROP TABLE IF EXISTS usuarios;

DROP TABLE IF EXISTS tendencias_mercado;

DROP TABLE IF EXISTS categorias_curso;

DROP TABLE IF EXISTS parceiros_curso;

DROP TABLE IF EXISTS categorias_habilidade;

DROP TABLE IF EXISTS areas_atuacao;

DROP TABLE IF EXISTS generos;

DROP TABLE IF EXISTS cargos;



-- ========== TABELAS BASE



CREATE TABLE cargos (

                        id_cargo UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                        nome_cargo VARCHAR(100) NOT NULL,

                        descricao CLOB,

                        CONSTRAINT cargos_nome_uk UNIQUE (nome_cargo)

);



CREATE TABLE generos (

                         id_genero UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                         nome_genero VARCHAR(30) NOT NULL,

                         CONSTRAINT generos_nome_uk UNIQUE (nome_genero)

);



CREATE TABLE areas_atuacao (

                               id_area UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                               nome_area VARCHAR(100) NOT NULL,

                               descricao CLOB,

                               CONSTRAINT areas_atuacao_nome_uk UNIQUE (nome_area)

);



CREATE TABLE categorias_habilidade (

                                       id_cat_hab UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                                       nome_cat_hab VARCHAR(100) NOT NULL,

                                       descricao CLOB,

                                       CONSTRAINT cat_habilidade_nome_uk UNIQUE (nome_cat_hab)

);



CREATE TABLE parceiros_curso (

                                 id_parceiro UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                                 nome_parceiro VARCHAR(150) NOT NULL,

                                 descricao CLOB,

                                 CONSTRAINT parceiros_curso_nome_uk UNIQUE (nome_parceiro)

);



CREATE TABLE categorias_curso (

                                  id_cat_curso UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                                  nome_cat_curso VARCHAR(100) NOT NULL,

                                  descricao CLOB,

                                  CONSTRAINT cat_curso_nome_uk UNIQUE (nome_cat_curso)

);



CREATE TABLE tendencias_mercado (

                                    id_tendencia UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                                    descricao CLOB NOT NULL,

                                    indice_demanda DECIMAL(6,2),

                                    fonte VARCHAR(100),

                                    data_analise DATE

);



-- ========== USUÁRIOS / HABILIDADES / CURSOS / ROTAS



CREATE TABLE usuarios (

                          id_usuario UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                          nome VARCHAR(100) NOT NULL,

                          email VARCHAR(100) NOT NULL,

                          senha_hash VARCHAR(255) NOT NULL,

                          data_nascimento DATE,



                          genero INTEGER, -- <<<<<<<<<< AQUI: INTEGER



                          pais VARCHAR(50),

                          id_cargo UUID NOT NULL,

                          id_area UUID NOT NULL,

                          data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,



                          CONSTRAINT usuarios_email_uk UNIQUE (email),

                          CONSTRAINT usuarios_cargo_fk FOREIGN KEY (id_cargo)

                              REFERENCES cargos(id_cargo) ON DELETE RESTRICT,

                          CONSTRAINT usuarios_area_fk FOREIGN KEY (id_area)

                              REFERENCES areas_atuacao(id_area) ON DELETE RESTRICT

);



CREATE TABLE habilidades (

                             id_habilidade UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                             nome VARCHAR(100) NOT NULL,

                             id_cat_hab UUID,

                             descricao CLOB,

                             CONSTRAINT habilidades_cat_fk FOREIGN KEY (id_cat_hab)

                                 REFERENCES categorias_habilidade(id_cat_hab) ON DELETE SET NULL

);



CREATE TABLE cursos (

                        id_curso UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                        titulo VARCHAR(150) NOT NULL,

                        descricao CLOB,

                        duracao_horas DECIMAL(4,1),

                        id_parceiro UUID,

                        link_curso VARCHAR(255),

                        id_cat_curso UUID,

                        CONSTRAINT cursos_parceiro_fk FOREIGN KEY (id_parceiro)

                            REFERENCES parceiros_curso(id_parceiro) ON DELETE SET NULL,

                        CONSTRAINT cursos_cat_fk FOREIGN KEY (id_cat_curso)

                            REFERENCES categorias_curso(id_cat_curso) ON DELETE SET NULL

);



CREATE TABLE rotas_requalificacao (

                                      id_rota UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

                                      nome_rota VARCHAR(150) NOT NULL,

                                      descricao CLOB,

                                      objetivo_profissional VARCHAR(150),

                                      id_tendencia UUID,

                                      gerada_por_ia BOOLEAN DEFAULT TRUE NOT NULL,

                                      CONSTRAINT rotas_tend_fk FOREIGN KEY (id_tendencia)

                                          REFERENCES tendencias_mercado(id_tendencia) ON DELETE SET NULL

);



-- ========== TABELAS DE LIGAÇÃO



CREATE TABLE usuario_habilidade (

                                    id_usuario UUID NOT NULL,

                                    id_habilidade UUID NOT NULL,

                                    nivel_proficiencia INTEGER,

                                    CONSTRAINT usuario_habilidade_pk PRIMARY KEY (id_usuario, id_habilidade),

                                    CONSTRAINT usuhab_usuario_fk FOREIGN KEY (id_usuario)

                                        REFERENCES usuarios(id_usuario) ON DELETE CASCADE,

                                    CONSTRAINT usuhab_habilidade_fk FOREIGN KEY (id_habilidade)

                                        REFERENCES habilidades(id_habilidade) ON DELETE CASCADE,

                                    CONSTRAINT usuhab_nivel_ck CHECK (nivel_proficiencia BETWEEN 1 AND 5)

);



CREATE TABLE curso_habilidade (

                                  id_curso UUID NOT NULL,

                                  id_habilidade UUID NOT NULL,

                                  CONSTRAINT curso_habilidade_pk PRIMARY KEY (id_curso, id_habilidade),

                                  CONSTRAINT curhab_curso_fk FOREIGN KEY (id_curso)

                                      REFERENCES cursos(id_curso) ON DELETE CASCADE,

                                  CONSTRAINT curhab_habilidade_fk FOREIGN KEY (id_habilidade)

                                      REFERENCES habilidades(id_habilidade) ON DELETE CASCADE

);



CREATE TABLE rota_curso (

                            id_rota UUID NOT NULL,

                            id_curso UUID NOT NULL,

                            ordem INTEGER,

                            CONSTRAINT rota_curso_pk PRIMARY KEY (id_rota, id_curso),

                            CONSTRAINT rotcur_rota_fk FOREIGN KEY (id_rota)

                                REFERENCES rotas_requalificacao(id_rota) ON DELETE CASCADE,

                            CONSTRAINT rotcur_curso_fk FOREIGN KEY (id_curso)

                                REFERENCES cursos(id_curso) ON DELETE CASCADE

);



CREATE TABLE usuario_rota (

                              id_usuario UUID NOT NULL,

                              id_rota UUID NOT NULL,

                              progresso_percentual DECIMAL(5,2) DEFAULT 0,

                              data_inicio TIMESTAMP,

                              data_conclusao TIMESTAMP,

                              CONSTRAINT usuario_rota_pk PRIMARY KEY (id_usuario, id_rota),

                              CONSTRAINT usurto_usuario_fk FOREIGN KEY (id_usuario)

                                  REFERENCES usuarios(id_usuario) ON DELETE CASCADE,

                              CONSTRAINT usurto_rota_fk FOREIGN KEY (id_rota)

                                  REFERENCES rotas_requalificacao(id_rota) ON DELETE CASCADE

);



-- =======================

-- SEED BÁSICO (UUID FIXOS)

-- =======================



-- CARGOS (UUID fixos)

INSERT INTO cargos (id_cargo, nome_cargo, descricao) VALUES

                                                         ('00000000-0000-0000-0000-000000000001', 'ALUNO', 'Usuário padrão da plataforma'),

                                                         ('00000000-0000-0000-0000-000000000002', 'MENTOR', 'Mentor humano de carreira'),

                                                         ('00000000-0000-0000-0000-000000000003', 'ADMIN', 'Administrador do sistema');



-- GÊNEROS (opcional, só tabela de apoio)

INSERT INTO generos (id_genero, nome_genero) VALUES

                                                 (RANDOM_UUID(), 'MASCULINO'),

                                                 (RANDOM_UUID(), 'FEMININO'),

                                                 (RANDOM_UUID(), 'OUTRO'),

                                                 (RANDOM_UUID(), 'PREFIRO_NAO_INFORMAR');



-- ÁREAS (UUID fixos)

INSERT INTO areas_atuacao (id_area, nome_area, descricao) VALUES

                                                              ('00000000-0000-0000-0000-0000000000A1',

                                                               'Tecnologia da Informação',

                                                               'Desenvolvimento de software, infraestrutura, QA, etc.'),

                                                              ('00000000-0000-0000-0000-0000000000A2',

                                                               'Dados e IA',

                                                               'Ciência/Engenharia de Dados, ML, MLOps');



-- CATEGORIAS HABILIDADE

INSERT INTO categorias_habilidade (id_cat_hab, nome_cat_hab, descricao) VALUES

                                                                            (RANDOM_UUID(), 'Programação', 'Linguagens, frameworks e práticas'),

                                                                            (RANDOM_UUID(), 'Dados', 'Modelagem, ETL, análise, ML'),

                                                                            (RANDOM_UUID(), 'Soft Skills', 'Competências comportamentais');



-- PARCEIROS

INSERT INTO parceiros_curso (id_parceiro, nome_parceiro, descricao) VALUES

                                                                        (RANDOM_UUID(), 'FIAP', 'Parceiro FIAP'),

                                                                        (RANDOM_UUID(), 'Coursera', 'Marketplace de cursos'),

                                                                        (RANDOM_UUID(), 'Alura', 'Tecnologia e negócios');



-- CATEGORIAS DE CURSO

INSERT INTO categorias_curso (id_cat_curso, nome_cat_curso, descricao) VALUES

                                                                           (RANDOM_UUID(), 'Microcurso', 'Conteúdo rápido'),

                                                                           (RANDOM_UUID(), 'Trilha', 'Coleção de cursos'),

                                                                           (RANDOM_UUID(), 'Bootcamp', 'Formação intensiva');



-- TENDÊNCIAS

INSERT INTO tendencias_mercado (id_tendencia, descricao, indice_demanda, fonte, data_analise) VALUES

                                                                                                  (RANDOM_UUID(), 'Demanda crescente por Data Engineers no Brasil', 78.50, 'LinkedIn', DATE '2025-10-01'),

                                                                                                  (RANDOM_UUID(), 'Back-end com Java/Spring segue sólido', 72.30, 'CAGED', DATE '2025-09-20');



-- HABILIDADES (exemplo simplificado)

INSERT INTO habilidades (id_habilidade, nome, id_cat_hab, descricao) VALUES

                                                                         (RANDOM_UUID(), 'Java', NULL, 'Linguagem de programação'),

                                                                         (RANDOM_UUID(), 'Spring Boot', NULL, 'Framework Java'),

                                                                         (RANDOM_UUID(), 'SQL', NULL, 'Consultas e modelagem');



-- (demais seeds opcionais...)



-- USUÁRIOS (agora batendo com cargos/áreas fixos)

INSERT INTO usuarios (

    id_usuario, nome, email, senha_hash,

    data_nascimento, genero, pais,

    id_cargo, id_area, data_cadastro

) VALUES

      (

          RANDOM_UUID(),

          'Administrador',

          'admin@mentorai.com',

          '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy',

          NULL,

          NULL, -- genero NULL para admin

          'Brasil',

          '00000000-0000-0000-0000-000000000003',

          '00000000-0000-0000-0000-0000000000A1',

          CURRENT_TIMESTAMP

      ),

      (

          RANDOM_UUID(),

          'Usuário Demo',

          'demo@mentorai.com',

          '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy',

          DATE '1995-08-15',

          2, -- FEMININO (ENUM = 2)

          'Brasil',

          '00000000-0000-0000-0000-000000000001',

          '00000000-0000-0000-0000-0000000000A2',

          CURRENT_TIMESTAMP

      );