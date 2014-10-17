/**
 * 
 */
package com.sosgps.wzt.terminal.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ModuleParamConfig;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.TEntTermtype;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TVehicleMsg;
import com.sosgps.wzt.orm.TermParamConfig;
import com.sosgps.wzt.terminal.bean.TTerminalBean;

 

/**
 * @author shiguang.zhou
 *
 */
public interface TerminalService {

	public TTermGroup getTTargetGroupByGroupId(Long groupId);
	
	public List getTTargetGroup(String empCode);
	
	public List getGroupTTargetObject(Long groupId);
	
	public List getUnGroupTTargetObject(Long groupId);
	
	public void saveTTargetObject(TTerminal tTargetObject);
	
	public void delTTargetObjectByIds(String[] ids);
	
	public TTerminal getTTargetObjectById(String id);
	
	public boolean update(com.sosgps.wzt.terminal.form.TerminalForm form,String old_parent_id);
	
	public boolean update(TTerminal terminal, String group_id, String old_parent_id);
	 	
	public boolean save(com.sosgps.wzt.terminal.form.TerminalForm form);
	
	public List getAllTermType();
	
	public TEntTermtype getTermTypeByCode(String code);
	
	public RefTermGroup getTermGroup(TTerminal id);
	
	//根据GPSSN查询终端 
	public boolean findTermById(String field);
	//SIMCARD->TERM
	public boolean findTermBySim(String sim);
	//获取终端
	public TTerminal findTerminal(String id);
	public TTerminal findTermBySimcard(String sim);
	public String insertTerminal(String entCode, String deviceId, String locateType,String areaName);//add by yanglei
	public String deleteTerinal(String entCode, String phoneNumber);//add by yanglei
	
	public List findTerminalByTermName(String termName, String entCode, boolean isVague);

	public List findByExample(TTerminal tTerminal);
	
	public Page<Object[]> listTerminal(String entCode, Long userId, int pageNo, int pageSize,
			String searchValue);
	// sos新增终端
	public void saveTTerminal(TTerminalBean terminalBean);
	// sos修改终端
	public void updateTTerminal(TTerminalBean terminalBean);
	// sos删除终端
	public void deleteTTerminal(TTerminalBean terminalBean);
	// sos查询企业终端数
	public Long findTermNum(String entCode);
	
	public List<TTerminal> findTerminal(String deviceId,String entCode,boolean isVague);
	//根据企业代码获取企业下的所有的终端 add by wangzhen
	public List<TTerminal> findAllTerminal(String entCode);
    //根据企业代码获取企业下的素有的终端号 add by wangzhen 
	public List<String> getDeviceId(String entCode);
	//通过终端和模块类型获得终端的配置文件
	public TermParamConfig getTermParamConfigByDeviceIdAndType(String deviceId,Integer type);
	//更新终端的配置文件
    public Integer updateTermConfig(TermParamConfig termPc);
    //删除终端对应的配置
    public Integer deleteTermParamConfig(String deviceId);
    //统计该类的终端配置有多少
    public int countTermParamConfig(String deviceId);
    //获取默认的配置
    public List<ModuleParamConfig> getAllModule();
    //保存配置
    public Integer saveTermParamConfig(TermParamConfig termPar0);
    
    public String queryTerminalTree(String entCode, Long userId, String searchValue);

    public abstract boolean saveVehicleMsg(TVehicleMsg paramTVehicleMsg);

    public abstract boolean updateVehicleMsg(TVehicleMsg paramTVehicleMsg);

    public abstract void deleteVehicleMsg(TVehicleMsg paramTVehicleMsg);

    public abstract Page<Object[]> listTerminalByDeviceId(String paramString1, Long paramLong, int paramInt1, int paramInt2, String paramString2, String paramString3);
  }
