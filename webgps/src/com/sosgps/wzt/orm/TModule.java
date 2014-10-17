package com.sosgps.wzt.orm;

import java.util.Set;

/**
 * TModule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TModule extends AbstractTModule implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public TModule() {
	}

	/** minimal constructor */
	public TModule(String moduleName, String moduleCode, String moduleType,
			String modulePath, String usageFlag, String createBy,
			String createDate, Long moduleId) {
		super(moduleName, moduleCode, moduleType, modulePath, usageFlag,
				createBy, createDate, moduleId);
	}

	/** full constructor */
	public TModule(String moduleName, String moduleCode, String moduleType,
			String modulePath, String moudleDesc, String usageFlag,
			String createBy, String createDate, Long moduleId,
			Long moduleGrade, Long parentid, Long leafFlag, Long visibleFlag,
			Long enableFlag, Long treeLevel, Long sortedIndex,
			Set refModuleRoles) {
		super(moduleName, moduleCode, moduleType, modulePath, moudleDesc,
				usageFlag, createBy, createDate, moduleId, moduleGrade,
				parentid, leafFlag, visibleFlag, enableFlag, treeLevel,
				sortedIndex, refModuleRoles);
	}

}
