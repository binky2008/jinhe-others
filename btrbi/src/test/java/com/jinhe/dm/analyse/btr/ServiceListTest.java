package com.jinhe.dm.analyse.btr;

import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Test;

import com.jinhe.tss.framework.sso.context.Context;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ServiceListTest {
	
	ServiceList serviceList = new ServiceList();
	BaseService baseService = new BaseServiceImpl();
	
	@Test
	public void testServiceList() {
		baseService.login("BL01037", "jonking");
		
		HttpSession session = Context.getRequestContext().getSession();
        
		session.setAttribute(BTRAfterLoginCustomizer.USER_GROUPS_NAME, Arrays.asList( "百世快运".split(",") ));
		Assert.assertTrue(serviceList.getOrgList().size() > 10);
		Assert.assertTrue(serviceList.getCenterList("浙江分公司").size() > 1);
		
		session.setAttribute(BTRAfterLoginCustomizer.USER_GROUPS_NAME, Arrays.asList( "百世快运,浙江分公司".split(",") ));
		Assert.assertTrue(serviceList.getOrgList().size() == 1);
		Assert.assertTrue(serviceList.getCenterList("浙江分公司").size() > 1);
		
		session.setAttribute(BTRAfterLoginCustomizer.USER_GROUPS_NAME, Arrays.asList( "百世快运,浙江分公司,杭州分拨".split(",") ));
		Assert.assertTrue(serviceList.getOrgList().size() == 1);
		Assert.assertTrue(serviceList.getCenterList("浙江分公司").size() == 1);
	}
	
    protected String getDefaultSource(){
    	return "connpool-vf-oracle";
    }
}
