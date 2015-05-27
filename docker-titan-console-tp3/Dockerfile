FROM java:8-jre

MAINTAINER Guido Schmutz <guido.schmutz@trivadis.com>

ENV TITAN_VERSION 0.9.0-M1

RUN curl -o /tmp/titan.zip http://s3.thinkaurelius.com/downloads/titan/titan-console-$TITAN_VERSION.zip
RUN unzip -q /tmp/titan.zip -d /opt && rm /tmp/titan.zip

ENV TITAN_HOME /opt/titan-console-$TITAN_VERSION
WORKDIR $TITAN_HOME

VOLUME ["/conf","/data"]
ADD start-gremlin-console.sh /opt/start-gremlin-console.sh

CMD ["/opt/start-gremlin-console.sh"]
