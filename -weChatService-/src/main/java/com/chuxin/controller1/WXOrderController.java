package com.chuxin.controller1;

import com.chuxin.pojo.WXOrder;
import com.chuxin.service.GroupClassVideoService;
import com.chuxin.service.VideoService;
import com.chuxin.service.WXPayService;
import com.chuxin.util.XmlSaxReader;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/8/18.
 */
@Controller
public class WXOrderController {
    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private GroupClassVideoService groupClassVideoService;

    @Autowired
    private VideoService videoService;

    @RequestMapping(value = "/postWXOrder",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveWXOrder(@RequestBody String orderData){
        Map<String,Object> wxOrder = wxPayService.generateOrder(orderData);
        return wxOrder;
    }

    @RequestMapping(value = "/acceptWXPayResult",method = RequestMethod.POST)
    @ResponseBody
    public String getWXPayResult(@RequestBody String params) throws Exception {
        Map<String,Object> noticeMap = XmlSaxReader.xmlToMap(params);
        Map<String,Object> returnMap = wxPayService.updateStatusByOrderId(noticeMap);
        return XmlSaxReader.mapToXml(returnMap);
    }

    @RequestMapping(value = "/getVIPUserInfo/{className}")
    @ResponseBody
    public WXOrder getVIPUserInfo(@PathVariable String className){
        WXOrder wxOrder = groupClassVideoService.findVIPOrderInfoByClassName(className);
        return wxOrder;
    }

    @RequestMapping(value = "/doRefund",method = RequestMethod.POST)
    @ResponseBody
    public String getRefund(@RequestBody String orderId){
        JSONObject jsonObject = JSONObject.fromObject(orderId);
        String orderId1 = jsonObject.getString("orderId");
        wxPayService.refund(orderId1);
        return "{success:'success'}";
    }

    @RequestMapping(value = "/acceptWXRefundResult",method = RequestMethod.POST)
    @ResponseBody
    public String getWXRefundResult(@RequestBody String params) throws Exception {
        Map<String,Object> noticeMap = XmlSaxReader.xmlToMap(params);
        Map<String,Object> returnMap = wxPayService.refundStatusByOrderId(noticeMap);
        return XmlSaxReader.mapToXml(returnMap);
    }

    @RequestMapping(value = "/doFindWXOrderByOpenId/{openId}")
    @ResponseBody
    public List<Map<String, Object>> doFindWXOrderByOpenId(@PathVariable String openId){
        return wxPayService.doFindWXOrderByOpenId(openId);
    }

    @RequestMapping(value = "/doApplyForRefund",method = RequestMethod.POST)
    @ResponseBody
    public String doApplyForRefund(@RequestBody String orderId){
        JSONObject jsonObject = JSONObject.fromObject(orderId);
        String orderId1 = jsonObject.getString("orderId");
        return wxPayService.doApplyForRefund(orderId1);
    }

}
