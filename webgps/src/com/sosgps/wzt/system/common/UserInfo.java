package com.sosgps.wzt.system.common;

import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TUser;

/**
 * <p>
 * Title: UserInfo
 * </p>
 * 
 * @version 1.0
 * 
 */

public class UserInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TUser user;// �û���Ϣ
	private TEnt ent;// ��ҵ��Ϣ

	public UserInfo() {
	}

	public UserInfo(TUser _user) {
		this.user = _user;
		this.userId = user.getId();
		this.userAccount = user.getUserAccount();
		this.roleName = _user.getRole().getRoleName();
		this.roleCode = _user.getRole().getRoleCode();
		this.empCode = _user.getEmpCode();
	}

	public UserInfo(TUser _user, TEnt _ent) {
		this.user = _user;
		this.ent = _ent;
		this.userId = user.getId();
		this.userAccount = user.getUserAccount();
		this.roleName = _user.getRole().getRoleName();
		this.roleCode = _user.getRole().getRoleCode();
		this.empCode = _user.getEmpCode();
	}

	private Long userId;// �û�ID
	private String userAccount;// �û�����
	private String roleName;// ��ɫ����
	private String roleCode;// ��ɫ����
	private String ip;// ��¼ip
	private String empCode; // ������ҵID
	private String empCodeBase64;
	private String userAccountBase64;
	private String passwordBase64;
	private String infoAcquisitionUrl;// ������ת����Ϣ�ɼ�ϵͳ
	private String targetTemplateType;//Ŀ��ά��ģ������ 0:��;1:Ѯ;2:��;
	
	public String getTargetTemplateType() {
		return targetTemplateType;
	}

	public void setTargetTemplateType(String targetTemplateType) {
		this.targetTemplateType = targetTemplateType;
	}

	public String getEmpCodeBase64() {
		return empCodeBase64;
	}

	public void setEmpCodeBase64(String empCodeBase64) {
		this.empCodeBase64 = empCodeBase64;
	}

	public String getUserAccountBase64() {
		return userAccountBase64;
	}

	public void setUserAccountBase64(String userAccountBase64) {
		this.userAccountBase64 = userAccountBase64;
	}

	public String getPasswordBase64() {
		return passwordBase64;
	}

	public void setPasswordBase64(String passwordBase64) {
		this.passwordBase64 = passwordBase64;
	}

	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            The roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return Returns the user.
	 */
	public TUser getUser() {
		return user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(TUser user) {
		this.user = user;
	}

	/**
	 * @return Returns the userAccount.
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount
	 *            The userAccount to set.
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @return Returns the userId.
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public TEnt getEnt() {
		return ent;
	}

	public void setEnt(TEnt ent) {
		this.ent = ent;
	}

	public String getInfoAcquisitionUrl() {
		return infoAcquisitionUrl;
	}

	public void setInfoAcquisitionUrl(String infoAcquisitionUrl) {
		this.infoAcquisitionUrl = infoAcquisitionUrl;
	}
}
