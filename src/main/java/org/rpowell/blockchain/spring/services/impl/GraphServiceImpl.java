package org.rpowell.blockchain.spring.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.graph.CypherQueries;
import org.rpowell.blockchain.spring.repositories.IGraphRepository;
import org.rpowell.blockchain.spring.services.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphServiceImpl implements IGraphService {

    @Autowired
    private IGraphRepository graphRepository;

    protected GraphServiceImpl() {}

    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }

    public Object getAddressCommunity(String address) {
        return execute(CypherQueries.getAddressQuery(address, 3));
    }

    public Object execute(String query) {
        return graphRepository.execute(query);
    }

    private Map<String, Object> toD3Format(Iterator<Map<String, Object>> result) {

        List<Map<String,Object>> nodes = new ArrayList<>();
        List<Map<String,Object>> rels= new ArrayList<>();

        for (int i = 0; result.hasNext(); i++) {

            Map<String, Object> row = result.next();
            nodes.add(map("title", row.get("movie"),"label","movie"));

            int target=i;

            i++;

            for (Object name : (Collection) row.get("cast")) {

                Map<String, Object> actor = map("title", name,"label","actor");

                int source = nodes.indexOf(actor);
                if (source == -1) {
                    nodes.add(actor);
                    source = i++;
                }

                rels.add(map("source",source,"target",target));
            }
        }

        return map("nodes", nodes, "links", rels);
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<>(2);

        result.put(key1,value1);
        result.put(key2,value2);

        return result;
    }
}
