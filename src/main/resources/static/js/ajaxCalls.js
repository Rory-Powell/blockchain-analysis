

function drawGraph(address) {
    var post_request = {
        "statements": [
            {
                "statement": "MATCH (n:Address) WHERE n.Addr = '" + address + "' OPTIONAL MATCH path=n-[*1..4]-(c) RETURN path LIMIT 2000",
                "resultDataContents": [
                    "graph"
                ]
            }
        ]
    };

    $.ajax({
        type: "POST",
        accept: "application/json",
        contentType:"application/json; charset=utf-8",
        url: "http://localhost:7474/db/data/transaction/commit",
        headers: {"Authorization":"Basic bmVvNGo6YmxvY2tjaGFpbg=="},
        data: JSON.stringify(post_request),
        success: function(data){
            draw_forced_directed(data);
        },
        failure: function(msg){console.log("failed")}
    });
}

