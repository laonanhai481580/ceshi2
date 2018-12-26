<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
		
	<script type="text/javascript">
		var params = '';
		function contentResize(){
			var height = $(window).height() - $('#customerSearchDiv').height() - 200;
			$("#list").jqGrid("setGridHeight",height);
		}
	
		$(document).ready(function(){
			params = getParams();
			$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
			$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
			contentResize();
		});
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#customerSearchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#customerSearchDiv input[type=hidden]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
		
		function search(){
			params = getParams();
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("日期前后选择有误,请重新设置!");
				$("#datepicker1").focus();
			}else{
				$('table.ui-jqgrid-btable').each(function(index,obj){
					obj.p.postData = params;
					$(obj).trigger("reloadGrid");
				});
			}
		}
		//选择质量特性
		function selectFeature(obj){
			$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
				overlayClose:false,
				title:"选择质量特性"
			});
		}
		
		function setFeatureValue(datas){
			$("#featureName").val(datas[0].value);
			$("#qualityFeature").val(datas[0].id);
		}
		//设置默认 参数
		function $addGridOption(jqGridOption){
			jqGridOption.postData = params;
		}
		
		function editRow(rowId){
			$.colorbox({
				href:'${spcctx}/data-acquisition/maintenance-input.htm?groupId='+rowId,
				iframe:true,
				innerWidth:750,
				innerHeight:512,
				overlayColse:false,
				title:"子组信息",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="data-acquisition-maintenance-save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
		
	/* 	function formatterDate(cellvalue, options, rowObject){ 
			return ((Date)(cellvalue)).Format("yyyy-MM-dd hh:mm:ss"); 
		} 
		
		// 对Date的扩展，将 Date 转化为指定格式的String 
		// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
		// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
		// 例子： 
		// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
		// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
		Date.prototype.Format = function(fmt) { //author: meizz 
			var o = { 
				"M+" : this.getMonth()+1,                 //月份 
				"d+" : this.getDate(),                    //日 
				"h+" : this.getHours(),                   //小时 
				"m+" : this.getMinutes(),                 //分 
				"s+" : this.getSeconds(),                 //秒 
				"q+" : Math.floor((this.getMonth()+3)/3), //季度 
				"S"  : this.getMilliseconds()             //毫秒 
			}; 
			if(/(y+)/.test(fmt)) 
				fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
			for(var k in o) 
				if(new RegExp("("+ k +")").test(fmt)) 
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
			return fmt; 
		} */
		
		function exports(){
			var params = getParams();
			$.showMessage("正在导出,请稍候... ...",'custom');
			$.post('${spcctx}/data-acquisition/export-maintenance-list.htm',params,function(result){
				$.clearMessage();
				if(result.error){
					alert(result.message);
				}else{
					window.location = $.getDownloadPath(result.fileId);
				}
			},'json');
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_maintenance";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-data-acquisition-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto">
			<form id="contentForm" name="contentForm" method="post" action="">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="spc_data-acquisition_maintenance-delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="spc_data-acquisition_export-maintenance-list">
						<button class='btn' onclick="exports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<button class="btn" onclick="reset();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<span style="color:red;" id="message"></span>
				</div>
				<div id="customerSearchDiv" style="display:block;padding:4px;">
					<table class="form-table-outside-border" style="width:99.7%;padding:4px;">
						<tr>
							<td style="width:80px;">采集时间</td>
							<td style="width: 200px;">
								<input id="datepicker1" type="text" readonly="readonly" style="width:72px;border: none;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>
								至
							    <input id="datepicker2" type="text" readonly="readonly" style="width:72px;border: none;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
							</td>
							<td>质量特性
								<input style="border: none;" name="featureName" id="featureName" onclick="selectFeature(this);" readonly="readonly"/>
								<input style="border: none;" name="params.qualityFeatures" id="qualityFeature" type="hidden"/>
							</td>
							<td style="padding-right: 4px;text-align:right;" valign="middle">
								<span>
									<!-- <button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
									<button class="btn" onclick="reset();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button> -->
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div>
					<table style="width:99.7%;">
						<tr>
							<td>
								<grid:jqGrid gridId="list" url="${spcctx}/data-acquisition/maintenance-list-datas.htm" code="SPC_SUB_GROUP" pageName="page"></grid:jqGrid>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>