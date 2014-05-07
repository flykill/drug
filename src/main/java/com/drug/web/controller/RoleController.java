package com.drug.web.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
 * 角色管理
 * 
 * @author andong
 * 
 */
@Controller
public class RoleController {

	@Autowired
	private ICommonDao commonDao;

	private String menuIds;

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
		return "drug/roleManagement";
	}

	/**
	 * 
	 * @description 保存角色
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "saveRule", method = RequestMethod.POST)
	@ResponseBody
	public Object saveRule(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, String> param = generalMap(request);
		Map<String, Object> reMap = new HashMap<String, Object>();
		String message = StringUtils.EMPTY;
		try {
			if (StringUtils.isBlank(param.get("id"))) {
				commonDao.insert("drug.insert_rule", param);
				message = "新增角色【" + param.get("roleName") + "】成功!";
			} else {
				commonDao.update("drug.update_role", param);
				message = "修改角色【" + param.get("roleName") + "】成功!";
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

	/**
	 * 查询角色信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "queryRole", method = RequestMethod.POST)
	@ResponseBody
	public Object queryRole(HttpServletRequest request,
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
			list = commonDao.queryForPage("drug.query_role_total",
					"drug.query_role_page", query, param);
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
	@RequestMapping(value = "queryRoleById", method = RequestMethod.POST)
	@ResponseBody
	public Object queryRoleById(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		Map<String, Object> entityMap = new HashMap<String, Object>();
		try {
			entityMap = commonDao.queryForObject("drug.query_role_id", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		menuIds = entityMap.get("MENUIDS").toString();
		reMap.put("success", true);
		reMap.put("entity", entityMap);
		return reMap;
	}
	
	@RequestMapping(value = "deleteRole", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteRole(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		String id = request.getParameter("id");
		try {
			commonDao.delete("drug.delete_role", id);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", "删除角色成功!");
		return reMap;
	}

	/**
	 * 查询目录树
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "queryTree", method = RequestMethod.POST)
	@ResponseBody
	public Object queryTree(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Map<String, Object>> parentMap = new LinkedHashMap<String, Map<String, Object>>();

		Map<String, Object> menuMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(menuIds)) {
			String[] menus = menuIds.split(",");
			for (int i = 0; i < menus.length; i++) {
				menuMap.put(menus[i], menus[i]);
			}
		}

		List<Map<String, Object>> fileList = commonDao.queryForList(
				"drug.query_menuFile", null);

		List<Map<String, Object>> treeeList = commonDao.queryForList(
				"drug.query_menuTree", null);

		for (Map<String, Object> fileMap : fileList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("id", fileMap.get("ID"));
			temp.put("text", fileMap.get("MENUNAME"));
			temp.put("state", "open");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			temp.put("children", list);
			parentMap.put(fileMap.get("ID").toString(), temp);
		}

		for (Map<String, Object> treeMap : treeeList) {
			Object parentId = treeMap.get("PARENTID");
			if (parentId != null && parentMap.containsKey(parentId.toString())) {
				List<Map<String, Object>> tempList = (List<Map<String, Object>>) parentMap
						.get(parentId.toString()).get("children");
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("id", treeMap.get("ID"));
				temp.put("text", treeMap.get("MENUNAME"));
				if (menuMap.containsKey(treeMap.get("ID").toString())) {
					temp.put("checked", true);
				}
				tempList.add(temp);
			}
		}
		if (StringUtils.isNotBlank(menuIds)) {
			menuIds = StringUtils.EMPTY;
		}
		return parentMap.values();
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