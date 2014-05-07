package com.drug.dao;

import java.util.List;
import java.util.Map;

import com.drug.util.Query;

/**
 * compress 公用DAO 类
 * 
 * @author andongdong
 * 
 */
public interface ICommonDao {

	<T> T queryForObject(String sqlId, Object param);

	<T> List<T> queryForList(String sqlId);

	<T> List<T> queryForList(String sqlId, Object param);
	
	<T> List<T> queryForBeanList(String sqlId, Map<String, Object> map);

	Map<String, Object> queryForMap(String sqlId, Object param, String key);

	Map<String, Object> queryForMap(String sqlId, Object param, String key,
			String value);

	List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> map);

	List<Map<String, Object>> queryForList(String sqlId,
			Map<String, Object> map, int startNum, int endNum);

	void update(String sqlId, Map<String, Object> map);

	void update(String sqlId, Object obj);

	void insert(String sqlId, Object obj);
	
	void delete(String sqlId,Object obj);

	void startBatch();

	void executeBatch();

	<T> List<T> queryForPage(String sqlTotalId, String sqlId, Query query,
			Object param);
     
     void insertDrug(Map<String,String> param);
     
     void updateDrug(Map<String,String> param);
     
     void insertRKOperation(Map<String,String> param);
     
     void insertCKOperation(Map<String,Object> param,List<Map<String,Object>> ckList);
}
