package com.hyiy.cummunity.service;

import com.hyiy.cummunity.dto.NotificationDTO;
import com.hyiy.cummunity.dto.PageDto;
import com.hyiy.cummunity.enums.NotificationStatusEnum;
import com.hyiy.cummunity.enums.NotificationTypeEnum;
import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.exception.CustomizeException;
import com.hyiy.cummunity.mapper.NotificationMapper;
import com.hyiy.cummunity.mapper.UserMapper;
import com.hyiy.cummunity.model.Notification;
import com.hyiy.cummunity.model.NotificationExample;
import com.hyiy.cummunity.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDto list(Long id, Integer page, Integer size) {
        PageDto<NotificationDTO> pageDto = new PageDto();
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(id);
        Integer totalCount = (int)notificationMapper.countByExample(example1);
        pageDto.setPages(totalCount,page,size);

        if(page>pageDto.getPageCount()){
            page = pageDto.getPageCount();
        }
        if (page<1)
            page = 1;
        Integer offset = size*(page-1);
        NotificationExample example2 = new NotificationExample();
        example2.createCriteria().andReceiverEqualTo(id);
        example2.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example2,new RowBounds(offset,size));


        if(notifications.size()==0){
            return pageDto;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notifacationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notifacationDTO);
            notifacationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notifacationDTO);
        }
        pageDto.setData(notificationDTOS);
        return pageDto;
    }

    public Long unreadCount(Long id) {
        NotificationExample unreadExample = new NotificationExample();
        unreadExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(unreadExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification.getReceiver()!=user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notifacationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notifacationDTO);
        notifacationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notifacationDTO;
    }
}
