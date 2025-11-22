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


-- ====================================================================
-- SEED (Prefixos Hexadecimais: A-F apenas)
-- ====================================================================

-- CARGOS (Prefix: C0)
INSERT INTO cargos (id_cargo, nome_cargo, descricao) VALUES
                                                         ('00000000-0000-0000-0000-000000000C01', 'Desenvolvedor Junior',  'Profissional iniciante em desenvolvimento'),
                                                         ('00000000-0000-0000-0000-000000000C02', 'Desenvolvedor Pleno',   'Profissional com experiência intermediária'),
                                                         ('00000000-0000-0000-0000-000000000C03', 'Arquiteto de Software', 'Responsável por decisões técnicas de alto nível'),
                                                         ('00000000-0000-0000-0000-000000000C04', 'Cientista de Dados',    'Especialista em análise e modelagem de dados'),
                                                         ('00000000-0000-0000-0000-000000000C05', 'DevOps Engineer',       'Especialista em infraestrutura e CI/CD'),
                                                         ('00000000-0000-0000-0000-000000000C06', 'Product Owner',         'Responsável pela visão do produto'),
                                                         ('00000000-0000-0000-0000-000000000C99', 'Administrador',         'Superusuário do sistema');

-- ÁREAS DE ATUAÇÃO (Prefix: A0 - usando zero em vez de 'A' letra para variar)
INSERT INTO areas_atuacao (id_area, nome_area, descricao) VALUES
                                                              ('00000000-0000-0000-0000-000000000A01', 'Back-end',       'Desenvolvimento do lado do servidor'),
                                                              ('00000000-0000-0000-0000-000000000A02', 'Front-end',      'Desenvolvimento de interfaces'),
                                                              ('00000000-0000-0000-0000-000000000A03', 'Mobile',         'Desenvolvimento de aplicativos móveis'),
                                                              ('00000000-0000-0000-0000-000000000A04', 'Data & IA',      'Engenharia de dados e Inteligência Artificial'),
                                                              ('00000000-0000-0000-0000-000000000A05', 'Cloud & Infra',  'Computação em nuvem e infraestrutura'),
                                                              ('00000000-0000-0000-0000-000000000A99', 'Gestão de TI',   'Gerenciamento e agilidade');

-- GÊNEROS
INSERT INTO generos (id_genero, nome_genero) VALUES
                                                 (RANDOM_UUID(), 'MASCULINO'),
                                                 (RANDOM_UUID(), 'FEMININO'),
                                                 (RANDOM_UUID(), 'OUTRO'),
                                                 (RANDOM_UUID(), 'PREFIRO_NAO_INFORMAR');

-- CATEGORIAS DE HABILIDADE (Prefix: CA -> C1)
INSERT INTO categorias_habilidade (id_cat_hab, nome_cat_hab, descricao) VALUES
                                                                            ('00000000-0000-0000-0000-000000000CA1', 'Linguagens',    'Linguagens de programação'),
                                                                            ('00000000-0000-0000-0000-000000000CA2', 'Frameworks',    'Bibliotecas e Frameworks'),
                                                                            ('00000000-0000-0000-0000-000000000CA3', 'Ferramentas',   'IDEs, Bancos de Dados e DevOps'),
                                                                            ('00000000-0000-0000-0000-000000000CA4', 'Cloud',         'Provedores e serviços de nuvem'),
                                                                            ('00000000-0000-0000-0000-000000000CA5', 'Soft Skills',   'Habilidades comportamentais');

-- HABILIDADES (Prefix: HA -> 4A)
INSERT INTO habilidades (id_habilidade, nome, id_cat_hab, descricao) VALUES
                                                                         ('00000000-0000-0000-0000-0000000004A1', 'Java',           '00000000-0000-0000-0000-000000000CA1', 'Linguagem robusta para back-end'),
                                                                         ('00000000-0000-0000-0000-0000000004A2', 'Python',         '00000000-0000-0000-0000-000000000CA1', 'Linguagem para dados e scripts'),
                                                                         ('00000000-0000-0000-0000-0000000004A3', 'JavaScript',     '00000000-0000-0000-0000-000000000CA1', 'Linguagem web'),
                                                                         ('00000000-0000-0000-0000-0000000004A4', 'Spring Boot',    '00000000-0000-0000-0000-000000000CA2', 'Framework Java para microsserviços'),
                                                                         ('00000000-0000-0000-0000-0000000004A5', 'React Native',   '00000000-0000-0000-0000-000000000CA2', 'Framework Mobile'),
                                                                         ('00000000-0000-0000-0000-0000000004A6', 'Docker',         '00000000-0000-0000-0000-000000000CA3', 'Containerização'),
                                                                         ('00000000-0000-0000-0000-0000000004A7', 'Kubernetes',     '00000000-0000-0000-0000-000000000CA3', 'Orquestração'),
                                                                         ('00000000-0000-0000-0000-0000000004A8', 'AWS',            '00000000-0000-0000-0000-000000000CA4', 'Amazon Web Services'),
                                                                         ('00000000-0000-0000-0000-0000000004A9', 'Azure',          '00000000-0000-0000-0000-000000000CA4', 'Microsoft Cloud'),
                                                                         ('00000000-0000-0000-0000-0000000004AA', 'Comunicação',    '00000000-0000-0000-0000-000000000CA5', 'Comunicação assertiva'),
                                                                         ('00000000-0000-0000-0000-0000000004AB', 'Liderança',      '00000000-0000-0000-0000-000000000CA5', 'Gestão de pessoas');

