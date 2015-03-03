package com.best.cwp.demo;

import java.util.List;

import com.jinhe.tss.framework.persistence.IDao;
 
public interface DemoDao extends IDao<DemoEntity> {
	
	DemoEntity getEntityById(Long id);
	
	List<DemoEntity> getAllEntities();
	
}
