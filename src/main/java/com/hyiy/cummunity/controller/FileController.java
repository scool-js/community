package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.FileDTO;
import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.exception.CustomizeException;
import com.hyiy.cummunity.provider.TencentCloudProvider;
import com.qcloud.cos.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    TencentCloudProvider tencentCloudProvider;
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        FileDTO fileDTO = new FileDTO();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        ObjectMetadata objectMetadata  = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        try {
            String fileUrl= tencentCloudProvider.upload(file.getInputStream(),file.getOriginalFilename(),objectMetadata);
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        return fileDTO;
    }
}
