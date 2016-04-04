// The query
var query= {"statements":[{"statement":"MATCH p=(n)-->(m)<--(k),(n)--(k) RETURN p Limit 100",
    "resultDataContents":["graph","row"]}]};

//the helper function provided by neo4j documents
function idIndex(a,id) {
    for (var i=0;i<a.length;i++) {
        if (a[i].id == id) return i;}
    return null;
}

// jQuery ajax call
var request = $.ajax({
    type: "POST",
    url: "http://localhost:7474/db/data/transaction/commit",
    accepts: { json: "application/json" },
    dataType: "json",
    contentType:"application/json",
    data: JSON.stringify(query),

    //now pass a callback to success to do something with the data
    success: function (data) {
        // parsing the output of neo4j rest api
        data.results[0].data.forEach(function (row) {
            row.graph.nodes.forEach(function (n) {
                if (idIndex(nodes,n.id) == null){
                    nodes.push({id:n.id,label:n.labels[0],title:n.properties.name});
                }
            });
            links = links.concat( row.graph.relationships.map(function(r) {
                // the neo4j documents has an error : replace start with source and end with target
                return {source:idIndex(nodes,r.startNode),target:idIndex(nodes,r.endNode),type:r.type};
            }));
        });
        var graph = {nodes:nodes, links:links};
    }
});