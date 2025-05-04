# Know-your-fan

**Know-your-fan** é uma aplicação desenvolvida para a organização **FURIA**, com o objetivo de conhecer melhor seus fãs. Através de login, integração com a Twitch, envio de documentos e respostas personalizadas, os usuários demonstram o quanto são fãs da FURIA e acumulam pontos com base em suas preferências.

## 🚀 Tecnologias utilizadas

### Backend

- Java 21
- Spring Boot
- Spring Security com JWT
- OAuth2 com integração à Twitch
- MySQL (via Docker)
- Tesseract (executável para leitura de imagem)

### Frontend

- Vite
- React
- Tailwind CSS

---

## 🔐 Funcionalidades

- **Autenticação JWT com roles (usuário e admin)**.
- **Login/Registro de usuários.**
- **Edição de perfil com gostos e preferências (jogador favorito, plataforma, etc).**
- **Sistema de pontuação baseado em engajamento do fã.**
- **Envio de foto de documento**: o backend faz OCR via Tesseract e valida o nome do usuário.
- **Conexão com Twitch via OAuth2**: verifica se o usuário segue a FURIA.
- **Geração de PDF do perfil do usuário.**
- **Dashboard exclusiva para admin** com gráficos sobre os fãs (preferências, jogadores favoritos, etc).
- **Geração de PDF da tela de gráficos.**

---

## 🧪 Usuário administrador mockado

Para testes administrativos:

- **Login**: `ADMIN`
- **Senha**: `admin`

---

## 🐳 Rodando a aplicação

### Pré-requisitos:

- Docker
- Docker Compose
- Java 21
- Node 22
- MySQL (opcional)
- Tesseract https://github.com/tesseract-ocr/tesseract
- Cadastro na Twitch: https://dev.twitch.tv/
  - Crie uma conta na Twitch dev
  - Registre um aplicativo
  - Siga o exemplo da url do .env-examplo
  - Copie o Client_id e Client_secret e cole no .env seguindo o exemplo

### Passos:

```bash
# Clone o projeto
git clone https://github.com/CarlosSerafimm/Know-Your-Fan.git
cd know-your-fan

```

### Backend

```bash

# Crie o arquivo .env na raiz do backend com as variáveis necessárias
# Siga o exemplo do .env-exemplo
cd backend

# Certifique-se de ter o Tesseract instalado localmente
# Dentro do arquivo abaixo ajuste a linha abaixo conforme o necessario:
# src/main/java/backend/service/ValidacaoService.java

# tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");


# Inicie o backend (Spring Boot)
./mvnw spring-boot:run

# Build e up
docker-compose up --build
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```
## 📡 Tabela de Rotas da API - Know-your-fan

| Método | Endpoint                     | Descrição                                                                |
|--------|------------------------------|--------------------------------------------------------------------------|
| POST   | `/auth/register`             | Registro de novo usuário                                                 |
| POST   | `/auth/login`                | Login e retorno de JWT                                                   |
| GET    | `/fan/pesquisar`                  | Retorna os dados do usuário logado                        |
| GET    | `/fan/listar`                  | Retorna os dados de todos os usuarios                       |
| POST   | `/fan/validart`     | Envia imagem de documento para validação via Tesseract                   |
| PUT   | `/fan`     | Atualiza os dados e preferencias do usuario logado                   |
| GET    | `/oauth/twitch/callback` | Conecta o usuario à sua conta twitch e verifica se segue a FURIA                   |
| GET    | `/api/graficos/dadosAgregados`              | Retorna a quantidade dos dados das preferencias para a criação de graficos                       |
| GET    | `/enums`           | Retorna todos os enums de preferencias                      |

## 🔑 Armazenamento do Token JWT

- O token JWT é **armazenado no `localStorage`**
- Enviado automaticamente no header:
  ```http
  Authorization: Bearer <token>
  ```

---


## 📄 Desenvolvido por:

### Carlos Serafim

[Linkedin](https://www.linkedin.com/in/carlos-serafim-951049306/)
