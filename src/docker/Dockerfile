FROM dockerfile/java:oracle-java8

WORKDIR /opt/oapp-processor

COPY target/oapp-processor.jar /opt/oapp-processor/oapp-processor.jar

EXPOSE 8080
CMD ["java", "-jar", "oapp-processor.jar"]