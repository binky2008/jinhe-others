package com.best.cwp.demo;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.best.cwp.TxTestSupport;

public class DemoTest extends TxTestSupport {
	
	@Autowired DemoAction demoAction;
	
	@Test
	public void test() {
		List<DemoEntity> list = demoAction.getAllEntities();
		Assert.assertEquals(0, list.size());
		
		DemoEntity entity = new DemoEntity();
		entity.setCode("test 1");
		entity.setName("test 1");
		entity = demoAction.create(entity );
		
		Long id = entity.getId();
		Assert.assertNotNull(id);
		entity = demoAction.getEntityById(id);
		Assert.assertNotNull(entity);
		Assert.assertEquals("test 1", entity.getCode());
		
		entity.setName("test 1 update");
		demoAction.update(entity);
		entity = demoAction.getEntityById(id);
		Assert.assertEquals("test 1 update", entity.getName());
		
		list = demoAction.getAllEntities();
		Assert.assertEquals(1, list.size());
		
		demoAction.delete(id);
		
		list = demoAction.getAllEntities();
		Assert.assertEquals(0, list.size());
	}

}
