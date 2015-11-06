package org.jfaster.mango.generator.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
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

    @RequestMapping(value = "/do", method = RequestMethod.GET)
    @ResponseBody
    public String generate(
            String tableName,
            String pojoName,
            String daoName,
            String[] columns,
            String[] properties,
            String keyPropertyClassName) throws Exception {

        try {
            TypeName keyPropertyType = null;
            if ("int".equals(keyPropertyClassName)) {
                keyPropertyType = TypeName.INT;
            } else if ("long".equals(keyPropertyClassName)) {
                keyPropertyType = TypeName.LONG;
            } else if ("String".equals(keyPropertyClassName))  {
                keyPropertyType = ClassName.get(String.class);
            }

            String code = CodeGenerator.generate(tableName, Lists.newArrayList(columns),
                    pojoName, Lists.newArrayList(properties),
                    daoName, properties[0], keyPropertyType);

            return JSON.toJSONString(Result.ok(code));
        } catch (Exception e) {
            return JSON.toJSONString(Result.error(e.getMessage()));
        }
    }

    private static class Result {

        private int status;
        private String msg;
        private String code;

        public static Result ok(String code) {
            Result r = new Result();
            r.setStatus(1);
            r.setCode(code);
            return r;
        }

        public static Result error(String msg) {
            Result r = new Result();
            r.setStatus(-1);
            r.setMsg(msg);
            return r;
        }

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
