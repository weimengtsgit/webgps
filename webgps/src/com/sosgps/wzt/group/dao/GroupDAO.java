/**
 * 
 */
package com.sosgps.wzt.group.dao;

import java.util.List;

import com.sosgps.wzt.orm.TTermGroup;

/**
 * @author shiguang.zhou
 *
 */
public interface GroupDAO {

	public List getTTargetGroup(String empCode);
	public void update(TTermGroup tTargetGroup);
	public TTermGroup findById(Long id);
	public void delete(TTermGroup persistentInstance);
	public List findChild(Long parentId);
	public List findChildLastSortById(Long groupId);
	public void save(TTermGroup persistentInstance);
	public TTermGroup findByParentId(Long parentId);
	// sos查询组关联终端
	public List findRefTermGroupById(Long groupId);
}
