package com.springboot.demo.dao;

import com.springboot.demo.common.MysqlRepository;
import com.springboot.demo.po.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hu.
 * @date 2017/9/21
 * @time 17:09
 */
@Component
public class UserDao extends MysqlRepository {

    public List<UserInfo> getUsers() {

        List<UserInfo> users = query("select * from w_user", null, UserInfo.class);

        return users;

    }
}
