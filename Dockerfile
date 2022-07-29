FROM openjdk:11
RUN apt update
RUN apt install -y maven pandoc texlive
COPY ./src ./src
COPY ./pom.xml ./
RUN mvn package
CMD sh target/bin/simplewebapp
