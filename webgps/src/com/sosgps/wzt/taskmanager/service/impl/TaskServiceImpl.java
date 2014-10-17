package com.sosgps.wzt.taskmanager.service.impl;

import java.util.List;

import com.sosgps.wzt.orm.TTask;
import com.sosgps.wzt.taskmanager.dao.TTaskDao;
import com.sosgps.wzt.taskmanager.service.TaskService;
/**
 * 任务管理接口实现类
 * @author zhangwei
 * 2009-05-21
 */
public class TaskServiceImpl implements TaskService{
	private TTaskDao tTaskDao;

	public Long saveTask(TTask task) throws RuntimeException{		

		return this.tTaskDao.save(task);

	}
	public void updateTask(TTask task) throws RuntimeException{		

		this.tTaskDao.update(task);

	}
	public void delTask(String entCode, String ids)throws RuntimeException{
		this.tTaskDao.deleteIn(entCode,ids);
	}	
	public List findBy(String entCode,String taskIds,String taskNames,String taskTypes)throws RuntimeException{
		return this.tTaskDao.findBy(entCode, taskIds, taskNames, taskTypes);
	}
	public List findByTaskName(Object entCode,Object taskName)throws RuntimeException{
		return this.tTaskDao.findByTaskName(entCode,taskName);
	}
	public TTaskDao getTTaskDao() {
		return tTaskDao;
	}

	public void setTTaskDao(TTaskDao taskDao) {
		tTaskDao = taskDao;
	}
}
