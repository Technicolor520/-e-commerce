package com.pyg.seckill.impl;

import com.alibaba.dubbo.config.annotation.Service;;
import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.mapper.TbSeckillOrderMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.seckill.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import utils.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/12 23:02
 * @description TODO
 **/

@Service
public class SeckillServiceimpl implements SeckillService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private CreateOrder createOrder;

    /**
     * 显示商品信息
     * @return
     */
    @Override
    public List<TbSeckillGoods> findSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }


    /**
     * 显示秒杀商品的详情
     * @param id
     * @return
     */
    @Override
    public TbSeckillGoods findOneSeckillGoodsFromRedis(Long id) {
        return (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(id);
    }

    /**
     * 秒杀下单
     * @param id
     * @param userId
     * @return
     */

    @Override
    public void saveOrder(Long id,String userId) {

        Object seckill_order = redisTemplate.boundHashOps("seckill_order").get(userId);
        if(seckill_order!=null){
            throw new RuntimeException("请先支付其他订单");
        }
        //判断是否有库存：从redis中出栈
        Object o = redisTemplate.boundListOps("seckill_goods_count_" + id).rightPop();
        if(o==null){
            throw new RuntimeException("商品已售罄");
        }

        //通过redis向多线程中传送userId，id
        Map map=new HashMap();
        map.put("userId",userId);
        map.put("id",id);
        redisTemplate.boundListOps("userId_id").leftPush(map);
        //使用多线程处理
        executor.execute(createOrder);
    }
}
