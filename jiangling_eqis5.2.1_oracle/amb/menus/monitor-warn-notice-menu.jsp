<%@ page contentType="text/html;charset=UTF-8"%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true }
		);
	});
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="monitor-iqc-warnNotices-list">
		<h3><a id="_iqc" onclick="_change_menu('<grid:authorize code="monitor-iqc-warnNotices-list" systemCode="monitor"></grid:authorize>');">进货检验监控消息</a></h3>
		<div style="padding:0px;">
			<div id="_iqc_list" class="west-notree" onclick="javascript:window.location='${monitorctx }/warn-notice/iqc-list.htm';">
				<span>进货监控消息台账</span>
			</div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="monitor-mfg-warnNotices-list">
		<h3><a id="_mfg" onclick="_change_menu('<grid:authorize code="monitor-mfg-warnNotices-list" systemCode="monitor"></grid:authorize>');">制造过程监控消息</a></h3>
		<div style="padding:0px;">
			<div id="_mfg_list" class="west-notree" onclick="javascript:window.location='${monitorctx }/warn-notice/list.htm';">
				<span>制造监控消息台账</span>
			</div>
		</div>
	</security:authorize>
<!-- 	<security:authorize ifAnyGranted="monitor-aftersales-warnRules-list"> -->
<%-- 		<h3><a id="_aftersales" onclick="_change_menu('<grid:authorize code="monitor-aftersales-warnNotices-list" systemCode="monitor"></grid:authorize>');">售后管理监控消息</a></h3> --%>
<!-- 		<div style="padding:0px;"> -->
<%-- 			<div id="_aftersales_list" class="west-notree" onclick="javascript:window.location='${monitorctx }/warn-notice/aftersales-list.htm';"> --%>
<!-- 				<span>售后监控消息台账</span> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</security:authorize> -->
</div>
<script type="text/javascript" class="source">
	$(function () {
		var indexObj = {};
		$("#accordion1").find("h3 a[id]").each(function(num,obj){
			indexObj[obj.id] = num;
		});
		$('#'+thirdMenu).addClass('west-notree-selected');
		if(thirdMenu=="_iqc_list"){
			$("#accordion1").accordion({active:indexObj['_iqc']});
		}else if(thirdMenu=="_mfg_list"){
			$("#accordion1").accordion({active:indexObj['_mfg']});
		}else if(thirdMenu=="_aftersales_list"){
			$("#accordion1").accordion({active:indexObj['_aftersales']});
		}
	});
	function _change_menu(url){
		window.location = url;
	}
</script>