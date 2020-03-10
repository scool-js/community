package com.hyiy.cummunity.advice;

import com.alibaba.fastjson.JSON;
import com.hyiy.cummunity.dto.ResultDto;
import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model, HttpServletResponse response){
        String contentType = request .getContentType();
        if(contentType.equals("application/json")){
            ResultDto resultDto = new ResultDto();
            if(ex instanceof CustomizeException){
                resultDto = ResultDto.errorOf((CustomizeException) ex);
            }
            else {
                resultDto =  ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter  writer= response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }
        else {
            if(ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
            }else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR);
            }
            return new ModelAndView("error");
        }

    }

}
