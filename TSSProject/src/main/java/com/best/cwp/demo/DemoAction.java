package com.best.cwp.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("DemoAction")
@RequestMapping("/demo")
public class DemoAction {
 
    @Autowired private DemoService service;
 
    @RequestMapping("/")
    @ResponseBody
    public List<DemoEntity> getAllEntities() {
        return service.getAllEntities();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public DemoEntity getEntityById(@PathVariable Long id) {
        return service.getEntityById(id);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public DemoEntity create(DemoEntity entity) {
        service.create(entity);
        return entity;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public DemoEntity update(DemoEntity entity) {
        service.update(entity);
        return entity;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public DemoEntity delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

