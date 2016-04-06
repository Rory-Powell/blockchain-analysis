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

    var graph = {nodes: nodes, links: links};

    var w = 960,
        h = 500;

    var vis = d3.select("#graph").append("svg:svg")
        .attr("width", w)
        .attr("height", h);

    var force = self.force = d3.layout.force()
        .nodes(graph.nodes)
        .links(graph.links)
        .gravity(.05)
        .distance(100)
        .charge(-100)
        .size([w, h])
        .start();

    var link = vis.selectAll("line.link")
        .data(graph.links)
        .enter().append("svg:line")
        .attr("class", "link")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    var node_drag = d3.behavior.drag()
        .on("dragstart", dragstart)
        .on("drag", dragmove)
        .on("dragend", dragend);

    function dragstart(d, i) {
        force.stop() // stops the force auto positioning before you start dragging
    }

    function dragmove(d, i) {
        d.px += d3.event.dx;
        d.py += d3.event.dy;
        d.x += d3.event.dx;
        d.y += d3.event.dy;
        tick(); // this is the key to make it work together with updating both px,py,x,y on d !
    }

    function dragend(d, i) {
        d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff
        tick();
        force.resume();
    }

    //color assignment
    var get_color = {"Address": "#E81042", "Wallet": "#80e810", "Transaction": "#10DDE8"};

    var node = svg.selectAll(".node")
        .data(graph.nodes).enter()
        .append("circle")
        .attr("class", function (d) { return "node " + d.label })
        .attr("r", radius)
        .attr("fill", function (d){return get_color[d.label]})
        .call(drag);

    node.append("svg:text")
        .attr("class", "nodetext")
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function(d) { return d.name });

    force.on("tick", tick);

    function tick() {
        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    }

}
