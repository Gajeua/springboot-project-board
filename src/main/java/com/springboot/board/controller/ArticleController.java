package com.springboot.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles") // 매핑 url을 디자인한다.
@Controller
public class ArticleController {

    @GetMapping("")
    public String articles(ModelMap map){
        map.addAttribute("articles", List.of());
        return "articles/index";
    }

}
