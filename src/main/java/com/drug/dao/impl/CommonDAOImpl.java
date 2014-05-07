package com.drug.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drug.dao.ICommonDao;
import com.drug.exception.DrugException;
import com.drug.ibatis.IBatisDaoTemplate;
import com.drug.ibatis.IbatisDaoTemplateFactory;
import com.drug.util.Query;

public class CommonDAOImpl implements ICommonDao {
	private IBatisDaoTemplate template = null;

	public CommonDAOImpl(String sqlType) {
		template = IbatisDaoTemplateFactory.getTemplate(sqlType);
	}

	public int queryCount(String sqlId) {
		return (Integer) template.queryForObject(sqlId, null);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sqlId) {
		return (List<T>) template.queryForList(sqlId, null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryForList(String sqlId,
			Map<String, Object> map, int startNum, int endNum) {
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return (List<Map<String, Object>>) template.queryForList(sqlId, map);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sqlId, Object param) {
		return (List<T>) template.queryForList(sqlId, param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryForList(String sqlId,
			Map<String, Object> map) {
		return (List<Map<String, Object>>) template.queryForList(sqlId, map);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForBeanList(String sqlId, Map<String, Object> map) {
		return (List<T>) template.queryForList(sqlId, map);
	}

	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String sqlId, Object param) {
		return (T) template.queryForObject(sqlId, param);
	}

	public void update(String sqlId, Map<String, Object> map) {
		template.update(sqlId, map);
	}

	@Override
	public void update(String sqlId, Object obj) {
		template.update(sqlId, obj);
	}

	@Override
	public void insert(String sqlId, Object obj) {
		template.insert(sqlId, obj);
	}

	@Override
	public void startBatch() {
		template.startBatch();
	}

	@Override
	public void executeBatch() {
		template.executeBatch();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryForPage(String sqlTotalId, String sqlId,
			Query query, Object param) {
		Integer total = (Integer) template.queryForObject(sqlTotalId, param);
		if (null == total) {
			total = 0;
		}
		query.setTotalItem(total);
		if (total == 0) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != param) {
			if (param instanceof Map) {
				map.putAll((Map<String, Object>) param);
			} else {
				map.put("param", param);
			}
		}
		map.put("query", query);
		return template.queryForList(sqlId, map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryForMap(String sqlId, Object param,
			String key) {
		return template.queryForMap(sqlId, param, key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryForMap(String sqlId, Object param,
			String key, String value) {
		return template.queryForMap(sqlId, param, key, value);
	}

	@Override
	public void delete(String sqlId, Object obj) {
		template.delete(sqlId, obj);
	}

	@Override
	public void insertDrug(Map<String, String> param) {
		template.startTransaction();
		try {
			// 保存产地
			Integer factoryCount = (Integer) template.queryForObject(
					"drug.query_factory_zjm", param);
			if (factoryCount == 0) {
				template.insert("drug.insert_factory", param);
			}
			// 保存药品
			template.insert("drug.insert_drug", param);
			template.commitTransaction();
		} catch (Exception e) {
			throw new DrugException(e.getMessage());
		} finally {
			template.endTransaction();
		}
	}

	@Override
	public void updateDrug(Map<String, String> param) {
		template.startTransaction();
		try {
			// 保存产地
			Integer factoryCount = (Integer) template.queryForObject(
					"drug.query_factory_zjm", param);
			if (factoryCount == 0) {
				template.insert("drug.insert_factory", param);
			}
			// 保存药品
			template.update("drug.update_drug", param);
			template.commitTransaction();
		} catch (Exception e) {
			throw new DrugException(e.getMessage());
		} finally {
			template.endTransaction();
		}
	}

	@Override
	public void insertRKOperation(Map<String, String> param) {
		template.startTransaction();
		try {
			// 保存入库操作
			template.insert("drug.insert_rk", param);
			// 更新库存
			Integer storeCount = (Integer) template.queryForObject(
					"drug.query_drugStore", param);
			if (null == storeCount) {
				template.insert("drug.insert_drugStore", param);
			} else {
				template.update("drug.update_drugStore_rk", param);
			}
			template.commitTransaction();
		} catch (Exception e) {
			throw new DrugException(e.getMessage());
		} finally {
			template.endTransaction();
		}
	}

	@Override
	public void insertCKOperation(Map<String, Object> param,
			List<Map<String, Object>> ckList) {
		template.startTransaction();
		try {
			Integer ckId = (Integer) template.queryForObject(
					"drug.query_ck_seq", null);
			param.put("ckId", ckId);
			// 保存ck01
			template.insert("drug.insert_ck01", param);
			// 保存ck02
			if (ckList != null) {
				for (Map<String, Object> map : ckList) {
					map.put("ckId", ckId);
					template.insert("drug.insert_ck02", map);
					// 更新库存
					Integer drugNumber = (Integer) template.queryForObject(
							"drug.query_drugStore", map);
					if (null == drugNumber
							|| drugNumber == 0
							|| Integer.valueOf(map.get("xsNumber").toString()) > drugNumber) {
						throw new DrugException("销售失败，库存没有该药品或者是销售数量大于库存!");
					}
					template.update("drug.update_drugStore_ck", map);
				}
			}
			template.commitTransaction();
		} catch (Exception e) {
			throw new DrugException(e.getMessage());
		} finally {
			template.endTransaction();
		}
	}

}
