package com.drug.ibatis;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * 数据库操作模板类
 * 
 * @author andongdong
 * 
 */
public class IBatisDaoTemplate {

	private SqlMapClient sqlMapClient = null;

	public IBatisDaoTemplate(SqlMapClient client) {
		sqlMapClient = client;
	}

	public Object insert(String id, Object parameterObject) {
		try {
			return sqlMapClient.insert(id, parameterObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to insert - id [" + id
					+ "], parameterObject [" + parameterObject + "]. Cause: "
					+ e);
		}
	}

	public int update(String id, Object parameterObject) {
		try {
			return sqlMapClient.update(id, parameterObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to update - id [" + id
					+ "] - parameterObject [" + parameterObject + "].  Cause: "
					+ e);
		}
	}

	public int delete(String id, Object parameterObject) {
		try {
			return sqlMapClient.delete(id, parameterObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to delete - id [" + id
					+ "] - parameterObject [" + parameterObject + "].  Cause: "
					+ e);
		}
	}

	public Object queryForObject(String id, Object parameterObject) {
		try {
			return sqlMapClient.queryForObject(id, parameterObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to execute queryForObject - id ["
					+ id + "], parameterObject [" + parameterObject
					+ "].  Cause: " + e);
		}
	}

	public Object queryForObject(String id, Object parameterObject,
			Object resultObject) {
		try {
			return sqlMapClient.queryForObject(id, parameterObject,
					resultObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForObject - id [" + id
					+ "], parameterObject [" + parameterObject + "].  Cause: "
					+ e);
		}
	}

	@SuppressWarnings("rawtypes")
	public List queryForList(String id, Object parameterObject) {
		try {
			return sqlMapClient.queryForList(id, parameterObject);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForList - id [" + id
					+ "], parameterObject [" + parameterObject + "].  Cause: "
					+ e);
		}
	}

	@SuppressWarnings("rawtypes")
	public List queryForList(String id, Object parameterObject, int skip,
			int max) {
		try {
			return sqlMapClient.queryForList(id, parameterObject, skip, max);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForList - id [" + id
					+ "], parameterObject [" + parameterObject + "], skip ["
					+ skip + "], max [" + max + "].  Cause: " + e);
		}
	}

	public void queryWithRowHandler(String id, Object parameterObject,
			RowHandler rowHandler) {
		try {
			sqlMapClient.queryWithRowHandler(id, parameterObject, rowHandler);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForList - id [" + id
					+ "], parameterObject [" + parameterObject
					+ "], rowHandler [ " + rowHandler + "].  Cause: " + e);
		}
	}

	@SuppressWarnings("rawtypes")
	public Map queryForMap(String id, Object parameterObject, String keyProp) {
		try {
			return sqlMapClient.queryForMap(id, parameterObject, keyProp);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForMap - id [" + id
					+ "], parameterObject [" + parameterObject + "], keyProp ["
					+ keyProp + "].  Cause: " + e);
		}
	}

	@SuppressWarnings("rawtypes")
	public Map queryForMap(String id, Object parameterObject, String keyProp,
			String valueProp) {
		try {
			return sqlMapClient.queryForMap(id, parameterObject, keyProp,
					valueProp);
		} catch (SQLException e) {
			throw new DaoException("Failed to queryForMap - id [" + id
					+ "], parameterObject [" + parameterObject + "], keyProp ["
					+ keyProp + "], valueProp [" + valueProp + "].  Cause: "
					+ e);
		}
	}

	public void startBatch() {
		try {
			sqlMapClient.startBatch();
		} catch (SQLException e) {
			throw new DaoException("Failed to startBatch.  Cause: e" + e);
		} finally {
			this.startTransaction();
		}
	}

	public int executeBatch() {
		int rows = 0;
		try {
			rows = sqlMapClient.executeBatch();
			this.commitTransaction();
		} catch (SQLException e) {
			throw new DaoException("Failed to executeBatch.  Cause: " + e);
		} finally {
			this.endTransaction();
		}
		return rows;
	}

	public void startTransaction() {
		try {
			sqlMapClient.startTransaction();
		} catch (SQLException e) {
			throw new DaoException("Failed to startTransaction.  Cause: " + e);
		}
	}

	public void commitTransaction() {
		try {
			sqlMapClient.commitTransaction();
		} catch (SQLException e) {
			throw new DaoException("Failed to commitTransaction.  Cause: " + e);
		}
	}

	public void endTransaction() {
		try {
			sqlMapClient.endTransaction();
		} catch (SQLException e) {
			throw new DaoException("Failed to endTransaction.  Cause: " + e);
		}
	}

}
