package com.jinhe.dm.analyse.btr;

import java.util.Map;

import com.jinhe.dm.data.sqlquery.SOUtil;
import com.jinhe.dm.report.ScriptPreCheator;

public class BtrScriptPreCheator implements ScriptPreCheator {

	public String preCheat(String script, Map<String, Object> dataMap) {
		// TODO 往dataMap里放入用户权限、角色、组织等信息
		return SOUtil.freemarkerParse(script, dataMap);
	}

}
