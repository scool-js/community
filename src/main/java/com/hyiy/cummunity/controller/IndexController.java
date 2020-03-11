package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "2")Integer size,
                        @RequestParam(name = "search",required = false)String search){


        PageDto pages = questionService.list(search,page,size);
        model.addAttribute("pages", pages);
        model.addAttribute("search", search);
        return "index";
    }
}
