package com.drug.web.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.drug.util.Query;
import com.drug.util.StackTraceUtil;

/**
 * 信息查询
 * 
 * @author andong
 * 
 */
@Controller
public class BaseQueryController {

	@Autowired
	private ICommonDao commonDao;
	
	private NumberFormat germanFormat =
			NumberFormat.getIntegerInstance(Locale.US);

	/**
	 * 
	 * @description 库存查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "storeQuery", method = RequestMethod.GET)
	public String storeQuery(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		return "drug/storeQuery";
	}

	/**
	 * 
	 * @description 入库查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "rkQuery", method = RequestMethod.GET)
	public String rkQuery(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		return "drug/rkQuery";
	}

	/**
	 * 
	 * @description 出库查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "ckQuery", method = RequestMethod.GET)
	public String ckQuery(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		modelMap.put("drugTypes", commonDao.queryForList("drug.query_drugType"));
		return "drug/ckQuery";
	}

	/**
	 * 库存查询
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryStore", method = RequestMethod.POST)
	@ResponseBody
	public Object queryStore(HttpServletRequest request,
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
		Integer numberTotal = 0;
		Double priceTotal;
		try {
			list = commonDao.queryForPage("drug.query_store_total",
					"drug.query_store_page", query, param);
			// query numberTotal
			numberTotal = commonDao.queryForObject(
					"drug.query_store_numberTotal", param);
			// query priceTotal
			priceTotal = commonDao.queryForObject(
					"drug.query_store_priceTotal", param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("drugList", list);
		reMap.put("total", query.getTotalItem());
		reMap.put("numberTotal", numberTotal==null?"0":germanFormat.format(numberTotal));
		reMap.put("priceTotal", priceTotal==null?"0":germanFormat.format(priceTotal));
		reMap.put("success", true);
		reMap.put("message", "查询成功!");
		return reMap;
	}

	/**
	 * 入库查询
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryRk", method = RequestMethod.POST)
	@ResponseBody
	public Object queryRk(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, String> param = generalMap(request);
		if (StringUtils.isNotBlank(param.get("operationDate"))) {
			param.put("beforeDate", param.get("operationDate") + "00:00:00");
			param.put("afterDate", param.get("operationDate") + "23:59:59");
		}
		Query query = new Query();
		String pageNumber = param.get("pageNumber");
		if (StringUtils.isBlank(pageNumber)) {
			pageNumber = "0";
		}
		query.setCurrentPageString(pageNumber);
		query.setPageSizeString(param.get("pageSize"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer numberTotal = 0;
		Double priceTotal;
		try {
			list = commonDao.queryForPage("drug.query_rk_total",
					"drug.query_rk_page", query, param);
			// query numberTotal
			numberTotal = commonDao.queryForObject(
					"drug.query_rk_numberTotal", param);
			// query priceTotal
			priceTotal = commonDao.queryForObject(
					"drug.query_rk_priceTotal", param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		for (Map<String, Object> map : list) {
			map.put("DRUGVALIDITY",
					format.format((Date) map.get("DRUGVALIDITY")));
			map.put("OPERATIONDATE",
					format.format((Date) map.get("OPERATIONDATE")));
		}
		reMap.put("drugList", list);
		reMap.put("total", query.getTotalItem());
		reMap.put("numberTotal", numberTotal==null?"0":germanFormat.format(numberTotal));
		reMap.put("priceTotal", priceTotal==null?"0":germanFormat.format(priceTotal));
		reMap.put("success", true);
		reMap.put("message", "查询成功!");
		return reMap;
	}

	/**
	 * 出库查询
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryCk", method = RequestMethod.POST)
	@ResponseBody
	public Object querCk(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, String> param = generalMap(request);
		if (StringUtils.isNotBlank(param.get("operationDate"))) {
			param.put("beforeDate", param.get("operationDate") + "00:00:00");
			param.put("afterDate", param.get("operationDate") + "23:59:59");
		}
		Query query = new Query();
		String pageNumber = param.get("pageNumber");
		if (StringUtils.isBlank(pageNumber)) {
			pageNumber = "0";
		}
		query.setCurrentPageString(pageNumber);
		query.setPageSizeString(param.get("pageSize"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer numberTotal = 0;
		Double priceTotal;
		try {
			list = commonDao.queryForPage("drug.query_ck_total",
					"drug.query_ck_page", query, param);
			// query numberTotal
			numberTotal = commonDao.queryForObject(
					"drug.query_ck_numberTotal", param);
			// query priceTotal
			priceTotal = commonDao.queryForObject(
					"drug.query_ck_priceTotal", param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		for (Map<String, Object> map : list) {
			map.put("OPERATIONDATE",
					format.format((Date) map.get("OPERATIONDATE")));
		}
		reMap.put("drugList", list);
		reMap.put("total", query.getTotalItem());
		reMap.put("numberTotal", numberTotal==null?"0":germanFormat.format(numberTotal));
		reMap.put("priceTotal", priceTotal==null?"0":germanFormat.format(priceTotal));
		reMap.put("success", true);
		reMap.put("message", "查询成功!");
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