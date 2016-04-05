function idIndex(a,id) {
    for (var i=0;i<a.length;i++)
    {if (a[i].id == id) return i;}
    return null;
}

function draw_forced_directed_bounded(data){

    //Creating graph object
    var nodes=[], links=[];
    data.results[0].data.forEach(function (row) {
        row.graph.nodes.forEach(function (n)
        {
            if (idIndex(nodes,n.id) == null)
                nodes.push({id:n.id,label:n.labels[0],title:n.properties.name});
        });
        links = links.concat( row.graph.relationships.map(function(r) {
            return {source:idIndex(nodes,r.startNode),target:idIndex(nodes,r.endNode),type:r.type, value:1};
        }));
    });

    graph = {nodes:nodes, links:links};

    var width = 960,
        height = 500,
        radius = 10;

    var fill = d3.scale.category20();

    var force = d3.layout.force()
        .gravity(.05)
        .charge(-240)
        .linkDistance(50)
        .size([width, height]);

    var svg = d3.select("#graph").append("svg")
        .attr("width", width)
        .attr("height", height);

    var link = svg.selectAll("line")
        .data(graph.links)
        .enter().append("line");

    var node = svg.selectAll("circle")
        .data(graph.nodes)
        .enter().append("circle")
        .attr("r", radius - .75)
        .style("fill", function(d) { return fill(d.group); })
        .style("stroke", function(d) { return d3.rgb(fill(d.group)).darker(); })
        .call(force.drag);

    force
        .nodes(graph.nodes)
        .links(graph.links)
        .on("tick", tick)
        .start();

    function tick() {
        node.attr("cx", function(d) { return d.x = Math.max(radius, Math.min(width - radius, d.x)); })
            .attr("cy", function(d) { return d.y = Math.max(radius, Math.min(height - radius, d.y)); });

        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });
    }
}
