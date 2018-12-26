<%@page import="org.hibernate.Hibernate"%>
<%@page import="com.ambition.chartdesign.entity.ChartDefinition"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript">
	$(function(){
		initForm();
		var saveSuccess = '${saveSuccess}';
		if(saveSuccess=='true'){
			window.parent.$("#table").trigger("reloadGrid");
			$.showMessage("保存成功!");
		}
	});
	function initForm(){
		$("div[series]").css("cursor","pointer").attr("title","系列")
			.click(function(){
				var val = $(":input[name=caculate]").val();
				$(":input[name=caculate]").val(val + "{" + $(this).attr("series") + "}").focus();
			});
		//格式化初始化
		$(":input[formatter]").click(function(){
			formatterClick(this);
		}).each(function(index,obj){
			formatterClick(obj);
		});
	}
	function formatterClick(obj){
		var targetName = $(obj).attr("formatter");
		if($(obj).is(":checked")){
			var targetNames = targetName.split(",");
			for(var i=0;i<targetNames.length;i++){
				if(targetNames[i]){
					$(":input[name="+targetNames[i]+"]").removeAttr("disabled");				
				}
			}
		}else{
			var targetNames = targetName.split(",");
			for(var i=0;i<targetNames.length;i++){
				if(targetNames[i]){
					$(":input[name="+targetNames[i]+"]").attr("disabled","disabled");
				}
			}
		}
	}
	function submitForm(url){
		if($("#form").valid()){
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
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
					onclick="javascript:window.location.href='${chartdesignctx}/chart-definition/step5.htm?isBack=true';">
					<span><span><b class="btn-icons btn-icons-prev"></b>上一步</span></span>
				</button>
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/step-end.htm')">
					<span><span><b class="btn-icons btn-icons-next"></b>完成配置</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			</div>
			<div id="opt-content">
				<div id="info" style="background:white;padding:4px;margin:0px 0px 8px 0px;font-weight:bold;font-size:14px;line-height:30px;">
					6/6.设置统计图信息,配置完成。
				</div>
				<form id="form" name="form"
					method="post" action="">
					<fieldset style="margin-top:4px;">
					<legend>基本信息</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:15%;">
									编码
								</td>
								<td style="width:35%;">
									<input type="text" style="width:90%;" name="code" value="${code}" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
								<td colspan="2">
								</td>
							</tr>
							<tr>
								<td>
									标题
								</td>
								<td>
									<input type="text" name="name" value="${name}" style="width:90%;" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
								<td style="width:15%;">
									小标题
								</td>
								<td style="width:35%;">
									<input type="text" name="titleName" value="${titleName}" style="width:90%;"/>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:4px;">
					<legend>显示设置</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="padding-bottom:6px;">
									<input type="checkbox" 
										name="isDefaultSearch" <s:if test="isDefaultSearch">checked="checked"</s:if> 
										value="1" 
										id="isDefaultSearch"/><label for="isDefaultSearch">打开统计图时立即查询</label>
								</td>
								<td colspan=2 style="padding-bottom:6px;">
									<input type="checkbox" 
										name="is3D" <s:if test="is3D">checked="checked"</s:if> 
										value="1" 
										id="is3D"/><label for="is3D">显示3D效果(支持饼图和柱状图)</label>
								</td>
							</tr>
							<tr>
								<td style="width:33%;border-top:1px solid #9cd9f8;padding-top:6px;padding-bottom:6px;">
									<input formatter="legendPosition" type="checkbox" 
										name="isShowLegend" <s:if test="isShowLegend">checked="checked"</s:if> 
										value="1" 
										id="isShowLegend"/><label for="isShowLegend">显示图例</label>
								</td>
								<td colspan=2 style="border-top:1px solid #9cd9f8;padding-top:6px;padding-bottom:6px;">
									图例位置:
									<s:radio list="positions" listKey="value" listValue="name" 
										name="legendPosition" 
										value="legendPosition"
										theme="simple"></s:radio>
								</td>
							</tr>
							<tr>
								<td rowspan="2" valign="top" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<input formatter="dataTablePosition,dataTableFormatType" type="checkbox" 
										name="isShowDataTable" <s:if test="isShowDataTable">checked="checked"</s:if> 
										value="1" 
										id="isShowDataTable"/><label for="isShowDataTable">显示数据表格</label>
								</td>
								<td colspan=2 style="border-top:1px solid #9cd9f8;padding-top:6px;">
									表格位置:
									<s:radio list="positions" listKey="value" listValue="name" 
										name="dataTablePosition" 
										value="dataTablePosition"
										theme="simple"></s:radio>
								</td>
							</tr>
							<tr>
								<td colspan=2 style="padding-bottom:6px;">
									显示格式:
									<s:radio list="dataTableFormatTypes" listKey="value" listValue="name" 
										name="dataTableFormatType" 
										value="dataTableFormatType"
										theme="simple"></s:radio>
								</td>
							</tr>
							<tr>
								<td  style="border-top:1px solid #9cd9f8;padding-top:6px;padding-bottom:6px;">
									<input type="checkbox" 
										name="isShowGroupType" <s:if test="isShowGroupType">checked="checked"</s:if> 
										value="1" 
										id="isShowGroupType"/><label for="isShowGroupType">显示统计分组</label>
								</td>
								<td style="width:33%;border-top:1px solid #9cd9f8;padding-top:6px;padding-bottom:6px;">
									<input type="checkbox" 
										name="isShowTotalType" <s:if test="isShowTotalType">checked="checked"</s:if> 
										value="1" 
										id="isShowTotalType"/><label for="isShowTotalType">显示统计对象</label>
								</td>
								<td style="width:33%;border-top:1px solid #9cd9f8;padding-top:6px;padding-bottom:6px;">
									<input type="checkbox" 
										name="isShowSearchSet" <s:if test="isShowSearchSet">checked="checked"</s:if> 
										value="1" 
										id="isShowSearchSet"/><label for="isShowSearchSet">显示查询条件</label>
								</td>
							</tr>
							<tr>
								<td colspan="3" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<label>排序方式按</label>
									<s:select list="sortBys"
										listKey="value"
										listValue="name"
										name="sortBy"
										value="sortBy"
										theme="simple">
									</s:select>
									<select name="sortByType">
										<option value="asc" <s:if test="sortByType == 'asc'">selected</s:if>>升序</option>
										<option value="desc" <s:if test="sortByType == 'desc'">selected</s:if>>降序</option>
									</select>
									<label>排序.</label>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<label>数据最多显示</label>
									<input type="text" name="showDataNums" 
										value="${showDataNums }" style="width:60px;"
										class="{digits:true,messages:{digits:'必须是整数!'}}"/>
									<label>条</label>
									<input type="checkbox" 
										name="isShowOthers" <s:if test="isShowOthers">checked="checked"</s:if> 
										value="1" 
										id="isShowOthers"/><label for="isShowOthers">超过设置条数合并到其他显示</label>
								</td>
							</tr>
							<tr>
								<td colspan="3"  style="border-top:1px solid #9cd9f8;padding-top:6px;">
									<label>横坐标旋转</label>
									<input type="text" name="rotation" 
										value="${rotation}" style="width:60px;"
										class="{number:true,messages:{digits:'必须是整数!'}}"/>
									<label>度显示(横坐标值比较长时设置).</label>
								</td>
							</tr>
						</table>
					</fieldset>
					<%--<fieldset style="margin-top:4px;">
						<legend>横坐标显示范围</legend>
						<input type="hidden" name="groupSetStrs"/>
						<table id="table"></table>
					</fieldset> --%>
				</form>
			</div>
		</div>
	</div>
</body>
</html>