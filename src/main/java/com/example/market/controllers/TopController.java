package com.example.market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.market.entities.Item;
import com.example.market.entities.User;
import com.example.market.security.SimpleLoginUser;
import com.example.market.services.ItemService;

@Controller
public class TopController {

    @Autowired
    private ItemService itemService;

    // トップページ
    @GetMapping("/")
    public String Top(
        Model model
    ) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken == false) {
            SimpleLoginUser simpleLoginUser = (SimpleLoginUser)authentication.getPrincipal();
            User loginUser = simpleLoginUser.getUser();
            List<Item> items = itemService.getOtherUsersItem(loginUser);
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("items", items);
        }
        model.addAttribute("title", "トップページ");
        model.addAttribute("main", "layout/top_page::main");
        return "layout/layout";
    }
    
}
