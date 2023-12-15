FROM openjdk:17
WORKDIR /carsystem

COPY carsystem/build/lib/* build/lib/

COPY carsystem/build/libs/carsystem.jar build/

WORKDIR /carsystem/build
ENTRYPOINT java -jar carsystem.jar