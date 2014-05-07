package com.drug.web.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drug.dao.ICommonDao;
import com.drug.util.PinyinUtils;
import com.drug.util.StackTraceUtil;

@Controller
public class DrugRKController {

	@Autowired
	private ICommonDao commonDao;

	/**
	 * 
	 * @description 入库返回页面
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "query", method = RequestMethod.GET)
	public String query(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		modelMap.put("drugTypes", commonDao.queryForList("drug.query_drugType"));
		return "drug/drugRK";
	}

	/**
	 * 入库操作
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "rkOperation", method = RequestMethod.POST)
	@ResponseBody
	public Object rkOperation(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, String> param = generalMap(request);
		if (null != param.get("gjFactoryName")
				&& StringUtils
						.isNotBlank(param.get("gjFactoryName").toString())) {
			param.put("gjFactoryId", PinyinUtils.getPinYin(param.get(
					"gjFactoryName").toString()));
		}
		if (null != param.get("factoryName_hidden")
				&& StringUtils.isNotBlank(param.get("factoryName_hidden").toString())) {
			param.put("factoryId",
					PinyinUtils.getPinYin(param.get("factoryName_hidden").toString()));
		}
		try {
			commonDao.insertRKOperation(param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", "药品录入成功!");
		return reMap;
	}

	@RequestMapping(value = "queryGjFactoryCode", method = RequestMethod.POST)
	@ResponseBody
	public Object queryGjFactoryCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		try {
			reList = commonDao.queryForList("drug.query_gjFactoryZjm", null);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("list", reList);
		return reMap;
	}
	

	@SuppressWarnings("rawtypes")
	private Map<String, String> generalMap(HttpServletRequest request) {
		Map<String, String> param = new HashMap<String, String>();
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String key = em.nextElement().toString();
			param.put(key, request.getParameter(key));
		}
		return param;
	}

}
