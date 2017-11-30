package com.chuxin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangzhen on 2017/8/18.
 */
public class ToolUtil {
    public static String generateOrderId(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date1 = simpleDateFormat.format(new Date());
        String orderId = date1 + randomNumbers();
        return orderId;
    }

    public static String randomNumbers(){
        String result = "";
        int i = 0;
        while (i < 6 ){
            int s = (int)(Math.random()*10);
            result = result + s;
            i++;
        }
        return result;
    }
}
