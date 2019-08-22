package com.winterchen.controller;

import com.winterchen.model.UserDomain;
import com.winterchen.pojo.Response;
import com.winterchen.pojo.ResponseHasData;
import com.winterchen.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/add")
    public int addUser(UserDomain user){
        return userService.addUser(user);
    }

    @GetMapping("/all")
    public Object findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize){
        return userService.findAllUser(pageNum,pageSize);
    }

    //存储String
    @PostMapping("/setValue")
    public Response setValue(){
        ResponseHasData response = new ResponseHasData();
        if(!template.hasKey("测试1")){
            template.opsForValue().set("测试1", "我是测试4");
            response.setMsg("使用redis缓存保存数据成功");
            response.setStatus(0);
            return response;
        }else{
            //template.delete("shabao");
            response.setMsg("key已存在");
            response.setStatus(1);
            return response;
        }
    }

    @PostMapping("/getValue")
    public String getValue(){

        if(!template.hasKey("shabao")){
            return "key不存在，请先保存数据";
        }else{
            String shabao = template.opsForValue().get("shabao");//根据key获取缓存中的val
            return "获取到缓存中的数据：shabao="+shabao;
        }
    }

    //存储集合
    @PostMapping("/setList")
    public String setList(){
        List list1 = new ArrayList();
        list1.add("1");
        list1.add("2");
        redisTemplate.opsForValue().set("jihe", list1);
        return "成功";
    }

    @PostMapping("/getList")
    public String getList(){
        List list = (List)redisTemplate.opsForValue().get("jihe");
        for (Object a:list){
            System.out.println(a.toString());
        }
        return "成功";
    }

    //存储对象
    @PostMapping("/setUser")
    public String setUser(){
        UserDomain user = new UserDomain();
        user.setUserName("张三");
        user.setPassword("123");
        user.setPhone("17355156565");
        user.setUserId(1);
        redisTemplate.opsForValue().set("userkey", user);
        return "成功";
    }

    @PostMapping("/getUser")
    public String getUser(){
        UserDomain user = (UserDomain)redisTemplate.opsForValue().get("userkey");
        System.out.println(user);
        return "成功";
    }
}
