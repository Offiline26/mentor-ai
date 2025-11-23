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
-- MENTOR-AI (H2) - MASSIVE DATA SEED (UUID SEQUENCIAL)
-- ====================================================================

-- 3.1 TENDÊNCIAS DE MERCADO (Trazido para cima pois Rotas dependem dele)
INSERT INTO tendencias_mercado (id_tendencia, descricao, indice_demanda, fonte, data_analise) VALUES
                                                                                                  ('00000000-0000-0000-0000-000000000101', 'Alta procura por arquitetos especialistas em Nuvem (Multi-cloud)', 92.5, 'LinkedIn Jobs 2025', DATE '2025-10-01'),
                                                                                                  ('00000000-0000-0000-0000-000000000102', 'Crescimento do desenvolvimento Mobile Híbrido', 85.0, 'StackOverflow Survey', DATE '2025-09-15'),
                                                                                                  ('00000000-0000-0000-0000-000000000103', 'IA Generativa no fluxo de desenvolvimento (Copilot)', 98.0, 'Gartner Trends', DATE '2025-11-01'),
                                                                                                  ('00000000-0000-0000-0000-000000000104', 'Cyber Security e Zero Trust', 89.0, 'TechCrunch', DATE '2025-08-20'),
                                                                                                  ('00000000-0000-0000-0000-000000000105', 'Modernização de Legado para Microsserviços', 75.0, 'IDC Report', DATE '2025-07-10'),
                                                                                                  ('00000000-0000-0000-0000-000000000106', 'Engenharia de Prompt e LLMs', 95.0, 'OpenAI Report', DATE '2025-10-20'),
                                                                                                  ('00000000-0000-0000-0000-000000000107', 'DevSecOps (Segurança no Pipeline)', 82.0, 'GitLab Report', DATE '2025-06-15'),
                                                                                                  ('00000000-0000-0000-0000-000000000108', 'Python como linguagem dominante em Data', 90.0, 'TIOBE Index', DATE '2025-09-01'),
                                                                                                  ('00000000-0000-0000-0000-000000000109', 'Automação RPA em processos corporativos', 68.0, 'Forrester', DATE '2025-05-30'),
                                                                                                  ('00000000-0000-0000-0000-000000000110', 'Crescimento de vagas remotas globais', 88.0, 'Deel Report', DATE '2025-11-10');

