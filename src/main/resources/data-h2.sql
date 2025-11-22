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
-- SEED DE DADOS (UUIDs NUMÉRICOS SEQUENCIAIS)
-- Estratégia: 000...001, 000...002, etc. Sem letras nos sufixos.
-- ====================================================================

-- CARGOS (IDs: 1 a 6 + 99)
INSERT INTO cargos (id_cargo, nome_cargo, descricao) VALUES
('00000000-0000-0000-0000-000000000001', 'Desenvolvedor Back-end Junior', 'Profissional focado em lógica de servidor, APIs e banco de dados, nível iniciante.'),
('00000000-0000-0000-0000-000000000002', 'Analista de Dados Pleno',       'Profissional responsável por ETL, dashboards e análise exploratória de dados.'),
('00000000-0000-0000-0000-000000000003', 'Engenheiro de DevOps Senior',   'Especialista em infraestrutura, CI/CD, Cloud e automação de processos.'),
('00000000-0000-0000-0000-000000000004', 'Product Owner (PO)',            'Responsável pela visão do produto, backlog e priorização de valor.'),
('00000000-0000-0000-0000-000000000005', 'Arquiteto de Soluções',         'Define padrões de projeto, escolhe tecnologias e desenha sistemas escaláveis.'),
('00000000-0000-0000-0000-000000000099', 'Administrador do Sistema',      'Superusuário com acesso total à plataforma MentorAI.');

-- ÁREAS DE ATUAÇÃO (IDs: 11 a 15)
INSERT INTO areas_atuacao (id_area, nome_area, descricao) VALUES
('00000000-0000-0000-0000-000000000011', 'Desenvolvimento de Software',   'Criação, manutenção e testes de aplicações web, mobile e desktop.'),
('00000000-0000-0000-0000-000000000012', 'Data Science & Analytics',      'Extração de conhecimento a partir de dados estruturados e não estruturados.'),
('00000000-0000-0000-0000-000000000013', 'Infraestrutura e Cloud',        'Gerenciamento de servidores, redes e serviços em nuvem (AWS, Azure).'),
('00000000-0000-0000-0000-000000000014', 'Segurança da Informação',       'Proteção de sistemas, redes e dados contra ataques cibernéticos.'),
('00000000-0000-0000-0000-000000000015', 'Gestão e Agilidade',            'Gerenciamento de projetos, produtos e liderança de times técnicos.');

-- GÊNEROS (IDs Aleatórios)
INSERT INTO generos (id_genero, nome_genero) VALUES
(RANDOM_UUID(), 'MASCULINO'),
(RANDOM_UUID(), 'FEMININO'),
(RANDOM_UUID(), 'OUTRO'),
(RANDOM_UUID(), 'PREFIRO_NAO_INFORMAR');

-- CATEGORIAS DE HABILIDADE (IDs: 21 a 25)
INSERT INTO categorias_habilidade (id_cat_hab, nome_cat_hab, descricao) VALUES
('00000000-0000-0000-0000-000000000021', 'Linguagens de Programação', 'Idiomas técnicos para escrita de código fonte.'),
('00000000-0000-0000-0000-000000000022', 'Frameworks e Bibliotecas',  'Conjuntos de ferramentas pré-prontas para agilizar o desenvolvimento.'),
('00000000-0000-0000-0000-000000000023', 'Plataformas de Nuvem',      'Serviços de computação em nuvem e orquestração.'),
('00000000-0000-0000-0000-000000000024', 'Bancos de Dados',           'Sistemas de gerenciamento de dados relacionais e não-relacionais.'),
('00000000-0000-0000-0000-000000000025', 'Soft Skills',               'Habilidades comportamentais e interpessoais.');

-- HABILIDADES (IDs: 31 a 41) - Vinculadas às categorias 21-25
INSERT INTO habilidades (id_habilidade, nome, id_cat_hab, descricao) VALUES
('00000000-0000-0000-0000-000000000031', 'Java 21',          '00000000-0000-0000-0000-000000000021', 'Linguagem robusta orientada a objetos.'),
('00000000-0000-0000-0000-000000000032', 'Python',           '00000000-0000-0000-0000-000000000021', 'Linguagem versátil para Data Science e Scripts.'),
('00000000-0000-0000-0000-000000000033', 'JavaScript',     '00000000-0000-0000-0000-000000000021', 'Linguagem web fullstack.'),
('00000000-0000-0000-0000-000000000034', 'Spring Boot 3',    '00000000-0000-0000-0000-000000000022', 'Framework líder para microsserviços em Java.'),
('00000000-0000-0000-0000-000000000035', 'React Native',     '00000000-0000-0000-0000-000000000022', 'Framework para desenvolvimento mobile híbrido.'),
('00000000-0000-0000-0000-000000000036', 'Docker',         '00000000-0000-0000-0000-000000000023', 'Containerização de apps.'),
('00000000-0000-0000-0000-000000000037', 'Kubernetes',     '00000000-0000-0000-0000-000000000023', 'Orquestração de containers.'),
('00000000-0000-0000-0000-000000000038', 'AWS',            '00000000-0000-0000-0000-000000000024', 'Amazon Web Services.'),
('00000000-0000-0000-0000-000000000039', 'Azure',          '00000000-0000-0000-0000-000000000024', 'Microsoft Cloud.'),
('00000000-0000-0000-0000-000000000040', 'Comunicação',    '00000000-0000-0000-0000-000000000025', 'Comunicação assertiva.'),
('00000000-0000-0000-0000-000000000041', 'Liderança',      '00000000-0000-0000-0000-000000000025', 'Gestão de pessoas.');

