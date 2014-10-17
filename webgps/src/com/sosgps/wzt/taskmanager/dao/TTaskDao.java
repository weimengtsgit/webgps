package com.sosgps.wzt.taskmanager.dao;

import java.util.List;

import com.sosgps.wzt.orm.TTask;

public interface TTaskDao {
	 public Long save(TTask task) throws RuntimeException;
	 public void update(TTask task)throws RuntimeException;
	 public void deleteIn(String entCode,String ids)throws RuntimeException;
	 public List findBy(String entCode,String taskIds,String taskNames,String taskTypes)throws RuntimeException;
	 public List findByTaskName(Object entCode,Object taskName)throws RuntimeException;
}
