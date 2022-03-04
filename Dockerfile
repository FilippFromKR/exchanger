FROM openjdk:15
ADD ./target/alpha_bank_t-0.0.1-SNAPSHOT.jar exchanger.jar
ENTRYPOINT [ "java", "-jar", "exchanger.jar"]