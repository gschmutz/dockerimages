Runs the Titan server from the Titan Graph Database's "all" distribution.

# Basic Usage

To start the Gremlin console with the default configuration files available:

    docker run -it gschmutz/titan-server

In order to adjust or further define property files to use within Gremlin,
attach a host directory to the container's `/conf` such as

    docker run -it -v $(pwd)/conf:/conf gschmutz/titan-gremlin

After running once your host directory will be populated with the distribution-default
configuration files. Modify those or add to them and they will available during
the next use of gremlin.

# Connecting to Cassandra and Elasticsearch Containers

First start containers for Cassandra and Elasticsearch, where the `--name` you choose
can be arbitrary or left off to use a generated name.
_Note: Cassandra's Thrift port is exposed to allow for external usage, such as Titan Browser._

    docker run -d --name cass -e PUBLISH_AS=192.168.59.103 -p 9160:9160 cass 
    docker run -d --name es -p 9300:9300 -e PUBLISH_AS=192.168.59.103:9300 itzg/elasticsearch

Replacing `192.168.59.103` with your Docker host's LAN IP address.

Now start Gremlin linking the containers to the respective aliases

* `--link <container>:cass`
* `--link <container>:es`

such as

    docker run -it --rm --link cass:cass --link es:es itzg/titan-gremlin

and with that you can follow the
[Graph of the Gods example](http://s3.thinkaurelius.com/docs/titan/current/getting-started.html), such as

    gremlin> GraphOfTheGodsFactory.load(g)
    gremlin> saturn = g.V.has('name','saturn').next()
    ==>v[256]
    gremlin> saturn.map()
    ==>name=saturn
    ==>age=10000
    gremlin> saturn.in('father').in('father').name
    ==>hercules
