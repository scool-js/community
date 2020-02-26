package com.hyiy.cummunity.service;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.dto.QuestionDTO;
import com.hyiy.cummunity.mapper.QuestionMapper;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.Question;
import com.hyiy.cummunity.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;
    public PageDto list(Integer page, Integer size) {
        PageDto pageDto = new PageDto();
        Integer totalCount = questionMapper.count();
        pageDto.setPages(totalCount,page,size);

        if(page>pageDto.getPageCount()){
            page = pageDto.getPageCount();
        }
        if (page<1)
            page = 1;
        Integer offset = size*(page-1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question:questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDto.setQuestions(questionDTOS);
        pageDto.setPage(page);
        return pageDto;
    }

    public PageDto list(Integer userId,Integer page, Integer size){
        PageDto pageDto = new PageDto();
        Integer totalCount = questionMapper.countByUserId(userId);
        pageDto.setPages(totalCount,page,size);

        if(page>pageDto.getPageCount()){
            page = pageDto.getPageCount();
        }
        if (page<1)
            page = 1;
        Integer offset = size*(page-1);
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question:questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDto.setQuestions(questionDTOS);
        pageDto.setPage(page);
        return pageDto;
    }
}
