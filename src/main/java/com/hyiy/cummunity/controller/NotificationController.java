package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.NotificationDTO;
import com.hyiy.cummunity.enums.NotificationTypeEnum;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id",value = "")Long id,
                          Model model, HttpServletRequest request)
                         {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
            return "redirect:/";
        NotificationDTO notificationDTO =  notificationService.read(id,user);
        if(notificationDTO.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()||NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterId();
        }
        return "redirect:/";
    }
}
