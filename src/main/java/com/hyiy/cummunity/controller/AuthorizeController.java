package com.hyiy.cummunity.controller;

import com.hyiy.cummunity.dto.AccessTokenDTO;
import com.hyiy.cummunity.dto.GithubUserDTO;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    GithubProvider provider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.client.redirect_uri}")
    private String clientRedirectUri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(clientRedirectUri);
        accessTokenDTO.setState(state);
        String accessToken = provider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUserDTO = provider.getUser(accessToken);
        if(githubUserDTO !=null&&githubUserDTO.getId()!=null){
            //登陆成功，写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUserDTO.getName());
            user.setAccountId(String.valueOf(githubUserDTO.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUserDTO.getAvatarUrl());
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));

        }
        else {
            //登陆失败，重新登陆
        }
        return  "redirect:/";
    }
}
