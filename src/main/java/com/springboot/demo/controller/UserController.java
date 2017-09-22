package com.springboot.demo.controller;

import com.springboot.demo.dao.UserDao;
import com.springboot.demo.po.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hu.
 * @date 2017/9/21
 * @time 17:16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserDao userDao;

    @RequestMapping("/index")
    public String index() {
        return "hello spring boot.....";
    }

    @RequestMapping("/getUsers")
    public List<UserInfo> getUsers() {

        List<UserInfo> users = userDao.getUsers();
        return users;
    }
}
