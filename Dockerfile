# 1ª fase: build da aplicação
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /usr/app

# copia o projeto inteiro (não só o pacote Java)
COPY . .

# gera o jar Spring Boot (pode usar build ou bootJar)
RUN gradle clean bootJar -x test

# 2ª fase: imagem leve só com o JAR
FROM eclipse-temurin:21-jre-alpine
WORKDIR /usr/app

# ajuste o nome do jar se for diferente (confere em build/libs)
COPY --from=build /usr/app/build/libs/mentor-ai-0.0.1-SNAPSHOT.jar app.jar

# Render usa a variável PORT — não precisa fixar a porta, mas pode expor 8080
EXPOSE 8081

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
