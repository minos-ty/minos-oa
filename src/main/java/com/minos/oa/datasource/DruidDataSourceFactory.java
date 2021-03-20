package com.minos.oa.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author minos
 * @date 2021/3/18 16:48
 */
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {
    public DruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }

    @Override
    public DataSource getDataSource() {
        try {
            //初始化Druid数据源
            ((DruidDataSource) this.dataSource).init();
        } catch (SQLException e ) {
           throw new RuntimeException(e);
        }
        return this.dataSource;
    }
}
