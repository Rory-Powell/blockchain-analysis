package org.rpowell.blockchain.controllers;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.services.IGraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        return "index";
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    public String listAddresses(Model model){
        model.addAttribute("addresses", graphService.getAllAddresses());
        log.info("Returning all addresses");
        return "addresses";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model){
        return "admin";
    }

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public String address(Model model){
        return "address";
    }

    @RequestMapping("address/{id}")
    public String getAddress(@PathVariable String id, Model model) {
        model.addAttribute("address", id);
        List<Address> associatedAddresses = graphService.getAssociatedAddresses(id);
        model.addAttribute("associated", associatedAddresses);
        model.addAttribute("associatedCount", associatedAddresses.size());
        return "address";
    }

    @RequestMapping(value="/denied", method = RequestMethod.GET)
    public String denied () {
        return "denied";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "index";
    }

    public void updateDatabase() {

    }

    @RequestMapping(value="/shutdown")
    public String shutdownServer () {
        graphService.shutdownServer();
        return "index";
    }

    @RequestMapping(value="/start")
    public String startServer () {
        graphService.startServer();
        return "index";
    }
}
