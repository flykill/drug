package com.drug.ibatis;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.drug.util.StackTraceUtil;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * ibatis配置类
 * 
 * @author andongdong
 * 
 */
public class IBatisConfig {
	private SqlMapClient sqlMapper = null;

	public IBatisConfig(String resource) {
		try {
			Resources.setCharset(Charset.forName("UTF-8"));
			Reader reader = Resources.getResourceAsReader(resource);
			sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			System.out.println(StackTraceUtil.getStackTrace(e));
		}
	}

	public SqlMapClient getSqlMapper() {
		return sqlMapper;
	}

}
