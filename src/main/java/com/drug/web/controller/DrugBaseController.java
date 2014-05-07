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
import com.drug.util.Query;
import com.drug.util.StackTraceUtil;

/**
 * 基本信息维护
 * 
 * @author andong
 * 
 */
@Controller
public class DrugBaseController {

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
		return "drug/drugBase";
	}

	/**
	 * 查询药品信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryDrug", method = RequestMethod.POST)
	@ResponseBody
	public Object queryDrug(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, String> param = generalMap(request);
		Query query = new Query();
		String pageNumber = param.get("pageNumber");
		if (StringUtils.isBlank(pageNumber)) {
			pageNumber = "0";
		}
		query.setCurrentPageString(pageNumber);
		query.setPageSizeString(param.get("pageSize"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			list = commonDao.queryForPage("drug.query_drug_total",
					"drug.query_drug_page", query, param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("drugList", list);
		reMap.put("total", query.getTotalItem());
		reMap.put("success", true);
		reMap.put("message", "查询成功!");
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
	@RequestMapping(value = "queryDrugById", method = RequestMethod.POST)
	@ResponseBody
	public Object queryDrugById(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		Map<String, Object> entityMap = new HashMap<String, Object>();
		try {
			entityMap = commonDao.queryForObject("drug.query_drug_id", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("entity", entityMap);
		return reMap;
	}

	/**
	 * 新增药品信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "saveDrug", method = RequestMethod.POST)
	@ResponseBody
	public Object saveDrug(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String message = StringUtils.EMPTY;
		Map<String, String> param = generalMap(request);
		if (null != param.get("factoryName")
				&& StringUtils.isNotBlank(param.get("factoryName").toString())) {
			param.put("factoryId",
					PinyinUtils.getPinYin(param.get("factoryName").toString()));
		}
		if (null != param.get("drugName")
				&& StringUtils.isNotBlank(param.get("drugName").toString())) {
			param.put("drugId",
					PinyinUtils.getPinYin(param.get("drugName").toString()));
		}
		try {
			Integer drugId = (Integer) commonDao.queryForObject(
					"drug.query_drug_drugCode_factory", param);
			if (StringUtils.isBlank(param.get("id"))) {
				if (drugId!=null && drugId > 0) {
					reMap.put("success", false);
					reMap.put(
							"message",
							param.get("drugName") + "【"
									+ param.get("factoryName") + "】 已经存在");
					return reMap;
				}
				commonDao.insertDrug(param);
				message = "新增药品【" + param.get("drugName") + "】成功!";
			} else {
				if (drugId!=null && !param.get("id").equals(drugId.toString())) {
					reMap.put("success", false);
					reMap.put(
							"message",
							param.get("drugName") + "【"
									+ param.get("factoryName") + "】 已经存在");
					return reMap;
				}
				commonDao.updateDrug(param);
				message = "修改药品【" + param.get("drugName") + "】成功!";
			}
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", message);
		return reMap;
	}

	@RequestMapping(value = "deleteDrug", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteDrug(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		try {
			commonDao.delete("drug.delete_drug", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", "删除药品成功!");
		return reMap;
	}

	@RequestMapping(value = "queryFactoryCode", method = RequestMethod.POST)
	@ResponseBody
	public Object queryFactoryCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		try {
			reList = commonDao.queryForList("drug.query_factoryZjm", null);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("list", reList);
		return reMap;
	}

	@RequestMapping(value = "queryDrugCode", method = RequestMethod.POST)
	@ResponseBody
	public Object queryDrugCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		try {
			reList = commonDao.queryForList("drug.query_drugZjm", null);
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
