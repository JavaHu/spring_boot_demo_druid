package com.springboot.demo;

import com.springboot.demo.common.MysqlRepository;
import com.springboot.demo.dao.UserDao;
import com.springboot.demo.po.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private MysqlRepository mysqlRepository;


    @Resource
    private UserDao userDao;


    @Test
    public void findAllUsers() {
        //   List<UserInfo> users = mysqlRepository.query("select * from w_user", null, UserInfo.class);
        List<UserInfo> users = userDao.getUsers();

        for (UserInfo user:users
             ) {
            System.out.println(user.getUserAccount());

        }
        assertNotNull(users);
        assertTrue(!users.isEmpty());

    }


}
