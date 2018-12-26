<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<a class="scroll-left-btn" onclick="_scrollRight();">&lt;&lt;</a>
<div class="fix-menu">
	<ul class="scroll-menu">
	    <security:authorize ifAnyGranted="epm_entrustHsf_list"> 
			<li id="entrust"><span><span><a href="<grid:authorize code="epm_entrustHsf_list" systemCode="epm"></grid:authorize>">实验委托</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="epm_exceptionSingle_list"> 
			<li id="exception"><span><span><a href="<grid:authorize code="epm_exceptionSingle_list" systemCode="epm"></grid:authorize>">异常管理</a></span></span></li>
		</security:authorize>
		 <security:authorize ifAnyGranted="epm_sample_list"> 
			<li id="sample"><span><span><a href="<grid:authorize code="epm_sample_list" systemCode="epm"></grid:authorize>">样品管理</a></span></span></li>
		</security:authorize>
		<security:authorize ifAnyGranted="epm_base"> 
			<li id="base"><span><span><a href="<grid:authorize code="epm_base" systemCode="epm"></grid:authorize>">基础设置</a></span></span></li>
		</security:authorize>	
		<security:authorize ifAnyGranted="epm_stat"> 
			<li id="analysis"><span><span><a href="<grid:authorize code="epm_stat" systemCode="epm"></grid:authorize>">统计分析</a></span></span></li>
		</security:authorize>	
	</ul>
</div>
<a class="scroll-right-btn" onclick="_scrollLeft();">&gt;&gt;</a>

<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>

