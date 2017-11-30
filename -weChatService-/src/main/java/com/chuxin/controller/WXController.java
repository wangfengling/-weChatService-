package com.chuxin.controller;

import com.chuxin.util.Decript;
import com.chuxin.util.SortToString;
import com.chuxin.wxservice.WXMessageService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wangzhen on 2017/6/29.
 */
@Controller
@RequestMapping("/WXMessage")
@PropertySource(value = "constFile.properties")
public class WXController {
    @Value("${wx.token}")
    private String token;

    @Autowired
    private WXMessageService wxMessageService;
    /**
     * Created by wangzhen on 2017/6/29.
     */
    @RequestMapping(method = RequestMethod.GET)
    public void authWXMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(token);
        arrayList.add(timestamp);
        arrayList.add(nonce);
        String sortString = SortToString.getSortToString(arrayList);
        String myToken  = Decript.SHA1(sortString);
        if (myToken != null && myToken != "" && myToken.equals(signature)){
            response.getWriter().println(echostr);
        }else {
            System.out.println("签名校验失败");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void processWXMessage(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException {
        String responseMessage = wxMessageService.processWXMessage(request);
        response.getWriter().println(responseMessage);
    }

}
