<%@page import="org.apache.commons.lang.StringUtils"%>
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
		$("div[series]").css("cursor","pointer").attr("title","系列")
			.click(function(){
				var val = $(":input[name=caculate]").val();
				$(":input[name=caculate]").val(val + "{" + $(this).attr("series") + "}").focus();
			});
		//格式化初始化
		$(":input[customClick]").click(function(){
			formatterClick(this);
		}).each(function(index,obj){
			formatterClick(obj);
		});
		//初始化特殊值设置的表格
		initGrid();
	}
	function formatterClick(obj){
		var formatter = $(obj).attr("formatter");
		if(formatter){
			if($(obj).is(":checked")){
				$(":input[name="+formatter+"]").removeAttr("disabled");
			}else{
				$(":input[name="+formatter+"]").attr("disabled","disabled");
			}
		}
		var disableName = $(obj).attr("disableName");
		if(disableName){
			if($(obj).is(":checked")){
				$(":input[name="+disableName+"]").attr("disabled","disabled");
			}else{
				$(":input[name="+disableName+"]").removeAttr("disabled");
			}
		}
	}
	//编辑参数
	var editId=null;
	var editParams={
		keys:true,
		url : 'clientArray',
		oneditfunc:function(rowId){
			oneditRow(rowId);
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
			if(!window.notAdd){
				var nextId = $("#" + rowId).next().attr("id");
				if(nextId){
					$("#table").jqGrid("editRow",nextId,editParams);
				}
			}
		},
		afterrestorefunc:function(rowId){
			editId = null;
		}
	};
	//编辑时的事件
	function oneditRow(rowId){
	}
	//初始化所有列
	var colModels = [
		{name:'name',label:'参数名称',width:140},
		{name:'id',label:'ID',hidden:true},
		{name:'nullParamValue',label:'参数值',width:90,editable:true,editrules:{number:true}},
		{name:'nullResultValue',label:'计算结果',width:90,editable:true,editrules:{number:true}},
		{name:'zeroParamValue',label:'参数值',width:90,editable:true,editrules:{number:true}},
		{name:'zeroResultValue',label:'计算结果',width:90,editable:true,editrules:{number:true}},
	];
	var data = ${specialValueSetData};
	function initGrid(){
		$("#table").jqGrid({
			rownumbers : true,
			datatype:'local',
			gridEdit: true,
			rownumbers : true,
			width : $("#opt-content").width()-40,
			colModel: colModels,
			data:data,
			gridEdit: true,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			ondblClickRow: function(rowId){
				var res = saveEditing(true);
				if(!res){
					return;
				}
				$("#table").jqGrid("editRow",rowId,editParams);
			}
		});
		jQuery("#table").jqGrid('setGroupHeaders', {
		  useColSpanStyle: true, 
		  groupHeaders:[
		    {startColumnName: 'nullParamValue', numberOfColumns: 2, titleText: '值为null时处理'},
		    {startColumnName: 'zeroParamValue', numberOfColumns: 2, titleText: '值为0时处理'}
		  ]
		});
		var height = $("#opt-content").height() - $("#table").position().top-110;
		if(height<90){
			height = 140;
		}
		$("#table").jqGrid("setGridHeight",height);
	}
	function saveEditing(notAdd){
		window.notAdd = notAdd;
		if(editId){
			var res = $("#table").jqGrid("saveRow",editId,editParams);
			delete window.notAdd;
			return res;
		}
		delete window.notAdd;
		return true;
	}
	function submitForm(url){
		var res = saveEditing(true);
		if(!res){
			return;
		}
		if($("#form").valid()){
			var ids = $("#table").jqGrid("getDataIDs");
			var caculateSpecialSet = [];
			for(var i=0;i<ids.length;i++){
				var str = "";
				var rowData = $("#table").jqGrid("getRowData",ids[i]);
				for(var j=0;j<colModels.length;j++){
					var fieldName = colModels[j].name;
					if(fieldName=='id'){
						continue;
					}
					var val = rowData[fieldName];
					if(val==undefined||val==''){
						continue;
					}
					if(str){
						str += ',';
					}
					str += fieldName + ":\"" + (val==undefined?"":val) + "\"";
				}
				//没有设置值时不处理
				if(str.indexOf("nullParamValue:")==-1&&str.indexOf("nullResultValue:")==-1
					&&str.indexOf("zeroParamValue:")==-1&&str.indexOf("zeroResultValue:")==-1){
					continue;	
				}
				caculateSpecialSet.push("{" + str + "}");
			}
			var caculateSpecialSetStrs = "[" + caculateSpecialSet.join(",") + "]";
			$(":input[name=caculateSpecialSetStrs]").val(caculateSpecialSetStrs);
			
			$('#form').attr('action',url);
			$("button").attr("disabled","disabled");
			$("#message").html("正在执行操作,请稍候... ...");
			$('#form').submit();
		}else{
			var error = $("#form").validate().errorList[0];
			$.showMessage(error.message);
			$(error.element).focus();
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
					onclick="submitForm('${chartdesignctx}/chart-definition/save-series-from-caculate.htm')">
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
					<input type="hidden" name="caculateSpecialSetStrs"/>
					<fieldset style="margin-top:4px;">
						<legend>基本信息</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:15%;">
									系列名称
								</td>
								<td style="width:35%;">
									<input type="text" style="width:90%;" name="name" value="${name}" class="{required:true,messages:{required:'必填!'}}"/>
								</td>
								<td style="width:15%;">
									统计图类型
								</td>
								<td style="width:35%;">
									<s:select list="chartTypes" name="chartType" 
										listKey="value" 
										listValue="name" 
										cssClass="{required:true,messages:{required:'必填!'}}"
										cssStyle="width:90%;"
										theme="simple"></s:select>
								</td>
							</tr>
							<tr>
								<td>
									最小值
								</td>
								<td>
									<input type="text" name="min" value="${min}" title="可为空" style="width:90%;" class="{number:true,messages:{number:'必须是数字!'}}"/>
								</td>
								<td>
									最大值
								</td>
								<td>
									<input type="text" name="max" value="${max}" title="可为空" style="width:90%;" class="{number:true,messages:{number:'必须是数字!'}}"/>
								</td>
							</tr>
							<tr>
								<td>
									小数位数
								</td>
								<td>
									<input type="text" name="formatNum" value="${formatNum}" title="可为空" style="width:90%;" class="{digits:true,messages:{digits:'必须是整数!'}}"/>
								</td>
								<td colspan="2">
									
								</td>
							</tr>
							<tr>
								<td>
									纵坐标
								</td>
								<td colspan="3">
									<input customClick="true" disableName="useYaXisName" formatter="yaXisPosition" type="radio" name="isYaXis" <s:if test="isYaXis">checked="checked"</s:if> value="1" 
									id="isYaXis1"/><label for="isYaXis1">本系列数据作为纵坐标</label>
									<s:radio list="yaXisPositions"
										listKey="value"
										listValue="name"
										name="yaXisPosition"
										value="yaXisPosition"
										cssClass="{required:true,messages:{required:'必填!'}}"
										theme="simple"></s:radio>
								</td>
							</tr>
							<tr>
								<td></td>
								<td colspan="3">
									<input customClick="true" disableName="yaXisPosition" formatter="useYaXisName" type="radio" name="isYaXis" <s:if test="!isYaXis">checked="checked"</s:if> value="0" 
									id="isYaXis2"/><label for="isYaXis2">引用其他纵坐标</label>
									<s:select 
										name="useYaXisName"
										value="useYaXisName"
										list="canUseYaXiss"
										listKey="value"
										listValue="name"
										cssClass="{required:true,messages:{required:'必填!'}}"
										emptyOption="true"
										theme="simple">
									</s:select>
								</td>
							</tr>
							<tr>
								<td>
									颜色
								</td>
								<td colspan="3">
									<%
										String[] colors = new String[]{"","7cb5ec","f7a35c", 
										    "8085e9", "f15c80", "e4d354", "8085e8", "8d4653", "90ed7d","91e8e1"};
										String colorValue = ActionContext.getContext().getValueStack().findString("color");
										for(int colIndex=0;colIndex<colors.length;colIndex++){
											String color = colors[colIndex];
											String label = "默认";
											if(StringUtils.isNotEmpty(color)){
												label = "&nbsp;&nbsp;";
											}
									%>
									<div style="float:left;margin-top:8px;">
										<input type="radio" name="color" value="<%=colors[colIndex] %>" id="color<%=colIndex%>" <%=color.equals(colorValue)?"checked=checked":"" %>/>
										<label for="color<%=colIndex%>" title="选择颜色" style="padding:6px;cursor:pointer;background:#<%=color%>"><%=label %></label>
									</div>
									<%} %>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>参数配置</legend>
						<table style="width: 100%;height:100%;">
							<tr>
								<td style="width:50%;">
									<input type="checkbox" name="isShow" <s:if test="isShow">checked="checked"</s:if> value="1" 
									id="isShow"/><label for="isShow">显示统计图</label>
								</td>
								<td style="width:50%;">
									<input type="checkbox" name="isDataSummation" <s:if test="isDataSummation">checked="checked"</s:if> value="1" 
									id="isDataSummation"/><label for="isDataSummation">数据累加</label>
								</td>
							</tr>
							<tr>
								<td>
									<input customClick="true" formatter="showLabelFormatter" type="checkbox" name="isFormatData" <s:if test="isFormatData">checked="checked"</s:if> value="1" 
										id="isFormatData"/><label for="isFormatData">数据格式化</label>
									<s:select 
										name="showLabelFormatter"
										value="showLabelFormatter"
										list="valueFormatters"
										listKey="value"
										listValue="name"
										emptyOption="true"
										theme="simple">
									</s:select>
								</td>
								<td>
									<input type="checkbox" name="isShowLabel" <s:if test="isShowLabel">checked="checked"</s:if> value="1" 
									id="isShowLabel"/><label for="isShowLabel">图中显示数据值</label>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input customClick="true" formatter="showTooltipFormatter" type="checkbox" name="isShowTooltip" <s:if test="isShowTooltip">checked="checked"</s:if> value="1" id="isShowTooltip"/><label for="isShowTooltip">鼠标移动显示数据</label>
									<div style="padding-left:18px;">
										格式化说明   this.series.name:系列名称,this.x:横坐标的值,this.y:纵坐标的值
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-bottom:6px;">
									<textarea rows="2" cols="" style="width:95%;height:30px;" name="showTooltipFormatter">${showTooltipFormatter}</textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="border-top:1px solid #9cd9f8;padding-top:6px;">
									系列显示名称(多个统计对象切换时,显示的系列名称根据设置显示)
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<s:iterator value="totalSetNames" id="totalObj">
										<div style="float:left;margin-left:8px;">
											<label>${totalObj.name}</label>
											<input type="text" name="showName_${totalObj.name}" value="${totalObj.value}"/>									
										</div>
									</s:iterator>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>计算公式</legend>
						<div>
							<s:iterator value="seriesList">
								<div style="float:left;border:1px solid blue;padding:4px;margin-left:4px;margin-top:4px;" series="${name}">{${name}}</div>
								<div style="float:left;border:1px solid blue;padding:4px;margin-left:4px;margin-top:4px;" series="${name}的合计">{${name}的合计}</div>
							</s:iterator>
						</div>
						<div>
							<textarea style="height:40px;margin-top:4px;width:98%;" rows=2 name="caculate" class="{required:true,messages:{required:'必填!'}}">${caculate}</textarea>
						</div>
					</fieldset>
					<fieldset style="margin-top:4px;">
						<legend>参数为特殊值时设置</legend>
						<table id="table"></table>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>