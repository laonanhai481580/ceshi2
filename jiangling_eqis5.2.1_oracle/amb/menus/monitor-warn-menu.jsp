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
	<security:authorize ifAnyGranted="monitor-iqc-warnRules-list,monitor-mfg-warnRules-list,monitor-aftersales-warnRules-list">
	<h3><a id="_warn_rule" onclick="_change_menu('<grid:authorize code="monitor-iqc-warnRules-list,monitor-mfg-warnRules-list,monitor-aftersales-warnRules-list" systemCode="monitor"></grid:authorize>');">监控规则设置</a></h3>
	<div style="padding:0px;">
		<div id="warn-rule-tree" class="demo"></div>
	</div>
	</security:authorize>
<%-- 	<h3><a id="_improve_period" onclick="_change_menu('${monitorctx }/period/list.htm');">改进完成周期设置</a></h3> --%>
<!-- 	<div style="padding:0px;"> -->
<%-- 		<div id="_period_list" class="west-notree" onclick="javascript:window.location='${monitorctx }/period/list.htm';"> --%>
<!-- 			<span>要求完成周期设置</span> -->
<!-- 		</div> -->
<!-- 	</div> -->
</div>
<script type="text/javascript" class="source">
	$(function () {
		if(thirdMenu!="warn-rule-tree"){
			$('#'+thirdMenu).addClass('west-notree-selected');
		}
		if(thirdMenu=="warn-rule-tree"){
			$("#warn-rule-tree").jstree({ 
				"html_data" : {
					"data" :
						"<ul>"+
						<security:authorize ifAnyGranted="monitor-iqc-warnRules-list">
						"<li onclick='selectedNode(this)' id='_iqc_rule'><a href='${monitorctx }/warn-rule/iqc-list.htm'>进料检验监控</a></li>"+
						</security:authorize>
						<security:authorize ifAnyGranted="monitor-mfg-warnRules-list">
						"<li onclick='selectedNode(this)' id='_mfg_rule'><a href='${monitorctx }/warn-rule/list.htm'>制造过程监控</a></li>"+
						</security:authorize>
// 						<security:authorize ifAnyGranted="monitor-aftersales-warnRules-list">
// 						"<li onclick='selectedNode(this)' id='_market_rule'><a href='${monitorctx }/warn-rule/aftersales-list.htm'>售后质量监控</a></li>"+
// 						</security:authorize>
						"</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			$("#accordion1").accordion({active:0});
		}
		if(thirdMenu=="_period_list"){
			$("#accordion1").accordion({active:1});
		}
	});
	function selectedNode(obj) {
		window.location = $(obj).children('a').attr('href');
	}
	function _change_menu(url){
		window.location = url;
	}
</script>