-- PARCEIROS DE CURSO (Prefix: PA -> 0F4)
INSERT INTO parceiros_curso (id_parceiro, nome_parceiro, descricao) VALUES
                                                                        ('00000000-0000-0000-0000-0000000000F1', 'FIAP',     'Faculdade de Tecnologia'),
                                                                        ('00000000-0000-0000-0000-0000000000F2', 'Alura',    'Plataforma de cursos online'),
                                                                        ('00000000-0000-0000-0000-0000000000F3', 'Udemy',    'Marketplace global'),
                                                                        ('00000000-0000-0000-0000-0000000000F4', 'Coursera', 'Cursos universitários'),
                                                                        ('00000000-0000-0000-0000-0000000000F5', 'Microsoft Learn', 'Documentação oficial');

-- CATEGORIAS DE CURSO (Prefix: CC -> CC)
INSERT INTO categorias_curso (id_cat_curso, nome_cat_curso, descricao) VALUES
                                                                           ('00000000-0000-0000-0000-000000000CC1', 'Bootcamp',   'Treinamento intensivo'),
                                                                           ('00000000-0000-0000-0000-000000000CC2', 'Graduação',  'Ensino superior'),
                                                                           ('00000000-0000-0000-0000-000000000CC3', 'Curso Rápido', 'Conteúdo focado em uma skill'),
                                                                           ('00000000-0000-0000-0000-000000000CC4', 'Trilha',     'Série de cursos sequenciais'),
                                                                           ('00000000-0000-0000-0000-000000000CC5', 'Workshop',   'Mão na massa ao vivo');

-- CURSOS (Prefix: CU -> C00)
INSERT INTO cursos (id_curso, titulo, descricao, duracao_horas, id_parceiro, id_cat_curso, link_curso) VALUES
                                                                                                           ('00000000-0000-0000-0000-00000000C001', 'Java Advanced', 'Domine Streams, Threads e JVM', 40.0, '00000000-0000-0000-0000-0000000000F1', '00000000-0000-0000-0000-000000000CC2', 'https://fiap.com.br'),
                                                                                                           ('00000000-0000-0000-0000-00000000C002', 'Spring Boot Microservices', 'Arquitetura distribuída com Spring', 20.0, '00000000-0000-0000-0000-0000000000F2', '00000000-0000-0000-0000-000000000CC4', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-00000000C003', 'AWS Cloud Practitioner', 'Certificação inicial de nuvem', 25.0, '00000000-0000-0000-0000-0000000000F3', '00000000-0000-0000-0000-000000000CC3', 'https://udemy.com/aws'),
                                                                                                           ('00000000-0000-0000-0000-00000000C004', 'React Native Zero to Hero', 'Crie apps Android e iOS', 35.0, '00000000-0000-0000-0000-0000000000F2', '00000000-0000-0000-0000-000000000CC1', 'https://alura.com.br/react'),
                                                                                                           ('00000000-0000-0000-0000-00000000C005', 'DevOps Masterclass', 'Docker, K8s e Jenkins', 50.0, '00000000-0000-0000-0000-0000000000F4', '00000000-0000-0000-0000-000000000CC4', 'https://coursera.org/devops');

-- CURSO_HABILIDADE
INSERT INTO curso_habilidade (id_curso, id_habilidade) VALUES
                                                           ('00000000-0000-0000-0000-00000000C001', '00000000-0000-0000-0000-0000000004A1'),
                                                           ('00000000-0000-0000-0000-00000000C002', '00000000-0000-0000-0000-0000000004A4'),
                                                           ('00000000-0000-0000-0000-00000000C002', '00000000-0000-0000-0000-0000000004A1'),
                                                           ('00000000-0000-0000-0000-00000000C003', '00000000-0000-0000-0000-0000000004A8'),
                                                           ('00000000-0000-0000-0000-00000000C004', '00000000-0000-0000-0000-0000000004A5'),
                                                           ('00000000-0000-0000-0000-00000000C004', '00000000-0000-0000-0000-0000000004A3'),
                                                           ('00000000-0000-0000-0000-00000000C005', '00000000-0000-0000-0000-0000000004A6'),
                                                           ('00000000-0000-0000-0000-00000000C005', '00000000-0000-0000-0000-0000000004A7');

-- TENDÊNCIAS DE MERCADO (Prefix: TE -> 0E)
INSERT INTO tendencias_mercado (id_tendencia, descricao, indice_demanda, fonte, data_analise) VALUES
                                                                                                  ('00000000-0000-0000-0000-0000000000E1', 'Alta demanda por Arquitetos Cloud (AWS/Azure)', 92.5, 'LinkedIn Jobs', DATE '2025-10-01'),
                                                                                                  ('00000000-0000-0000-0000-0000000000E2', 'Crescimento de vagas em Desenvolvimento Mobile Híbrido', 85.0, 'StackOverflow', DATE '2025-09-15'),
                                                                                                  ('00000000-0000-0000-0000-0000000000E3', 'IA Generativa aplicada ao desenvolvimento', 98.0, 'Gartner', DATE '2025-11-01'),
                                                                                                  ('00000000-0000-0000-0000-0000000000E4', 'Escassez de especialistas em Cyber Security', 89.0, 'TechCrunch', DATE '2025-08-20'),
                                                                                                  ('00000000-0000-0000-0000-0000000000E5', 'Modernização de Legado Java para Microsserviços', 75.0, 'IDC Report', DATE '2025-07-10');

-- USUÁRIOS (Prefix: US -> 0005)
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

          '00000000-0000-0000-0000-000000000C01',

          '00000000-0000-0000-0000-000000000A02',

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

          '00000000-0000-0000-0000-000000000C01',

          '00000000-0000-0000-0000-000000000A02',

          CURRENT_TIMESTAMP

      );

