FROM openjdk:8u191-jdk-alpine3.9


RUN mkdir -p drcofflineService
RUN mkdir -p /tmp/drcoffline/logs/
ADD lib/db2jcc4.jar drcofflineService
ADD lib/json-simple-1.1.jar drcofflineService
ADD lib/mysql-connector-java-5.1.45-bin.jar drcofflineService
ADD lib/quartz-2.2.3.jar drcofflineService
ADD lib/quartz-jobs-2.2.3.jar drcofflineService
ADD lib/slf4j-api-1.7.7.jar drcofflineService
ADD lib/slf4j-log4j12-1.7.7.jar drcofflineService
ADD lib/log4j-1.2.16.jar drcofflineService
ADD lib/c3p0-0.9.1.1.jar drcofflineService
ADD build/libs/drcoffline.jar drcofflineService
#ADD src/log4j.xml drcofflineService
ADD src/queries.properties drcofflineService
ADD src/settings.properties drcofflineService

# Certificates
ADD carootcert.pem $JAVA_HOME/lib/security/
ADD caintermediatecert.pem $JAVA_HOME/lib/security/
RUN keytool -noprompt -storepass changeit -import -alias ibmroot -keystore $JAVA_HOME/lib/security/cacerts -file $JAVA_HOME/lib/security/carootcert.pem
RUN keytool -noprompt -storepass changeit -import -alias ibmintermediate -keystore $JAVA_HOME/lib/security/cacerts -file $JAVA_HOME/lib/security/caintermediatecert.pem

HEALTHCHECK --interval=5m --timeout=3s --start-period=60s --retries=3 \ 
   CMD ps|grep -v grep | grep "main.java.App" > /dev/null || exit 1

WORKDIR drcofflineService
CMD java -classpath '*' main.java.App
