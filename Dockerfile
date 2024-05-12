FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY /app .

RUN /app gradle installDist

CMD ./build/install/app/bin/app