-- USUARIO_HABILIDADE
INSERT INTO usuario_habilidade (id_usuario, id_habilidade, nivel_proficiencia) VALUES
                                                                                   ('00000000-0000-0000-0000-000000000052', '00000000-0000-0000-0000-0000000004A1', 3),
                                                                                   ('00000000-0000-0000-0000-000000000052', '00000000-0000-0000-0000-0000000004A9', 2),
                                                                                   ('00000000-0000-0000-0000-000000000053', '00000000-0000-0000-0000-0000000004A6', 4),
                                                                                   ('00000000-0000-0000-0000-000000000053', '00000000-0000-0000-0000-0000000004A8', 3),
                                                                                   ('00000000-0000-0000-0000-000000000054', '00000000-0000-0000-0000-0000000004A3', 3),
                                                                                   ('00000000-0000-0000-0000-000000000054', '00000000-0000-0000-0000-0000000004A5', 2);

-- ROTAS DE REQUALIFICAÇÃO (Prefix: RO -> 00B)
INSERT INTO rotas_requalificacao (id_rota, nome_rota, descricao, objetivo_profissional, id_tendencia, gerada_por_ia) VALUES
                                                                                                                         ('00000000-0000-0000-0000-0000000000B1', 'De Java Jr a Arquiteto Cloud', 'Rota intensiva de Cloud e Microservices', 'Arquiteto de Software', '00000000-0000-0000-0000-0000000000E1', TRUE),
                                                                                                                         ('00000000-0000-0000-0000-0000000000B2', 'Front-end para Mobile Dev', 'Migração de carreira usando React', 'Desenvolvedor Senior', '00000000-0000-0000-0000-0000000000E2', TRUE),
                                                                                                                         ('00000000-0000-0000-0000-0000000000B3', 'Especialização em DevOps', 'Infraestrutura moderna', 'DevOps Engineer', '00000000-0000-0000-0000-0000000000E5', TRUE);

-- ROTA_CURSO (Vinculando RO a CU)
INSERT INTO rota_curso (id_rota, id_curso, ordem) VALUES
                                                      ('00000000-0000-0000-0000-0000000000B1', '00000000-0000-0000-0000-00000000C001', 1), -- Java Adv
                                                      ('00000000-0000-0000-0000-0000000000B1', '00000000-0000-0000-0000-00000000C002', 2), -- Spring
                                                      ('00000000-0000-0000-0000-0000000000B1', '00000000-0000-0000-0000-00000000C003', 3), -- AWS
                                                      ('00000000-0000-0000-0000-0000000000B2', '00000000-0000-0000-0000-00000000C004', 1), -- React Native
                                                      ('00000000-0000-0000-0000-0000000000B3', '00000000-0000-0000-0000-00000000C005', 1); -- DevOps

-- USUARIO_ROTA
INSERT INTO usuario_rota (id_usuario, id_rota, progresso_percentual, data_inicio) VALUES
                                                                                      ('00000000-0000-0000-0000-000000000052', '00000000-0000-0000-0000-0000000000B1', 15.5, CURRENT_TIMESTAMP), -- Thiago fazendo Rota 1
                                                                                      ('00000000-0000-0000-0000-000000000054', '00000000-0000-0000-0000-0000000000B2', 0.0, CURRENT_TIMESTAMP);  -- João fazendo Rota 2