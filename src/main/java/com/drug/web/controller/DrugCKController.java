package com.drug.web.controller;

import java.util.ArrayList;
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
import com.drug.exception.DrugException;
import com.drug.util.PinyinUtils;
import com.drug.util.StackTraceUtil;

@Controller
public class DrugCKController {

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
		return "drug/drugCK";
	}

	/**
	 * 
	 * @description 提交订单
	 * 
	 * @author andongdong
	 * @date 2012-9-25
	 */
	@ResponseBody
	@RequestMapping(value = "ckOperation", method = RequestMethod.POST)
	public Object ckOperation(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ckType", request.getParameter("ckType"));
		param.put("totalPrice", request.getParameter("totalPrice"));

		String rows = request.getParameter("rows");
		List<Map<String, Object>> operationList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < Integer.valueOf(rows); i++) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("drugCode",
					request.getParameter("ckList[" + i + "][drugCode]"));
			tempMap.put("xsPrice",
					request.getParameter("ckList[" + i + "][xsPrice]"));
			tempMap.put("xsNumber",
					request.getParameter("ckList[" + i + "][xsNumber]"));
			tempMap.put("factoryName",
					request.getParameter("ckList[" + i + "][factoryName]"));
			String factoryName = request.getParameter("ckList[" + i
					+ "][factoryName]");
			if (StringUtils.isNotBlank(factoryName)) {
				tempMap.put("factoryId", PinyinUtils.getPinYin(factoryName));
			}
			operationList.add(tempMap);
		}
		try {
			commonDao.insertCKOperation(param, operationList);
		} catch (Exception e) {
			reMap.put("success", false);
			if (e instanceof DrugException) {
				reMap.put("message", e.getMessage());
			} else {
				reMap.put("message", StackTraceUtil.getStackTrace(e));
			}
			return reMap;
		}
		reMap.put("success", true);
		reMap.put("message", "药品销售成功!");
		return reMap;
	}
}