-- 3.2 CARGOS
INSERT INTO cargos (id_cargo, nome_cargo, descricao) VALUES
                                                         ('00000000-0000-0000-0000-000000000001', 'Desenvolvedor Back-end Junior', 'Iniciante em lógica de servidor e APIs.'),
                                                         ('00000000-0000-0000-0000-000000000002', 'Desenvolvedor Back-end Pleno', 'Experiência em arquitetura e banco de dados.'),
                                                         ('00000000-0000-0000-0000-000000000003', 'Desenvolvedor Back-end Senior', 'Especialista em performance e escalabilidade.'),
                                                         ('00000000-0000-0000-0000-000000000004', 'Desenvolvedor Front-end Junior', 'Iniciante em HTML, CSS e JS.'),
                                                         ('00000000-0000-0000-0000-000000000005', 'Desenvolvedor Front-end Pleno', 'Experiência com React, Angular ou Vue.'),
                                                         ('00000000-0000-0000-0000-000000000006', 'Desenvolvedor Front-end Senior', 'Especialista em UX e performance web.'),
                                                         ('00000000-0000-0000-0000-000000000007', 'Desenvolvedor Fullstack Junior', 'Conhecimento básico de todo o stack.'),
                                                         ('00000000-0000-0000-0000-000000000008', 'Desenvolvedor Fullstack Pleno', 'Autonomia no front e back-end.'),
                                                         ('00000000-0000-0000-0000-000000000009', 'Desenvolvedor Fullstack Senior', 'Domínio completo da arquitetura web.'),
                                                         ('00000000-0000-0000-0000-000000000010', 'Desenvolvedor Mobile iOS', 'Especialista em Swift e ecossistema Apple.'),
                                                         ('00000000-0000-0000-0000-000000000011', 'Desenvolvedor Mobile Android', 'Especialista em Kotlin e ecossistema Google.'),
                                                         ('00000000-0000-0000-0000-000000000012', 'Desenvolvedor Mobile Híbrido', 'Especialista em React Native ou Flutter.'),
                                                         ('00000000-0000-0000-0000-000000000013', 'Analista de Dados Junior', 'Coleta e limpeza de dados.'),
                                                         ('00000000-0000-0000-0000-000000000014', 'Analista de Dados Pleno', 'Criação de dashboards e insights.'),
                                                         ('00000000-0000-0000-0000-000000000015', 'Cientista de Dados', 'Modelagem preditiva e Machine Learning.'),
                                                         ('00000000-0000-0000-0000-000000000016', 'Engenheiro de Dados', 'Construção de pipelines e ETL.'),
                                                         ('00000000-0000-0000-0000-000000000017', 'DevOps Engineer', 'Automação, CI/CD e Infraestrutura.'),
                                                         ('00000000-0000-0000-0000-000000000018', 'SRE (Site Reliability Engineer)', 'Garantia de confiabilidade e uptime.'),
                                                         ('00000000-0000-0000-0000-000000000019', 'Arquiteto de Software', 'Definição de padrões e tecnologias.'),
                                                         ('00000000-0000-0000-0000-000000000020', 'Arquiteto de Soluções Cloud', 'Especialista em AWS, Azure ou GCP.'),
                                                         ('00000000-0000-0000-0000-000000000021', 'Analista de Segurança (CyberSec)', 'Proteção de redes e aplicações.'),
                                                         ('00000000-0000-0000-0000-000000000022', 'Pentester (Hacker Ético)', 'Testes de intrusão e vulnerabilidade.'),
                                                         ('00000000-0000-0000-0000-000000000023', 'Product Owner (PO)', 'Gestão de backlog e valor do produto.'),
                                                         ('00000000-0000-0000-0000-000000000024', 'Scrum Master', 'Facilitador de metodologias ágeis.'),
                                                         ('00000000-0000-0000-0000-000000000025', 'Tech Lead', 'Liderança técnica de equipes.'),
                                                         ('00000000-0000-0000-0000-000000000026', 'QA Tester (Manual)', 'Testes funcionais e de usabilidade.'),
                                                         ('00000000-0000-0000-0000-000000000027', 'QA Automation Engineer', 'Automação de testes E2E e unitários.'),
                                                         ('00000000-0000-0000-0000-000000000028', 'UX Designer', 'Design de experiência do usuário.'),
                                                         ('00000000-0000-0000-0000-000000000029', 'UI Designer', 'Design de interfaces visuais.'),
                                                         ('00000000-0000-0000-0000-000000000099', 'Administrador do Sistema', 'Acesso total à plataforma.');

-- 3.3 ÁREAS DE ATUAÇÃO
INSERT INTO areas_atuacao (id_area, nome_area, descricao) VALUES
                                                              ('00000000-0000-0000-0000-000000000011', 'Desenvolvimento de Software',   'Criação, manutenção e testes de aplicações web, mobile e desktop.'),
                                                              ('00000000-0000-0000-0000-000000000012', 'Data Science & Analytics',      'Extração de conhecimento a partir de dados estruturados e não estruturados.'),
                                                              ('00000000-0000-0000-0000-000000000013', 'Infraestrutura e Cloud',        'Gerenciamento de servidores, redes e serviços em nuvem (AWS, Azure).'),
                                                              ('00000000-0000-0000-0000-000000000014', 'Segurança da Informação',       'Proteção de sistemas, redes e dados contra ataques cibernéticos.'),
                                                              ('00000000-0000-0000-0000-000000000015', 'Gestão e Agilidade',            'Gerenciamento de projetos, produtos e liderança de times técnicos.'),
                                                              ('00000000-0000-0000-0000-000000000016', 'Cibersegurança',                'Proteção e conformidade.'),
                                                              ('00000000-0000-0000-0000-000000000017', 'Gestão de Produtos (Product)',   'Estratégia e roadmap.'),
                                                              ('00000000-0000-0000-0000-000000000018', 'Agilidade e Processos',          'Scrum, Kanban e Lean.'),
                                                              ('00000000-0000-0000-0000-000000000019', 'Design (UX/UI)',                 'Experiência e interface.'),
                                                              ('00000000-0000-0000-0000-000000000020', 'Qualidade de Software (QA)',     'Testes e garantia de qualidade.');

