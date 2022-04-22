package com.jiuzhang.seckill.service;

import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOverSellService {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    public String processSeckill(long activityId) {
        SeckillActivity activity = seckillActivityDao.querySeckillActivityById(activityId);
        int avaliableStock = activity.getAvailableStock();
        String result;

        if (avaliableStock > 0) {
            result = "Congratulation!";
            System.out.println(result);
            avaliableStock -= 1;
            activity.setAvailableStock(avaliableStock);
            seckillActivityDao.updateSeckillActivity(activity);
        } else {
            result = "Sorry, we are out of stock.";
            System.out.println(result);
        }

        return result;
    }
}
