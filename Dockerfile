FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.2

RUN apt-get update && apt-get install -yq unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

ENV JDBC_DATABASE_URL=jdbc:postgresql://dpg-cp15j68l6cac73eofa5g-a:5432/postgres?password=yZvQYakk6ZD9VDHGCOcdR9VHspneOtkz&user=postgresql_p72_user

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app