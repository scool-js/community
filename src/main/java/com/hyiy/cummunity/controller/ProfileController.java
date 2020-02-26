package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ProfileController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action",value = "")String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "2")Integer size){
        Cookie[] cookies =request.getCookies();
        User user =null;
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie!=null){
                    if(cookie.getName().equals("token")){
                        String token = cookie.getValue();
                        user =userMapper.findByToken(token);
                        if(user!=null){
                            request.getSession().setAttribute("user", user);
                        }
                        break;
                    }
                }
            }
        }
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
