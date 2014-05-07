package com.drug.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.drug.util.Query;
import com.drug.util.StackTraceUtil;

/**
 * 用户管理
 * 
 * @author andong
 * 
 */
@Controller
public class PersonController {

	@Autowired
	private ICommonDao commonDao;

	/**
	 * 
	 * @description 角色查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "query", method = RequestMethod.GET)
	public String query(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		return "drug/personManagement";
	}

	/**
	 * 查询用户信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryPerson", method = RequestMethod.POST)
	@ResponseBody
	public Object queryPerson(HttpServletRequest request,
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
			list = commonDao.queryForPage("drug.query_person_total",
					"drug.query_person_page", query, param);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("roleList", list);
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
	@RequestMapping(value = "queryPersonById", method = RequestMethod.POST)
	@ResponseBody
	public Object queryPersonById(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		Map<String, Object> entityMap = new HashMap<String, Object>();
		try {
			entityMap = commonDao.queryForObject("drug.query_person_id", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		if (null != entityMap.get("roleId")
				&& StringUtils.isNotBlank(entityMap.get("roleId").toString())) {
			String[] roles = entityMap.get("roleId").toString().split(",");
			entityMap.put("roleList", Arrays.asList(roles));
		}
		reMap.put("success", true);
		reMap.put("entity", entityMap);
		return reMap;
	}

	@RequestMapping(value = "deletePerson", method = RequestMethod.POST)
	@ResponseBody
	public Object deletePerson(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		try {
			commonDao.delete("drug.delete_person", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", "删除用户成功!");
		return reMap;
	}

	/**
	 * 
	 * @description 角色查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "queryRole", method = RequestMethod.POST)
	@ResponseBody
	public Object queryRole(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		List<Map<String, Object>> list = commonDao
				.queryForList("drug.query_role_combox");
		return list;
	}

	/**
	 * 
	 * @description 保存角色
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "savePerson", method = RequestMethod.POST)
	@ResponseBody
	public Object savePerson(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {

		Map<String, String> param = generalMap(request);
		Map<String, Object> reMap = new HashMap<String, Object>();
		String message = StringUtils.EMPTY;
		try {
			if (StringUtils.isBlank(param.get("id"))) {
				commonDao.insert("drug.insert_person", param);
				message = "新增用户【" + param.get("userName") + "】成功!";
			} else {
				commonDao.update("drug.update_person", param);
				message = "修改用户【" + param.get("userName") + "】成功!";
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