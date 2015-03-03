package com.best.cwp.demo;

import java.util.List;
 
public interface DemoService {

	DemoEntity getEntityById(Long id);
	
	List<DemoEntity> getAllEntities();

	DemoEntity create(DemoEntity entity);
	
	DemoEntity update(DemoEntity entity);
	
	DemoEntity delete(Long id);
}

