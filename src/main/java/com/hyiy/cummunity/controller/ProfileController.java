package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action",value = "")String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "2")Integer size){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
            return "redirect:/";
        if(action.equals("question")){
            model.addAttribute("section", "question");
            model.addAttribute("sectionName", "我的问题");
        }
        else if(action.equals("replies")){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        PageDto pageDto = questionService.list(user.getId(),page,size);
        model.addAttribute("pages",pageDto);
        return "profile";
    }
}
