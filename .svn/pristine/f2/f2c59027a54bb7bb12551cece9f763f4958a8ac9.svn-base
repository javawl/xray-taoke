package com.xray.taoke.admin.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.xray.taoke.admin.common.Constant;
import com.xray.taoke.admin.model.Cashinfo;
import com.xray.taoke.admin.model.UoUser;

public class WxPayService {
    private static Logger logger = LoggerFactory.getLogger(WxPayService.class);
    private static String transfer_url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    private static WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);

    public static WxPayService instance = new WxPayService();

    public String doTransfers(Cashinfo data) {
        String tradeid = getTradeid(data);

        String userid = data.getStr("userid");
        UoUser user = UoUser.dao.queryByUserId(userid);
        int verno = user.getInt("verno");
        String return_msg = "";
        if (Cashinfo.dao.updateShouDong(data.getLong("seqid"), tradeid, verno) > 0) {
            UoUser.dao.updateQbcashing(userid, verno);
            Map<String, String> resultXML = this.transfers(data.getLong("seqid"));
            resultXML = this.transfers(data.getLong("seqid"));
            double qbcash = Double.valueOf(resultXML.get("qbcash"));
            logger.info("doTransfers,userid:{},tradeid:{}", userid, tradeid);
            long seqid = data.getLong("seqid");
            String appid = data.getStr("appid");
            String openid = data.getStr("openid");
            if ("SUCCESS".equals(resultXML.get("result_code"))) {
                logger.info("succ doTransfers,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
                Cashinfo.dao.updateCashSucc(seqid, qbcash);
                UoUser.dao.updateCashNo(data.getStr("userid"));
                wxApi.sendText(appid, openid, MpInfoService.instance.getCashSucc(appid, Cashinfo.dao.findById(seqid)));
            } else {
                logger.error("fail doTransfers,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
                Cashinfo.dao.updateCashFail(seqid, resultXML.get("err_code") + "," + resultXML.get("err_code_des"));
                UoUser.dao.updateCashFail(data.getStr("userid"));
            }
        }
        return return_msg;
    }

    public String doResetTrans(Cashinfo data) {
        long seqid = data.getLong("seqid");
        // int count = CashService.instance.getCount(seqid + "");
        String return_msg = "";
        String appid = data.getStr("appid");
        String openid = data.getStr("openid");
        
        String userid = data.getStr("userid");
        UoUser user = UoUser.dao.queryByUserId(userid);
        int verno = user.getInt("verno");
        
        // if (count != 0)
        // return "该笔已经重复打款！";
        Map<String, String> resultXML = this.transfers(seqid);
        double qbcash = Double.valueOf(resultXML.get("qbcash"));
        if ("SUCCESS".equals(resultXML.get("result_code"))) {
            UoUser.dao.updateQbcashing(userid, verno);
            logger.info("succ doTransfers,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
            Cashinfo.dao.updateCashSucc(seqid, qbcash);
            UoUser.dao.updateCashNo(data.getStr("userid"));
            wxApi.sendText(appid, openid, MpInfoService.instance.getCashSucc(appid, Cashinfo.dao.findById(seqid)));
        } else {
            logger.error("fail doResetTrans,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
            Cashinfo.dao.updateCashFailTwice(seqid, resultXML.get("err_code") + "," + resultXML.get("err_code_des"));
        }
        // CashService.instance.incCount(seqid + "");
        return return_msg;
    }

    public String doAutoTrans(Cashinfo data) {
        String tradeid = getTradeid(data);

        String userid = data.getStr("userid");
        UoUser user = UoUser.dao.queryByUserId(userid);
        int verno = user.getInt("verno");
        String return_msg = "";

        if (Cashinfo.dao.updateAuto(data.getLong("seqid"), tradeid, verno) > 0) {
            UoUser.dao.updateQbcashing(userid, verno);
            Map<String, String> resultXML = this.transfers(data.getLong("seqid"));
            resultXML = this.transfers(data.getLong("seqid"));
            double qbcash = Double.valueOf(resultXML.get("qbcash"));
            logger.info("doTransfers,userid:{},tradeid:{}", userid, tradeid);
            long seqid = data.getLong("seqid");
            String appid = data.getStr("appid");
            String openid = data.getStr("openid");
            if ("SUCCESS".equals(resultXML.get("result_code"))) {
                logger.info("succ doTransfers,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
                Cashinfo.dao.updateCashSucc(seqid, qbcash);
                UoUser.dao.updateCashNo(data.getStr("userid"));
                wxApi.sendText(appid, openid, MpInfoService.instance.getCashSucc(appid, Cashinfo.dao.findById(seqid)));
            } else {
                logger.error("fail doTransfers,seqid,{},userid:{},qbcash:{}", seqid, data.getStr("userid"), qbcash);
                Cashinfo.dao.updateCashFail(seqid, resultXML.get("err_code") + "," + resultXML.get("err_code_des"));
                UoUser.dao.updateCashFail(data.getStr("userid"));
            }
        }

        return return_msg;
    }

    private Map<String, String> transfers(long seqid) {
        Cashinfo data = Cashinfo.dao.findById(seqid);
        String appid = data.getStr("appid");
        String wxpayid = MpInfoService.instance.getMpInfo(appid).get("wxpayid");
        String mch_id = Constant.getMchid(wxpayid);
        String cert_path = Constant.getPath(wxpayid);
        String paternerKey = Constant.getApiPay(wxpayid);
        String ip = Constant.getIp(wxpayid);// TODO
        // 处理amount TODO
        String openid = data.getStr("openid");
        double amount = data.getDouble("cashnum") * 100;

        amount = Math.round(amount);
        DecimalFormat decimalFormat = new DecimalFormat("###################");

        String amount_str = decimalFormat.format(amount);

        double qbcash = Double.valueOf(amount_str) / 100;

        String tradeid = data.getStr("tradeid");

        Map<String, String> params = new HashMap<String, String>();
        params.put("mch_appid", appid);
        params.put("mchid", mch_id);
        params.put("openid", openid);
        params.put("nonce_str", System.currentTimeMillis() + "");
        params.put("amount", amount_str);
        params.put("partner_trade_no", tradeid);
        params.put("check_name", "NO_CHECK");
        params.put("spbill_create_ip", ip);
        params.put("desc", "补贴红包");
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);

        String xml = PaymentKit.toXml(params);
        String xmlResult = HttpUtils.postSSL(transfer_url, xml, cert_path, mch_id);
        Map<String, String> resultXML = PaymentKit.xmlToMap(xmlResult.toString());

        resultXML.put("qbcash", String.valueOf(qbcash));
        return resultXML;

        // if ("SUCCESS".equals(resultXML.get("result_code"))) {
        // logger.info("succ doTransfers,seqid,{},userid:{},qbcash:{}", seqid,
        // data.getStr("userid"), qbcash);
        // Cashinfo.dao.updateCashSucc(seqid, qbcash);
        // wxApi.sendText(appid, openid, MpInfoService.instance.getCashSucc(appid,
        // Cashinfo.dao.findById(seqid)));
        // } else {
        // logger.error("fail doTransfers,seqid,{},userid:{},qbcash:{}", seqid,
        // data.getStr("userid"), qbcash);
        // Cashinfo.dao.updateCashFail(seqid, resultXML.get("err_code") + "," +
        // resultXML.get("err_code_des"));
        // UoUser.dao.updateCashFail(data.getStr("userid"));
        // }
        // return resultXML.get("return_msg");
    }

    private static String getTradeid(Cashinfo data) {
        String str = String.valueOf(data.getLong("cashtime"));
        str = str.substring(str.length() - 4, str.length());
        return (data.getLong("seqid") + 100000) + str;
    }
}
