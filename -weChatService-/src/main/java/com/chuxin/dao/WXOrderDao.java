package com.chuxin.dao;

import java.util.List;
import java.util.Map;

public interface WXOrderDao {
    List<Map<String,Object>> doFindWXOrderByOpenId(String orderId);
}
