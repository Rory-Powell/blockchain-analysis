package org.rpowell.blockchain.spring.controllers;

import org.rpowell.blockchain.spring.services.IGraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

@Controller
public class GraphController {

    private static final Logger log = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    private IGraphService graphService;

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    public String listAddresses(Model model){
        model.addAttribute("addresses", graphService.getAllAddresses());
        log.info("Returning all addresses");
        return "addresses";
    }

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public String getAddress(Model model){
        return "address";
    }

    @RequestMapping("/graph")
    public Map<String, Object> graph() {
        return graphService.graph("1ByMhDLywwXqdqBghb5MuA4Jy8KM5R4d3V", 2);
    }
}
