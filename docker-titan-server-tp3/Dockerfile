FROM java:8-jre

MAINTAINER Guido Schmutz <guido.schmutz@trivadis.com>

#ENV TITAN_VERSION 0.9.0-M1
ENV TITAN_VERSION 0.9.0-SNAPSHOT

#RUN curl -o /tmp/titan.zip http://s3.thinkaurelius.com/downloads/titan/titan-server-$TITAN_VERSION.zip
#RUN unzip -q /tmp/titan.zip -d /opt && rm /tmp/titan.zip
RUN mkdir /opt/titan-server-$TITAN_VERSION
COPY titan-server-$TITAN_VERSION* /opt/titan-server-$TITAN_VERSION/

ENV TITAN_HOME /opt/titan-server-$TITAN_VERSION
WORKDIR $TITAN_HOME

VOLUME ["/conf","/data"]
ADD start-titan-server.sh /opt/start-titan-server.sh

ADD gremlin-server-cassandra-es.yaml /opt/titan-server-$TITAN_VERSION/conf/gremlin-server-cassandra-es.yaml

EXPOSE 8182

CMD ["/opt/start-titan-server.sh"]
