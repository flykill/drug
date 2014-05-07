package com.drug.web.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drug.dao.ICommonDao;
import com.drug.util.StackTraceUtil;

/**
 * 公用查询类
 * 
 * @author andong
 * 
 */
@Controller
public class DrugCommonController {

	@Autowired
	private ICommonDao commonDao;

	/**
	 * 查询药品信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryDrugByCode", method = RequestMethod.POST)
	@ResponseBody
	public Object queryDrugByCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String drugCode = request.getParameter("drugCode");
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		try {
			entityList = commonDao.queryForList("drug.query_drug_code",
					drugCode);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("entityList", entityList);
		reMap.put("total", entityList.size());
		return reMap;
	}

	/**
	 * 查询药品信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryDrug", method = RequestMethod.GET)
	@ResponseBody
	public Object queryDrug(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, String> paramMap = generalMap(request);
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		try {
			entityList = commonDao.queryForList("drug.query_drug", paramMap);
		} catch (Exception e) {
			return new ArrayList<Map<String, Object>>();
		}
		return entityList;
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