-- 3.4 GÊNEROS
INSERT INTO generos (id_genero, nome_genero) VALUES
                                                 (RANDOM_UUID(), 'MASCULINO'),
                                                 (RANDOM_UUID(), 'FEMININO'),
                                                 (RANDOM_UUID(), 'OUTRO'),
                                                 (RANDOM_UUID(), 'PREFIRO_NAO_INFORMAR');

-- 3.5 CATEGORIAS DE HABILIDADE
INSERT INTO categorias_habilidade (id_cat_hab, nome_cat_hab, descricao) VALUES
                                                                            ('00000000-0000-0000-0000-000000000021', 'Linguagens de Programação', 'Idiomas para codificação.'),
                                                                            ('00000000-0000-0000-0000-000000000022', 'Frameworks Back-end', 'Ferramentas para APIs e servidores.'),
                                                                            ('00000000-0000-0000-0000-000000000023', 'Frameworks Front-end', 'Ferramentas para interfaces web.'),
                                                                            ('00000000-0000-0000-0000-000000000024', 'Cloud & DevOps', 'Serviços de nuvem e CI/CD.'),
                                                                            ('00000000-0000-0000-0000-000000000025', 'Bancos de Dados', 'SQL e NoSQL.'),
                                                                            ('00000000-0000-0000-0000-000000000026', 'Data & IA', 'Ferramentas de dados e Machine Learning.'),
                                                                            ('00000000-0000-0000-0000-000000000027', 'Soft Skills', 'Habilidades comportamentais.');

-- 3.6 HABILIDADES
INSERT INTO habilidades (id_habilidade, nome, id_cat_hab, descricao) VALUES
                                                                         ('00000000-0000-0000-0000-000000000031', 'Java 21',          '00000000-0000-0000-0000-000000000021', 'Linguagem robusta para enterprise.'),
                                                                         ('00000000-0000-0000-0000-000000000032', 'Python',           '00000000-0000-0000-0000-000000000021', 'Linguagem versátil para dados e web.'),
                                                                         ('00000000-0000-0000-0000-000000000033', 'JavaScript (ES6+)', '00000000-0000-0000-0000-000000000021', 'Linguagem essencial da web.'),
                                                                         ('00000000-0000-0000-0000-000000000034', 'TypeScript',     '00000000-0000-0000-0000-000000000021', 'JavaScript com tipagem estática.'),
                                                                         ('00000000-0000-0000-0000-000000000035', 'C#',               '00000000-0000-0000-0000-000000000021', 'Linguagem da plataforma .NET.'),
                                                                         ('00000000-0000-0000-0000-000000000036', 'Go (Golang)',      '00000000-0000-0000-0000-000000000021', 'Linguagem focada em performance.'),
                                                                         ('00000000-0000-0000-0000-000000000037', 'Spring Boot 3',    '00000000-0000-0000-0000-000000000022', 'Framework Java líder de mercado.'),
                                                                         ('00000000-0000-0000-0000-000000000038', 'Node.js',          '00000000-0000-0000-0000-000000000022', 'Runtime JS para backend.'),
                                                                         ('00000000-0000-0000-0000-000000000039', '.NET Core',        '00000000-0000-0000-0000-000000000022', 'Plataforma Microsoft cross-platform.'),
                                                                         ('00000000-0000-0000-0000-000000000040', 'React.js',         '00000000-0000-0000-0000-000000000023', 'Biblioteca para interfaces web.'),
                                                                         ('00000000-0000-0000-0000-000000000041', 'Angular',          '00000000-0000-0000-0000-000000000023', 'Framework completo para SPA.'),
                                                                         ('00000000-0000-0000-0000-000000000042', 'React Native',     '00000000-0000-0000-0000-000000000023', 'Desenvolvimento mobile híbrido.'),
                                                                         ('00000000-0000-0000-0000-000000000043', 'Flutter',          '00000000-0000-0000-0000-000000000023', 'Toolkit UI do Google.'),
                                                                         ('00000000-0000-0000-0000-000000000044', 'Docker',           '00000000-0000-0000-0000-000000000024', 'Containerização de aplicações.'),
                                                                         ('00000000-0000-0000-0000-000000000045', 'Kubernetes',       '00000000-0000-0000-0000-000000000024', 'Orquestração de containers.'),
                                                                         ('00000000-0000-0000-0000-000000000046', 'AWS (Amazon)',     '00000000-0000-0000-0000-000000000024', 'Líder em cloud computing.'),
                                                                         ('00000000-0000-0000-0000-000000000047', 'Microsoft Azure',  '00000000-0000-0000-0000-000000000024', 'Nuvem corporativa da Microsoft.'),
                                                                         ('00000000-0000-0000-0000-000000000048', 'Terraform',        '00000000-0000-0000-0000-000000000024', 'Infraestrutura como Código.'),
                                                                         ('00000000-0000-0000-0000-000000000049', 'CI/CD',            '00000000-0000-0000-0000-000000000024', 'Integração e entrega contínua.'),
                                                                         ('00000000-0000-0000-0000-000000000050', 'SQL',              '00000000-0000-0000-0000-000000000025', 'Linguagem de consulta estruturada.'),
                                                                         ('00000000-0000-0000-0000-000000000051', 'NoSQL (MongoDB)',  '00000000-0000-0000-0000-000000000025', 'Banco de dados orientado a documentos.'),
                                                                         ('00000000-0000-0000-0000-000000000052', 'Comunicação',      '00000000-0000-0000-0000-000000000027', 'Clareza na transmissão de ideias.'),
                                                                         ('00000000-0000-0000-0000-000000000053', 'Liderança',        '00000000-0000-0000-0000-000000000027', 'Gestão e motivação de pessoas.'),
                                                                         ('00000000-0000-0000-0000-000000000054', 'Resolução de Problemas', '00000000-0000-0000-0000-000000000027', 'Pensamento crítico.'),
                                                                         ('00000000-0000-0000-0000-000000000055', 'Gestão de Tempo',  '00000000-0000-0000-0000-000000000027', 'Priorização.'),
                                                                         ('00000000-0000-0000-0000-000000000056', 'Inglês Técnico',   '00000000-0000-0000-0000-000000000027', 'Leitura e escrita técnica.'),
                                                                         ('00000000-0000-0000-0000-000000000057', 'Scrum / Agile',    '00000000-0000-0000-0000-000000000027', 'Metodologias ágeis.');

