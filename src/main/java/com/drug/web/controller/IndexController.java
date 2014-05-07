package com.drug.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description
 * 
 * @author andongdong
 * @date 2012-5-7
 */
@Controller
public class IndexController {

	/**
	 * 
	 * @description 返回页面
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@RequestMapping(value = "query", method = RequestMethod.GET)
	public String query(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		return "index";
	}
}
