package com.com.pyg.seckill.task;

import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SeckillGoodsTask {

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "30 51 21 15 4 ?")
    public void initSeckillGoods(){
//          1、查询符合要求的数据
//        要求：审核通过+时间范围之内+库存大于0
//        select * from tb_seckill_goods where STATUS='1' and stock_count>0       and  start_time<now() and  end_time>now()

        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andStatusEqualTo("1")
                                .andStockCountEqualTo(0)
                                .andStartTimeLessThan(new Date())
                                .andEndTimeGreaterThan(new Date());
        List<TbSeckillGoods> tbSeckillGoods = seckillGoodsMapper.selectByExample(example);

//        2、把数据一个一个地放到redis中

        for (TbSeckillGoods tbSeckillGood : tbSeckillGoods) {
            Long id = tbSeckillGood.getId();
            redisTemplate.boundHashOps("seckill_goods").put(tbSeckillGood.getGoodsId(),tbSeckillGood);
            for (Integer i = 0; i < tbSeckillGood.getStockCount(); i++) {
                redisTemplate.boundListOps("seckill_goods_count_"+id).leftPush(id);
            }
        }
        System.out.println("商品已放入redis中");

    }
}
