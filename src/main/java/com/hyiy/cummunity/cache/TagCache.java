package com.hyiy.cummunity.cache;

import com.hyiy.cummunity.dto.TagDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDto> get() {
        List<TagDto> tagDtos = new ArrayList<>();
        TagDto dto = new TagDto();
        dto.setCategoryName("开发语言");
        dto.setTags(Arrays.asList("js", "html", "css", "java", "node", "python", "delphi", "go", "c", "c++", "node"));
        TagDto franework = new TagDto();
        franework.setCategoryName("平台架构");
        franework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask"));
        TagDto serve = new TagDto();
        serve.setCategoryName("服务器");
        serve.setTags(Arrays.asList("linux", "nginx", "ubuntu", "docker", "centos"));
        TagDto db = new TagDto();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "sql serve", "oracle", "redis", "sqlite"));
        tagDtos.add(serve);
        tagDtos.add(dto);
        tagDtos.add(db);
        tagDtos.add(franework);
        return tagDtos;
    }
    public static String filterValid(String tags){
        String[] split = tags.split(",");
        List<TagDto> tagDtos = get();
        List<String> collect = tagDtos.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !collect.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
