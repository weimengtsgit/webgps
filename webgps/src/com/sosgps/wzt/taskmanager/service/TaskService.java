package com.sosgps.wzt.taskmanager.service;

import java.util.List;

import com.sosgps.wzt.orm.TTask;

/**
 * 任务管理接口
 * @author Administrator
 *
 */
public interface TaskService { 
	public Long saveTask(TTask task) throws RuntimeException;
	public void updateTask(TTask task) throws RuntimeException;
	public void delTask(String entCode,String ids)throws RuntimeException;
	public List findBy(String entCode,String taskIds,String taskNames,String taskTypes)throws RuntimeException;
	public List findByTaskName(Object entCode,Object taskName)throws RuntimeException;
}
