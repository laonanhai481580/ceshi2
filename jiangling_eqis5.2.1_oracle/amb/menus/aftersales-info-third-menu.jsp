<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<script type="text/javascript">
$(function() {
	$( "#accordion1" ).accordion({
		animated:false,
		collapsible:false,
		event:'click',
		fillSpace:true 
	});
	
});
</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="aftersales-faultInfo-info">
	<h3><a id="_aftersales_fault_info" onclick="_change_menu('${aftersalesctx}/aftersales-info-view/faultInfoView-list.htm');">售后三包信息</a></h3>
	<div>
		<div id="aftersales_fault_info" class="demo"></div>
	</div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-faultInfo-polato">
	<h3><a id="_aftersales-plato"  onclick="_change_menu('${aftersalesctx}/aftersales-info/faultInfo-plato.htm');">柏拉图统计</a></h3>
	<div>
		<div id="aftersales-plato" class="demo"></div>
	</div>	
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-faultInfo-r1000,aftersales-faultInfo-r1000-graph,aftersales-r1000-caranaly">
	<h3><a id="_aftersales-analysis"  onclick="_change_menu('<grid:authorize code="aftersales-faultInfo-r1000,aftersales-faultInfo-r1000-graph,aftersales-r1000-caranaly" systemCode="aftersales"></grid:authorize>');">R1000统计分析</a></h3>
	<div>
		<div id="aftersales-analysis" class="demo"></div>
	</div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-faultInfo-cup,aftersales-faultInfo-cup_graph,aftersales-faultInfo-cup_report">
	<h3><a id="_aftersales-cpu"  onclick="_change_menu('<grid:authorize code="aftersales-faultInfo-cup,aftersales-faultInfo-cup_graph,aftersales-faultInfo-cup_report" systemCode="aftersales"></grid:authorize>');">CPU统计分析</a></h3>
	<div>
		<div id="aftersales-cpu" class="demo"></div>
	</div>
	</security:authorize>	
	<security:authorize ifAnyGranted="aftersales-faultInfo-week">
	<h3><a id="_aftersales-week-report"  onclick="_change_menu('${aftersalesctx}/week-month-report/weekReport-listEditable.htm');">售后周报</a></h3>
	<div>
		<div id="aftersales-week-report" class="demo"></div>
	</div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-faultInfo-month">
	<h3><a id="_aftersales-month-report"  onclick="_change_menu('${aftersalesctx}/week-month-report/monthReport-listEditable.htm');">售后月报</a></h3>
	<div>
		<div id="aftersales-month-report" class="demo"></div>
	</div>
	</security:authorize>
	<security:authorize ifAnyGranted="aftersales-faultInfo-base,rankingWeight-list,supplierBfsaleImprove-listEditable,partsMergeParent-listEditable,partsRepeat-listEditable,doublePartsCode-listEditable">
	<h3><a id="_aftersales-report-base" onclick="_change_menu('<grid:authorize code="aftersales-faultInfo-base,rankingWeight-list,supplierBfsaleImprove-listEditable,partsMergeParent-listEditable,partsRepeat-listEditable,doublePartsCode-listEditable" systemCode="aftersales"></grid:authorize>');">报表基础配置</a></h3>
	<div>
		<div id="aftersales-report-base" class="demo"></div>
	</div>	
	</security:authorize>
