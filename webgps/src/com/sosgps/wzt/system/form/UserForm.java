package com.sosgps.wzt.system.form;

import java.util.List;

import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TUser;



public class UserForm extends BaseForm {
private TUser user = new TUser();
private String empId ;
private String roleId;
private List roleList;
private TRole role ;
private java.lang.Long id;
private String empCode;

private String oldPassword;
private String veriMessage;

public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}
/**
 * @return Returns the role.
 */
public TRole getRole() {
    return role;
}
/**
 * @param role The role to set.
 */
public void setRole(TRole role) {
    this.role = role;
}
/**
 * @return Returns the roleId.
 */
public String getRoleId() {
    return roleId;
}
/**
 * @param roleId The roleId to set.
 */
public void setRoleId(String roleId) {
    this.roleId = roleId;
}
/**
 * @return Returns the empId.
 */
public String getEmpId() {
    return empId;
}
/**
 * @param empId The empId to set.
 */
public void setEmpId(String empId) {
    this.empId = empId;
}
/**
 * @return Returns the user.
 */
public TUser getUser() {
    return user;
}
/**
 * @param user The user to set.
 */
public void setUser(TUser user) {
    this.user = user;
}
/**
 * @return Returns the roleList.
 */
public List getRoleList() {
    return roleList;
}
/**
 * @param roleList The roleList to set.
 */
public void setRoleList(List roleList) {
    this.roleList = roleList;
}


public String getEmpCode() {
	return empCode;
}

public void setEmpCode(String empCode) {
	this.empCode = empCode;
}

public String getOldPassword() {
	return oldPassword;
}

public void setOldPassword(String oldPassword) {
	this.oldPassword = oldPassword;
}

public String getVeriMessage() {
	return veriMessage;
}

public void setVeriMessage(String veriMessage) {
	this.veriMessage = veriMessage;
}
}
