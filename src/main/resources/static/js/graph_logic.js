function idIndex(a,id) {
    for (var i=0;i<a.length;i++)
    {if (a[i].id == id) return i;}
    return null;
}

function dragstart(d) {
    d3.select(this).classed("fixed", d.fixed = true);
}

function draw_forced_directed(data){

    //Creating graph object
    var nodes=[], links=[];
    data.results[0].data.forEach(function (row) {
        row.graph.nodes.forEach(function (n)
        {
            if (idIndex(nodes,n.id) == null)
                nodes.push({id:n.id, label:n.labels[0], title:n.properties.name});
        });
        links = links.concat( row.graph.relationships.map(function(r) {
            return {source:idIndex(nodes,r.startNode),target:idIndex(nodes,r.endNode),type:r.type, value:1};
        }));
    });

    graph = {nodes:nodes, links:links};


    // force layout setup
    var width = 1100, height = 800;
    var radius = 6;

    var fill = d3.scale.category20();

    var force = d3.layout.force()
        .gravity(1.0)
        .charge(-300)
        .linkDistance(100)
        .size([width, height]);

    var svg = d3.select("#graph").append("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("pointer-events", "all");

    force.nodes(graph.nodes).links(graph.links).start();


    var link = svg.selectAll(".link") // render relationships as lines
        .data(graph.links).enter()
        .append("line").attr("class", "link");

    //color assignment
    var get_color = {"Address": "#E81042", "Wallet": "#80e810", "Transaction": "#10DDE8"};

    var drag = force.drag()
        .on("dragstart", dragstart);

    // render nodes as circles, css-class from label
    var node = svg.selectAll(".node")
        .data(graph.nodes).enter()
        .append("circle")
        .attr("class", function (d) { return "node " + d.label })
        .attr("r", radius)
        .attr("fill", function (d){return get_color[d.label]})
        .call(drag);

    // html title attribute for title node-attribute
    node.append("title")
        .text(function (d) { return d.title; });

    // force feed algo ticks for coordinate computation
    force.on("tick", function() {

        link.attr("x1", function(d) { return d.source.x; })
            .attr("stroke", "#999")
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node.attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; });
    });
}