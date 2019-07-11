package com.xray.taoke.act.web.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.ext.route.ControllerBind;
import com.xray.act.exception.RtException;
import com.xray.act.jfinal.JfController;
import com.xray.act.util.StringUtil;
import com.xray.taoke.act.common.Constant;
import com.xray.taoke.act.model.TbGoods;
import com.xray.taoke.act.model.LinkInfo;
import com.xray.taoke.act.service.MpInfoService;
import com.xray.taoke.act.service.TbGoodsService;
import com.xray.taoke.act.service.TkHdkService;
import com.xray.taoke.act.service.UoUserService;
import com.xray.taoke.act.vo.TbGoodsVo;

@ControllerBind(controllerKey = "/")
public class IndexController extends JfController {
    protected static Logger logger = LoggerFactory.getLogger(IndexController.class);
    private static String[] hdk = { "hdkssb", "hdkkqb", "hdkxlb", "hdkzsb" };

    public void index() {
        renderError(403);
    }

    public void titem() {
        try {
            Map<String, String> map = MpInfoService.instance.getMpObj(getPara("sid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            TbGoodsVo vdata = null;
            TbGoods mdata = TbGoods.dao.queryByItemid(appid, getPara("id"));
            if (mdata != null) {
                vdata = mdata.buildVo(TbGoodsVo.class);
            } else {
                vdata = TbGoodsService.instance.getTbGoodsVo(getPara("id"));
                if (vdata != null) {
                    vdata = TbGoodsService.instance.gyByItemid(appid, vdata);
                }
            }
            if (vdata == null) {
                render("/html/tips_noitem.html");
                return;
            }

            vdata.setJierate(MpInfoService.instance.getRate(appid, vdata.getTkmoney()));
            vdata.setJierate(getUserRate(getPara("mid")));
            setAttr("data", vdata);

            render("/html/tb_item.html");
        } catch (Exception e) {
            logger.error("err titem", e);
            doErrMsg(e);
        }
    }

    public void tlist() {
        try {
            Map<String, String> map = MpInfoService.instance.getMpObj(getPara("sid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            int type = getParaToInt("type", 0);
            int page = getParaToInt("page", 1);
            setAttr("type", type);
            setAttr("page", page);

            double rate = getUserRate(getPara("mid"));
            List<TbGoodsVo> dataList = TbGoodsService.instance.getHdkList(hdk[type], page);
            for (TbGoodsVo data : dataList) {
                data.setJierate(MpInfoService.instance.getRate(appid, data.getTkmoney()));
                data.setJierate(rate);
            }
            setAttr("dataList", dataList);

            render("/html/tb_list.html");
        } catch (Exception e) {
            logger.error("err tlist", e);
            doErrMsg(e);
        }
    }

    public void tshop() {
        try {
            Map<String, String> map = MpInfoService.instance.getMpObj(getPara("sid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            double rate = getUserRate(getPara("mid"));
            List<TbGoodsVo> dataList = TbGoodsService.instance.getHdkList("hdkssb", 1);
            for (TbGoodsVo data : dataList) {
                data.setJierate(MpInfoService.instance.getRate(appid, data.getTkmoney()));
                data.setJierate(rate);
            }
            setAttr("dataList1", dataList);

            dataList = TbGoodsService.instance.getHdkList("hdkkqb", TbGoodsService.instance.getHdkkqbType());
            for (TbGoodsVo data : dataList) {
                data.setJierate(MpInfoService.instance.getRate(appid, data.getTkmoney()));
                data.setJierate(rate);
            }
            setAttr("dataList2", dataList);

            dataList = TbGoodsService.instance.getHdkList("hdkxlb", 1);
            for (TbGoodsVo data : dataList) {
                data.setJierate(MpInfoService.instance.getRate(appid, data.getTkmoney()));
                data.setJierate(rate);
            }
            setAttr("dataList3", dataList);

            dataList = TbGoodsService.instance.getHdkList("hdkzsb", 1);
            for (TbGoodsVo data : dataList) {
                data.setJierate(MpInfoService.instance.getRate(appid, data.getTkmoney()));
                data.setJierate(rate);
            }
            setAttr("dataList4", dataList);

            render("/html/tb_shop.html");
        } catch (Exception e) {
            logger.error("err tlist", e);
            doErrMsg(e);
        }
    }

    public void tsou() {
        setAttr("sid", getPara("sid"));
        render("/html/tb_sou.html");
    }

    @SuppressWarnings("unchecked")
    public void tcate() {
        try {
            Map<String, String> map = MpInfoService.instance.getMpObj(getPara("sid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            int cid = getParaToInt("cid", 1);
            int min_id = getParaToInt("mpage", 1);

            double rate = getUserRate(getPara("mid"));
            Map<String, Object> data = TkHdkService.instance.doCate(cid, min_id);
            List<TbGoodsVo> list = (List<TbGoodsVo>) data.get("dataList");
            TbGoodsService.instance.batchTbGoodsVo(list);
            for (TbGoodsVo vo : list) {
                vo.setJierate(MpInfoService.instance.getRate(appid, vo.getTkmoney()));
                vo.setJierate(rate);
            }
            setAttr("data", data);

            render("/html/tb_soulist.html");
        } catch (Exception e) {
            logger.error("err tcate", e);
            doErrMsg(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void tsoulist() {
        try {
            Map<String, String> map = MpInfoService.instance.getMpObj(getPara("sid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            String keyword = getPara("word");
            int min_id = getParaToInt("mpage", 1);
            int tp_p = getParaToInt("tpage", 1);

            double rate = getUserRate(getPara("mid"));
            Map<String, Object> data = TkHdkService.instance.doSearch(keyword, min_id, tp_p);
            List<TbGoodsVo> list = (List<TbGoodsVo>) data.get("dataList");
            TbGoodsService.instance.batchTbGoodsVo(list);
            for (TbGoodsVo vo : list) {
                vo.setJierate(MpInfoService.instance.getRate(appid, vo.getTkmoney()));
                vo.setJierate(rate);
            }
            setAttr("data", data);

            render("/html/tb_soulist.html");
        } catch (Exception e) {
            logger.error("err tsoulist", e);
            doErrMsg(e);
        }
    }

    public void tzhao() {
        try {
            LinkInfo data = LinkInfo.dao.queryByLinkid(getPara("id"));
            if (data == null)
                throw new RtException(Constant.para_err);

            Map<String, String> map = MpInfoService.instance.getMpObj(data.getStr("appid"));
            setAttr("sid", map.get("sid"));
            String appid = map.get("appid");

            double rate = getUserRate(getPara("mid"));
            List<TbGoodsVo> dataList = TbGoods.dao.queryByItemids(appid, data.getStr("itemids"));
            for (TbGoodsVo vdata : dataList) {
                vdata.setJierate(MpInfoService.instance.getRate(appid, vdata.getTkmoney()));
                vdata.setJierate(rate);
            }
            setAttr("dataList", dataList);

            render("/html/tb_zhao.html");
        } catch (Exception e) {
            logger.error("err tzhao", e);
            doErrMsg(e);
        }
    }

    private double getUserRate(String mid) {
        if (StringUtil.isNotEmpty(mid))
            setCookie("sqmid", mid, Integer.MAX_VALUE);
        else
            mid = getCookie("sqmid");
        return UoUserService.instance.getRate(mid);
    }

    private void doErrMsg(Exception e) {
        if (e instanceof RtException) {
            int code = ((RtException) e).getCode();
            if (Constant.para_err == code) {
                render("/html/tips_paraerr.html");
                return;
            }
        }
        render("/html/tips_neterr.html");
    }

}
