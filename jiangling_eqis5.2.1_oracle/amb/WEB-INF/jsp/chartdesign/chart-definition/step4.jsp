<%@page import="com.ambition.chartdesign.entity.ChartSeries"%>
<%@page import="com.ambition.chartdesign.entity.ChartDefinition"%>
<%@page import="org.hibernate.Hibernate"%>
<%@page import="com.ambition.chartdesign.entity.ChartDatasource"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		initForm();
	});
	function initForm(){
		//初始化表格	
		initGrid();
	}
	function submitForm(url){
		var ids = $("#table").jqGrid("getDataIDs");
		if(ids.length==0){
			alert("图表系列不能为空!");
			return;
		}
		$(":input[name=ids]").val(ids.join(","));
		$('#form').attr('action',url);
		$("button").attr("disabled","disabled");
		$("#message").html("正在执行操作,请稍候... ...");
		$('#form').submit();
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	//初始化所有列
	var colModels = [
		{name:'name',label:'系列名称'},
		{name:'id',label:'ID',hidden:true},
		{name:'goalDataSourceId',label:'数据集ID',hidden:true},
		{name:'goalDataSourceName',label:'数据集名称',hidden:true},
		{name:'caculate',label:'计算公式',hidden:true},
		{name:'datasourceType',label:'数据来源类型',hidden:true},
		{name:'caculateFormatter',width:280,label:'数据来源',
			formatter:function(value,options,rowObj){
				var html = "<label>";
				if(rowObj.datasourceType == '<%=ChartSeries.DATASOURCE_TYPE_DATA%>'){
					html += rowObj.goalDataSourceName;
				}else{
					html += rowObj.caculate;
				}
				html += "</label>";
				return html;
			}
		},
		{name:'chartType',label:'统计图类型',width:100,align:'center',formatter:'select',editrules:{required:true},
			editoptions:{value:'column:柱状图;line:折线图;spline:曲线图;pie:饼图'}
		},
		{name:'isShow',width:70,label:'是否显示',align:'center',
			formatter:function(value,options,rowObj){
				if(value){
					return "是";
				}else{
					return "否";
				}
			}
		}/**,
		{name:'displayOrder',index:'displayOrder',label:'排序',width:70,sortable:false,
			formatter:function(val,o,rowObj){
				var top = "<a id='top_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:10px;' href=\"javascript:void(0);toTop('top_"+rowObj.id+"');\" title='向上'><span class=\"ui-icon ui-icon-arrowthick-1-n\" style='cursor:pointer;'></span></a>";
				var bottom = "<a id='bottom_"+rowObj.id+"' class=\"small-button-bg\" style='float:left;margin-left:6px;' href=\"javascript:void(0);toBottom('bottom_"+rowObj.id+"');\" title='向下'><span class=\"ui-icon ui-icon-arrowthick-1-s\" style='cursor:pointer;'></span></a>";
				return top+ bottom;
			}
		}*/
	];
	function initGrid(){
		$("#table").jqGrid({
			rownumbers : true,
			url:'${chartdesignctx}/chart-definition/temp-series-datas.htm',
			rownumbers : true,
			width : $("#tableBtn").width(),
			colModel: colModels,
			gridEdit: false,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){
				$("#table").tableDnDUpdate({});
			}
		});
		var height = $("#opt-content").height() - $("#table").position().top-110;
		if(height<90){
			height = 90;
		}
		$("#table").jqGrid("setGridHeight",height);
		$("#table").tableDnD({});
	}
	function delRow(){
		var ids = $("#table").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			return;
		}
		if(!confirm("确定要删除吗?")){
			return;
		}
		var url = '${chartdesignctx}/chart-definition/delete-series.htm';
		$.showMessage("正在删除,请稍候... ...",'custom');
		$.post(url,{deleteIds:ids.join(",")},function(result){
			if(result){
				alert(result);
			}else{
				$.showMessage("删除成功！");
				$("#table").trigger("reloadGrid");
			}
		});
	}
	/*---------------------------------------------------------
	函数名称:showIdentifiersDiv
	参          数:
	功          能:标识为（下拉选）
	------------------------------------------------------------*/
	function showIdentifiersDiv(){
		if($("#flag").css("display")=='none'){
			removeSearchBox();
			$("#flag").show();
			var position = $("#_task_button").position();
			$("#flag").css("left",position.left+15);
			$("#flag").css("top",position.top+28);
			$("#flag").width(400).find("li").width(400-10);
		}else{
			$("#flag").hide();
		}
	}
	
	var identifiersDiv;
	function hideIdentifiersDiv(){
		identifiersDiv = setTimeout('$("#flag").hide()',300);
	}
	
	function show_moveiIdentifiersDiv(){
		clearTimeout(identifiersDiv);
	}
	
	//添加系列
	function addFromDatasource(datasourceId,id){
		var url = "${chartdesignctx}/chart-definition/set-series-from-datasource.htm?goalDataSourceId=" + datasourceId;
		if(id){
			url += "&id=" + id;
		}
		$.colorbox({
			href:url,
			iframe:true, 
			width:$(window).width()-50, 
			height:$(window).height()-50,
			overlayClose:false,
			title:"设置统计图系列"
		});
	}
	
	//添加系列
	function addFromCaculate(id){
		var url = "${chartdesignctx}/chart-definition/set-series-from-caculate.htm";
		if(id){
			url += "?id=" + id;
		}
		$.colorbox({
			href:url,
			iframe:true, 
			width:$(window).width()-50, 
			height:$(window).height()-50,
			overlayClose:false,
			title:"设置统计图系列"
		});
	}
	
	function editObj(){
		var ids = $("#table").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请先选择数据!");
			return;
		}
		var data = $("#table").jqGrid("getRowData",ids[0]);
		if(data.datasourceType == '<%=ChartSeries.DATASOURCE_TYPE_DATA%>'){
			addFromDatasource(data.goalDataSourceId,data.id);
		}else{
			addFromCaculate(data.id);
		}
	}
	function toTop(clickId){
		var rowTr = $("#" + clickId).closest("tr");
		var rowId = rowTr.attr("id");
		var prevTr = $("tr[id="+rowId+"]").prev();
		var prevId = prevTr.attr("id");
		if(prevId){
			prevTr.before(rowTr);
		}
		clearSelectionAndSel(rowId);
	}
	
	function toBottom(clickId){
		var rowTr = $("#" + clickId).closest("tr");
		var rowId = rowTr.attr("id");
		var nextTr = $("tr[id="+rowId+"]").next();
		var nextId = nextTr.attr("id");
		if(nextId){
			nextTr.after(rowTr);
		}
		clearSelectionAndSel(rowId);
	}
	
	function clearSelectionAndSel(clickId){
		var selIds = $("#table").jqGrid("getGridParam","selarrrow");
		var $t = $("#table")[0];
		for(var index in selIds){
			$("#table").jqGrid("setSelection",selIds[index],false);		
		}
		$t.p.selarrrow=[];
		$("#table").jqGrid("setSelection",clickId,false);
		$("#table")[0].updatepager(true,false);
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="text-align:right;">
				<span id="message" style="color:red;position:absolute;left:4px;top:8px;">
					<s:actionmessage theme="mytheme" />
				</span>
				<button class='btn' type="button"
					onclick="javascript:window.location.href='${chartdesignctx}/chart-definition/step3.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/step5.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>下一步</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					4/6.添加统计图系列
					<label style="font-size:10px;margin-left:10px;font-weight:normal;">选择行后可按住鼠标调整顺序</label>
				</div>
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="ids"/>
					<div class="opt-body">
						<div class="opt-btn" id="tableBtn" style="margin-bottom:2px;">
							<button class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();">
								<span><span><b class="btn-icons btn-icons-add"></b>添加</span></span>
							</button>
							<button class='btn' type="button" onclick="editObj();">
								<span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span>
							</button>
							<button class='btn' type="button" onclick="delRow()">
								<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
							</button>
						</div>
						<table id="table"></table>
					</div>
				</form>
				<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
					<ul >
					<s:iterator value="datasources">
					 <li onclick="addFromDatasource(${id});">
					 <a href="#">数据集【${name}】</a>
					 </li>
					 </s:iterator>
					 <li onclick="addFromCaculate();">
					 <!-- <span><img style="border: none;" src="/imatrix/task/images/blue.gif"/></span> -->
					 <a href="#">自定义计算公式</a>
					 </li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>