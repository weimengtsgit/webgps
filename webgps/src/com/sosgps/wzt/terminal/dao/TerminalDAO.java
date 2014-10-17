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
	
	// sos��ѯ��ҵ�ն���
	public Long findTermNum(String entCode);
    //ͨ����ҵ��Ѱ��ҵ�µ����е��ն� add by wangzhen
	public List<TTerminal> findAllTerminal(String entCode);
	//ͨ����ҵ��Ѱ��ҵ�µ����е��ն�Id add by wangzhen
	public List<String> getDeviceId(String entCode);
    ////ͨ���ն˺�ģ�����ͻ���ն˵������ļ�
    public TermParamConfig findTermParamConfigByDeviceIdAndType(String deviceId, Integer type);
    //�����ն������ļ�
    public Integer updateTermConfig(TermParamConfig termPc);
    //ɾ���ն������ļ�
    public Integer deleteTermParamConfig(String deviceIds);
    //ͳ���ն������ļ�
    public int countTermParamConfig(String deviceIds);
    //��ȡ���е�ģ������
    public List<ModuleParamConfig> getAllModule();
    //��������
    public Integer saveTermParamConfig(TermParamConfig termPar0);
    //�޸���Ա���ڱ��������ֶ�
    public boolean updateEmployeeAttend(String deviceId,String termName,String simcard,String groupName,Long groupId);
    public TTermGroup findTermGroupById(Long id);
    public abstract Page<Object[]> listTerminalByDeviceId(String paramString1, Long paramLong, int paramInt1, int paramInt2, String paramString2, String paramString3);
  }
