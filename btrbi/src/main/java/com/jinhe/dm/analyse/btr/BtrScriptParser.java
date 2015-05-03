package com.jinhe.dm.analyse.btr;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jinhe.dm.data.sqlquery.SOUtil;
import com.jinhe.dm.report.ScriptParser;
import com.jinhe.tss.framework.sso.context.Context;

/**
 * ${permission_1}: 登陆用户所属分公司
 * ${permission_2}: 登陆用户所属分拨
 */
public class BtrScriptParser implements ScriptParser {

	public String parse(String script, Map<String, Object> dataMap) {
		/*
    	 * 往dataMap里放入用户权限、角色、组织等信息，作为宏代码解析。
    	 */
    	if(Context.getRequestContext() != null) {
    		HttpSession session = Context.getRequestContext().getRequest().getSession();
    		Enumeration<String> keys = session.getAttributeNames();
    		while(keys.hasMoreElements()) {
    			String key = keys.nextElement();
    			dataMap.put(key, session.getAttribute(key).toString());
    		}
    	}
    	
    	// TODO 对value自身进行宏解析, eg: ${permission_1} 替换成 用户公司， ${permission_2} 替换成 用户分拨
    	
		return SOUtil.freemarkerParse(script, dataMap);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getFatherGroups() {
		HttpSession session = Context.getRequestContext().getSession();
		return (List<String>) session.getAttribute(BTRAfterLoginCustomizer.USER_GROUPS_NAME);
	}

}
