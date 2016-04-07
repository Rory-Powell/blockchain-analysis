package org.rpowell.blockchain.spring.controllers;

import org.rpowell.blockchain.spring.services.IGraphService;
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

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model){
        return "admin";
    }

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public String address(Model model){
        return "address";
    }

    @RequestMapping("address/{id}")
    public String getAddress(@PathVariable String id, Model model){
        model.addAttribute("address", id);
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
}
