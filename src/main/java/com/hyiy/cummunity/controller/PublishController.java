package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.cache.TagCache;
import com.hyiy.cummunity.dto.QuestionDTO;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.Question;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {



    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id")Long id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id", id);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id")Long id,
            HttpServletRequest request,
            Model model
            ){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        if(title == null||title.equals("")){
            model.addAttribute("error", "标题不为空");
            return "publish";
        }
        if(description == null||description.equals("")){
            model.addAttribute("error", "内容不为空");
            return "publish";
        }
        if(tag == null||tag.equals("")){
            model.addAttribute("error", "标签不为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        if(user ==null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        if(!StringUtils.isEmpty(TagCache.filterValid(tag))){
            model.addAttribute("error", "输入非法标签"+TagCache.filterValid(tag));
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
