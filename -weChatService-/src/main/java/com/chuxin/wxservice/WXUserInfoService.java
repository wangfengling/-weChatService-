package com.chuxin.wxservice;

import com.chuxin.util.HttpMethodUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Service
public class WXUserInfoService {

    @Value("${wx.userInfoUrl}")
    private String userInfoUrl;

    @Autowired
    private WXAsseceTokenService WXAsseceTokenService;
    /**
     * Created by wangzhen on 2017/7/12.
     * 判断用户是否已经订阅过
     */
    public String getUserInfoByOpenId(String openId){
        String token = WXAsseceTokenService.getAsseceToken();
        String userInfo = HttpMethodUtil.doGet(String.format(userInfoUrl,token,openId));
        return userInfo;
    }
}