-- 3.7 PARCEIROS DE CURSO
INSERT INTO parceiros_curso (id_parceiro, nome_parceiro, descricao) VALUES
                                                                        ('00000000-0000-0000-0000-000000000061', 'Udemy',    'Marketplace global de ensino.'),
                                                                        ('00000000-0000-0000-0000-000000000062', 'Coursera', 'Cursos em parceria com universidades.'),
                                                                        ('00000000-0000-0000-0000-000000000063', 'Alura',    'Escola de Tecnologia.'),
                                                                        ('00000000-0000-0000-0000-000000000064', 'FIAP',     'Faculdade de Tecnologia.'),
                                                                        ('00000000-0000-0000-0000-000000000065', 'Microsoft Learn', 'Plataforma oficial Microsoft.'),
                                                                        ('00000000-0000-0000-0000-000000000066', 'AWS Training', 'Treinamento oficial da Amazon.'),
                                                                        ('00000000-0000-0000-0000-000000000067', 'Pluralsight', 'Cursos técnicos avançados.'),
                                                                        ('00000000-0000-0000-0000-000000000068', 'Rocketseat', 'Foco em JS, React e Node.'),
                                                                        ('00000000-0000-0000-0000-000000000069', 'DIO', 'Digital Innovation One.'),
                                                                        ('00000000-0000-0000-0000-000000000070', 'edX', 'Cursos de Harvard e MIT.');

-- 3.8 CATEGORIAS DE CURSO
INSERT INTO categorias_curso (id_cat_curso, nome_cat_curso, descricao) VALUES
                                                                           ('00000000-0000-0000-0000-000000000071', 'Bootcamp Intensivo', 'Cursos de curta duração com foco prático.'),
                                                                           ('00000000-0000-0000-0000-000000000072', 'Graduação/MBA', 'Formação acadêmica.'),
                                                                           ('00000000-0000-0000-0000-000000000073', 'Certificação Oficial', 'Preparatórios para exames.'),
                                                                           ('00000000-0000-0000-0000-000000000074', 'Curso Livre',  'Focado em habilidades específicas.'),
                                                                           ('00000000-0000-0000-0000-000000000075', 'Workshop',     'Evento prático.');

