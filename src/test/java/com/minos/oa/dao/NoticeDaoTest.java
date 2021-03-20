package com.minos.oa.dao;

import com.minos.oa.entity.Notice;
import com.minos.oa.utils.MybatisUtils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author minos
 * @date 2021/3/19 16:31
 */
public class NoticeDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            NoticeDao mapper = sqlSession.getMapper(NoticeDao.class);
            Notice notice = new Notice();

            notice.setReceiverId(1L);
            notice.setContent("good");
            notice.setCreateTime(new Date());

            mapper.insert(notice);

            return null;
        });
    }
}