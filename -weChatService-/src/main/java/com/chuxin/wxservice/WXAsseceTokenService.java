package com.chuxin.wxservice;

import com.chuxin.util.HttpMethodUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangzhen on 2017/6/30.
 */
@Service
public class WXAsseceTokenService {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appsercet}")
    private String secret;

    @Value("${wx.assecetokenUrl}")
    private String asseceTokenUrl;

    @Value("${wx.assecetoken}")
    private String asseceToken;

    @Value("${wx.WebPageToken}")
    private String webPageTokenUrl;

    @Value("${wx.expire}")
    private int expireTime;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getAsseceToken(){
        if (isExistAsseceToken(asseceToken)){
            if (isCommonKeyExpire(asseceToken)){
                return stringRedisTemplate.opsForValue().get(asseceToken);
            }
        }
        String token = generateAsseceToken();
        stringRedisTemplate.opsForValue().set(asseceToken,token);
        stringRedisTemplate.expire(asseceToken,expireTime, TimeUnit.SECONDS);
        return token;
    }

    public boolean isExistAsseceToken(String key){
        if (stringRedisTemplate.opsForValue().get(key) == null || stringRedisTemplate.opsForValue().get(key) == ""){
            return false;
        }
        return true;
    }

    public boolean isCommonKeyExpire(String key){
        if (stringRedisTemplate.getExpire(key) > 0){
            return true;
        }
        return false;
    }

    public String generateAsseceToken(){
        String url = String.format(asseceTokenUrl);
        String result = HttpMethodUtil.doGet(url);
        JSONObject json = JSONObject.fromObject(result);
        return json.get("access_token").toString();
    }


    /**
     * Created by wangzhen on 2017/7/5.
     * 通过code码获取网页TOKEN
     */
    public String getWebpageAsseceTokenByCode(String code){
        String url = String.format(webPageTokenUrl,code);
        String result = HttpMethodUtil.doGet(url);
        return result;
    }

}
