package com.pyg.user.service.impl;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySql2OracleOutputVisitor;
import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbUserMapper;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/31 11:26
 * @description TODO
 **/

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbUserMapper userMapper;


    /**
     * 发送短信验证码
     * @param phone
     */
    @Override
    public void sendCode(String phone) {

        //判断次手机号发送了几次
        Object count = redisTemplate.boundValueOps("sms_count_" + phone).get();

        if(count==null){
            redisTemplate.boundValueOps("sms_count_"+phone).set(1,1,TimeUnit.HOURS);
        }else {
            Integer smsCount= (Integer) count;
            if(smsCount>=3){
                throw new RuntimeException("此号码发送频繁，请一个小时后再试");
            }else{
                redisTemplate.boundValueOps("sms_count_"+phone).set(smsCount+1,1,TimeUnit.HOURS);
            }
        }

        //发一次需要记录一下
        //将内容放入mq中
        Map<String, String> map = new HashMap();
        map.put("phoneNumbers", phone);
        map.put("signName", "品位优雅购物");
        map.put("templateCode", "SMS_130926832");
        String numeric = RandomStringUtils.randomNumeric(4);
        map.put("templateParam", "{\"code\":\"" + numeric + "\"}");
        System.out.println(numeric);
        jmsTemplate.convertAndSend("pyg_sms", map);

//        把验证码放入到redis中
//        redisTemplate.boundValueOps("sms_"+phone).set(numeric,5, TimeUnit.MINUTES);
        redisTemplate.boundValueOps("sms_"+phone).set(numeric,5, TimeUnit.MINUTES);
    }

    /**
     * 注册
     * @param code
     * @param user
     */
    @Override
    public void register(String code, TbUser user) {

//        TODO 按照手机号码和用户名判断是否有重复
//        1、校验验证码
//        String code 页面上传入
        String numeric= (String) redisTemplate.boundValueOps("sms_"+user.getPhone()).get();
        if(numeric==null){
            throw new RuntimeException("验证码失效");
        }
        if(!code.equals(numeric)){
            throw new RuntimeException("验证码输入有误");
        }
//            2、mysql 中出入数据
//        注意默认值
//        密码加密
        String password=user.getPassword();//明文
        password= DigestUtils.md5Hex(password);
        user.setPassword(password);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        userMapper.insert(user);

    }
}
