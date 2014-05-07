package com.drug.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * service 工厂类
 * 
 * @author andongdong
 * 
 */
public class ServiceFactory implements ApplicationContextAware {

	private boolean alreadyInit = false;

	private ApplicationContext appContext;

	private ServiceFactory() {
	}

	/**
	 * * 获得工厂的静态实例<br>
	 * <li>目的</li> <br>
	 * <li>任何前台java类都可以调用此工厂来访问service服务,而不需要类似于webx思想一样和web框架紧密绑定</li>
	 * 
	 * @return
	 */
	public static ServiceFactory getInstance() {
		return SigletonHolder.factory;
	}

	private static class SigletonHolder {
		private static ServiceFactory factory = new ServiceFactory();
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> service) {
		if (service == null) {
			return null;
		}
		String svcName = getServiceName(service);
		return (T) appContext.getBean(svcName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(String beanName) {
		return (T) appContext.getBean(beanName);
	}

	private <T> String getServiceName(Class<T> service) {
		String svcName = service.getSimpleName();
		svcName = svcName.substring(0, 1).toLowerCase() + svcName.substring(1);
		return svcName;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ServiceFactory serviceFactory = ServiceFactory.SigletonHolder.factory;
		if (!serviceFactory.alreadyInit) {
			serviceFactory.appContext = applicationContext;
			serviceFactory.alreadyInit = true;
		}
	}
	
	public String[] getBeanNamesForType(Class<?> clazz) {
		return appContext.getBeanNamesForType(clazz);
	}
	
	public String[] getAliases(String beanName){
		return appContext.getAliases(beanName);
	}

}
