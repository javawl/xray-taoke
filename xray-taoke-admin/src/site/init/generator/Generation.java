package generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xray.act.jfinal.JfUtil;
import com.xray.act.util.StringUtil;
import com.xray.admin.common.Constant;

public class Generation {

    private static final String keyWord = "example";
    private static final String basePath = System.getProperty("user.dir").replace("\\", "/");

    public static final int PACKAGETYPE_ADMIN = 1;
    public static final int PACKAGETYPE_WEB = 2;

    private List<String> fields;

    private static String model_name;
    private String module_text;
    private int package_type;

    /**
     * @param moduleName
     *            模块名称
     * @param modelName
     *            表名称
     * @param packageName
     *            包名
     * @param packageType
     *            包类型
     * @param absolutePath
     *            指定生成目录（默认为当前路径）
     */
    public void run(String moduleName, String packageName, String modelName, int packageType, String absolutePath) {
        module_text = moduleName;
        model_name = modelName;
        package_type = packageType;

        String examplePath = basePath + "/src/site/skeleton/example"; // 范例地址
        String javaPath = !StringUtil.isEmpty(absolutePath) ? absolutePath : basePath + "/src/main/java";
        String webappPath = !StringUtil.isEmpty(absolutePath) ? absolutePath : basePath + "/src/main/webapp";

        fields = getFields(modelName);
        // 生成Model
        createFile(modelName, examplePath + "/" + toClassName(keyWord) + ".java", javaPath + "/com/xray/" + packageName + "/" + getJavaPackageTypeName(packageType) + "/model/" + toClassName(modelName) + ".java");
        // 生成Controller
        createFile(modelName, examplePath + "/" + toClassName(keyWord) + "Controller.java", javaPath + "/com/xray/" + packageName + "/" + getJavaPackageTypeName(packageType) + "/web/controller/" + toClassName(modelName) + "Controller.java");
        // 生成list.html
        createFile(modelName, examplePath + "/list.html", webappPath + "/template/" + getWebPackageName(packageType) + "/list.html");
        // 生成form.html
        createFile(modelName, examplePath + "/form.html", webappPath + "/template/" + getWebPackageName(packageType) + "/form.html");
        System.out.println("end...");
    }

