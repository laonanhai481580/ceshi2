<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script type="text/javascript">
	$().ready(function(){
		$("#searchDiv").toggle();
		$("#supplierList").jqGrid('setGridHeight',$(".ui-layout-center").height()-$(".opt-btn").height()-$("#searchDiv").height()-120);
		var date = new Date();
		$("#year").val(date.getFullYear());
		var quarter;
		if(date.getMonth()<3){
			quarter = 1;
		}else if(date.getMonth()>=3&&date.getMonth()<6){
			quarter = 2;
		}else if(date.getMonth()>=6&&date.getMonth()<9){
			quarter = 3;
		}else if(date.getMonth()>=9&&date.getMonth()<12){
			quarter = 4;
		}
		$("#quarter").val(quarter);
	});
	//获取表单的值
	function getParams(){
		var params = {};
		$("#searchDiv select").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()&&jObj.val()!=""){
				params[obj.name] = jObj.val();
			}
		});
		
		return params;
	}
	//统计
	function search(){
		$("#supplierList")[0].p.postData = getParams();
		$("#supplierList")[0].p.postData.searchParameters={};
		$("#supplierList").setGridParam({page:1}).trigger("reloadGrid");
	}
	function $addGridOption(jqGridOption){ 		
		jqGridOption.postData=getParams();
	}

	//重新改变布局大小
	function contentResize(){
		var display = $("#searchDiv").css("display");
		if(display=='none'){
			$("#supplierList").jqGrid('setGridHeight',$(".ui-layout-center").height()-$(".opt-btn").height()-110);
		}else{
			$("#supplierList").jqGrid('setGridHeight',$(".ui-layout-center").height()-$(".opt-btn").height()-$("#searchDiv").height()-120);
		}
	}
	function $gridComplete(){
		contentResize();
	}
	
	function percentFormate(cellValue,obj,rowObj){
		return (cellValue*100).toFixed(2)+"%";
	}
	
	function supplierNumFormate(cellValue,obj,rowObj){
		return "<span style='color:blue;font-weight:bolder;background-color:palegreen;cursor:pointer;'"+
		" onclick=\"showDetail('"+$("#year").val()+"','"+$("#quarter").val()+"','"+rowObj.evaluateCarType+"')\">       " +cellValue + "        </span>";
	}
	
	function showDetail(year,quarter,carType){
		$.colorbox({
			href:"${supplierctx}/report/quarterLevel-detail.htm?_list_code=SUPPLIER_QUARTER_LEVEL_DETAIL&params.year="+year+"&params.quarter="+quarter+"&params.carType="+carType,
			iframe:true, 
			innerWidth:1000, 
			innerHeight:600,
			overlayClose:false,
			title:"供应商季度评价清单",
			onClosed:function(){
			}
		});
	}
	</script>
</head>

<body>
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="_quarter_level";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%
			request.setAttribute("selLevel",0);
		%>
		<%@include file="../evaluate/quarter/left.jsp"%>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>&nbsp;
					</div>
					<div id="searchDiv"  style="padding:10px;padding-bottom:0px;margin-right:0px">
						<form action="" onsubmit="return false;">
						<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;" >
							<tr>
								<td style="text-align: right;">年</td>
								<td>
									<select id="year" name="params.year" style="width:175px;">
										<option value="2014">2014</option>
										<option value="2015">2015</option>
										<option value="2016">2016</option>
										<option value="2017">2017</option>
										<option value="2018">2018</option>
										<option value="2019">2019</option>
										<option value="2020">2020</option>
										<option value="2021">2021</option>
										<option value="2022">2022</option>
									</select>
								</td>
								<td style="text-align: right;">季度</td>
								<td>
									<select id="quarter" name="params.quarter" style="width:175px;">
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
									</select></td>
							</tr>
						</table>
						</form>
					</div>
					<div id="opt-content">
					<form id="sForm" name="sForm" method="post"  action=""></form>
					<div id="message"><s:actionmessage theme="mytheme" /></div>	
					<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
						<form id="contentForm"  method="post"  action="">
							<grid:jqGrid gridId="supplierList" url="${supplierctx}/report/quarterLevel-listDatas.htm" submitForm="sForm"  code="SUPPLIER_MONTH_LEVEL"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
</html>