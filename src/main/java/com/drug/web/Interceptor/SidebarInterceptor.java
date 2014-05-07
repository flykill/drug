package com.drug.web.Interceptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.drug.dao.ICommonDao;
import com.drug.entity.UserEntity;

/**
 * 
 * @description sidebar 拦截器 为modelview对象增加 sidebar 参数
 * 
 * @author andong
 * @date 2013-1-5
 */
public class SidebarInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ICommonDao commonDao;

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("person");
		if (null != obj) {
			UserEntity entity = (UserEntity) obj;
			if (null != modelAndView) {
				modelAndView.getModelMap()
						.put("userName", entity.getUserName());
				modelAndView.getModelMap().put("isLogin", true);
				modelAndView.getModelMap().put("menuList",
						generalMenu(entity.getMenuList()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object generalMenu(List<String> menuList) {
		Map<String, Map<String, Object>> reMap = new LinkedHashMap<String, Map<String, Object>>();

		Map<String, String> menuMap = new HashMap<String, String>();
		for (String menu : menuList) {
			menuMap.put(menu, menu);
		}
		
		Map<String, Object> fileMap = commonDao.queryForMap(
				"drug.query_menuFile", null, "ID", "MENUNAME");
		List<Map<String, Object>> treeList = commonDao.queryForList(
				"drug.query_menuTree", null);

		for (Map<String, Object> treeMap : treeList) {
             String menuId = treeMap.get("ID").toString();
             String parentId = treeMap.get("PARENTID").toString();
             if(menuMap.containsKey(menuId)){
            	 if(reMap.containsKey(parentId)){
            		 List<Map<String,Object>> tempList= (List<Map<String,Object>>)((Map<String,Object>)reMap.get(parentId)).get("children");
            		 tempList.add(treeMap);
            	 }else{
            		 Map<String,Object> tempMap = new HashMap<String, Object>();
            		 tempMap.put("fileName", fileMap.get(new BigDecimal(parentId)));
            		 List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
                     tempList.add(treeMap);
            		 tempMap.put("children",tempList);
            		 reMap.put(parentId, tempMap);
            	 }
             }
		}
		return reMap.values();
	}
}
