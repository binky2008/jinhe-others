package com.best.cwp.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service("DemoService")
public class DemoServiceImpl implements DemoService {
    
    @Autowired private DemoDao dao;

	public DemoEntity getEntityById(Long id) {
		return dao.getEntityById(id);
	}

	public List<DemoEntity> getAllEntities() {
		return dao.getAllEntities();
	}

	public DemoEntity create(DemoEntity entity) {
		return dao.create(entity);
	}
	
	public DemoEntity update(DemoEntity entity) {
		return (DemoEntity) dao.update(entity);
	}
	
	public DemoEntity delete(Long id) {
		return dao.deleteById(id);
	}
 
}

