package com.drug.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 分页算法封装。 <br>
 * 分页须设置：<li>TotalItem（总条数）,缺省为0</li><br>
 * <li>应该在dao中被设置 PageSize（每页条数）</li><br>
 * <li>应在web层被设置 QueryBase 缺省为20</li><br>
 * <li>子类可以通过覆盖 getDefaultPageSize() 修改 CurrentPage（当前页）,缺省为1</li><br>
 * 首页， 应在web层被设置 分页后，可以得到： <li>TotalPage（总页数）</li><br>
 * <li>FirstItem(当前页开始记录位置，从1开始记数)</li><br>
 * <li>PageLastItem(当前页最后记录位置)</li><br>
 * <li>页面上，每页显示条数名字应为：</li><br>
 * <li>lines ，当前页名字应为： page</li>
 * 
 */
public class Query implements Serializable {

	private static final long serialVersionUID = 7603300820027561064L;

	private static final Integer defaultPageSize = 20;

	private static final Integer defaultFriatPage = 1;

	private static final Integer defaultTotleItem = 0;

	private static final Integer DEFAULT_PAGE_BUTTON_COUNT = 10;

	private Integer totalItem;

	private Integer pageSize = 10;

	private Integer currentPage;

	private Integer pageButtonCount = 5;

	// 页数按钮列表
	private List<Integer> buttonList;

	// 总页数
	// private int totalPage;

	/**
	 * @return Returns the defaultPageSize.
	 */
	protected final Integer getDefaultPageSize() {
		return defaultPageSize;
	}

	public boolean isFirstPage() {
		return this.getCurrentPage().intValue() == 1;
	}

	public int getPreviousPage() {
		int back = this.getCurrentPage().intValue() - 1;

		if (back <= 0) {
			back = 1;
		}

		return back;
	}

	public boolean isLastPage() {
		if (this.getTotalPage() == 0) {
			return true;
		}
		return this.getTotalPage() == this.getCurrentPage().intValue();
	}

	public int getNextPage() {
		int back = this.getCurrentPage().intValue() + 1;

		if (back > this.getTotalPage()) {
			back = this.getTotalPage();
		}

		return back;
	}

	/**
	 * @return Returns the currentPage.
	 */
	public Integer getCurrentPage() {
		if (currentPage == null) {
			return defaultFriatPage;
		}

		return currentPage;
	}

	/**
	 * @param currentPage
	 *            The currentPage to set.
	 */
	public void setCurrentPage(Integer cPage) {
		if ((cPage == null) || (cPage.intValue() <= 0)) {
			this.currentPage = defaultFriatPage;
		} else {
			this.currentPage = cPage;
		}
	}

	public void setCurrentPageString(String s) {
		if (StringUtils.isBlank(s)) {
			return;
		}
		try {
			setCurrentPage(Integer.parseInt(s));
		} catch (NumberFormatException ignore) {
			this.setCurrentPage(defaultFriatPage);
		}
	}

	/**
	 * @return Returns the pageSize.
	 */
	public Integer getPageSize() {
		if (pageSize == null) {
			return getDefaultPageSize();
		}

		return pageSize;
	}

	public boolean hasSetPageSize() {
		return pageSize != null;
	}

	/**
	 * @param pageSize
	 *            The pageSize to set.
	 */
	public void setPageSize(Integer pSize) {
		if (pSize == null) {
			throw new IllegalArgumentException("PageSize can't be null.");
		}

		if (pSize.intValue() < 0) {
			throw new IllegalArgumentException("PageSize must great than zero.");
		}

		this.pageSize = pSize;
	}

	public void setPageSizeString(String pageSizeString) {
		if (StringUtils.isBlank(pageSizeString)) {
			return;
		}

		try {
			Integer integer = new Integer(pageSizeString);
			this.setPageSize(integer);
		} catch (NumberFormatException ignore) {
		}
	}

	/**
	 * @return Returns the totalItem.
	 */
	public Integer getTotalItem() {
		if (totalItem == null) {
			// throw new IllegalStateException("Please set the TotalItem
			// first.");
			return defaultTotleItem;
		}

		return totalItem;
	}

	/**
	 * @param totalItem
	 *            The totalItem to set.
	 */
	public void setTotalItem(Integer tItem) {
		if (tItem == null) {
			// throw new IllegalArgumentException("TotalItem can't be null.");
			tItem = 0;
		}
		this.totalItem = tItem;
		int current = this.getCurrentPage().intValue();
		int lastPage = this.getTotalPage();

		if (current > lastPage) {
			this.setCurrentPage(lastPage);
		}
	}

	public int getTotalPage() {
		int pgSize = this.getPageSize().intValue();
		int total = this.getTotalItem().intValue();
		int result = total / pgSize;

		if ((total % pgSize) != 0) {
			result++;
		}

		return result;
	}

	public int getPageFirstItem() {
		int cPage = this.getCurrentPage().intValue();

		if (cPage == 1 && totalItem == 0) {
			return 0;
		}
		cPage--;
		int pgSize = this.getPageSize().intValue();
		return (pgSize * cPage)+1;
	}

