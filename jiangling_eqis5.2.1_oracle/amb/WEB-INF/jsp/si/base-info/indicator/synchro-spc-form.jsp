<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	var params = '';
	var isUsingComonLayout=false;
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		contentResize();
		search();
	});
	function contentResize(){
		var height = $(window).height() - $('#customerSearchDiv').height() - 160;
		$("#list").jqGrid("setGridHeight",height);
	}
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
	//设置默认 参数
	function $addGridOption(jqGridOption){
		jqGridOption.postData = params;
	}
	//查询
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
	function editRow(rowId){
		$.colorbox({
			href:'${spcctx}/data-acquisition/maintenance-input.htm?groupId='+rowId,
			iframe:true,
			innerWidth:750,
			innerHeight:500,
			overlayColse:false,
			title:"子组信息",
			onClosed:function(){
				$("#list").trigger("reloadGrid");
			}
		});
	}
	//同步
	function submitForm(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1==""||date2==""){
			$("#message").html("请选择同步时间范围");
			return false;
		}
		if(date1&&date2&&date1>date2){
			$("#message").html("开始日期不能超过结束日期");
			return false;
		}else{
			params=getParams();
			$("#message").html("正在同步中 请稍候... ...");
			$("button").attr("disabled","disabled");
			var url = "${sictx}/base-info/indicator/synchro-check-datas.htm?indicatorId="+<%=request.getParameter("indicatorId")%>;
			$.post(url,params,function(result){
				$("button").attr("disabled","");
				$("#message").html("");
				if(result.error){
					$("#message").html("同步失败：<font style='color:red;'>" + result.message + "</font>");
				}else{
					$("#message").html("同步成功");
					search();
					setTimeout(function() {
						$("#message").html("");
					}, 3000);
				}
			},'json');
		}
	}
</script>
</head>

<body>
	<div class="opt-body">
		<div class="opt-btn">
			<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-sync"></b>同步</span></span></button>
			<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span ><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>	
			<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			<div style="margin-left:4px;color:red;line-height:30px;" id="message" class="mess"></div>
		</div>
		<div id="opt-content">
			<form id="contentForm" method="post" action="">
			    <fieldset>
			    	<legend style="text-align: center;height: 30px;"><h3>数据推送至SPC同步检索</h3></legend>
			    	<div id="customerSearchDiv" style="display:block;padding:4px;">
				    	<table class="form-table-outside-border" style="width:99.7%;padding:4px;">
							<tr>
								<td style="width:130px;">时间范围/采集时间</td>
								<td>
									<input id="datepicker1" type="text" readonly="readonly" style="width:72px;border: none;" name="params.startDate_ge_date" value=""/>
									至
								    <input id="datepicker2" type="text" readonly="readonly" style="width:72px;border: none;" name="params.endDate_le_date" value=""/>
								</td>
								<td>质量特性
									<input style="border: none;" type="text" id="featureName" name="featureName" value="${name}" readonly="readonly"/>
									<input type="hidden" id="qualityFeature" name="params.qualityFeatures" value="${id}" />
								</td>
								<td style="padding-right: 4px;text-align:right;" valign="middle">
									<span>
										<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
										<button class="btn" onclick="reset();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
									</span>
								</td>
							</tr>
						</table>
					</div>
		    		<div style="margin-top: 10px;"><grid:jqGrid gridId="list" url="${spcctx}/data-acquisition/maintenance-list-datas.htm" code="SPC_SUB_GROUP" pageName="page"></grid:jqGrid></div>
			    </fieldset>
		    </form>
		</div>
	</div>
</body>
</html>