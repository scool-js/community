package com.hyiy.cummunity.mapper;

import com.hyiy.cummunity.dto.GithubUserDTO;
import com.hyiy.cummunity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    @Insert("insert into user(NAME,ACCOUNT_ID,TOKEN,GMT_CREATE,GMT_MODIFIED,AVATAR_URL) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{creator}")
    User findById(Integer creator);
}
