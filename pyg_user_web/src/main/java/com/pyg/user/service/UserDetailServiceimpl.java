package com.pyg.user.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/2 18:44
 * @description TODO
 **/


public class UserDetailServiceimpl implements UserDetailsService {

    //springSecurity只判断访问资源是否需要登录
    //若没有登录，CAS登录

    //CAS登录后的username交个SpringSecurity管理

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username,"",authorities);
    }
}
