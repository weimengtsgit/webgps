package com.sosgps.wzt.group.service;
import java.util.List;
import com.sosgps.wzt.orm.TEnt;
public interface GroupService {
	public List getTTargetGroup(String empCode);
	public boolean modTGNameById(Long groupId, String groupName,String starttime,String endtime,String groupstatus, Long week);
	public boolean delTGById(Long groupId);
	public boolean addChildTG(Long groupId,String childGroupName,TEnt ent,String starttime,String endtime, Long userId, Long week);
	public TEnt getEnt(String id);
	// sos查询组关联终端
	public List findRefTermGroupById(Long groupId);
}
