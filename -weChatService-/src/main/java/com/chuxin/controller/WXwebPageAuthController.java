package com.chuxin.controller;

import com.chuxin.wxservice.WXPageDevelopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangzhen on 2017/7/5.
 */
@Controller
public class WXwebPageAuthController {
    @Autowired
    private WXPageDevelopService WXPageDevelopService;

    @Value("${wx.authUrl}")
    private String authUrl;
    @RequestMapping(value = "/getUserInfo/{code}")
    @ResponseBody
    public String getUserInfoByCode(@PathVariable String code){
        return WXPageDevelopService.getUserInfoByCode(code);
    }

    @RequestMapping(value = "/getAuthUrlEncode")
    @ResponseBody
    public String getAuthUrlEncode(@RequestParam String url){
        String urlencode = WXPageDevelopService.getAuthorizationUrl(url);
        return urlencode;
    }

}
