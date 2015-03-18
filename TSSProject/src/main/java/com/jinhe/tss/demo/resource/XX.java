package com.jinhe.tss.demo.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.permission.IResource;
import com.jinhe.tss.util.BeanUtil;

@Entity
@Table(name = "xx_tbl")
@SequenceGenerator(name = "xx_sequence", sequenceName = "xx_sequence", initialValue = 1, allocationSize = 10)
public class XX extends OperateInfo implements IXForm, IDecodable, IResource {
    
	public static final Long DEFAULT_PARENT_ID = 0L;
    
	public static final String APPLICATION = "cwp";
    public static final String RESOURCE_TYPE = "R1"; 
    
    public static final String OPERATION_VIEW    = "1"; // 查看
    public static final String OPERATION_EDIT    = "2"; // 整改
    public static final String OPERATION_DELETE  = "3"; // 删除
    public static final String OPERATION_DISABLE = "4"; // 停用启用
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "xx_sequence")
    private Long    id;         // 主键
    
    @Column(length = 100, nullable = false)
    private String  name;       // 名称
    
    private Long    parentId;  // 父节点
    private Integer seqNo;    // 排序号
    private String  decode;  // 层码，要求唯一
    private Integer levelNo;// 层次值

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);;
        return map;
    }

    public Map<String, Object> getAttributes4XForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        return map;
    }

    public Class<?> getParentClass() {
        if(this.parentId.equals(DEFAULT_PARENT_ID)) {
            return XXResource.class;
        }
        return this.getClass();
    }

	public String getResourceType() {
		return RESOURCE_TYPE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getDecode() {
		return decode;
	}

	public void setDecode(String decode) {
		this.decode = decode;
	}

	public Integer getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}

	public Serializable getPK() {
		return this.getId();
	}
}
