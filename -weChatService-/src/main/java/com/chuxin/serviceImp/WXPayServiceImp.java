package com.chuxin.serviceImp;

import com.chuxin.dao.WXOrderDao;
import com.chuxin.dao.WXOrderRepository;
import com.chuxin.pojo.WXOrder;
import com.chuxin.service.WXPayService;
import com.chuxin.util.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by wangzhen on 2017/8/18.
 */
@Service
public class WXPayServiceImp implements WXPayService {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.mch_id}")
    private String mch_id;

    @Value("${wx.unifiedorder}")
    private String unifiedOrderUrl;

    @Value("${wx.ip}")
    private String ip;

    @Value("${wx.notify_url}")
    private String notify_url;

    @Value("${wx.key}")
    private String secret;
    @Autowired
    private WXOrderRepository wxOrderRepository;

    @Autowired
    private WXOrderDao wxOrderDao;

    @Override
    @Transactional
    public Map<String,Object> generateOrder(String  orderParams){
        Map<String,Object> payParams1 = new HashMap<>();
        Date date = new Date();
        JSONObject jsonObject = JSONObject.fromObject(orderParams);
        String openId = jsonObject.getString("openId");
        String school = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        String address = jsonObject.getString("address");
        String grade = jsonObject.getString("grade");
        int price = jsonObject.getInt("price");
        int disCount = jsonObject.getInt("disCount");
        int courseId = jsonObject.getInt("courseId");
        String classDate = jsonObject.getString("date");
        String orderId = ToolUtil.generateOrderId(date);
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appid", appId);
        data.put("mch_id",mch_id);
        data.put("device_info","WEB");
        data.put("nonce_str", nonceStr);
        data.put("body","文升付费课程");
        data.put("out_trade_no",orderId);
        data.put("total_fee",price);
        data.put("spbill_create_ip",ip);
        data.put("notify_url",notify_url);
        data.put("trade_type","JSAPI");
        data.put("openid",openId);
        data.put("sign",Decript.MD5(data,secret).toUpperCase());
        try {
            String xmlString = XmlSaxReader.mapToXml(data);
            String prepayInfo = HttpMethodUtil.doPost(unifiedOrderUrl,xmlString);//调用统一接口生成预付订单
            Map<String,Object> payParams = XmlSaxReader.xmlToMap(prepayInfo);
            WXOrder wxOrder = new WXOrder(orderId,price,openId,school,phone,address,grade,date,date, WXOrder.Status.NotPay.name(),disCount,courseId,classDate);
            wxOrderRepository.save(wxOrder);
            payParams1.put("appId",payParams.get("appid"));
            payParams1.put("package","prepay_id="+payParams.get("prepay_id"));
            payParams1.put("timeStamp",date.getTime());
            payParams1.put("nonceStr",nonceStr);
            payParams1.put("signType","MD5");
            String params = Decript.MD5(payParams1,secret);
            payParams1.put("sign",params);
            payParams1.put("orderId",orderId);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return payParams1;
    }

    @Override
    public void deleteByOrderId(String orderId) {
        wxOrderRepository.deleteByOrderId(orderId);
    }

    @Override
    @Transactional
    public Map<String,Object>  updateStatusByOrderId(Map<String,Object> params) {
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("return_code","SUCCESS");
        map1.put("return_msg","OK");
        Map<String,Object> map = params;
        WXOrder wxOrder = wxOrderRepository.findByOrderId(map.get("out_trade_no").toString());
        if (wxOrder.getStatus().equals(WXOrder.Status.YesPay.name())){
            return map1;
        }
        String sign = map.get("sign").toString();
        map.remove("sign");
        String md5Sign = Decript.MD5(map,secret).toUpperCase();
        if (sign.equals(md5Sign)){
            String price = map.get("total_fee").toString();
            if (price.equals(wxOrder.getOrderPrice()+"")){
                wxOrder.setStatus(WXOrder.Status.YesPay.name());
            }
        }
        return map1;
    }

    public void refund(String orderId){
        // 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        String out_refund_no = ToolUtil.generateOrderId(new Date());
        String out_trade_no = orderId;// 商户侧传给微信的订单号
        String total_fee = "1";
        String refund_fee = "1";// 退款金额
        String nonce_str = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);;// 随机字符串
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appid", appId);
        data.put("mch_id", mch_id);
        data.put("nonce_str", nonce_str);
        data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_refund_no);
        data.put("total_fee", total_fee);
        data.put("refund_fee", refund_fee);
        data.put("sign",Decript.MD5(data,secret).toUpperCase());
        String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        try {
            String xmlString = XmlSaxReader.mapToXml(data);
            String s= new ClientCustomSSL().doRefund(createOrderURL, xmlString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Transactional
    public Map<String,Object> refundStatusByOrderId(Map<String,Object> params){
        Map<String,Object> map1 = new HashMap<>();
        map1.put("return_code","SUCCESS");
        map1.put("return_msg","OK");
        byte[] decode = Base64.getDecoder().decode(params.get("req_info").toString());
        try {
            byte [] encode = MessageDigest.getInstance("MD5").digest(secret.getBytes("UTF-8"));
            String hexString = Decript.toHexString(encode).toLowerCase();
            String result = Decript.Aes256Decode(decode,hexString.getBytes());
            Map map = XmlSaxReader.xmlToMap(result);

            WXOrder wxOrder = wxOrderRepository.findByOrderId(map.get("out_trade_no").toString());
            if (wxOrder.getStatus().equals(WXOrder.Status.Refunded.name())){
                return map1;
            }
            wxOrder.setStatus(WXOrder.Status.Refunded.name());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map1;
    }

    @Override
    public List<Map<String, Object>> doFindWXOrderByOpenId(String orderId) {
        return wxOrderDao.doFindWXOrderByOpenId(orderId);
    }

    @Transactional
    public String  doApplyForRefund(String orderId){

        wxOrderRepository.updateStatusByOrderId(orderId,WXOrder.Status.Refunding.name());
        return "SUCCESS";
    }
}
