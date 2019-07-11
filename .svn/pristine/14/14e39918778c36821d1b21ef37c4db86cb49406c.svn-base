{{package}}

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.xray.admin.web.controller.AbstractController;
import com.xray.act.util.StringUtil;
import com.xray.act.web.vo.RtnFactory;
import com.xray.admin.utils.DateUtil;
{{import_model}}
import com.xray.admin.web.vo.PageVo;

@ControllerBind(controllerKey = "/{{actionKey}}")
public class {{ClassName}}Controller extends AbstractController {

    public void goList() {
        int pno = getParaToInt("pno", 1);
        int state = getParaToInt("state", 0);
        String word = getPara("word");
        String inputtimeStr = getPara("inputtimeStr", "");

        setAttr("state", state);
        setAttr("word", word);
        setAttr("inputtimeStr", inputtimeStr);

        PageVo page = new PageVo(pno);
        setAttr("page", page);

        Map<String, Object> cond = new HashMap<String, Object>();
        if (state != 0)
            cond.put("state", state);
        if (!StringUtil.isEmpty(word))
            cond.put("word", word);
        if (!StringUtil.isEmpty(inputtimeStr))
            cond.put("inputtime", DateUtil.str2Date("yyyy-MM-dd", inputtimeStr).getTime());
        page.setOrderby("seqid desc");
        setAttr("dataList", {{ClassName}}.dao.queryList(cond, page));
        render("/template/{{webPackageTypeName}}/{{packageName}}/list.html");
    }
    public void goAdd() {
        render("/template/{{webPackageTypeName}}/{{packageName}}/form.html");
    }

    public void goEdit() {
        String id = getPara("id");

        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("id", id);

        setAttr("data", {{ClassName}}.dao.findById(id));
        render("/template/{{webPackageTypeName}}/{{packageName}}/form.html");
    }

    public void doAdd() {
        {{fields_para}}
        int state = getParaToInt("state");

        {{ClassName}} data = new {{ClassName}}();
        {{fields_set}}
        data.set("state", state);
        data.set("inputtime", System.currentTimeMillis());
        data.save();

        renderRtn(RtnFactory.succ);
    }

    public void doEdit() {
        String id = getPara("id");
        {{fields_para}}
        int state = getParaToInt("state");

        {{ClassName}} data = {{ClassName}}.dao.findById(id);
        {{fields_set}}
        data.set("state", state);
        data.update();

        renderRtn(RtnFactory.succ);
    }

    public void doDelete() {
        String id = getPara("id");
        {{ClassName}}.dao.deleteById(id);
        renderRtn(RtnFactory.succ);
    }

    public void doDeletes() {
        String idsStr = getPara("idsStr");
        {{ClassName}}.dao.deletes(Arrays.asList(idsStr.split(",")));
        renderRtn(RtnFactory.succ);
    }
}
