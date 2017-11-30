package com.chuxin.wxservice;

import com.chuxin.pojo.KeyValues;
import com.chuxin.util.HttpMethodUtil;
import com.chuxin.util.KeyValuesToJSON;
import com.chuxin.util.XmlSaxReader;
import net.sf.json.JSONObject;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/7/1.
 */
@Service
public class WXMessageService {
    @Value("${wx.industryInfoSetUrl}")
    private String industrySetUrl;

    @Value("${wx.industryInfoGetUrl}")
    private String industryGetUrl;

    @Value("${wx.templateIdUrl}")
    private String templateIdUrl;

    @Value("${wx.templateListUrl}")
    private String templateListUrl;

    @Value("${wx.sendTemplateMessageUrl}")
    private String sendTemplateUrl;
    @Autowired
    private XmlSaxReader xmlSaxReader;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private com.chuxin.wxservice.WXAsseceTokenService WXAsseceTokenService;
    /**
     * Created by wangzhen on 2017/7/12.
     * 处理微信发过来的消息
     */
    public String processWXMessage(HttpServletRequest request) throws IOException, DocumentException {
        Map<String,String> paramsMap = xmlSaxReader.getXmlToMap(request);
        if (paramsMap.get("MsgType").equals("event") && paramsMap.get("Event").equals("subscribe") && paramsMap.containsKey("Ticket")){
            String inviterID = paramsMap.get("EventKey").substring(8);
            String inviterSort = stringRedisTemplate.opsForValue().get(inviterID.concat("sort"));
            if (inviterSort != null){
                stringRedisTemplate.opsForValue().set(inviterID.concat("sort"), (Integer.parseInt(inviterSort)+1)+"");
            }else {
                stringRedisTemplate.opsForValue().set(inviterID.concat("sort"),"1");
            }
        }
        return "";
    }

    /**
     * Created by wangzhen on 2017/7/12.
     * 设置模板行业消息
     */
    public void setIndustryInfo(String industryInfo){
        String token = WXAsseceTokenService.getAsseceToken();
        String[] industryInfoString = industryInfo.split(",");
        JSONObject jsonObject = new JSONObject();
        for (int i = 1; i < industryInfoString.length+1; i++){
            jsonObject.put("industry_id"+i,industryInfoString[i-1]);
        }
        HttpMethodUtil.doPost(String.format(industrySetUrl,token),jsonObject.toString());
    }
    /**
     * Created by wangzhen on 2017/7/12.
     * 获取模板行业消息
     */
     public String getIndustryInfo(){
         String token = WXAsseceTokenService.getAsseceToken();
         String industryInfo = HttpMethodUtil.doGet(String.format(industryGetUrl,token));
         return industryInfo;
     }

    /**
     * Created by wangzhen on 2017/7/12.
     * 获取模板ID
     */
    public String getTemplateId(String templateId){
        String token = WXAsseceTokenService.getAsseceToken();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template_id_short",templateId);
        String templateId1 = HttpMethodUtil.doPost(String.format(templateIdUrl,token),jsonObject.toString());
        return templateId1;
    }

    /**
     * Created by wangzhen on 2017/7/12.
     * 获取模板列表
     */
    public String getAllTemplateList(){
        String token = WXAsseceTokenService.getAsseceToken();
        String templateList = HttpMethodUtil.doGet(String.format(templateListUrl,token));
        return templateList;
    }

