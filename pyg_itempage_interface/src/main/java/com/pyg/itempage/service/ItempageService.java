package com.pyg.itempage.service;

import com.pyg.groupEntity.Goods;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/27 20:43
 * @description TODO
 **/


public interface ItempageService {


    Goods findOne(Long goodsId);

    List<Goods> findAll();

}