</div>

		
<script type="text/javascript" class="source">
	$(function () {
		var aIndex = 0;
		var aId = thirdMenu;
		if(thirdMenu=="aftersales_fault_info"){
			$("#aftersales_fault_info").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
						<security:authorize ifAnyGranted="aftersales-faultInfo-info">
						  "<li onclick='selectedNode(this)' id='myaftersales_fault_info'><a href='${aftersalesctx}/aftersales-info-view/faultInfoView-list.htm'>售后三包信息</a></li>"+
						</security:authorize>
					  <security:authorize ifAnyGranted="aftersales-faultInfo-bak">
					  "<li onclick='selectedNode(this)' id='myaftersales_fault_info_bak'><a href='${aftersalesctx}/aftersales-info-bak/list.htm'>三包单纠正记录</a></li>"+
					  </security:authorize>
					  <security:authorize ifAnyGranted="aftersales-faultInfo-bak-plato">
					  "<li onclick='selectedNode(this)' id='myaftersales_fault_info_bak_plato'><a href='${aftersalesctx}/aftersales-info-bak/bakInfo-plato.htm'>三包纠正信息柏拉图</a></li>"+
					  </security:authorize>
			          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales_fault_info";
		}else if(thirdMenu=="aftersales-plato"){
			$("#aftersales-plato").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
							<security:authorize ifAnyGranted="aftersales-faultInfo-polato">
								 "<li onclick='selectedNode(this)' id='aftersales-faultInfo-plato'><a href='${aftersalesctx}/aftersales-info/faultInfo-plato.htm'>柏拉图统计</a></li>"+
							 </security:authorize>
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-plato";
		}else if(thirdMenu=="aftersales-analysis"){
			$("#aftersales-analysis").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
				          <security:authorize ifAnyGranted="aftersales-faultInfo-r1000">
						  "<li onclick='selectedNode(this)' id='ronethousand_trend_graph'><a href='${aftersalesctx}/aftersales-info/faultInfo-ronethousand-trend-graph.htm'>R1000故障率综合统计</a></li>"+
						  </security:authorize>
				          <security:authorize ifAnyGranted="aftersales-faultInfo-r1000-graph">
					      "<li onclick='selectedNode(this)' id='ronethousand_caytype_graph'><a href='${aftersalesctx}/aftersales-info/faultInfo-ronethousand-graph.htm'>R1000车型对比分析</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="aftersales-r1000-caranaly">
					      "<li onclick='selectedNode(this)' id='ronethousand_susystem_report'><a href='${aftersalesctx}/aftersales-info/faultInfo-ronethousand-report-graph.htm'>R1000子系统对比分析</a></li>"+
					      </security:authorize>
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-analysis";
		}else if(thirdMenu=="aftersales-cpu"){
			$("#aftersales-cpu").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
				          <security:authorize ifAnyGranted="aftersales-faultInfo-cup">
					      "<li onclick='selectedNode(this)' id='cpu_trend_graph'><a href='${aftersalesctx}/aftersales-info/faultInfo-cpu-trend-graph.htm'>CPU故障金额综合统计</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="aftersales-faultInfo-cup_graph">
					      "<li onclick='selectedNode(this)' id='cpu_caytype_graph'><a href='${aftersalesctx}/aftersales-info/faultInfo-cpu-graph.htm'>CPU故障金额车型分析</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="aftersales-faultInfo-cup_report">
					      "<li onclick='selectedNode(this)' id='cpu_susystem_report'><a href='${aftersalesctx}/aftersales-info/faultInfo-cpu-report-graph.htm'>CPU故障金额子系统分析</a></li>"+
					      </security:authorize>
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-cpu";
		}else if(thirdMenu=="aftersales-week-report"){
			$("#aftersales-week-report").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
						   <security:authorize ifAnyGranted="aftersales-faultInfo-week">
								"<li onclick='selectedNode(this)' id='fault_info_week_report'><a  href='${aftersalesctx}/week-month-report/weekReport-listEditable.htm'>周报</a></li>"+
						    </security:authorize>
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-week-report";
		}else if(thirdMenu=="aftersales-month-report"){
			$("#aftersales-month-report").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
						   <security:authorize ifAnyGranted="aftersales-faultInfo-month">
								 "<li onclick='selectedNode(this)' id='fault_info_month_report'><a href='${aftersalesctx}/week-month-report/monthReport-listEditable.htm'>月报</a></li>"+
							</security:authorize>
				          "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-month-report";
		}else if(thirdMenu=="aftersales-report-base"){
			$("#aftersales-report-base").jstree({ 
				"html_data" : {
					"data" :  
				          "<ul>"+
// 					      "<li onclick='selectedNode(this)' id='fault_info_part_rone_cpu'><a href='${aftersalesctx}/report-base/partRoneCpu-listEditable.htm'>零部件R1000CPU配置</a></li>"+
// 					      "<li onclick='selectedNode(this)' id='fault_info_make_month_fault'><a href='${aftersalesctx}/report-base/makeMonthFault-listEditable.htm'>制造月3MIS故障数</a></li>"+
// 					      "<li onclick='selectedNode(this)' id='fault_info_rone_cpu_ranking'><a href='${aftersalesctx}/report-base/roneCpuRanking-listEditable.htm'>综合排名</a></li>"+
// 					      "<li onclick='selectedNode(this)' id='fault_info_this_month_repair_times'><a href='${aftersalesctx}/report-base/thisMonthRepairTimes-listEditable.htm'>本月维修频次</a></li>"+
						  <security:authorize ifAnyGranted="aftersales-faultInfo-base">
					      "<li onclick='selectedNode(this)' id='fault_info_twg_score'><a href='${aftersalesctx}/report-base/twgScore-listEditable.htm'>TGW维护</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="rankingWeight-list">
					      "<li onclick='selectedNode(this)' id='fault_info_ranking_weight'><a href='${aftersalesctx}/report-base/rankingWeight-list.htm'>指标权重维护</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="supplierBfsaleImprove-listEditable">
					      "<li onclick='selectedNode(this)' id='fault_info_supplier_bfsale'><a href='${aftersalesctx}/report-base/supplierBfsaleImprove-listEditable.htm'>供应商售前改进维护</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="partsMergeParent-listEditable">
					      "<li onclick='selectedNode(this)' id='fault_info_parts_merge'><a href='${aftersalesctx}/report-base/partsMergeParent-listEditable.htm'>零部件合并规则维护</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="partsRepeat-listEditable">
					      "<li onclick='selectedNode(this)' id='fault_info_parts_repeat'><a href='${aftersalesctx}/report-base/partsRepeat-listEditable.htm'>合并前多子系统零件</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="doublePartsCode-listEditable">
					      "<li onclick='selectedNode(this)' id='fault_info_double-parts_code'><a href='${aftersalesctx}/report-base/doublePartsCode-listEditable.htm'>合并零部件重复零部件</a></li>"+
					      </security:authorize>
					      "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_aftersales-report-base";
		}
		$("#accordion1 > h3 > a").each(function(index,obj){
			if(obj.id == aId){
				aIndex = index;
				return false;
			}
		});
		$("#accordion1").accordion({active:aIndex});
	});
	function selectedNode(obj) {
		window.location = $(obj).children('a').attr('href');
	}
	  function _change_menu(url){
		window.location=url;
	}
</script>