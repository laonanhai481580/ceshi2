<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ambition.chartdesign.entity.ChartSeries"%>
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
			cancel();
			//$.showMessage("保存成功!");
			return;
		}
		initForm();
		var $obj = $(":input[name=linkUrl_list]");
		var tdWidth = $obj.closest("td").width();
		var width = tdWidth - $obj.position().left;
		$obj.width(width);
		$(":input[name=linkUrl_custom]").width(width-2);
	});
	function initForm(){
		initGrid();
		//格式化初始化
		$(":input[customClick]").click(function(){
			formatterClick(this);
		}).each(function(index,obj){
			formatterClick(obj);
		});
	}
	function formatterClick(obj){
		var formatter = $(obj).attr("formatter");
		if(formatter){
			var formatterObj = $(":input[name="+formatter+"]");
			if(!$(obj).is(":checked")||$(obj).is(":disabled")){
				formatterObj.attr("disabled","disabled");
			}else{
				formatterObj.removeAttr("disabled");
			}
			formatterObj.each(function(index,obj){
				var customClick = $(obj).attr("customClick");
				if(customClick){
					formatterClick(obj);
				}
			});
		}
		var disableName = $(obj).attr("disableName");
		if(disableName){
			if($(obj).is(":checked")||$(obj).is(":disabled")){
				$(":input[name="+disableName+"]").attr("disabled","disabled");
			}else{
				$(":input[name="+disableName+"]").removeAttr("disabled");
			}
		}
	}
	function submitForm(url){
		var res = saveEditing();
		if(res == false){
			return;
		}
		if($("#form").valid()){
			var ids = $("#table").jqGrid('getDataIDs');
			var strs = [];
			for(var i=0;i<ids.length;i++){
				var obj = $("#table").jqGrid("getRowData",ids[i]);
				var fieldType = fieldTypeMap[obj.fieldName];
				var str = '{leftBracket:"'+obj.leftBracket+'",fieldName:"'+obj.fieldName+'",fieldType:"'+fieldType+'",operate:"'+obj.operate+'",value:"'+obj.value+'",rightBracket:"'+obj.rightBracket+'",joinStr:"'+obj.joinStr+'"}';
				strs.push(str);
			}
			$(":input[name=dataConditionStrs]").val("[" + strs.join(",") + "]");
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
	//初始化所有列
	function initGrid(){
		$("#table").jqGrid({
			datatype:'local',
			rownumbers : true,
			width : $("#tableBtn").width(),
			data : ${dataConditionStrs},
			colModel: [
				{	name:'leftBracket',
					index:'leftBracket',
					label:'(',
					width:60,
					sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'','(':'(','((':'((','(((':'(((','((((':'(((('}}
				},
				{name:'fieldName',index:'fieldName',label:'字段',width:170,editable:true,sorable:false,editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:${dataColumnJsonStr}}
				},
				{name:'operate',index:'operate',label:'操作符',width:85,editable:true,sorable:false,editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'=':'等于','>':'大于','>=':'大于等于','<':'小于','<=':'小于等于','<>':'不等于','include':'包含','notinclude':'不包含'}}
				},
				{name:'value',index:'value',label:'值',width:140,editable:true,sorable:false,editable:true},
				{name:'rightBracket',index:'rightBracket',label:')',width:60,sorable:false,
					sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'',')':')','))':'))',')))':')))','))))':'))))'}}
				},
				{name:'joinStr',index:'joinStr',label:'并且/或者',width:90,sorable:false,
					editable:true,
					edittype:'select',
					formatter:'select',
					editoptions:{value:{'':'','and':'并且','or':'或者'}}
				}
			],
			gridEdit: true,
		    multiselect: true,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){},
			ondblClickRow: function(rowId){
				if(editId){
					var res = $("#table").jqGrid("saveRow",editId,editParams);
					if(!res){
						return;
					}
				}
				$("#table").jqGrid("editRow",rowId,editParams);
			}
		});
		$("#table").jqGrid("setGridHeight",80);
	}
	var fieldTypeMap = ${fieldTypeMap};
	function fieldNameChange(rowId,obj,isInit){
		var type = fieldTypeMap[obj.value];
		var valueObj = $("#" + rowId + "_value");
		if(type == '<%=Hibernate.DATE.getName()%>'
			||type == '<%=Hibernate.TIMESTAMP.getName()%>'){
			if(!valueObj.hasClass("hasDatepicker")){
				if(!isInit){
					valueObj.val("");
				}
				valueObj.datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
			}
		}else{
			valueObj.removeClass("hasDatepicker").unbind();
		}
	}
	var editId=null;
	var editParams={
		keys:true,
		url : 'clientArray',
		oneditfunc:function(rowId){
			var obj = $("#" + rowId + " :input[name=fieldName]").change(function(){
				fieldNameChange(rowId,this);
			});
			if(obj.length>0){
				fieldNameChange(rowId,obj[0],true);			
			}
			editId = rowId;
		},
		aftersavefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
		},
		afterrestorefunc:function(rowId){
			$("#" + rowId + " a[setBtn]").hide();
			editId = null;
		}
	};
	function addRow(){
		if(editId){
			var res = $("#table").jqGrid("saveRow",editId,editParams);
			if(!res){
				return;
			}
		}
		var id = (new Date()).getTime();
		$("#table").jqGrid(
			'addRow',
			{
				rowID : id,
				position : "last",
				addRowParams : editParams
		});
	}
	function delRow(){
		var ids = $("#table").jqGrid("getGridParam","selarrrow");
		for(var i=0;i<ids.length;i++){
			$("#table").jqGrid("delRowData",ids[i]);
		}
	}
	function viewCondition(){
		if(editId){
			var res = $("#table").jqGrid("saveRow",editId,editParams);
			if(!res){
				return;
			}
		}
		var ids = $("#table").jqGrid('getDataIDs');
		var str = '';
		for(var i=0;i<ids.length;i++){
			var obj = $("#table").jqGrid("getRowData",ids[i]);
			str += " " + obj.leftBracket + ' ' + obj.fieldName + ' ' + obj.operate + ' ' + obj.value + ' ' + obj.rightBracket + ' ' + obj.joinStr;
		}
		alert(str);
	}
	function saveEditing(){
		if(editId){
			return $("#table").jqGrid("saveRow",editId,editParams);
		}
	}
