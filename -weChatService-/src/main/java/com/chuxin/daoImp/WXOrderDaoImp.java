package com.chuxin.daoImp;

import com.chuxin.dao.WXOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WXOrderDaoImp implements WXOrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> doFindWXOrderByOpenId(String openId) {

        SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select w.order_id,w.order_price,w.pay_date,c.name,c.price,c.pic_id,w.status from wxorder w left join coursegroup c on w.course_id = c.id where w.open_id = ? and w.status != 'NotPay' ORDER BY w.pay_date DESC ";
        List<Map<String,Object>> orderList = jdbcTemplate.query(sql, new Object[]{openId}, (ResultSet resultSet, int i) -> {
                Map<String,Object> dataMap = new HashMap<>();
                Date time1=new Date(resultSet.getTimestamp("pay_date").getTime());
                String pubtime= formattime.format(time1);
                dataMap.put("orderId",resultSet.getString("order_id"));
                dataMap.put("date",pubtime);
                dataMap.put("courseName",resultSet.getString("name"));
                dataMap.put("price",resultSet.getString("price"));
                dataMap.put("picId",resultSet.getString("pic_id"));
                String status = resultSet.getString("status");
                dataMap.put("status",status.equals("YesPay") ? "申请退款" : status.equals("Refunding") ? "退款中..." : status.equals("Refunded") ? "退款成功" : "" );
                return dataMap;

        });
        return orderList;
    }
}
