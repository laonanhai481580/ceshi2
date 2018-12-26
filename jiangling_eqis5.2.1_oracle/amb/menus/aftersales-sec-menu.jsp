<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <div id="secNav"> -->
<a class="scroll-left-btn" onclick="_scrollLeft();">&lt;&lt;</a>
<div class="fix-menu">
<ul class="scroll-menu">
	<security:authorize ifAnyGranted="AFS_LAR_DATA_LIST">
		<li  id="lar" class="last"><span><span><a href="<grid:authorize code="AFS_LAR_DATA_LIST" systemCode="aftersales"></grid:authorize>">LAR数据</a></span></span></li>
	</security:authorize>	
		<security:authorize ifAnyGranted="AFS_VLRR_DATA_LIST">
		<li  id="vlrr" class="last"><span><span><a href="<grid:authorize code="AFS_VLRR_DATA_LIST" systemCode="aftersales"></grid:authorize>">VLRR数据</a></span></span></li>
	</security:authorize>	
		<security:authorize ifAnyGranted="AFS_OBA_DATA_LIST">
		<li  id="oba" class="last"><span><span><a href="<grid:authorize code="AFS_OBA_DATA_LIST" systemCode="aftersales"></grid:authorize>">OBA数据</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="AFS_FAR_ANALYSIS_LIST,AFS_FAR_ANALYSIS_INPUT">
		<li  id="far" class="last"><span><span><a href="<grid:authorize code="AFS_FAR_ANALYSIS_LIST,AFS_FAR_ANALYSIS_INPUT" systemCode="aftersales"></grid:authorize>">FAR解析</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="AFS_BASEINFO_LAR_TARGET_LIST,AFS_BASEINFO_DEFECTION_LIST,AFS_BASEINFO_CUSTOMER_LIST,AFS_BASEINFO_VLRR_WARMING_LIST,AFS_BASEINFO">
		<li  id="baseInfo" class="last"><span><span><a href="<grid:authorize code="AFS_BASEINFO_LAR_TARGET_LIST,AFS_BASEINFO_DEFECTION_LIST,AFS_BASEINFO_CUSTOMER_LIST,AFS_BASEINFO_VLRR_WARMING_LIST,AFS_BASEINFO" systemCode="aftersales"></grid:authorize>">基础设置</a></span></span></li>
	</security:authorize>	
<%-- 	<security:authorize ifAnyGranted="AFS_ANALYSIS,AFS_LAR_PASS_LIST,AFS_VLRR_BAD_LIST,AFS_VLRR_BAD_ITEM_LIST,AFS_OBA_BAD_LIST,AFS_OBA_BAD_ITEM_LIST,AFS_FAR_CLOSE_LIST,AFS_FAR_BAD_ITEM_LIST">
		<li  id="analysis" class="last"><span><span><a href="<grid:authorize code="AFS_ANALYSIS,AFS_LAR_PASS_LIST,AFS_VLRR_BAD_LIST,AFS_VLRR_BAD_ITEM_LIST,AFS_OBA_BAD_LIST,AFS_OBA_BAD_ITEM_LIST,AFS_FAR_CLOSE_LIST,AFS_FAR_BAD_ITEM_LIST" systemCode="aftersales"></grid:authorize>">统计分析</a></span></span></li>
	</security:authorize> --%>	
	
		<li  id="analysis" class="last"><span><span><a href="http://192.168.119.218/ofilm-bi/" target="_blank">统计分析</a></span></span></li>
		
</ul>
</div>
<a class="scroll-right-btn" onclick="_scrollRight();">&gt;&gt;</a>
<div class="hid-header" onclick="headerChange(this);" title="隐藏"></div>
<script>
	var topMenu;
	$("#" + secMenu).addClass('sec-selected');
</script>
