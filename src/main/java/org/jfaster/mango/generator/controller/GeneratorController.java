package org.jfaster.mango.generator.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;
import org.jfaster.mango.generator.core.CodeGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ash
 */
@Controller
public class GeneratorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/do", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String generate(
            String tableName,
            String pojoName,
            String daoName,
            String[] columns,
            String[] properties,
            String keyPropertyClassName,
            String strAutoInc) throws Exception {

        if (StringUtils.isBlank(tableName)) {
            return error("数据库表名不能为空");
        }
        if (StringUtils.isBlank(pojoName)) {
            return error("pojo类全名不能为空");
        }
        if (StringUtils.isBlank(daoName)) {
            return error("dao类全名不能为空");
        }
        if (StringUtils.isBlank(keyPropertyClassName)) {
            return error("主键属性类名不能为空");
        }
        if (columns.length == 0) {
            return error("数据库字段数量不能为0");
        }
        if (columns.length != properties.length) {
            return error("数据库字段数量与类属性数量不匹配");
        }
        for (String column : columns) {
            if (StringUtils.isBlank(column)) {
                return error("数据库字段不能为空");
            }
        }
        for (String property : properties) {
            if (StringUtils.isBlank(property)) {
                return error("类属性不能为空");
            }
        }

        ClassName pojoType;
        try {
            pojoType = ClassName.bestGuess(pojoName);
        } catch (Exception e) {
            return error("pojo类全名不合法");
        }
        ClassName daoType;
        try {
            daoType = ClassName.bestGuess(daoName);
        } catch (Exception e) {
            return error("dao类全名不合法");
        }

        TypeName keyPropertyType;
        if ("int".equals(keyPropertyClassName)) {
            keyPropertyType = TypeName.INT;
        } else if ("long".equals(keyPropertyClassName)) {
            keyPropertyType = TypeName.LONG;
        } else if ("String".equals(keyPropertyClassName))  {
            keyPropertyType = ClassName.get(String.class);
        } else {
            return error("主键属性类名不合法");
        }

        boolean isKeyAutoInc = Boolean.valueOf(strAutoInc);

        try {
            String codeContent = CodeGenerator.generate(tableName, Lists.newArrayList(columns),
                    pojoType, Lists.newArrayList(properties),
                    daoType, properties[0], keyPropertyType, isKeyAutoInc);
            return ok(daoType.simpleName() + ".java", codeContent);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private static String ok(String codeName, String code) {
        Result r = new Result();
        r.setStatus(1);
        r.setCodeName(codeName);
        r.setCodeContent(code);
        return JSON.toJSONString(r);
    }

    private static String error(String msg) {
        Result r = new Result();
        r.setStatus(-1);
        r.setMsg(msg);
        return JSON.toJSONString(r);
    }

    private static class Result {

        private int status;
        private String msg;
        private String codeName;
        private String codeContent;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public String getCodeContent() {
            return codeContent;
        }

        public void setCodeContent(String codeContent) {
            this.codeContent = codeContent;
        }
    }

}
