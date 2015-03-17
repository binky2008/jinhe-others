package com.best.cwp;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.IH2DBServer;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;

@ContextConfiguration(
	  locations={
			"classpath:META-INF/framework-spring.xml",
		    "classpath:META-INF/remote/um-remote.xml",
		    "classpath:META-INF/spring-mvc.xml",
		    "classpath:META-INF/spring-test.xml"
	  }   
) 
@TransactionConfiguration(defaultRollback = true) // 自动回滚设置为false，否则数据将不插进去
public abstract class TxTestSupport extends AbstractTransactionalJUnit4SpringContextTests { 
 
    protected static Logger log = Logger.getLogger(TxTestSupport.class);    
    
    @Autowired protected IH2DBServer dbserver;
    @Autowired protected ParamService paramService;
    
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    
    @Before
    public void setUp() throws Exception {
        Global.setContext(super.applicationContext);
        Context.setResponse(response = new MockHttpServletResponse());
		Context.initRequestContext(request = new MockHttpServletRequest());
    	
    	// 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    protected String getDefaultSource(){
    	return "connectionpool";
    }
 
    @After
    public void tearDown() throws Exception {
        dbserver.stopServer();
    }
    
    protected void login(Long userId, String loginName) {
    	OperatorDTO loginUser = new OperatorDTO(userId, loginName);
    	String token = TokenUtil.createToken("1234567890", userId); 
        IdentityCard card = new IdentityCard(token, loginUser);
        Context.initIdentityInfo(card);
    }
}
