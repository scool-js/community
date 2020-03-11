package com.hyiy.cummunity.mapper;

import com.hyiy.cummunity.dto.QuestionQueryDTO;
import com.hyiy.cummunity.model.Question;
import com.hyiy.cummunity.model.QuestionExample;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionExample example1, RowBounds rowBounds);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}