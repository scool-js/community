package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.dto.QuestionDTO;
import com.hyiy.cummunity.mapper.QuestionMapper;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.Question;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.QuestionService;
import org.h2.mvstore.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "2")Integer size){
        Cookie[] cookies =request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie!=null){
                    if(cookie.getName().equals("token")){
                        String token = cookie.getValue();
                        User user =userMapper.findByToken(token);
                        if(user!=null){
                            request.getSession().setAttribute("user", user);
                        }
                        break;
                    }
                }
            }
        }

        PageDto pages = questionService.list(page,size);
        model.addAttribute("pages", pages);
        return "index";
    }
}
