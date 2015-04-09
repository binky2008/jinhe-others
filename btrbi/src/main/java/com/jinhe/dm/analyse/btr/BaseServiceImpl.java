package com.jinhe.dm.analyse.btr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jinhe.dm.data.sqlquery.SQLExcutor;

@Service("BaseService")
@SuppressWarnings("unchecked")
public class BaseServiceImpl implements BaseService {
 
	public List<Map<String, Object>>  getOrgList() {
		String script = "select '-1' as id, -1 as pk, '-1' as code, '全网' as name from dual" +
				" union all " +
				"select t.name as id, t.id as pk, t.code as code, t.name as name from gtv_org_golden t where t.parent_id=5555";
		SQLExcutor excutor = new SQLExcutor(false);
		excutor.excuteQuery(script);
 
		List<String> fatherGroups = ServiceList.getFatherGroups();
        if(fatherGroups != null) {
            if(fatherGroups.size() == 1) { // 总部员工
            	return excutor.result;
            } 
            else if(fatherGroups.size() >= 2) { // 分公司员工 & 分拨员工，只能看到其所在的分公司
            	for(Map<String, Object> temp : excutor.result) {
            		if(temp.get("name").equals(fatherGroups.get(1))) {
            			return Arrays.asList(temp);
            		}
            	}
            }
        }
 
		return new ArrayList<Map<String, Object>>();
	}
	
	public List<Map<String, Object>> getCenterList(String org) {
		String script = "select '-1' as id, -1 as pk, '-1' as code, '全部' as name from dual" +
				" union all " +
				" select t.name as id, t.id as pk, t.code as code, t.name as name from gt_site t " +
				" where type_code = '01' and status = 'ENABLE'  and org_name = '" + org + "' ";
		SQLExcutor excutor = new SQLExcutor(false);
		excutor.excuteQuery(script);
		
		List<String> fatherGroups = ServiceList.getFatherGroups();
		if(fatherGroups != null && fatherGroups.size() >= 3) { // 分拨员工，只能看到其所在的分拨
        	for(Map<String, Object> temp : excutor.result) {
        		if(temp.get("name").equals(fatherGroups.get(2))) {
        			return Arrays.asList(temp);
        		}
        	}
        }
		
		return excutor.result;
	}
	
	public List<Map<String, Object>> getAllCenterList() {
		String script = "select '-1' as id, -1 as pk, '-1' as code, '全部' as name from dual" +
				" union all " +
				" select t.name as id, t.id as pk, t.code as code, t.name as name from gt_site t " +
				" where type_code = '01' and status = 'ENABLE' ";
		SQLExcutor excutor = new SQLExcutor(false);
		excutor.excuteQuery(script);
 
		return excutor.result;
	}
}
