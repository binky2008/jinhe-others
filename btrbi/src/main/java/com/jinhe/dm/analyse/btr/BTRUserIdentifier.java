package com.jinhe.dm.analyse.btr;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.data.sqlquery.SqlConfig;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.sso.UMPasswordIdentifier;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.InfoEncoder;

/**
 * <p>
 * UM本地用户密码身份认证器<br>
 * 根据用户帐号、密码等信息，通过UM本地数据库进行身份认证
 * </p>
 */
@Deprecated  // 原来DMS单独一个应用时，在DMS登陆需要这个
public class BTRUserIdentifier extends UMPasswordIdentifier {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    ILoginService loginservice = (ILoginService) Global.getBean("LoginService");
    
    protected boolean customizeValidate(IPWDOperator operator, String password){
        
    	log.debug("用户登陆时密码在TSS的主用户组中验证不通过，转向V5进行再次验证。");
        
        String loginName = operator.getLoginName();
        boolean result = login(loginName, password);
 
        if(result) {
            log.info("用户【" + loginName + "】的密码在V5中验证通过。");
            return true;
        } 
        else {
            log.warn("用户【" + loginName + "】的密码在V5中验证不通过。");
            return false;
        } 
    }
    
    public boolean login(String loginName, String password) {
		if (loginName == null || password == null)
			return false;

		String script = SqlConfig.getScript("login", 1);
		Map<Integer, Object> paramsMap = new HashMap<Integer, Object>();
		paramsMap.put(1, loginName);
		paramsMap.put(2, InfoEncoder.string2MD5(password).toLowerCase());

		SQLExcutor excutor = new SQLExcutor(false);
		excutor.excuteQuery(script, paramsMap);

		if (excutor.result.isEmpty()) {
			return false;
		}

		Map<String, Object> row = excutor.result.get(0);
		Long userId = EasyUtils.obj2Long(row.get("id"));
		HttpSession session = Context.getRequestContext().getRequest().getSession();
		session.setAttribute("BTR-USERID", userId);
		
		return true;
	}
}
