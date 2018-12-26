<%@page import="com.ambition.chartdesign.entity.ChartDatasource"%>
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
		var saveSuccess = '${saveSuccess}';
		if(saveSuccess=='true'){
			window.parent.$("#table").trigger("reloadGrid");
			//$.showMessage("保存成功!");
			cancel();
			return;
		}
		initForm();
	});
	function initForm(){
		//列名
		addTableColumns(${columns});
		//参数
		addTableParams(${params});
	}
	//添加列名
	function addTableColumns(tableColumns){
		//缓存历史数据
		var hisObj = {};
		$("tr .tablecolumn").each(function(index,obj){
			var columnName = $(obj).find(":input[fieldName=columnName]").val();
			var alias = $(obj).find(":input[fieldName=alias]").val();
			hisObj[columnName] = alias;
		});
		$("#fieldTable .tablecolumn").remove();
		var flags = "";
		for(var index = 0;index<tableColumns.length;index++){
			var tableColumn = tableColumns[index];
			var prevFlag = "a" + index;
			flags += prevFlag + ",";
			var $tr = $("<tr class='tablecolumn'></tr>");
			$tr.append("<td style='text-align:center;'>"+(index+1)+"</td>");
			var columnName = tableColumn['columnName'];
			$tr.append("<td><input style='width:90%;background:#f1ffff;' type='text' fieldName='columnName' name='"+prevFlag+"_columnName' value='"+columnName+"' title='"+columnName+"' readonly=readonly/></td>");
			var alias = tableColumn['alias'];
			if(hisObj[columnName]){
				alias = hisObj[columnName];
			}
			alias = alias?alias:'';
			$tr.append("<td><input style='width:90%;' class=\"{required:true,messages:{required:'必填!'}}\" fieldName='alias' type='text' name='"+prevFlag+"_alias' value='"+alias+"'/></td>");
			$("#fieldTable").append($tr);
		}
		$("#columnFlags").val(flags);
	}
	//添加参数
	function addTableParams(tableParams){
		//缓存历史数据
		var hisObj = {};
		$("tr.paramcolumn").each(function(index,obj){
			var paramName = $(obj).find(":input[fieldName=paramName]").val();
			var alias = $(obj).find(":input[fieldName=alias]").val();
			hisObj[paramName] = alias;
		});
		$("#paramTable .paramcolumn").remove();
		var flags = "";
		for(var index = 0;index<tableParams.length;index++){
			var tableParam = tableParams[index];
			var prevFlag = "b" + index;
			flags += prevFlag + ",";
			var $tr = $("<tr class='paramcolumn'></tr>");
			$tr.append("<td style='text-align:center;'>"+(index+1)+"</td>");
			var paramName = tableParam['paramName'];
			$tr.append("<td><input style='width:90%;background:#f1ffff;' type='text' fieldName='paramName' name='"+prevFlag+"_paramName' value='"+paramName+"' title='"+paramName+"'/></td>");
			var alias = tableParam['alias'];
			if(!alias&&hisObj[paramName]){
				alias = hisObj[paramName];
			}
			alias = alias?alias:'';
			$tr.append("<td><input style='width:90%;' class='{required:true,messages:{required:\"必填!\"}}' type='text' fieldName='alias' name='"+prevFlag+"_alias' value='"+alias+"'/></td>");
			$("#paramTable").append($tr);
		}
		$("#paramFlags").val(flags);
	}
	/**
	* 测试SQL语句的正确性
	*/
	function testSql(){
		var params = {};
		$("#form :input[name]").each(function(index,obj){
			params[obj.name] = $(obj).val();
		});
		if(params.databaseSettingType != 'local' && !params.databaseName){
			alert("请选择数据库!");
			return;
		}
		if(!params.sql){
			alert("SQL不能为空!");
			return;
		}
		$("button").attr("disabled","disabled");
		$.showMessage("正在执行SQL,请稍候... ...");
		$.post('${chartdesignctx}/custom-search/test-sql.htm',params,function(result){
			$("button").removeAttr("disabled");
			$("#message").html("");
			if(result.error){
				alert(result.message);
			}else{
				$.showMessage("SQL语句测试成功!");
				addTableColumns(result.columns);
				addTableParams(result.params);
			}
		},'json');
	}
	function submitForm(){
		if($("#form").valid()){
			if($("tr .tablecolumn").length==0){
				alert("字段不能为空!");
				return;
			}
			var params = {};
			$("#form :input[name]").each(function(index,obj){
				if(obj.type == 'radio'){
					if($(obj).is(":checked")){
						params[obj.name] = $(obj).val();
					}
				}else{
					params[obj.name] = $(obj).val();
				}
			});
			if(params.databaseSettingType != 'local' && !params.databaseName){
				alert("请选择数据库!");
				return;
			}
			$("button").attr("disabled","disabled");
			$.showMessage("正在执行操作,请稍候... ...");
			$.post('${chartdesignctx}/custom-search/save-table.htm',params,function(result){
				$("button").removeAttr("disabled");
				$("#message").html("");
				if(result.error){
					alert(result.message);
				}else{
					//$("#id").val(result.id);
					$.showMessage("执行成功!");
					window.parent.$("#table").trigger("reloadGrid");
					cancel();
				}
			},'json');
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
			<div class="opt-btn">
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/custom-search/save-table.htm')">
					<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
				</button>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color:red;">
				</span>
			</div>
			<div id="opt-content">
				<form id="form" name="form"
					method="post" action="">
					<input type="hidden" name="id" value="${id}"/>
					<fieldset style="margin-top:4px;">
						<legend>基本信息</legend>
						<table style="width: 100%;">
							<tr>
								<td align=right><font color="red">*</font>数据源名称:
									<input name="name" value="${name}" style="width:120px;" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
								<%
									String databaseSettingType = ActionContext.getContext().getValueStack().findString("databaseSettingType");
								%>
								<%=databaseSettingType %>
								<td style="padding-left:40px;" colspan="2">
									<input type="radio" name="databaseSettingType" id="local" 
										value="<%=ChartDatasource.DATABASE_SETTING_TYPE_LOCAL%>"
										<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(databaseSettingType)?"":"checked=checked"%>/>
									<label for="local">QIS数据库</label>
									
									<input type="radio" name="databaseSettingType" id="custom" 
										value="<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM%>"
										<%=ChartDatasource.DATABASE_SETTING_TYPE_CUSTOM.equals(databaseSettingType)?"checked=checked":""%>/>
									<label for="custom">自定义数据库</label>
									<s:select list="databaseSettings" 
										name="databaseName"
										listKey="showName"
										listValue="showName"
										theme="simple"
										cssStyle="margin-left:4px;"
										cssClass="{required:true,messages:{required:'必填!'}}"
										value="databaseName"></s:select>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>SQL</legend>
						<table style="width:100%;">
							<tr>
								<td>
									<textarea rows="4" style="width:100%;font-size:18px;" name="sql" class="{required:true,messages:{required:'必填!'}}">${sql}</textarea>
								</td>
							</tr>
							<tr>
								<td style="text-align:center;">
									<button class='btn'
										type="button"
										onclick="testSql()">
										<span><span><b class="btn-icons btn-icons-test"></b>执行SQL语句</span></span>
									</button>
								</td>
							</tr>
						</table>
					</fieldset>
					<table style="width:100%;margin-top:4px;">
						<tr>
							<td style="width:50%;" valign="top">
								<fieldset style="margin-top:4px;">
								<legend>字段信息</legend>
								<div style="overflow-y:auto;height:300px;" id="fieldDiv">
								<table style="width:100%;"  class="form-table-border-left" id="fieldTable">
									<tr>
										<td style="width:15%;">
											序号
										</td>
										<td style="width:30%;">
											字段名
										</td>
										<td style="width:50%;">
											别名
										</td>
									</tr>
								</table>
								</div>
								</fieldset>
							</td>
							<td style="width:50%;" valign="top">
								<fieldset style="margin-top:4px;">
									<legend>参数信息</legend>
									<div style="overflow-y:auto;height:300px;" id="paramDiv">
									<table style="width:100%;"  class="form-table-border-left" id="paramTable">
										<tr>
											<td style="width:15%;">
												序号
											</td>
											<td style="width:40%;">
												查询参数
											</td>
											<td style="width:45%;">
												别名
											</td>
										</tr>
									</table>
									</div>	
								</fieldset>
							</td>
						</tr>
					</table>
					<input type="hidden" name="columnFlags" value="" id="columnFlags"/>
					<input type="hidden" name="paramFlags" value="" id="paramFlags"/>
				</form>
			</div>
		</div>
	</div>
</body>
</html>