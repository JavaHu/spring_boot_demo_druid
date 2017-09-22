package com.springboot.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hu.
 * @date 2017/9/21
 * @time 16:56
 */
@Component
public abstract class MysqlRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询的通用方法
     *
     * @param sql
     * @param paramsValue
     */
    public <T> List<T> query(String sql, Object[] paramsValue, Class<T> tClass) {

        RowMapper<T> rowMapper = null;
        //  获取连接
        rowMapper = new BeanPropertyRowMapper<T>(tClass);

        // 返回的集合
        return jdbcTemplate.query(sql, rowMapper, paramsValue);

    }

    /**
     * 获取分页数据
     *
     * @param sql
     * @param paramsValue
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> List<T> queryPage(String sql, Object[] paramsValue, Class<T> tClass, int pageNo, int pageSize) {

        RowMapper<T> rowMapper = null;
        //  获取连接
        rowMapper = new BeanPropertyRowMapper<T>(tClass);

        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append(" limit " + ((pageNo - 1) * pageSize) + "," + pageSize);
        // 返回的集合
        return jdbcTemplate.query(sqlBuilder.toString(), rowMapper, paramsValue);

    }

    public int getCount(String sql, Object[] paramsValue) {
        return jdbcTemplate.queryForObject(sql, Integer.class, paramsValue);
    }

    public <T> T getOne(String sql, Object[] paramsValue, Class<T> tClass) throws RuntimeException {
        // 对象
        RowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(tClass);
        return jdbcTemplate.queryForObject(sql, rowMapper, paramsValue);
    }


    /**
     * 更新的通用方法
     *
     * @param sql         更新的sql语句(update/insert/delete)
     * @param paramsValue sql语句中占位符对应的值(如果没有占位符，传入null)
     */
    public int update(String sql, Object[] paramsValue) throws RuntimeException {
        // 执行更新
        return jdbcTemplate.update(sql, paramsValue);
    }


}
