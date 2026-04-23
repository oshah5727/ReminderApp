package com.studious.studious;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Handles routing for the home page and static pages like about/contact

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "homepage";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

}
