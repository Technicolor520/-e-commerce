package com.pyg.user.service;

import com.pyg.pojo.TbUser; /**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/31 11:26
 * @description TODO
 **/


public interface UserService {


    void sendCode(String phone);

    void register(String code, TbUser user);
}
