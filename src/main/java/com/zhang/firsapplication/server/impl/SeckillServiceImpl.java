package com.zhang.firsapplication.server.impl;


import com.zhang.firsapplication.bean.Seckill;
import com.zhang.firsapplication.bean.SeckillOrder;
import com.zhang.firsapplication.dao.SeckillMapper;
import com.zhang.firsapplication.dao.SeckillOrderMapper;
import com.zhang.firsapplication.dto.Exposer;
import com.zhang.firsapplication.dto.SeckillExecution;
import com.zhang.firsapplication.enums.SeckillStatEnum;
import com.zhang.firsapplication.exception.RepeatKillException;
import com.zhang.firsapplication.exception.SeckillCloseException;
import com.zhang.firsapplication.exception.SeckillException;
import com.zhang.firsapplication.server.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/10/6
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //设置秒杀redis缓存的key
    private final String key = "seckill";

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Seckill> findAll() {
        //List<Seckill> seckillList = redisTemplate.boundHashOps("seckill").values();
        HashOperations hashOperations = redisTemplate.opsForHash();
        List<Seckill> seckillList = hashOperations.values("seckill");
        if (seckillList == null || seckillList.size() == 0){
            //说明缓存中没有秒杀列表数据
            //查询数据库中秒杀列表数据，并将列表数据循环放入redis缓存中
            seckillList = seckillMapper.selectAll();
            for (Seckill seckill : seckillList){
                //将秒杀列表数据依次放入redis缓存中，key:秒杀表的ID值；value:秒杀商品数据
//                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
                hashOperations.put(key, ObjectUtils.nullSafeToString(seckill.getSeckillId()),seckill);
                logger.info("findAll -> 从数据库中读取放入缓存中");
            }
        }else{
            logger.info("findAll -> 从缓存中读取");
        }
        return seckillList;
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillMapper.selectByPrimaryKey(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
        if (seckill == null) {
            //说明redis缓存中没有此key对应的value
            //查询数据库，并将数据放入缓存中
            seckill = seckillMapper.selectByPrimaryKey(seckillId);
            if (seckill == null) {
                //说明没有查询到
                return new Exposer(false, seckillId);
            } else {
                //查询到了，存入redis缓存中。 key:秒杀表的ID值； value:秒杀表数据
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
                logger.info("exportSeckillUrl -> 从数据库中读取并放入缓存中");
            }
        } else {
            logger.info("exportSeckillUrl -> 从缓存中读取");
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //获取系统时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转换特定字符串的过程，不可逆的算法
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    //生成MD5值
    private String getMD5(Long seckillId) {
        //设置盐值字符串，随便定义，用于混淆MD5值
        String salt = "sjajaspu-i-2jrfm;sd";
        String base = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }


    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     * <p>
     * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     * @return
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：1.减库存；2.储存秒杀订单
        Date nowTime = new Date();

        try {
            //记录秒杀订单信息
            int insertCount = seckillOrderMapper.insertOrder(seckillId, money, userPhone);
            //唯一性：seckillId,userPhone，保证一个用户只能秒杀一件商品
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存
                int updateCount = seckillMapper.reduceStock(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    SeckillOrder seckillOrder = seckillOrderMapper.findById(seckillId, userPhone);

                    //更新缓存（更新库存数量）
                    Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(String.valueOf(seckillId));
                    seckill.setStockCount(seckill.getSeckillId() - 1);
                    redisTemplate.boundHashOps(key).put(seckillId, seckill);

                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, seckillOrder);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}