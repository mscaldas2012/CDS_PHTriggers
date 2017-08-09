FROM openjdk
VOLUME /tmp

ADD target/phTriggers-0.0.1-SNAPSHOT.jar .

RUN sh -c 'touch /phTriggers-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","phTriggers-0.0.1-SNAPSHOT.jar"]