package com.drug.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drug.dao.ICommonDao;
import com.drug.entity.UserEntity;
import com.drug.util.StackTraceUtil;

/**
 * 登录页面
 * 
 * @author andong
 * 
 */
@Controller
public class LoginController {

	@Autowired
	private ICommonDao commonDao;

	/**
	 * 
	 * @description 角色查询
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "loginValidate", method = RequestMethod.POST)
	@ResponseBody
	public Object loginValidate(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		String userCode = request.getParameter("userCode");
		String pwd = request.getParameter("pwd");
		Map<String, Object> personMap = new HashMap<String, Object>();
		try {
			personMap = commonDao.queryForObject("drug.query_person_userCode",
					userCode);
		} catch (Exception e) {
			reMap.put("success", false);
			reMap.put("message", StackTraceUtil.getStackTrace(e));
			return reMap;
		}
		if (null == personMap) {
			reMap.put("success", false);
			reMap.put("message", "该用户不存在！");
			return reMap;
		}
		if ("2".equals(personMap.get("EFFECTIVE"))) {
			reMap.put("success", false);
			reMap.put("message", "该用户为无效用户！");
			return reMap;
		}
		if (!pwd.equals(personMap.get("PWD"))) {
			reMap.put("success", false);
			reMap.put("message", "用户名和密码不对，请重新输入");
			return reMap;
		}
		// set session
		setPersonEntity(request, personMap);
		reMap.put("success", true);
		return reMap;
	}

	/**
	 * 
	 * @description 注销
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "loginOut", method = RequestMethod.GET)
	public Object loginOut(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		HttpSession session = request.getSession();
		session.removeAttribute("person");
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:../login.html");
		return view;
	}

	public void setPersonEntity(HttpServletRequest request,
			Map<String, Object> personMap) {
		HttpSession session = request.getSession();
		session.setAttribute("person", generalEntity(personMap));
	}

	public UserEntity generalEntity(Map<String, Object> personMap) {
		UserEntity entity = new UserEntity();
		entity.setUserName(personMap.get("USERNAME").toString());
		entity.setUserCode(personMap.get("USERCODE").toString());

		List<String> roleList = new ArrayList<String>();
		List<String> menuList = new ArrayList<String>();

		String[] roles = personMap.get("ROLEIDS").toString().split(",");
		for (int i = 0; i < roles.length; i++) {
			roleList.add(roles[i]);
			String menus = commonDao.queryForObject("drug.query_menus_id",
					roles[i]);
			if (StringUtils.isNotBlank(menus)) {
				menuList.addAll(Arrays.asList(menus.split(",")));
			}
		}
		entity.setRoleList(roleList);
		entity.setMenuList(menuList);

		return entity;
	}

}