	public int getPageLastItem() {
		int cPage = this.getCurrentPage().intValue();
		int pgSize = this.getPageSize().intValue();
		int assumeLast = pgSize * cPage;
		int totalItem = getTotalItem().intValue();

		if (assumeLast > totalItem) {
			return totalItem;
		} else {
			return assumeLast;
		}
	}

	protected String getSQLBlurValue(String value) {
		if (value == null) {
			return null;
		}

		return value + '%';
	}

	/**
	 * <p>
	 * comment
	 * </p>
	 * 
	 * @author shengyong
	 * @version:$Id: QueryBase.java,v 1.1 2005/03/14 22:20:05 qianxiao Exp $
	 */
	protected String formatDate(String datestring) {
		if ((datestring == null) || datestring.equals("")) {
			return null;
		} else {
			return (datestring + " 00:00:00");
		}
	}

	/**
	 * 23:59:59
	 */
	protected String addDateEndPostfix(String datestring) {
		if ((datestring == null) || datestring.equals("")) {
			return null;
		}

		return datestring + " 23:59:59";
	}

	/**
	 * 00:00:00
	 */
	protected String addDateStartPostfix(String datestring) {
		if ((datestring == null) || datestring.equals("")) {
			return null;
		}

		return datestring + " 00:00:00";
	}

	/**
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public Date getAddDate(Date date) {
		Calendar cad = Calendar.getInstance();

		cad.setTime(date);
		cad.add(Calendar.DATE, 1);
		return cad.getTime();
	}

	public boolean hasSearchCondition() {
		return true;
	}

	/**
	 * 对分页按钮显示的设置：<br>
	 * <li>根据参数将分页按钮页码保存到List中</li><br>
	 * <li>若总页数 小于 buttonNum，则按照实际页数显示</li>
	 * 
	 * @author:guoyan
	 * @date:2009-3-30
	 * @param buttonNum
	 *            要显示分页按钮个数
	 */
	public void setPaginationButton(int buttonNum) {
		int currPageTemp = this.getCurrentPage();
		int i = currPageTemp / buttonNum;
		int j = currPageTemp % buttonNum;
		if (i <= 0) {
			i = 1;
		} else if (j != 0) {
			i++;
		}
		if (buttonList != null && currPageTemp <= buttonList.get(buttonNum - 1)) {
			return;
		}
		buttonList = new ArrayList<Integer>();
		if (this.getTotalItem() == 0) {
			return;
		}
		for (int k = (i - 1) * buttonNum; k < i * buttonNum; k++) {
			buttonList.add(k + 1);
			if (this.getTotalPage() == k + 1) {
				break;
			}
		}
	}

	/**
	 * 参考c2j相关程序实现, 设置分页按钮
	 * 
	 * @author:chuanshuang.liucs
	 * @date:2009-4-5
	 * @param page_button_count
	 */
	public void setMovedPaginationButton(int pageButtonCount) {

		if (pageButtonCount == 0) {
			this.pageButtonCount = DEFAULT_PAGE_BUTTON_COUNT;
		}
		this.pageButtonCount = pageButtonCount;
	}

	public List<Integer> getButtonList() {

		// 总页数
		int totalPage = this.getTotalPage();
		// 当前页数
		int curPage = this.getCurrentPage();

		// 不需要移动的页数
		int noNeedToMovePage = (pageButtonCount + 1) / 2;

		int numOfPagesNoNeedToMoveFromTotalPage = (pageButtonCount) / 2;

		// 起始页
		int startPage = 0;
		// 结束页
		int endPage = 0;

		if (curPage <= noNeedToMovePage) {
			// 当前页小于需要移动的页数，则按钮开始于1
			startPage = 1;
			// 如果总页数大于显示按钮数，结束于显示按钮数；否则结束于总页数
			if (totalPage > pageButtonCount) {
				endPage = pageButtonCount;
			} else {
				endPage = totalPage;
			}
		} else if (totalPage - curPage <= numOfPagesNoNeedToMoveFromTotalPage) {
			// 当前页数接近总页数，已不需移动，按钮结束于总页数
			endPage = totalPage;
			// 如果总页数小于显示按钮数，这开始于1；否则...
			if (totalPage > pageButtonCount) {
				startPage = totalPage - pageButtonCount + 1;
			} else {
				startPage = 1;
			}
		} else {
			// 当前页左侧显示的数目
			int leftPageNum = (pageButtonCount - 1) / 2;
			// 当前页右侧显示的数目
			int rightPageNum = (pageButtonCount) / 2;

			startPage = curPage - leftPageNum;
			if (totalPage > curPage + leftPageNum) {
				endPage = curPage + rightPageNum;
			} else {
				endPage = totalPage;
			}
		}
		// 构建button列表，从startPage到endPage
		buttonList = new ArrayList<Integer>();
		for (int i = startPage; i <= endPage; i++) {
			buttonList.add(i);
		}
		return buttonList;
	}

	public void setButtonList(List<Integer> buttonList) {
		this.buttonList = buttonList;
	}

}