</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button"
					onclick="submitForm('${chartdesignctx}/chart-definition/save-series-from-datasource.htm')">
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
					<input type="hidden" name="dataConditionStrs"/>
					<input type="hidden" name="goalDataSourceId" value="${goalDataSourceId}"/>
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
								<td colspan="2">
									<textarea rows="2" cols="" style="width:95%;height:30px;" name="showTooltipFormatter">${showTooltipFormatter}</textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-bottom:6px;">
									<input customClick="true" formatter="linkType" type="checkbox" name="isLink" <s:if test="isLink">checked="checked"</s:if> value="1" id="isLink"/><label for="isLink">链接地址</label>
									<label style="margin-left:10px;">
										<input customClick="true" disableName="linkUrl_custom"  formatter="linkUrl_list" type="radio" name="linkType" <s:if test="linkType=='list'">checked="checked"</s:if> value="<%=ChartSeries.LINKTYPE_LIST%>" 
											id="linkType1"/><label for="linkType1">自定义台帐</label>
											<s:select 
												name="linkUrl_list"
												value="linkUrl"
												list="listViews"
												listKey="value"
												listValue="name"
												emptyOption="true"
												disabled="disabled"
												cssClass="{required:true,messages:{required:'必填!'}}"
												theme="simple">
											</s:select>
									</label>
									<div style="padding-left:98px;padding-top:8px;">
										<input customClick="true" disableName="linkUrl_list" formatter="linkUrl_custom" type="radio" name="linkType" <s:if test="linkType!='list'">checked="checked"</s:if> value="<%=ChartSeries.LINKTYPE_URL%>" 
											id="linkType2"/><label for="linkType2">其他地址&nbsp;&nbsp;</label>
											<input type="text" name="linkUrl_custom" value="<s:if test="linkType!='list'">${linkUrl}</s:if>" class="{required:true,messages:{required:'必填!'}}"/>
									</div>
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
						<legend>数据集过滤条件</legend>
						<div class="opt-body">
							<div class="opt-btn" id="tableBtn" style="margin-bottom:2px;">
								<button class='btn' type="button" onclick="addRow()">
									<span><span><b class="btn-icons btn-icons-add"></b>添加</span></span>
								</button>
								<button class='btn' type="button" onclick="delRow()">
									<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
								</button>
								<button class='btn' type="button" onclick="viewCondition()">
									<span><span><b class="btn-icons btn-icons-search"></b>查看条件</span></span>
								</button>
							</div>
							<table id="table"></table>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>