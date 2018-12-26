<%@ page contentType="text/html;charset=UTF-8"%>
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
<script type="text/javascript" class="source">
	$(function () {
		var aIndex = 0;
		var aId = thirdMenu;
		if(thirdMenu=="allStatAnalyse"){
			$("#allStatAnalyse").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-general-plato">
			          "<li onclick='selectedNode(this)' id='generalPlato'><a href='${carmfgctx}/manu-analyse/general-plato.htm'>不良柏拉图分析</a></li>"+
			          </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-unquality-trend">
				      "<li onclick='selectedNode(this)' id='unqualityRateTrend'><a href='${carmfgctx}/manu-analyse/unquality-trend.htm'>单项不良率推移图</a></li>"+
				      </security:authorize>
				      <security:authorize ifAnyGranted="carmfg-incomingCarInfo-list">
				      "<li onclick='selectedNode(this)' id='incomingCarInfo'><a href='${carmfgctx}/incoming-car-info/list.htm'>入库车辆信息查询</a></li>"+
				      </security:authorize>
				      <security:authorize ifAnyGranted="carmfg-audit-general-plato-analysis">
				      "<li onclick='selectedNode(this)' id='allPlatoanalysis'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/audit-general-plato-analysis.htm'>综合不良对比分析</a></li>"+
				      </security:authorize>
				      "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_allStatAnalyse";
		}else if(thirdMenu=="auditStatAnalyse"){
			$("#auditStatAnalyse").jstree({ 
				"html_data" : {
					"data" :  
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-audit-deduction-trend">
				      "<li onclick='selectedNode(this)' id='auditTrend'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/audit-deduction-trend.htm'>AUDIT评价推移图</a></li>"+
				      </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-audit-general-plato">
			          "<li onclick='selectedNode(this)' id='auditPlato'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/audit-general-plato.htm'>AUDIT不良柏拉图分析</a></li>"+
			          </security:authorize>
				      "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_auditStatAnalyse";
		}else if(thirdMenu=="unqualityStatAnalyse"){
			$("#unqualityStatAnalyse").jstree({ 
				"html_data" : {
					"data" :
			          "<ul>"+
			          <security:authorize ifAnyGranted="carmfg-defective-liability-identify">
					  "<li onclick='selectedNode(this)' id='defectiveItem'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items.htm'>不良责任鉴定</a></li>"+
					  </security:authorize>
			          /* <security:authorize ifAnyGranted="carmfg-defective-liability-search">
					  "<li onclick='selectedNode(this)' id='defectiveItemSearch'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items-search.htm'>不良责任查询</a></li>"+
					  </security:authorize> */
					  <security:authorize ifAnyGranted="carmfg-defective-liability-items-total">
					  "<li onclick='selectedNode(this)' id='defectiveItemTotal'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items-total.htm'>数据采集总台账</a></li>"+
					  </security:authorize>
					  <security:authorize ifAnyGranted="carmfg-defective-liability-count">
					  "<li onclick='selectedNode(this)' id='defectiveItemCount'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/defective-items-count.htm'>不良统计汇总</a></li>"+
					  </security:authorize>
			          <security:authorize ifAnyGranted="carmfg-in-part-defective-item">
					  "<li onclick='selectedNode(this)' id='inPartDefectiveItem'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/in-part-defective-items.htm'>厂内责任部门不良月报</a></li>"+
					  </security:authorize>
					  <security:authorize ifAnyGranted="carmfg-part-defective-item">
					  "<li onclick='selectedNode(this)' id='partDefectiveItem'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/part-defective-items.htm'>外购件责任月报</a></li>"+
					  </security:authorize>
// 					  <security:authorize ifAnyGranted="carmfg-part-eliminate">
// 					  "<li onclick='selectedNode(this)' id='partEliminated'><a href='${carmfgctx}/data-acquisition/data-acquisition-analyse/part-eliminate.htm'>零部件制造淘汰率报告</a></li>"+
// 					  </security:authorize>
// 					  <security:authorize ifAnyGranted="carmfg-part-use">
// 					  "<li onclick='selectedNode(this)' id='partUse'><a href='${carmfgctx}/part-use/list.htm'>零部件用量维护</a></li>"+
// 					  </security:authorize>
					  "</ul>"
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
			aId = "_unqualityStatAnalyse";
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
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="carmfg-general-plato,carmfg-unquality-trend,carmfg-incomingCarInfo-list">
		<h3><a id="_allStatAnalyse" onclick="_change_menu('<grid:authorize code="carmfg-general-plato,carmfg-unquality-trend,carmfg-incomingCarInfo-list" systemCode="carmfg"></grid:authorize>');">综合统计分析</a></h3>
		<div>
			<div id="allStatAnalyse" class="demo"></div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-audit-general-plato,carmfg-audit-deduction-trend">
		<h3><a id="_auditStatAnalyse" onclick="_change_menu('<grid:authorize code="carmfg-audit-general-plato,carmfg-audit-deduction-trend" systemCode="carmfg"></grid:authorize>');">AUDIT统计分析</a></h3>
		<div>
			<div id="auditStatAnalyse" class="demo"></div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="carmfg-defective-liability-identify,carmfg-defective-liability-search,carmfg-defective-liability-count,carmfg-in-part-defective-item,carmfg-part-defective-item,carmfg-part-eliminate,carmfg-part-use,carmfg-defective-liability-items-total">
		<h3><a id="_unqualityStatAnalyse" onclick="_change_menu('<grid:authorize code="carmfg-defective-liability-identify,carmfg-defective-liability-search,carmfg-defective-liability-count,carmfg-in-part-defective-item,carmfg-part-defective-item,carmfg-part-eliminate,carmfg-part-use,carmfg-defective-liability-items-total" systemCode="carmfg"></grid:authorize>');">不良责任统计分析</a></h3>
		<div>
			<div id="unqualityStatAnalyse" class="demo"></div>
		</div>
	</security:authorize>
</div>