-- PARCEIROS DE CURSO (IDs: 51 a 55)
INSERT INTO parceiros_curso (id_parceiro, nome_parceiro, descricao) VALUES
('00000000-0000-0000-0000-000000000051', 'Udemy',    'Marketplace global de ensino e aprendizado.'),
('00000000-0000-0000-0000-000000000052', 'Coursera', 'Cursos em parceria com grandes universidades.'),
('00000000-0000-0000-0000-000000000053', 'Alura',    'Maior plataforma de tecnologia do Brasil.'),
('00000000-0000-0000-0000-000000000054', 'FIAP',     'Faculdade referência em tecnologia e inovação.'),
('00000000-0000-0000-0000-000000000055', 'Microsoft Learn', 'Plataforma oficial de documentação e cursos.');

-- CATEGORIAS DE CURSO (IDs: 61 a 65)
INSERT INTO categorias_curso (id_cat_curso, nome_cat_curso, descricao) VALUES
('00000000-0000-0000-0000-000000000061', 'Bootcamp Intensivo', 'Cursos de curta duração com foco prático e imersivo.'),
('00000000-0000-0000-0000-000000000062', 'Formação Completa',  'Trilhas longas que cobrem do básico ao avançado.'),
('00000000-0000-0000-0000-000000000063', 'Certificação Oficial', 'Preparatórios para exames de certificação.'),
('00000000-0000-0000-0000-000000000064', 'Workshop Prático',   'Aulas focadas em resolver um problema específico.'),
('00000000-0000-0000-0000-000000000065', 'Pós-Graduação / MBA', 'Ensino superior focado em especialização.');

-- CURSOS (IDs: 71 a 75) - Vinculados a Parceiros (5x) e Categorias (6x)
INSERT INTO cursos (id_curso, titulo, descricao, duracao_horas, id_parceiro, id_cat_curso, link_curso) VALUES
('00000000-0000-0000-0000-000000000071', 'Java Masterclass: Do Zero ao Expert', 'Curso completo cobrindo JVM e POO.', 55.0, '00000000-0000-0000-0000-000000000051', '00000000-0000-0000-0000-000000000062', 'https://udemy.com/java'),
('00000000-0000-0000-0000-000000000072', 'Spring Boot 3 & Microsserviços', 'Aprenda a criar APIs escaláveis.', 32.0, '00000000-0000-0000-0000-000000000053', '00000000-0000-0000-0000-000000000062', 'https://alura.com.br/spring'),
('00000000-0000-0000-0000-000000000073', 'AWS Cloud Practitioner', 'Preparatório oficial.', 15.0, '00000000-0000-0000-0000-000000000055', '00000000-0000-0000-0000-000000000063', 'https://aws.amazon.com'),
('00000000-0000-0000-0000-000000000074', 'Desenvolvimento Mobile com React Native', 'Crie apps iOS e Android.', 40.0, '00000000-0000-0000-0000-000000000051', '00000000-0000-0000-0000-000000000061', 'https://udemy.com/react'),
('00000000-0000-0000-0000-000000000075', 'Python Data Science', 'Pandas, NumPy e Scikit-Learn.', 45.0, '00000000-0000-0000-0000-000000000052', '00000000-0000-0000-0000-000000000062', 'https://coursera.org/python');

-- CURSO_HABILIDADE (Ligação Curso 7x -> Habilidade 3x)
INSERT INTO curso_habilidade (id_curso, id_habilidade) VALUES
('00000000-0000-0000-0000-000000000071', '00000000-0000-0000-0000-000000000031'), -- Java Masterclass ensina Java
('00000000-0000-0000-0000-000000000072', '00000000-0000-0000-0000-000000000034'), -- Spring ensina Spring
('00000000-0000-0000-0000-000000000072', '00000000-0000-0000-0000-000000000031'), -- Spring ensina Java
('00000000-0000-0000-0000-000000000073', '00000000-0000-0000-0000-000000000038'), -- AWS ensina AWS
('00000000-0000-0000-0000-000000000074', '00000000-0000-0000-0000-000000000035'), -- React Native ensina React
('00000000-0000-0000-0000-000000000074', '00000000-0000-0000-0000-000000000033'), -- React Native ensina JS
('00000000-0000-0000-0000-000000000075', '00000000-0000-0000-0000-000000000032'); -- Python DS ensina Python

