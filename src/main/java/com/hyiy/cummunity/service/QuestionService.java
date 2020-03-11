package com.hyiy.cummunity.service;

import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.dto.QuestionDTO;
import com.hyiy.cummunity.dto.QuestionQueryDTO;
import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.exception.CustomizeException;
import com.hyiy.cummunity.mapper.QuestionExtMapper;
import com.hyiy.cummunity.mapper.QuestionMapper;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.Question;
import com.hyiy.cummunity.model.QuestionExample;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.model.UserExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    public PageDto list(String search ,Integer page, Integer size) {
        if(!StringUtils.isEmpty(search)){
            String[] tags = search.split(" ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PageDto pageDto = new PageDto();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        pageDto.setPages(totalCount,page,size);

        if(page>pageDto.getPageCount()){
            page = pageDto.getPageCount();
        }
        if (page<1)
            page = 1;
        Integer offset = size*(page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question:questions){
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> user = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user.get(0));
            questionDTOS.add(questionDTO);
        }
        pageDto.setData(questionDTOS);
        pageDto.setPage(page);
        return pageDto;
    }

    public PageDto list(Long userId,Integer page, Integer size){
        PageDto pageDto = new PageDto();
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example1);
        pageDto.setPages(totalCount,page,size);

        if(page>pageDto.getPageCount()){
            page = pageDto.getPageCount();
        }
        if (page<1)
            page = 1;
        Integer offset = size*(page-1);
        QuestionExample example2 = new QuestionExample();
        example2.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExample(example2);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question:questions){
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> user = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user.get(0));
            questionDTOS.add(questionDTO);
        }
        pageDto.setData(questionDTOS);
        pageDto.setPage(page);
        return pageDto;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOTE_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(question.getCreator());
        List<User> user = userMapper.selectByExample(example);
        questionDTO.setUser(user.get(0));
        return  questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }
        else {
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion,example);
            if(updated!=1){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOTE_FOUND);
            }
        }

    }

    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isEmpty(questionDTO.getTag())){
            return  new ArrayList<>();
        }
        String str = StringUtils.replace(questionDTO.getTag(),",","|");
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(str);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS =  questions.stream().map(question1 -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(question1, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
