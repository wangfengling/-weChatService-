package com.chuxin.dao;

import com.chuxin.pojo.WXOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wangzhen on 2017/8/17.
 * 操作订单的
 */
public interface WXOrderRepository extends CrudRepository<WXOrder,Long> {

    WXOrder save(WXOrder wxOrder);

    void deleteByOrderId(String orderId);

    @Modifying
    @Query(value = "update WXOrder u set u.status= ?2 where u.orderId = ?1")
    int updateStatusByOrderId(String orderId,String status);

    WXOrder findByOrderId(String orderId);

    WXOrder findByOpenIdAndCourseIdAndStatus(String openId,int courseId,String status);

    List<WXOrder> findAllByCourseIdNotInAndOpenIdAndStatus(List<Integer> courseIdList,String openId,String status);

    List<WXOrder> findAllByOpenIdAndStatus(String openId,String status);
}