    /**
     * Created by wangzhen on 2017/7/12.
     * 发送给用户模板消息
     */
    public String sendTemplateMessage(Map<String,String> map){
        String token = WXAsseceTokenService.getAsseceToken();
        List<KeyValues> keyValuesList = new ArrayList<KeyValues>();
        keyValuesList.add(new KeyValues("touser",map.get("openId"),0));
        keyValuesList.add(new KeyValues("template_id","PxQ1v7qZN1WIWAfC4m1rmacHWv9ymmkizL38_GQIIEM",0));
        keyValuesList.add(new KeyValues("url",map.get("url"),0));
        keyValuesList.add(new KeyValues("miniprogram",null,0));
        keyValuesList.add(new KeyValues("data",null,0));
        keyValuesList.add(new KeyValues("first",null,1));
        keyValuesList.add(new KeyValues("value","您已经成功创建班级！邀请2位好友组班，一起来上阅读课吧！",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword1",null,1));
        keyValuesList.add(new KeyValues("value",map.get("courseName"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword2",null,1));
        keyValuesList.add(new KeyValues("value","班级创建成功于" + map.get("date"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "remark",null,1));
        keyValuesList.add(new KeyValues("value","如有任何问题，请咨询课程顾问晓阅老师，微信号：wensheng996。\n" +
                "［阅读很重要，思考更重要！］\n",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        HttpMethodUtil.doPost(String.format(sendTemplateUrl,token), KeyValuesToJSON.getJsonFromKeyValues(keyValuesList));
        return "";
    }

    public String sendClassFailMessage(Map<String,String> map){
        String token = WXAsseceTokenService.getAsseceToken();
        List<KeyValues> keyValuesList = new ArrayList<KeyValues>();
        keyValuesList.add(new KeyValues("touser",map.get("openId"),0));
        keyValuesList.add(new KeyValues("template_id","6Y-ehSPs8phAdH2wgH8dXGlhUnNVjedr5LEal5UqaUs",0));
        keyValuesList.add(new KeyValues("url",map.get("url"),0));
        keyValuesList.add(new KeyValues("miniprogram",null,0));
        keyValuesList.add(new KeyValues("data",null,0));
        keyValuesList.add(new KeyValues("first",null,1));
        keyValuesList.add(new KeyValues("value","您创建的班级没有组班成功！请您重新创建班级，并在48小时内邀请2位好友加入，就可以成功组班啦!",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword1",null,1));
        keyValuesList.add(new KeyValues("value",map.get("courseName"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword2",null,1));
        keyValuesList.add(new KeyValues("value",map.get("date"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "remark",null,1));
        keyValuesList.add(new KeyValues("value","如有任何问题，请咨询课程顾问晓阅老师，微信号：wensheng996。\n" +
                "［阅读很重要，思考更重要！］\n",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        HttpMethodUtil.doPost(String.format(sendTemplateUrl,token), KeyValuesToJSON.getJsonFromKeyValues(keyValuesList));
        return "";
    }

    public String sendJoinClassMessage(Map<String,String> map){
        String token = WXAsseceTokenService.getAsseceToken();
        List<KeyValues> keyValuesList = new ArrayList<KeyValues>();
        keyValuesList.add(new KeyValues("touser",map.get("openId"),0));
        keyValuesList.add(new KeyValues("template_id","QJDMIElGsZzzcRTXBp9-G8L2G8BqXhvmKm-ogH-ScRI",0));
        keyValuesList.add(new KeyValues("url",map.get("url"),0));
        keyValuesList.add(new KeyValues("miniprogram",null,0));
        keyValuesList.add(new KeyValues("data",null,0));
        keyValuesList.add(new KeyValues("first",null,1));
        keyValuesList.add(new KeyValues("value","您已经成功加入班级！您可邀请好友加入组班，一起来上阅读课吧！",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword1",null,1));
        keyValuesList.add(new KeyValues("value",map.get("courseName"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword2",null,1));
        keyValuesList.add(new KeyValues("value",map.get("date"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "remark",null,1));
        keyValuesList.add(new KeyValues("value","（如有任何问题，请咨询课程顾问晓阅老师，微信号：wensheng996。\n" +
                "［阅读很重要，思考更重要！］）",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        HttpMethodUtil.doPost(String.format(sendTemplateUrl,token), KeyValuesToJSON.getJsonFromKeyValues(keyValuesList));
        return "";
    }

    public String sendClassSuccessMessage(Map<String,String> map){
        String token = WXAsseceTokenService.getAsseceToken();
        List<KeyValues> keyValuesList = new ArrayList<KeyValues>();
        keyValuesList.add(new KeyValues("touser",map.get("openId"),0));
        keyValuesList.add(new KeyValues("template_id","1l6BJDyE1lMEpgbP2PBWl24RcIspFEMgWoTYlUf_MDs",0));
        keyValuesList.add(new KeyValues("url",map.get("url"),0));
        keyValuesList.add(new KeyValues("miniprogram",null,0));
        keyValuesList.add(new KeyValues("data",null,0));
        keyValuesList.add(new KeyValues("first",null,1));
        keyValuesList.add(new KeyValues("value","您已成功创建班级！ 邀请两位好友组班，一起上课吧！",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword1",null,1));
        keyValuesList.add(new KeyValues("value",map.get("courseName"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword2",null,1));
        keyValuesList.add(new KeyValues("value","1/3",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "keyword3",null,1));
        keyValuesList.add(new KeyValues("value",map.get("date"),2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        keyValuesList.add(new KeyValues( "remark",null,1));
        keyValuesList.add(new KeyValues("value","（如有任何问题，请咨询课程顾问晓阅老师，微信号：wensheng996。\n" +
                "［阅读很重要，思考更重要！］)\n",2));
        keyValuesList.add(new KeyValues( "color","#173177",2));
        HttpMethodUtil.doPost(String.format(sendTemplateUrl,token), KeyValuesToJSON.getJsonFromKeyValues(keyValuesList));
        return "";
    }
}