    private void createFile(String name, String exampleFilePath, String newFilePath) {
        InputStream in = null;
        OutputStream out = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            String dirPath = newFilePath.substring(0, newFilePath.lastIndexOf("/"));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File newFile = new File(newFilePath);
            if (newFile.exists()) {
                newFile.delete();
            }
            in = new FileInputStream(new File(exampleFilePath));
            out = new FileOutputStream(newFile);
            br = new BufferedReader(new InputStreamReader(in));
            bw = new BufferedWriter(new OutputStreamWriter(out));
            String lineStr = "";
            while ((lineStr = br.readLine()) != null) {
                if (lineStr.contains("{{package}}")) {
                    bw.write("package " + dirPath.split(basePath + "/src/main/java/")[1].split(keyWord)[0].replaceAll("/", ".") + ";\t\n");
                    bw.flush();
                    continue;
                } else if (lineStr.contains("{{import_model}}")) {
                    bw.write("import " + dirPath.split(basePath + "/src/main/java/")[1].split(keyWord)[0].replaceAll("/", ".").split(".web.controller")[0] + ".model." + toClassName(name) + ";\t\n");
                    bw.flush();
                    continue;
                } else if (lineStr.contains("{{ClassName}}")) {
                    lineStr = lineStr.replace("{{ClassName}}", toClassName(name));
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{modelName}}")) {
                    lineStr = lineStr.replace("{{modelName}}", name);
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{title}}")) {
                    lineStr = lineStr.replace("{{title}}", "后台系统");
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{actionKey}}")) {
                    lineStr = lineStr.replace("{{actionKey}}", toActionKey(name));
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{webPackageTypeName}}/{{packageName}}")) {
                    lineStr = lineStr.replace("{{webPackageTypeName}}/{{packageName}}", getWebPackageName(package_type));
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{moduleName}}")) {
                    lineStr = lineStr.replace("{{moduleName}}", module_text);
                    bw.write(lineStr + "\n");
                } else if (lineStr.contains("{{fields_para}}")) {
                    for (String field : fields) {
                        bw.write("\t\tString " + field + " = getPara(\"" + field + "\");\n");
                    }
                } else if (lineStr.contains("{{fields_set}}")) {
                    for (String field : fields) {
                        bw.write("\t\tdata.set(\"" + field + "\"," + field + ");\n");
                    }
                } else if (lineStr.contains("{{fields_th}}")) {
                    for (String field : fields) {
                        bw.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<th>" + field + "</th>\n");
                    }
                } else if (lineStr.contains("{{fields_td}}")) {
                    for (String field : fields) {
                        bw.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td>${item." + field + "!}</td>\n");
                    }
                } else if (lineStr.contains("{{fields_col}}")) {
                    for (String field : fields) {
                        bw.write("\t\t\t\t\t\t\t\t\t<div class='col-md-6'>\n");
                        bw.write("\t\t\t\t\t\t\t\t\t\t<div class='form-group'>\n");
                        bw.write("\t\t\t\t\t\t\t\t\t\t\t<label>" + field + "</label>\n");
                        bw.write("\t\t\t\t\t\t\t\t\t\t\t<input id='" + field + "' type='text' class='form-control' placeholder='请输入" + field + "' value='<#if data??>${data." + field + "!}</#if>'>\n");
                        bw.write("\t\t\t\t\t\t\t\t\t\t</div>\n");
                        bw.write("\t\t\t\t\t\t\t\t\t</div>\n");
                    }
                } else if (lineStr.contains("{{fields_check}}")) {
                    for (String field : fields) {
                        bw.write("\t\t\t\t\tvar " + field + " = $(\"#" + field + "\").val();\n");
                        bw.write("\t\t\t\t\tif(!" + field + ") {\n");
                        bw.write("\t\t\t\t\t\tbase.showWarMsg('请输入" + field + "');\n");
                        bw.write("\t\t\t\t\t\treturn;\n");
                        bw.write("\t\t\t\t\t}\n");
                    }
                } else if (lineStr.contains("{{fields_formdata}}")) {
                    for (String field : fields) {
                        bw.write("\t\t\t\t\t" + field + ":" + field + ",\n");
                    }
                } else {
                    bw.write(lineStr + "\n");
                }
                bw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
            }
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Tookits
     */
    private static List<String> getFields(String modelName) {
        JfUtil.bindModel();
        List<String> fieldList = new ArrayList<String>();
        String sql = "SHOW COLUMNS FROM " + modelName;
        List<Record> dataList = Db.use(Constant.db_dataSource).find(sql);
        for (Record data : dataList) {
            if (!data.getStr("Field").equals("state") && !data.getStr("Field").equals("inputtime") && !data.getStr("Field").equals("seqid"))
                fieldList.add(data.getStr("Field"));
        }
        return fieldList;
    }

    private static String toClassName(String word) {
        // 首字母大写
        word = word.substring(0, 1).toUpperCase() + word.substring(1);
        // 下划线后的首字母大写
        if (word.contains("_"))
            word = word.split("_")[0] + word.split("_")[1].substring(0, 1).toUpperCase() + word.split("_")[1].substring(1);
        return word;
    }

    private static String toActionKey(String word) {
        // 下划线变斜杠
        if (word.contains("_"))
            word = word.replace("_", "/");
        return word;
    }

    private static String getJavaPackageTypeName(int packageType) {
        String packageTypeName;
        switch (packageType) {
        case PACKAGETYPE_ADMIN:
            packageTypeName = "admin";
            break;
        default:
            packageTypeName = "act";
            break;
        }
        return packageTypeName;
    }

    private static String getWebPackageName(int packageType) {
        String name;
        switch (packageType) {
        case PACKAGETYPE_ADMIN:
            name = "admin";
            break;
        default:
            name = "pages";
            break;
        }
        
        if (model_name.contains("_"))
            name+= "/"+ model_name.split("_")[1];
        else
            name+= "/"+ model_name;
        return name;
    }
}
