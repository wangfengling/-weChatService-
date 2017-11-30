package com.chuxin.controller;

import com.chuxin.wxservice.WXMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangzhen on 2017/7/12.
 */
@Controller
public class WXMessageController {
    @Autowired
    private WXMessageService wxMessageService;

    @RequestMapping(value = "/setIndustryInfo/{industryInfo}")
    @ResponseBody
    public void setIndustryInfo(@PathVariable String industryInfo){
        wxMessageService.setIndustryInfo(industryInfo);
    }

    @RequestMapping(value = "/getIndustryInfo")
    @ResponseBody
    public String getIndustryInfo(){
        String industryInfo = wxMessageService.getIndustryInfo();
        return industryInfo;
    }

    @RequestMapping(value = "/getTemplateId/{templateId}")
    @ResponseBody
    public String getTempLateId(@PathVariable String templateId){
        String templateId1 = wxMessageService.getTemplateId(templateId);
        return templateId1;
    }

    @RequestMapping(value = "/getTemplateList")
    @ResponseBody
    public String getTemplateList(){
        String templateList = wxMessageService.getAllTemplateList();
        return templateList;
    }
}
