FROM adoptopenjdk/openjdk16:alpine-jre
ARG JAR_FILE=target/SongCreator-1.0-SNAPSHOT.jar
WORKDIR /opt/song-app
COPY ${JAR_FILE} song-app.jar
ENTRYPOINT ["java","-jar","song-app.jar"]