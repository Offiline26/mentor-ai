# 🧠 MentorAI – Plataforma de Requalificação com IA Generativa

![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 3.4](https://img.shields.io/badge/Spring_Boot-3.4-brightgreen?style=for-the-badge&logo=springboot)
![Azure](https://img.shields.io/badge/Azure-Cloud-blue?style=for-the-badge&logo=microsoftazure)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![Ollama](https://img.shields.io/badge/AI-Ollama_Phi3-black?style=for-the-badge)

> **Disruptive Architectures: IoT, IoB & Generative IA**
>
> Projeto acadêmico focado na implementação de uma solução de **Deep Learning (Generative AI)** integrada a uma arquitetura de microsserviços Java, com foco em **IoB (Internet of Behavior)** para requalificação profissional.

---

## 📋 Sobre o Projeto

O **MentorAI** é uma plataforma que utiliza Inteligência Artificial Generativa para analisar o comportamento e perfil de um profissional (cargo, habilidades atuais, objetivos e tempo disponível) e gerar, em tempo real, uma **Rota de Requalificação Personalizada**.

Diferente de soluções que apenas consomem APIs públicas, este projeto implementa uma **LLM (Large Language Model)** rodando **localmente (On-Premise)** na infraestrutura do projeto, garantindo privacidade total dos dados (IoB) e controle sobre a inferência.

### 🚀 Destaques Técnicos
* **IA On-Premise:** Execução do modelo **Microsoft Phi-3 (3.8B)** dentro de containers Docker.
* **Arquitetura Híbrida:** Backend Java Spring Boot orquestrando a comunicação com o serviço de inferência de IA.
* **Resiliência:** Configuração avançada de *timeouts* e *connection pools* para suportar inferência de Deep Learning em CPU na Nuvem Azure.
* **IoB (Internet of Behavior):** Análise de dados comportamentais do usuário para gerar insights de carreira.

---

## 🏗️ Arquitetura e Infraestrutura (Azure Cloud)

A solução está hospedada na **Microsoft Azure**, dimensionada especificamente para suportar a carga de trabalho de Deep Learning sem o custo de uma GPU dedicada.

| Componente | Especificação | Justificativa Técnica |
| :--- | :--- | :--- |
| **VM Size** | **Standard B4as_v2** (AMD EPYC) | Processador otimizado para multitarefa e operações de ponto flutuante. |
| **Recursos** | **4 vCPUs / 16 GiB RAM** | Memória dimensionada para alocar o modelo Phi-3 (4GB+) inteiramente na RAM, eliminando latência de Swap. |
| **Container AI** | **Ollama (Docker)** | Limite de memória ajustado para **12GB** no `docker-compose` para garantir estabilidade do SO. |
| **Backend** | **Spring Boot 3.4** | Executando na JVM 21 (LTS) para máxima eficiência e uso de Virtual Threads. |

---

## 💻 Estrutura do Código (Java 21 + Spring AI)

O projeto segue os padrões de **Clean Architecture** e **SOLID**. Abaixo, a documentação das classes principais desenvolvidas para a disciplina:

### 1. `MentoriaAiController.java` (Camada de Exposição)
Responsável por expor a API RESTful para o Front-end Mobile.
* **Endpoint:** `POST /api/ia/mentor/recomendacoes`
* **Função:** Recebe o DTO `RecomendacaoRotaRequest` (dados IoB), orquestra a chamada ao Service e retorna o plano de carreira estruturado.
* **Validação:** Uso estrito de `@Valid` e Bean Validation.

### 2. `MentoriaAiService.java` (Camada de Negócio & Prompt Engineering)
O núcleo da inteligência do sistema.
* **Prompt Engineering:** Utiliza *Text Blocks* do Java 21 para construir prompts estruturados, instruindo a LLM a atuar como um "Mentor Sênior".
* **Parsing Determinístico:** Implementa lógica proprietária para converter a resposta textual da IA em objetos Java estruturados (`List<String>`), garantindo integridade no JSON de resposta.
* **Sanitização:** Tratamento de nulos (`nullSafe`) para robustez.

### 3. `TimeoutConfig.java` (Resiliência de Infraestrutura)
Classe crítica desenvolvida para lidar com a latência de inferência em CPU.
* **Desafio:** Modelos de IA em CPU podem levar +40s para responder.
* **Solução:** Implementação de um `RestClientCustomizer` que define timeouts globais de **180 segundos (3 minutos)**, prevenindo `SocketTimeoutException` e garantindo a entrega da resposta.

### 4. `SecurityConfig.java` (Segurança)
Gerencia a autenticação e autorização da plataforma.
* **JWT:** Proteção stateless dos endpoints.
* **Configuração:** Liberação estratégica das rotas de IA (`/api/ia/**`) e Dados Auxiliares (`/api/cargos`) para permitir fluxo fluido no app Mobile.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
* Docker & Docker Compose instalados.
* Requisito de Hardware: Mínimo 8GB RAM (Ideal 16GB).

### Passos para Execução (Docker)

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/seu-usuario/mentor-ai.git](https://github.com/seu-usuario/mentor-ai.git)
   cd mentor-ai

2. **Inicie a Stack: O docker-compose iniciará o Backend, Banco de Dados (Oracle/H2), RabbitMQ e o Serviço de IA.**
* docker compose up -d
* Nota: Na primeira execução, o download da imagem da IA (aprox. 3GB) pode levar alguns minutos.

3. **Acompanhe os Logs:**
* docker compose logs -f mentorai-backend

## 🔌 Testando a API de IA (Exemplo)
Para validar a integração da IA Generativa e a resposta do modelo Phi-3:

**Endpoint:** POST http://localhost:8081/api/ia/mentor/recomendacoes

Body (JSON):

{
  "nomeUsuario": "Thiago",
  "objetivoProfissional": "Arquiteto de Soluções Cloud",
  "cargoAtual": "Desenvolvedor Java Senior",
  "areaAtuacao": "TI",
  "habilidadesAtuais": ["Java", "Spring Boot", "Microservices"],
  "habilidadesDesejadas": ["Azure", "Kubernetes", "DevOps"],
  "horasPorSemana": 10
}

Resposta Esperada (Gerada via Deep Learning):

{
    "resumoEstrategia": "Para transicionar de Desenvolvedor Java para Arquiteto Cloud, o foco deve ser em infraestrutura como código e orquestração...",
    "passosSugeridos": [
        "Dominar os padrões de arquitetura em nuvem (Well-Architected Framework).",
        "Aprofundar conhecimentos em orquestração de containers (K8s) e CI/CD."
    ],
    "habilidadesPrioritarias": [
        "Kubernetes Administration",
        "Terraform / IaC",
        "Azure Solutions Architect"
    ],
    "sugestoesDeCursos": [
        "Certificação AZ-305",
        "CKA (Certified Kubernetes Administrator)"
    ]
}

👨‍💻 Autores
Thiago Mendes do Nascimento - RM555352
Guilherme Gonçalves Britto  - RM558475
Vinicius Banciela           - RM558117

"A inteligência artificial não substitui o arquiteto, mas o arquiteto que domina a IA substituirá o que não a utiliza."
