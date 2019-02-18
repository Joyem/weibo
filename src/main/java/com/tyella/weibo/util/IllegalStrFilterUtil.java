package com.tyella.weibo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//对用户输入内容进行校验，防止sql注入攻击
public class IllegalStrFilterUtil {
    private static final Logger Logger = LoggerFactory.getLogger(IllegalStrFilterUtil.class);

    /**
     * 对常见的sql注入攻击进行拦截
     *
     * @param sInput
     * @return
     *  true 表示参数不存在SQL注入风险
     *  false 表示参数存在SQL注入风险
     */
    public static Boolean sqlStrFilter(String sInput) {
        if (sInput == null || sInput.trim().length() == 0) {
            return false;
        }
        sInput = sInput.toUpperCase();

        if (sInput.indexOf("DELETE") >= 0 || sInput.indexOf("ASCII") >= 0 || sInput.indexOf("UPDATE") >= 0 || sInput.indexOf("SELECT") >= 0
                || sInput.indexOf("'") >= 0 || sInput.indexOf("SUBSTR(") >= 0 || sInput.indexOf("COUNT(") >= 0 || sInput.indexOf(" OR ") >= 0
                || sInput.indexOf(" AND ") >= 0 || sInput.indexOf("DROP") >= 0 || sInput.indexOf("EXECUTE") >= 0 || sInput.indexOf("EXEC") >= 0
                || sInput.indexOf("TRUNCATE") >= 0 || sInput.indexOf("INTO") >= 0 || sInput.indexOf("DECLARE") >= 0 || sInput.indexOf("MASTER") >= 0) {
            Logger.error("该参数怎么SQL注入风险：sInput=" + sInput);
            return false;
        }
        Logger.info("通过sql检测");
        return true;
    }


}
