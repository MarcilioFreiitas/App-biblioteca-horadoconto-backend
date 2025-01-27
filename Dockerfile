# Etapa de build
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

# Etapa final
FROM openjdk:17-jdk-slim

EXPOSE 8080

# Copiar o JAR gerado
COPY --from=build /target/horadoconto-0.0.1-SNAPSHOT.jar app.jar

# Copiar as imagens para o diretório desejado no contêiner
COPY --from=build /src/main/resources/static/imagens/capas /usr/share/app/imagens/capas

# Copiar e usar o script de inicialização
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh", "java", "-jar", "app.jar"]
