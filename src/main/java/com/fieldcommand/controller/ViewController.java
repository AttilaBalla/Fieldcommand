package com.fieldcommand.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String renderIndex() {
        return "index";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String renderAbout() {
        return "about";
    }

    @RequestMapping(value = "/releases", method = RequestMethod.GET)
    public String renderReleases() {
        return "releases";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String renderAdminSubPages(@RequestParam(value="subPage", required=false) String subPage) {
        System.out.println(subPage);

        if(subPage == null) {
            return "administration";
        }
        else {
            return "/fragments/subpages/" + subPage;
        }
    }
    //temporary
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String renderLogin() {
        return "login";
    }
}
