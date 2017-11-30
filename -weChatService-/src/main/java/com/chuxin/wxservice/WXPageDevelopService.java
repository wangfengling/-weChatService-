package com.chuxin.wxservice;

import com.chuxin.util.HttpMethodUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by wangzhen on 2017/7/5.
 */
@Service
public class WXPageDevelopService {
    @Value("${wx.userInfo}")
    private String userInfoUrl;
    @Value("${wx.authUrl}")
    private String authUrl;
    @Autowired
    private com.chuxin.wxservice.WXAsseceTokenService WXAsseceTokenService;
    /**
     * Created by wangzhen on 2017/7/5
     * 通过code去获取网页授权用户信息
     */
    public String getUserInfoByCode(String code){
        String tokenAndOpenId = WXAsseceTokenService.getWebpageAsseceTokenByCode(code);
        JSONObject jsonObject = JSONObject.fromObject(tokenAndOpenId);
        String url = String.format(userInfoUrl,jsonObject.get("access_token").toString(),jsonObject.get("openid").toString());
        String result = HttpMethodUtil.doGet(url);
        return result;
    }
    /**
     * Created by wangzhen on 2017/7/5
     * f返回授权url
     */
    public String getAuthorizationUrl(String url){
        try {
            String urlencode = URLEncoder.encode(url,"UTF-8");
            return  String.format(authUrl,urlencode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
