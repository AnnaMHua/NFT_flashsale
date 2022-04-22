package com.jiuzhang.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.OrderDao;
import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.Order;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.service.RedisService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "pay_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {

    @Resource
    private OrderDao orderDao;

    @Resource
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RedisService redisService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("接收到订单支付状态校验消息:" + message);
        Order order = JSON.parseObject(message, Order.class);
        // 1. 查询订单
        Order orderInfo = orderDao.queryOrder(order.getOrderNo());

        //2.判断订单是否完成支付
        if (orderInfo.getOrderStatus() != 2) {

            //3. 未完成支付。关闭订单
            log.info("未完成支付。关闭订单.订单号："+ orderInfo.getOrderNo() );
            orderInfo.setOrderStatus(99);
            orderDao.updateOrder(orderInfo);
            //4.恢复数据库库存
            seckillActivityDao.revertStock(order.getSeckillActivityId());
            //恢复Redis库存
            redisService.revertStock("Stock:" + order.getSeckillActivityId());

            //5.将用户从已购名单中删除
            redisService.removeLimitMember(order.getSeckillActivityId(), order.getUserId());

        }
    }
}
