package com.drug.ibatis;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.CLOB;

import com.ibatis.sqlmap.engine.type.BaseTypeHandler;
import com.ibatis.sqlmap.engine.type.SimpleDateFormatter;
import com.ibatis.sqlmap.engine.type.TypeHandler;

public class OracleObjectTypeHandler extends BaseTypeHandler implements
		TypeHandler {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			String jdbcType) throws SQLException {
		ps.setObject(i, parameter);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object object = rs.getObject(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			if (object instanceof java.sql.Date) {
				java.sql.Timestamp sqlTimestamp = rs.getTimestamp(columnName);
				object = new java.util.Date(sqlTimestamp.getTime());
			}
			if (object instanceof CLOB) {
				Clob clob = rs.getClob(columnName);
				int size = (int) clob.length();
				object = clob.getSubString(1, size);
			}
			return object;
		}
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object object = rs.getObject(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			if (object instanceof java.sql.Date) {
				java.sql.Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
				object = new java.util.Date(sqlTimestamp.getTime());
			}
			if (object instanceof CLOB) {
				Clob clob = rs.getClob(columnIndex);
				int size = (int) clob.length();
				object = clob.getSubString(1, size);
			}
			return object;
		}
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object object = cs.getObject(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			if (object instanceof java.sql.Date) {
				java.sql.Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
				object = new java.util.Date(sqlTimestamp.getTime());
			}
			if (object instanceof CLOB) {
				Clob clob = cs.getClob(columnIndex);
				int size = (int) clob.length();
				object = clob.getSubString(1, size);
			}
			return object;
		}
	}

	@Override
	public Object valueOf(String s) {
		return SimpleDateFormatter.format(DATE_FORMAT, s);
	}

}
