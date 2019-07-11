{{package}}

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.xray.act.jfinal.JfModel;
import com.xray.act.util.SqlUtil;
import com.xray.admin.common.Constant;
import com.xray.admin.web.vo.PageVo;

@TableBind(configName = Constant.db_dataSource, tableName = "{{modelName}}", pkName = "seqid")
public class {{ClassName}} extends JfModel<{{ClassName}}> {
    private static final long serialVersionUID = 1L;
    public static final {{ClassName}} dao = new {{ClassName}}();

    public List<{{ClassName}}> queryList(Map<String, Object> cond, PageVo page) {
        String sql = "SELECT * FROM `{{modelName}}` WHERE 1=1";
        StringBuilder sb = new StringBuilder();
        if (cond != null) {
            if (cond.containsKey("state")) {
                sb.append(" AND `state`=").append(cond.get("state"));
            }
//            if (cond.containsKey("word")) {
//                sb.append(" AND `field1` LIKE '%").append(cond.get("word")).append("%'");
//            }
            if (cond.containsKey("inputtime")) {
                sb.append(" AND `inputtime`=").append(cond.get("inputtime"));
            }
        }
        if (page != null) {
            String countSql = "SELECT count(1) FROM `{{modelName}}` WHERE 1=1" + sb.toString();
            int count = Db.queryLong(countSql).intValue();
            page.setCount(count);
            if (count <= 0) {
                return new ArrayList<{{ClassName}}>();
            }
            sb.append(page.orderbySql());
            sb.append(page.limitSql());
        }
        return dao.find(sql + sb.toString());
    }

    public boolean deletes(List<String> idList) {
        return Db.update("DELETE FROM `{{modelName}}` WHERE `seqid` IN (" + SqlUtil.join(idList) + ")") == idList.size();
    }

}
