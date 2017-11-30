package com.chuxin.controller;

import com.chuxin.wxservice.WXUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Controller
public class WXUserInfoController {
    @Autowired
    private WXUserInfoService wxUserInfoService;

    @RequestMapping(value = "/getUserInfoByOpenId/{openId}")
    @ResponseBody
    public String getUserInfoByOpenId(@PathVariable  String openId){
        String userInfo = wxUserInfoService.getUserInfoByOpenId(openId);
        return userInfo;
    }
}
