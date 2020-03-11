package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.CommentCreateDto;
import com.hyiy.cummunity.dto.CommentDto;
import com.hyiy.cummunity.dto.ResultDto;
import com.hyiy.cummunity.enums.CommentTypeEnum;
import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.model.Comment;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDto dto,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user ==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(dto==null || StringUtils.isEmpty(dto.getContent())){
            return ResultDto.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(dto.getParentId());
        comment.setContent(dto.getContent());
        comment.setType(dto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment,user);
        return ResultDto.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List> comments(@PathVariable(name = "id") Long id){
        List<CommentDto> commentDTOs= commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDto.okOf(commentDTOs);
    }
}
