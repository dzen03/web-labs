FROM quay.io/wildfly/wildfly
EXPOSE 23200

ADD target/web3-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/web3.war

#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-Djboss.http.port=23200"]