

package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class GetLoginRegisterPageController {
    
    //Make login the default page@GetMapping("/")
    @GetMapping("/")
    public ModelAndView getLoginPageDefault() {
        return new ModelAndView("loginRegisterPage");
    }
    
}