-- TENDÊNCIAS (IDs: 81 a 85)
INSERT INTO tendencias_mercado (id_tendencia, descricao, indice_demanda, fonte, data_analise) VALUES
('00000000-0000-0000-0000-000000000081', 'Alta procura por arquitetos especialistas em Nuvem', 92.5, 'LinkedIn', DATE '2025-10-01'),
('00000000-0000-0000-0000-000000000082', 'Desenvolvimento Mobile Híbrido em alta', 85.0, 'StackOverflow', DATE '2025-09-15'),
('00000000-0000-0000-0000-000000000083', 'Adoção de IA Generativa nas empresas', 98.0, 'Gartner', DATE '2025-11-01'),
('00000000-0000-0000-0000-000000000084', 'Escassez de especialistas em Cyber Security', 89.0, 'TechCrunch', DATE '2025-08-20'),
('00000000-0000-0000-0000-000000000085', 'Migração de Monólitos para Microsserviços', 75.0, 'IDC', DATE '2025-07-10');

-- USUÁRIOS (IDs: 91 a 95)
-- Senha Padrão para TODOS: Admin@123
-- Hash Válido: $2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy
INSERT INTO usuarios (id_usuario, nome, email, senha_hash, data_nascimento, genero, pais, id_cargo, id_area) VALUES
('00000000-0000-0000-0000-000000000091', 'Administrador MentorAI', 'admin@mentorai.com', '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1985-01-01', 1, 'Brasil', '00000000-0000-0000-0000-000000000099', '00000000-0000-0000-0000-000000000015'),
('00000000-0000-0000-0000-000000000092', 'Lucas Silva',            'lucas.dev@email.com',  '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1999-05-20', 1, 'Brasil', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011'),
('00000000-0000-0000-0000-000000000093', 'Amanda Costa',           'amanda.dados@email.com','$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1995-03-15', 2, 'Portugal', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000012'),
('00000000-0000-0000-0000-000000000094', 'Roberto Almeida',        'beto.ops@email.com',   '$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '1990-11-30', 1, 'Brasil', '00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000013'),
('00000000-0000-0000-0000-000000000095', 'Fernanda Souza',         'nanda.mobile@email.com','$2b$10$mDn1QxWAF1esglWOvThEEurwjZ2V540nTbKd/lpPoQJsBwRIAEQxy', DATE '2001-07-22', 2, 'Argentina', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000013');

-- USUARIO_HABILIDADE (User 9x -> Hab 3x)
INSERT INTO usuario_habilidade (id_usuario, id_habilidade, nivel_proficiencia) VALUES
('00000000-0000-0000-0000-000000000092', '00000000-0000-0000-0000-000000000031', 3),
('00000000-0000-0000-0000-000000000092', '00000000-0000-0000-0000-000000000039', 2),
('00000000-0000-0000-0000-000000000093', '00000000-0000-0000-0000-000000000036', 4),
('00000000-0000-0000-0000-000000000093', '00000000-0000-0000-0000-000000000038', 3),
('00000000-0000-0000-0000-000000000094', '00000000-0000-0000-0000-000000000033', 3),
('00000000-0000-0000-0000-000000000094', '00000000-0000-0000-0000-000000000035', 2);

-- ROTAS (IDs: 101 a 103)
INSERT INTO rotas_requalificacao (id_rota, nome_rota, descricao, objetivo_profissional, id_tendencia) VALUES
('00000000-0000-0000-0000-000000000101', 'Transição de Java Junior para Arquiteto Cloud', 'Trilha focada em fortalecer o back-end.', 'Arquiteto de Soluções', '00000000-0000-0000-0000-000000000081' ),
('00000000-0000-0000-0000-000000000102', 'Especialização em Desenvolvimento Mobile', 'Focado em profissionais React Native.', 'Desenvolvedor Mobile Senior', '00000000-0000-0000-0000-000000000085'),
('00000000-0000-0000-0000-000000000103', 'Imersão em Data Science', 'Do zero aos modelos preditivos.', 'Cientista de Dados', '00000000-0000-0000-0000-000000000084');

-- ROTA_CURSO (Rota 10x -> Curso 7x)
INSERT INTO rota_curso (id_rota, id_curso, ordem) VALUES
('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000071', 1), -- Java Adv
('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000072', 2), -- Spring
('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000073', 3), -- AWS
('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000074', 1), -- React Native
('00000000-0000-0000-0000-000000000103', '00000000-0000-0000-0000-000000000075', 1); -- DevOps

-- USUARIO_ROTA (User 9x -> Rota 10x)
INSERT INTO usuario_rota (id_usuario, id_rota, progresso_percentual, data_inicio) VALUES
('00000000-0000-0000-0000-000000000092', '00000000-0000-0000-0000-000000000101', 15.0, CURRENT_TIMESTAMP), -- Lucas fazendo Rota Java
('00000000-0000-0000-0000-000000000095', '00000000-0000-0000-0000-000000000102', 5.0, CURRENT_TIMESTAMP);  -- Fernanda fazendo Rota Mobile

