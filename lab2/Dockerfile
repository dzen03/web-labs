FROM quay.io/wildfly/wildfly
EXPOSE 23200

ADD target/web2.war /opt/jboss/wildfly/standalone/deployments/

#RUN ["rm", "/opt/jboss/wildfly/standalone/configuration/standalone.xml"]
#COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/

#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-Djboss.http.port=23200"]
