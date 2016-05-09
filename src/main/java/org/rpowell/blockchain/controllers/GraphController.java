package org.rpowell.blockchain.controllers;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.services.IGraphService;
import org.rpowell.blockchain.util.graph.DatabaseForm;
import org.rpowell.blockchain.util.graph.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

@Controller
public class GraphController {

    private static final Logger log = LoggerFactory.getLogger(GraphController.class);

    public static boolean dbUpdated = true;

    private int addressCount;
    private int nodeCount;
    private int transactionCount;
    private int ownerCount;

    @Autowired
    private IGraphService graphService;

    /**
     * The home screen.
     * @param model The model.
     * @return      The HTML file name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        if (dbUpdated) {
            addressCount = graphService.getAddressCount();
            nodeCount = graphService.getNodeCount();
            transactionCount = graphService.getTransactionCount();
            ownerCount = graphService.getOwnerCount();

            dbUpdated = false;
        }

        model.addAttribute("addressCount", addressCount);
        model.addAttribute("nodeCount", nodeCount);
        model.addAttribute("transactionCount", transactionCount);
        model.addAttribute("ownerCount", ownerCount);

        if(nodeCount == 0) {
            model.addAttribute("isPopulated", false);
            model.addAttribute("currentBlockCount", graphService.getCurrentBlockCount());
            model.addAttribute("databaseForm", new DatabaseForm());
        } else {
            model.addAttribute("isPopulated", true);
        }
        
        addSearchForm(model);
        return "index";
    }

    /**
     * The search endpoint.
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute SearchForm searchForm){
        return "redirect:/address/" + searchForm.getSearchAddress();
    }

    /**
     * The addresses screen.
     * @param model The model.
     * @return      The HTML file name.
     */
    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    public String addresses(Model model){
        model.addAttribute("addresses", graphService.getAllAddresses());
        addSearchForm(model);
        log.info("Returning all addresses");
        return "addresses";
    }

    /**
     * The owners screen.
     * @param model The model.
     * @return      The HTML file name.
     */
    @RequestMapping(value = "/owners", method = RequestMethod.GET)
    public String owners(Model model){
        model.addAttribute("owners", graphService.getAllOwners());
        addSearchForm(model);
        log.info("Returning all owners");
        return "owners";
    }

    /**
     * The address screen.
     * @param id    The hash of the address.
     * @param model The model.
     * @return      The HTML file name.
     */
    @RequestMapping("/address/{id}")
    public String address(@PathVariable String id, Model model) {
        model.addAttribute("address", id);
        List<Address> associatedAddresses = graphService.getAssociatedAddresses(id);
        model.addAttribute("associated", associatedAddresses);
        model.addAttribute("associatedCount", associatedAddresses.size());
        addSearchForm(model);
        return "address";
    }

    /**
     * Populate the database server with an amount of blocks.
     * @param databaseForm    The form data for the request.
     */
    @RequestMapping(value="/populateDB", method = RequestMethod.POST)
    public String populateDatabase(@ModelAttribute DatabaseForm databaseForm) {
        graphService.populateDatabase(databaseForm.getCount());
        dbUpdated = true;
        return "redirect:/";
    }

    /**
     * Update the database server with the most recent blocks.
     */
    @RequestMapping(value="/updateDB", method = RequestMethod.POST)
    public String updateDatabase() {
        graphService.updateDatabase();
        dbUpdated = true;
        return "redirect:/";
    }

    /**
     * Utility method for adding the required search attribute to a model.
     * @param model The model.
     */
    public void addSearchForm(Model model) {
        model.addAttribute("searchForm", new SearchForm());
    }
}
