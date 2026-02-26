package com.maxbon.springFirst.controller;

import com.maxbon.springFirst.domain.user.auth.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String MainPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        if(principalDetails != null) {
            model.addAttribute("user", principalDetails.getUser());
        }

        return "main";
    }
}
