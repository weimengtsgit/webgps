/**
 * Copyright (C) 2012 SOSGPS All Rights Reserved
 */

package com.sosgps.v21.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * This interceptor is for general-purpose. Now only be utilized for partition
 * tables.
 * 
 * @author Qiang
 * 
 */
public class CustomizeInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */

	private static final long serialVersionUID = 7951566105251767225L;

	private static final Log log = LogFactory
			.getLog(CustomizeInterceptor.class);

	private static Properties properties;

	private static List<String> tablesHashList;

	static {
		properties = LoadProperties("interceptor.properties");
		String tablesString = (String) properties.get("PartitionTables");
		tablesHashList = Arrays.asList(tablesString.split(","));
	}

	private String seed_ = "";

	// Constructor with seed
	public CustomizeInterceptor(String seed) {
		if (seed_ != null && !seed_.equals("")) {
			seed_ = seed;
		} else {
			log.error("Can't set a null or empty string as seed!");
		}
	}

	// Default constructor
	public CustomizeInterceptor() {

	}

	/**
	 * Client dev should set seed of this interceptor, so it could compute
	 * accurate table name then inject it in onPrepareStatement method.
	 * 
	 * @param seed
	 */
	public void setSeed(String seed) {
		if (seed != null && !seed.equals("")) {
			seed_ = seed;
		} else {
			log.error("Can't set a null or empty string as seed!");
		}
	}

	/**
	 * Workhorse class to change hash table name
	 */
	@Override
	public String onPrepareStatement(String sql) {
		String orgTableName = getTableNameFromSQL(sql);
		if (orgTableName.contains(".")) {
			String[] element = orgTableName.split("\\.");
			orgTableName = element[element.length - 1];
		}

		if (!tablesHashList.contains(orgTableName)) {
			log.debug(orgTableName + " doesn't need partition.");
			return sql;
		}
		String tableSuffix = computerTableSuffix();
		sql = updateTableName(sql, tableSuffix);
		//cleanup();
		return sql;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		guestSeedFromSQL(entity, properties.getProperty("benchMarkColumn"));
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		guestSeedFromSQL(entity, properties.getProperty("benchMarkColumn"));
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * Try to parse entcode from SQL statement's where clause and set it to
	 * seed_. If null, do nothing.
	 * 
	 * @param entity
	 * @param benchMarkColumn,
	 *            column name.Decide which column to use to partition table.
	 */
	private void guestSeedFromSQL(Object entity, String benchMarkColumn) {

	}

	/**
	 * 
	 * @param hashSeed,
	 *            for now it's entcode.
	 * @param partitionTableNum,
	 *            partition table number, for now, it's 100
	 * @return the suffix of the partitioned table, like 0,1,2,...100 etc.
	 */
	private String computerTableSuffix() {
		int index = seed_.hashCode() % 100;
		index = index > 0 ? index : -index;
		return "_" + Integer.toString(index);
	}

	/**
	 * Set seed_ to empty after this intercepter finish its work
	 */
	private void cleanup() {
		seed_ = "";
	}

	/**
	 * Get table name from sql string.
	 * 
	 * @param sql,
	 *            sql statement string.
	 * @return
	 */
	private String getTableNameFromSQL(String sql) {
		String tableName = "";
		String[] sqlElement = sql.split("\\s");
		if ("select".equalsIgnoreCase(sqlElement[0])
				|| "delete".equalsIgnoreCase(sqlElement[0])) {
			for (int i = 0; i < sqlElement.length; i++) {
				if ("from".equalsIgnoreCase(sqlElement[i])) {
					i = i + 1;
					while ("".equals(sqlElement[i]))
						i++;
					tableName = sqlElement[i];
					if (tableName.startsWith("("))
					    continue;
					return tableName;
				}
			}
			return tableName;
		} else if ("insert".equalsIgnoreCase(sqlElement[0])) {
			for (int i = 0; i < sqlElement.length; i++) {
				if ("into".equalsIgnoreCase(sqlElement[i])) {
					i = i + 1;
					while ("".equals(sqlElement[i]))
						i++;
					tableName = sqlElement[i];
					return tableName;
				}
			}
			return tableName;
		} else if ("update".equalsIgnoreCase(sqlElement[0])) {
			int i = 1;
			while ("".equals(sqlElement[i]))
				i++;
			tableName = sqlElement[i];
			return tableName;
		} else {
		    
			//log.error("It's not a valid SQL Statement.Can't get table name from it!");
			return tableName;
		}
	}

	/**
	 * Update table name in sql string.
	 * 
	 * @param sql,
	 *            sql statement string.
	 * @param tableSuffix,
	 *            tableSuffix like "_0", "_1"..."_99".
	 * @return
	 */
	private String updateTableName(String sql, String tableSuffix) {
		String[] sqlElement = sql.trim().split("\\s");

		if ("select".equalsIgnoreCase(sqlElement[0])
				|| "delete".equalsIgnoreCase(sqlElement[0])) {
			for (int i = 0; i < sqlElement.length; i++) {
				if ("from".equalsIgnoreCase(sqlElement[i])) {
					i = i + 1;
					while ("".equals(sqlElement[i]))
						i++;
					String orgTableName = sqlElement[i];
					if (orgTableName.startsWith("("))
					    continue;
					sqlElement[i] = orgTableName + tableSuffix;
					break;
				}
			}
		} else if ("insert".equalsIgnoreCase(sqlElement[0])) {
			for (int i = 0; i < sqlElement.length; i++) {
				if ("into".equalsIgnoreCase(sqlElement[i])) {
					i = i + 1;
					while ("".equals(sqlElement[i]))
						i++;
					String orgTableName = sqlElement[i];
					sqlElement[i] = orgTableName + tableSuffix;
					break;
				}
			}
		} else if ("update".equalsIgnoreCase(sqlElement[0])) {
			int i = 1;
			while ("".equals(sqlElement[i]))
				i++;
			String orgTableName = sqlElement[i];
			sqlElement[i] = orgTableName + tableSuffix;
		} else {
			log
					.error("It's not a valid SQL Statement.Can't get table name from it!");
		}
		String conSql = "";
		for (String element : sqlElement)
			conSql += element + " ";
		log.debug(conSql);
		return conSql;
	}

	/**
	 * Load all properties from interceptor.properties, for now, 1.
	 * PartitionTables property is defined. All table name should put there if
	 * it needs partition. 2. benchMarkColumn, decide which column to use to
	 * partition table.
	 * 
	 * @param filePath,
	 *            the location of interceptor.properties
	 */
	private static Properties LoadProperties(String filePath) {
		Properties prop = new Properties();
		InputStream in = CustomizeInterceptor.class
				.getResourceAsStream(filePath);
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			log.error("Can't load proprties from " + filePath);
		}
		return prop;
	}
}