-- 3.9 CURSOS (Dependem de Parceiros 006x e Categorias 007x)
INSERT INTO cursos (id_curso, titulo, descricao, duracao_horas, id_parceiro, id_cat_curso, link_curso) VALUES
                                                                                                           ('00000000-0000-0000-0000-000000000081', 'Java Masterclass', 'Do Zero ao Expert.', 55.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000074', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000082', 'Spring Boot 3', 'APIs escaláveis.', 32.0, '00000000-0000-0000-0000-000000000063', '00000000-0000-0000-0000-000000000074', 'https://alura.com.br'),
                                                                                                           ('00000000-0000-0000-0000-000000000083', 'Testes com JUnit', 'Qualidade de código.', 12.0, '00000000-0000-0000-0000-000000000063', '00000000-0000-0000-0000-000000000074', 'https://alura.com.br'),
                                                                                                           ('00000000-0000-0000-0000-000000000084', 'AWS Cloud Practitioner', 'Certificação oficial.', 20.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000073', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000085', 'Microsoft Azure Fundamentals', 'Fundamentos da nuvem.', 15.0, '00000000-0000-0000-0000-000000000065', '00000000-0000-0000-0000-000000000073', 'https://microsoft.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000086', 'Docker & Kubernetes', 'Containerização do zero.', 40.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000074', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000087', 'DevOps Culture', 'CI/CD e Terraform.', 25.0, '00000000-0000-0000-0000-000000000062', '00000000-0000-0000-0000-000000000074', 'https://coursera.org'),
                                                                                                           ('00000000-0000-0000-0000-000000000088', 'React Native Zero to Hero', 'Apps iOS e Android.', 35.0, '00000000-0000-0000-0000-000000000063', '00000000-0000-0000-0000-000000000074', 'https://alura.com.br'),
                                                                                                           ('00000000-0000-0000-0000-000000000089', 'React.js Next.js', 'Front-end moderno.', 45.0, '00000000-0000-0000-0000-000000000068', '00000000-0000-0000-0000-000000000071', 'https://rocketseat.com.br'),
                                                                                                           ('00000000-0000-0000-0000-000000000090', 'Flutter Completo', 'Apps com Dart.', 40.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000074', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000091', 'Python Data Science', 'Pandas e Scikit-Learn.', 30.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000074', 'https://udemy.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000092', 'Engenharia de Dados Azure', 'Pipelines na nuvem.', 28.0, '00000000-0000-0000-0000-000000000065', '00000000-0000-0000-0000-000000000073', 'https://microsoft.com'),
                                                                                                           ('00000000-0000-0000-0000-000000000093', 'MBA em Arquitetura', 'Formação executiva.', 360.0, '00000000-0000-0000-0000-000000000064', '00000000-0000-0000-0000-000000000072', 'https://fiap.com.br'),
                                                                                                           ('00000000-0000-0000-0000-000000000094', 'Cyber Security', 'Segurança ofensiva.', 15.0, '00000000-0000-0000-0000-000000000069', '00000000-0000-0000-0000-000000000071', 'https://dio.me'),
                                                                                                           ('00000000-0000-0000-0000-000000000095', 'Scrum Master', 'Gestão ágil.', 16.0, '00000000-0000-0000-0000-000000000061', '00000000-0000-0000-0000-000000000073', 'https://udemy.com');

-- 3.10 CURSO_HABILIDADE (Vincula Cursos 008x a Habilidades 003x)
INSERT INTO curso_habilidade (id_curso, id_habilidade) VALUES
                                                           ('00000000-0000-0000-0000-000000000081', '00000000-0000-0000-0000-000000000031'), -- Java Masterclass ensina Java
                                                           ('00000000-0000-0000-0000-000000000082', '00000000-0000-0000-0000-000000000034'), -- Spring ensina Spring
                                                           ('00000000-0000-0000-0000-000000000082', '00000000-0000-0000-0000-000000000031'), -- Spring ensina Java
                                                           ('00000000-0000-0000-0000-000000000084', '00000000-0000-0000-0000-000000000038'), -- AWS ensina AWS
                                                           ('00000000-0000-0000-0000-000000000088', '00000000-0000-0000-0000-000000000035'), -- React Native ensina React
                                                           ('00000000-0000-0000-0000-000000000091', '00000000-0000-0000-0000-000000000032'); -- Python DS ensina Python

-- 3.11 USUÁRIOS (IDs: 091 a 095) - Depende de Cargo (000x) e Area (001x)
INSERT INTO usuarios (id_usuario, nome, email, senha_hash, data_nascimento, genero, pais, id_cargo, id_area) VALUES
                                                                                                                 ('00000000-0000-0000-0000-000000000091', 'Administrador MentorAI', 'admin@mentorai.com', '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1985-01-01', 0, 'Brasil', '00000000-0000-0000-0000-000000000099', '00000000-0000-0000-0000-000000000015'),
                                                                                                                 ('00000000-0000-0000-0000-000000000092', 'Lucas Silva',            'lucas.dev@email.com',  '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1999-05-20', 0, 'Brasil', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011'),
                                                                                                                 ('00000000-0000-0000-0000-000000000093', 'Amanda Costa',           'amanda.dados@email.com','$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1995-03-15', 1, 'Portugal', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000012'),
                                                                                                                 ('00000000-0000-0000-0000-000000000094', 'Roberto Almeida',        'beto.ops@email.com',   '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1990-11-30', 0, 'Brasil', '00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000013'),
                                                                                                                 ('00000000-0000-0000-0000-000000000095', 'Fernanda Souza',         'nanda.mobile@email.com','$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '2001-07-22', 1, 'Argentina', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000013');

-- 3.12 USUARIO_HABILIDADE
INSERT INTO usuario_habilidade (id_usuario, id_habilidade, nivel_proficiencia) VALUES
                                                                                   ('00000000-0000-0000-0000-000000000092', '00000000-0000-0000-0000-000000000031', 3), -- Lucas
                                                                                   ('00000000-0000-0000-0000-000000000093', '00000000-0000-0000-0000-000000000036', 4); -- Amanda

-- 3.13 ROTAS DE REQUALIFICAÇÃO (IDs: 301 a 303) - Depende de Tendências (010x)
INSERT INTO rotas_requalificacao (id_rota, nome_rota, descricao, objetivo_profissional, id_tendencia, gerada_por_ia) VALUES
                                                                                                                         ('00000000-0000-0000-0000-0000000000301', 'De Java Jr a Arquiteto Cloud', 'Trilha focada em fortalecer o back-end e migrar para infraestrutura.', 'Arquiteto de Soluções', '00000000-0000-0000-0000-000000000101', TRUE),
                                                                                                                         ('00000000-0000-0000-0000-0000000000302', 'Especialização em Desenvolvimento Mobile', 'Focado em profissionais React Native.', 'Desenvolvedor Mobile Senior', '00000000-0000-0000-0000-000000000102', TRUE),
                                                                                                                         ('00000000-0000-0000-0000-0000000000303', 'Imersão em Data Science', 'Do zero aos modelos preditivos com Python.', 'Cientista de Dados', '00000000-0000-0000-0000-000000000108', TRUE);

-- 3.14 ROTA_CURSO (Vincula Rotas 30x a Cursos 8x)
INSERT INTO rota_curso (id_rota, id_curso, ordem) VALUES
                                                      ('00000000-0000-0000-0000-0000000000301', '00000000-0000-0000-0000-000000000081', 1), -- Java
                                                      ('00000000-0000-0000-0000-0000000000301', '00000000-0000-0000-0000-000000000082', 2), -- Spring
                                                      ('00000000-0000-0000-0000-0000000000301', '00000000-0000-0000-0000-000000000085', 3), -- Azure
                                                      ('00000000-0000-0000-0000-0000000000302', '00000000-0000-0000-0000-000000000088', 1), -- React Native
                                                      ('00000000-0000-0000-0000-0000000000303', '00000000-0000-0000-0000-000000000091', 1); -- Python DS

-- 3.15 USUARIO_ROTA (Vincula Users 9x a Rotas 30x)
INSERT INTO usuario_rota (id_usuario, id_rota, progresso_percentual, data_inicio) VALUES
                                                                                      ('00000000-0000-0000-0000-000000000092', '00000000-0000-0000-0000-0000000000301', 15.0, CURRENT_TIMESTAMP), -- Lucas -> Java
                                                                                      ('00000000-0000-0000-0000-000000000095', '00000000-0000-0000-0000-0000000000302', 5.0, CURRENT_TIMESTAMP);  -- Fernanda -> Mobile
























