package com.chuxin.wxservice;


import com.chuxin.util.HttpMethodUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangzhen on 2017/6/30.
 */
@Service
public class WXAccountManageService {
    @Value("${wx.ticketUrl}")
    private String qRCodeURL;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.jsAPITicketUrl}")
    private String jsAPITicketUrl;
    @Value("${wx.jsApiTicket}")
    private String jsAPITicket;

    @Value("${wx.expire}")
    private int expireTime;
    @Autowired
    private WXAsseceTokenService WXAsseceTokenService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * Created by wangzhen on 2017/6/30.
     * 对外提供带参数的二维码的ticket
     */
    public String getQRCodeTicket(String openId){
        if (isExistKey(openId)){
            return stringRedisTemplate.opsForValue().get(openId);
        }
        String ticket = generateQRCode(openId);
        stringRedisTemplate.opsForValue().set(openId,ticket);
        stringRedisTemplate.expire(openId,7200, TimeUnit.SECONDS);
        return ticket;
    }

    /**
     * Created by wangzhen on 2017/6/30.
     * 判断redis缓存中是否含有当前openId生成的ticket
     */
    public boolean isExistKey(String openId){
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        if (valueOperations.get(openId) != null){
            return true;
        }
        return false;
    }

    /**
     * Created by wangzhen on 2017/6/30.
     * 根据openId生成ticket
     */
    public String generateQRCode(String openId){
        String token = WXAsseceTokenService.getAsseceToken();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action_name","QR_LIMIT_STR_SCENE");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("scene_str",openId);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("scene",jsonObject2);
        jsonObject.put("action_info",jsonObject1);
        String transJson = jsonObject.toString();
        String ticket = HttpMethodUtil.doPost(qRCodeURL.concat(token),transJson);
        return  ticket;
    }
    /**
     * Created by wangzhen on 2017/7/2.
     * 对外提供jsApi_ticket
     */
    public String getJsApiTicket(){
        if (WXAsseceTokenService.isCommonKeyExpire(jsAPITicket)){
            return stringRedisTemplate.opsForValue().get(jsAPITicket);
        }
        String ticket = generateJSAPITicket();
        stringRedisTemplate.opsForValue().set(jsAPITicket,ticket);
        stringRedisTemplate.expire(jsAPITicket,expireTime, TimeUnit.SECONDS);
        return ticket;
    }
    /**
     * Created by wangzhen on 2017/7/2.
     * 获取微信js-sdk 接口配置信息的jsapi_ticket
     */
    public String generateJSAPITicket(){
        String asseceToken = WXAsseceTokenService.getAsseceToken();
        String url = String.format(jsAPITicketUrl,asseceToken);
        String result = HttpMethodUtil.doGet(url);
        JSONObject jsonObject = JSONObject.fromObject(result);
        return jsonObject.get("ticket").toString();
    }
}
