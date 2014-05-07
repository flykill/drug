<div id="B-head">
    <div id="head-inner">
        <h2>乐善药店管理系统</h2>
        <p id="head-user-line">
	        <#if isLogin >
				你好，<strong>${userName!}</strong>&nbsp;&nbsp;
	        	<a href="../login/loginOut.do" class="user-logout">退出</a>
			<#else>
				<a href="../login.html" class="user-logout">登录</a>
	        </#if>
        </p>
    </div>
</div><!-- head -->