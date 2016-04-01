package org.rpowell.blockchain.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    // @ResponseBody
    public String index() {
        return "hello.html";
    }
}

