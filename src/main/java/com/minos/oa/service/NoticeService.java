package com.minos.oa.service;

import com.minos.oa.dao.NoticeDao;
import com.minos.oa.entity.Notice;
import com.minos.oa.utils.MybatisUtils;

import java.util.List;

/**
 * @author minos
 * @date 2021/3/20 13:55
 */
public class NoticeService {
    public List<Notice> getNoticeList(Long receiverId) {
        return (List<Notice>) MybatisUtils.executeQuery(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            return noticeDao.selectByReceiverId(receiverId);
        });
    }
}
