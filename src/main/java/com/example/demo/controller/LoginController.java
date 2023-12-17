package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/index")
    public String registration(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            model.addAttribute("currentUserName", ((User) principal).getUsername());
            System.out.println(((User) principal).getUsername());
        }
        return "index";
    }
}
