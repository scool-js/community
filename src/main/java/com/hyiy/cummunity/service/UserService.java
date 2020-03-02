package com.hyiy.cummunity.service;

import com.hyiy.cummunity.mapper.QuestionExtMapper;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.User;
import com.hyiy.cummunity.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUser =userMapper.selectByExample(example);
        if(dbUser.size()==0){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            dbUser.get(0).setGmtModified(System.currentTimeMillis());
            dbUser.get(0).setAvatarUrl(user.getAvatarUrl());
            dbUser.get(0).setName(user.getName());
            dbUser.get(0).setToken(user.getToken());
            User user1 = new User();
            user1.setGmtModified(System.currentTimeMillis());
            user1.setAvatarUrl(user.getAvatarUrl());
            user1.setName(user.getName());
            user1.setToken(user.getToken());
            UserExample example1 = new UserExample();
            example1.createCriteria().andIdEqualTo(dbUser.get(0).getId());
            userMapper.updateByExampleSelective(user,example1);
        }
    }
}
