package com.chuxin.controller;

import com.chuxin.wxservice.WXAsseceTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangzhen on 2017/6/30.
 */
@Controller
public class WXTokenController {
    @Autowired
    private WXAsseceTokenService WXAsseceTokenService;
    @RequestMapping(value = "/commonsWXToken",method = RequestMethod.GET)
    @ResponseBody
    public String getCommonsWXToken(){
        return WXAsseceTokenService.getAsseceToken();
    }
}
