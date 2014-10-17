/**
 * 
 */
package com.sosgps.wzt.terminal.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ModuleParamConfig;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TermParamConfig;

/**
 * @author shiguang.zhou
 *
 */
public interface TerminalDAO {
	
	public List findByExample(TTerminal tTerminal);
	
	public List findByGroupId(Long groupId);
	
	public List findNoGroupId(Long groupId);
	
	public boolean update(TTerminal tTargetObject);
	
	public void save(TTerminal tTargetObject);
	
	public void delTTargetObjectById(String id);
	
	public TTerminal findById(String id);
	
	public void delete(TTerminal tTargetObject);
	
	public List findAll();
	
	public TTerminal findTermById(String field);

	public List findByEntCode(Object entCode);
	
	public TTerminal findTermBySim(String sim);
	
	public List findTermByTerminal(final TTerminal transientInstance);//add by yanglei
	
	public List findTerminal(String deviceId,String entCode,boolean isVague) ;

	public List findTerminalByTermName(String termName, String entCode, boolean isVague);
	
	public Page<Object[]> listTerminal(String entCode, Long userId, int pageNo, int pageSize,
			String searchValue);
	
	// sos查询企业终端数
	public Long findTermNum(String entCode);
    //通过企业搜寻企业下的所有的终端 add by wangzhen
	public List<TTerminal> findAllTerminal(String entCode);
	//通过企业搜寻企业下的所有的终端Id add by wangzhen
	public List<String> getDeviceId(String entCode);
    ////通过终端和模块类型获得终端的配置文件
    public TermParamConfig findTermParamConfigByDeviceIdAndType(String deviceId, Integer type);
    //更新终端配置文件
    public Integer updateTermConfig(TermParamConfig termPc);
    //删除终端配置文件
    public Integer deleteTermParamConfig(String deviceIds);
    //统计终端配置文件
    public int countTermParamConfig(String deviceIds);
    //获取所有的模块配置
    public List<ModuleParamConfig> getAllModule();
    //保存配置
    public Integer saveTermParamConfig(TermParamConfig termPar0);
    //修改人员考勤报表冗余字段
    public boolean updateEmployeeAttend(String deviceId,String termName,String simcard,String groupName,Long groupId);
    public TTermGroup findTermGroupById(Long id);
    public abstract Page<Object[]> listTerminalByDeviceId(String paramString1, Long paramLong, int paramInt1, int paramInt2, String paramString2, String paramString3);
  }
