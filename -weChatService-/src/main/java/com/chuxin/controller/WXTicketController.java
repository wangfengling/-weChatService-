package com.chuxin.controller;

import com.chuxin.util.Decript;
import com.chuxin.wxservice.WXAccountManageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhen on 2017/6/30.
 */
@Controller
public class WXTicketController {
    @Value("${wx.noncestr}")
    private String noncestr;
    @Value("${wx.appId}")
    private String appId;
    @Autowired
    private WXAccountManageService WXAccountManageService;


    /**
     * Created by wangzhen on 2017/7/2.
     * 微信QRCode二维码获取
     */
    @RequestMapping(value = "/qrCodeTicket/{openId}" ,method = RequestMethod.GET)
    @ResponseBody
    public String getQRCodeTicket(@PathVariable(value = "openId") String userId){
        return WXAccountManageService.getQRCodeTicket(userId);
    }

    /**
     * Created by wangzhen on 2017/7/2.
     * 微信接口 jsAPITicket获取去
     */
    @RequestMapping(value = "/jsApiTicket",method = RequestMethod.GET)
    @ResponseBody
    public String getJsApiTicket(@RequestParam String url){
        String jsApiTicket = WXAccountManageService.getJsApiTicket();
        long timestamp = System.currentTimeMillis();
        String signature = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s",jsApiTicket,noncestr,timestamp,url);
        Map map = new HashMap<String,String>();
        map.put("appId",appId);
        map.put("timestamp",timestamp);
        map.put("nonceStr",noncestr);
        map.put("signature",Decript.SHA1(signature));
        return  JSONObject.toJSONString(map);
    }
}
