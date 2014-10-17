package com.sosgps.v21.dao;
 
import java.util.List;

public interface TargetTemplateDao {
	public List<Object[]> getCurrentTargetTemplateCount(String entCode,
			int type, int year, int targetOn);

	public List<Object> getTargetTemplate(String entCode,
			int type, int year, int targetOn, String tableName);
	
}
