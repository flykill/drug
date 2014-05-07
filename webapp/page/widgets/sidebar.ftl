<div id="B-side">
    <#list menuList as fileEntity>
	    <div class="cdrPanel sp">
			<div class="hd">
				<h2>${fileEntity.fileName}</h2>
				<a href="#" class="tip ico"> <span class="ico fold" title="折叠">折叠</span>
					<span class="ico unfold " title="展开">展开</span>
				</a>
			</div>
			<div class="bd">
				<ul class="nav">
				<#list fileEntity.children as menuEntity>
					<li><a href="${menuEntity.MENUURL}">${menuEntity.MENUNAME}</a></li>
				</#list>
				</ul>
			</div>
		</div>
    </#list>
</div>
