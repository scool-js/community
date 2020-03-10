package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.CommentDto;
import com.hyiy.cummunity.dto.QuestionDTO;
import com.hyiy.cummunity.enums.CommentTypeEnum;
import com.hyiy.cummunity.service.CommentService;
import com.hyiy.cummunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        questionService.incView(id);
        List<CommentDto> commentDtos = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentDtos);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
