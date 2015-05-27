// Open Titan and its ManagementSystem
titanGraph = TitanFactory.open('conf/titan-cassandra.properties')
schema = titanGraph.openManagement()
// Vertex Labels
schema.makeVertexLabel("song").make()
schema.makeVertexLabel("artist").make()
// Property Keys
schema.makePropertyKey("songType").dataType(String.class).make()
schema.makePropertyKey("performances").dataType(Integer.class).make()
nameKey = schema.makePropertyKey("name").dataType(String.class).make()
weightKey = schema.makePropertyKey("weight").dataType(Integer.class).make()
// Edge Labels
schema.makeEdgeLabel("sungBy").make()
schema.makeEdgeLabel("writtenBy").make()
followedLabel = schema.makeEdgeLabel("followedBy").make()
// Indices
schema.buildIndex("verticesByName", Vertex.class).addKey(nameKey).unique().buildCompositeIndex()
schema.buildEdgeIndex(followedLabel, "followsByWeight", Direction.BOTH, Order.decr, weightKey)
// Commit schemata and release resources
schema.commit()
titanGraph.close()
