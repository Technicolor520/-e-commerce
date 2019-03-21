package com.pyg.shop.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.managerinterface.SellerService;
import com.pyg.pojo.TbSeller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/16 21:13
 * @description 自定义的认证类
 **/


public class UserDetailsServiceimpl implements UserDetailsService {

  /*  @Reference*/
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbSeller seller = sellerService.findOne(username);
        if(seller==null){
            return  null;
        }else {
            if(!seller.getStatus().equals("1")){
                return null;
            }
            List<GrantedAuthority> authorities=new ArrayList<>();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            authorities.add(grantedAuthority);
            return new User(username,seller.getPassword(),authorities);
        }
    }
}
