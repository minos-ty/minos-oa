package com.minos.oa.dao;

import com.minos.oa.entity.Notice;

import java.util.List;

/**
 * @author minos
 * @date 2021/3/19 15:30
 */
public interface NoticeDao {
    public void insert(Notice notice);
    public List<Notice> selectByReceiverId(Long receiverId);
}
