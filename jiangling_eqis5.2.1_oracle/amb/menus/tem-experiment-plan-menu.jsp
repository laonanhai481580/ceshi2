<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="accordion1" class="basic">
<security:authorize ifAnyGranted="tem-year-plan-list,tem-year-plan-input">
	<h3><a id="experimentPlan" onclick="_change_menu('<grid:authorize code="tem-year-plan-list,tem-year-plan-input" systemCode="tem"></grid:authorize>')">实验计划管理</a></h3>
	<div>
		<div id="_experimentPlan" class="demo"></div>
	</div>
</security:authorize>
</div>
<script type="text/javascript">
	function selectedNode(obj){
		window.location = $(obj).children('a').attr('href');
	}
	$(function () {
		var aId=0;
		if(thirdMenu=="experimentPlan"){
			aId = 'experimentPlan';
			$("#_experimentPlan").jstree({ 
				"html_data" : {
					"data" : 
			          "<ul>"
			          <security:authorize ifAnyGranted="tem-year-plan-list">
			          +"<li onclick='selectedNode(this)' id='yearPlanList'><a href='${temctx }/parts-inner/experiment-plan/year-plan-list.htm'>年度实验计划台账</a></li>"
			          </security:authorize>
			          <security:authorize ifAnyGranted="tem-year-plan-input">
			          +"<li onclick='selectedNode(this)' id='yearPlanInput'><a href='${temctx }/parts-inner/experiment-plan/year-plan-input.htm'>年度实验计划表单</a></li>"
			          </security:authorize>
			          <security:authorize ifAnyGranted="tem-month-plan-list">
			          +"<li onclick='selectedNode(this)' id='monthPlanList'><a href='${temctx }/parts-inner/experiment-plan/month-plan-list.htm'>月度实验计划台账</a></li>"
			          </security:authorize>
			          +"</ul>"
				},
				"ui" : {
					"initially_select" : [treeMenu]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
		}
	var aIndex = 0;
	$("#accordion1 > h3 > a").each(function(index,obj){
		if(obj.id == aId){
			aIndex = index;
			return false;
		}
	});
	$("#accordion1").accordion({ fillSpace:	true,active:aIndex });
});
	$().ready(function(){
		/* $('#'+thirdMenu).addClass('west-notree-selected'); */
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
	function _change_menu(url){
		window.location=url;
	}
</script>