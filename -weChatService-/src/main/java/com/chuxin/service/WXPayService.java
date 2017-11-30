package com.chuxin.service;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/8/18.
 */
public interface WXPayService {

    Map<String,Object> generateOrder(String orderParams);

    void deleteByOrderId(String orderId);

    Map<String,Object> updateStatusByOrderId(Map<String,Object> params);

    void refund(String orderId);

    Map<String,Object> refundStatusByOrderId(Map<String,Object> params);

    List<Map<String,Object>> doFindWXOrderByOpenId(String orderId);

    String  doApplyForRefund(String orderId);

}
