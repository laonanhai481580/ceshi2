var lastsel;var newId;var hasEdit=false;var deleteUrl="";var editing=false;var delRowWarning="请选择！";
//fieldName1:集合字段名  fieldName1=[{},{},...];fieldName2='[{},{},...]'

//隐藏所有的标准列表控件
function initStandardListControl(controlId){
	$("input[pluginType=STANDARD_LIST_CONTROL]").css("display","none");
}
function isHasEdit(gridId){
	if(typeof(gridId)!="undefined"){
		saveRowWhenAdd(lastsel,gridId);
		return hasEdit;
	}
	var controls=$("input[pluginType='STANDARD_LIST_CONTROL']");
	for(var i=0;i<controls.length;i++){
			var tableId="tb_"+$(controls[i]).attr("id");
			//保存时有一行正编辑，取值有问题，需要保存当前正编辑的列表中的值并去掉编辑状态
			//jQuery('#'+tableId).jqGrid('restoreRow',lastsel,function(){hasEdit=false;});
			saveRowWhenAdd(lastsel,tableId);
		if(hasEdit){
			return hasEdit;
		}
	}
	return hasEdit;
}
/*---------------------------------------------------------
函数名称:getSubTableDatas
参          数:无
功          能:保存时获得子表数据,其格式为:
		carUseApplication=[{"id":"52521","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"汤祁中的用车申请"},{"id":"76991","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"76835","useType":"短途","deptCheckResult":"张君正","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"何庆的用车申请"},{"id":"75200","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"76235","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"91716","useType":"短途","deptCheckResult":"","officeCheckResult":"","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"108380","useType":"短途","deptCheckResult":"","officeCheckResult":"","leaderCheckResult":"","isDirectAssign":"是","applicationTheme":"王滨的用车申请"}];aa=[{"a":"1","b":"2"}]
------------------------------------------------------------*/
function getSubTableDatas(formId){
var notSave=isHasEdit();
if(!notSave){
	$("#"+formId).find("input[name='subTableVals']").remove();
	var controls=$("input[pluginType='STANDARD_LIST_CONTROL']");
	var results="";
	for(var i=0;i<controls.length;i++){
		var tableId="tb_"+$(controls[i]).attr("id");
		var indexname=$("#"+tableId).jqGrid('getGridParam','indexname');
		var collectionObj=getOneGridDatas(formId,tableId,$(controls[i]).attr("name"));		
		if(collectionObj!="")results=results+collectionObj+";";
	}
	if(results.indexOf(";")>=0)results=results.substring(0,results.length-1);
	$("#"+formId).append("<textarea name='subTableVals' style='display:none;'>"+results+"</textarea>");

}
return !notSave;
	
}

function getFormGridDatas(formId,tableId){
	var notSave=isHasEdit(tableId);
	if(!notSave){
		$("#"+formId).find("input[name='subTableVals']").remove();
		var fieldName=$("#"+tableId).jqGrid('getGridParam','myattrName');
		var results="";
		var collectionObj=getOneGridDatas(formId,tableId,fieldName);
		if(collectionObj!="")results=results+collectionObj+";";
		if(results.indexOf(";")>=0)results=results.substring(0,results.length-1);
		$("#"+formId).append("<textarea name='subTableVals' style='display:none;'>"+results+"</textarea>");

	}
	return !notSave;
		
	}
	//返回值格式为fieldName:fieldOrderName={}
	function getOneGridDatas(formId,tableId,fieldName){
			var collectionObj="";
			var indexname=$("#"+tableId).jqGrid('getGridParam','indexname');
			collectionObj=fieldName+":"+indexname+"=";
			var arr=$("#"+tableId).jqGrid('getRowData');//[{id:'1',name:'',....},{id:'1',name:'',....}]
			var colModel=$("#"+tableId).jqGrid('getGridParam','colModel');
			var vals="";
			if(arr.length>0)vals="[";
			for(var j=0;j<arr.length;j++){
				var obj=arr[j];
				
				delete obj['act'];
				var objVal="";
				for(var p in obj){
					if(objVal=="")objVal="{";
					// 方法
		            if(typeof(obj[p])!="function"){
		            	// p 为属性名称，obj[p]为对应属性的值 
		            	if(p=="id"&&obj[p].indexOf("new_")>=0){//增加的数据的id设为""
		            		objVal=objVal+"\""+p+"\":\"\",";
		            	}else{
							var result=getkeyByValue(p,obj[p],colModel);
							if(p=="id"){
								objVal=objVal+"\""+p+"\":\""+result[1]+"\",";
							}else{
								if(p.indexOf("_")>=0){
									var re=/_/g;
									p=p.replace(re,".");
								}
								objVal=objVal+"\""+p+"\":{\"value\":\""+result[1]+"\",\"datatype\":\""+result[0]+"\",\"classname\":\""+result[2]+"\"},";
							}
		            	}
		            }
				}
				if(objVal.indexOf(",")>=0)objVal=objVal.substring(0,objVal.length-1);
				if(objVal!="")objVal=objVal+"}";
				vals=vals+objVal+",";
			}
			if(vals.indexOf(",")>=0)vals=vals.substring(0,vals.length-1);
			if(arr.length>0)vals=vals+"]";
			collectionObj=collectionObj+vals;
			return collectionObj;
	}
	
	function restoreOtherTable(currentTableId){
		var controls=$("input[pluginType='STANDARD_LIST_CONTROL']");
		for(var i=0;i<controls.length;i++){
			var tableId="tb_"+$(controls[i]).attr("id");
			if(tableId!=currentTableId){
				jQuery('#'+tableId).jqGrid('restoreRow',lastsel,function(){hasEdit=false;});
			}
		}
	}

	//key:value,根据value获得key;mms中配的为准
	function getkeyByValue(currenProperty,currentValue,colModel){
		var result=new Array();
		for(var i=0;i<colModel.length;i++){
			if(currenProperty==colModel[i].name){
				result[0]=colModel[i].mydatatype;
				var className=colModel[i].classname;
				if(typeof(className)!='undefined'){
					result[2]=className;
				}else{
					result[2]="";
				}
				var valueset=colModel[i].valueset;//valueset:{key:value,key:value...}
				if(typeof(valueset)!='undefined'){
					for(var val in valueset){
						if(typeof(valueset[val])!='function'){
							if(valueset[val]==currentValue){
								result[1]=val;
								return result;
							}
						}
					}
				}
			}
		}
		result[1]=currentValue;
		return result;
	}

	function deleteFormTableData(tableId,rowid,deleteUrl,isCustomGrid,imatrixCtx){
		if(confirm("确认删除吗?")){
			if(rowid.indexOf("new_")<0){
				var url=deleteUrl;
				if(url.indexOf("?")<0){
					url=url+"?id="+rowid+"&callback=?";
				}else{
					url=url+rowid+"&callback=?";
				}
				if(deleteUrl==""){
					alert("请在系统构建平台中配删除数据的url");
				}else{
					if(typeof(isCustomGrid)!="undefined"&&isCustomGrid){	
						url=imatrixCtx+"/mms/form/"+url;
					}
					$.getJSON(
								url,
								function(data){
									jQuery("#"+tableId).jqGrid('delRowData',rowid);
									hasEdit=false;
									alert(data.msg);
								}
							);
					
				}
			}else{
				jQuery("#"+tableId).jqGrid('delRowData',rowid);
				hasEdit=false;
			}
		}
	}
	//列表管理/字段信息/对应字段列编辑时的下拉框的onchange事件（自定义标签中也有用）
	function tableColumnChange(obj){
		$("#"+obj.rowid+"_headname").attr("value",$("#"+obj.currentInputId).find("option:selected").text());
	}
/*---------------------------------------------------------
函数名称:saveRowWhenAdd
参          数:无
功          能:保存行
------------------------------------------------------------*/
function saveRowWhenAdd(lastsel,tableId){
	return jQuery('#'+tableId).jqGrid('saveRow',lastsel,function(){},'',{},function(){hasEdit=false;},function(){},function(){hasEdit=false;});
}
/*---------------------------------------------------------
函数名称:getLastNewId
参          数:无
功          能:获得最新增加的id值，其值为整数
------------------------------------------------------------*/
function getLastNewId(tableId){
	var id=jQuery('#'+tableId).attr('newId');
	if(typeof(id)=='undefined'||id==''){
		jQuery('#'+tableId).attr('newId',1);
		return 1;
	}else{
		var crnNewId=parseInt(id);
		jQuery('#'+tableId).attr('newId',crnNewId+1);
		return crnNewId+1;
	}
}
/*---------------------------------------------------------
函数名称:changeAct
参          数:无
功          能:增加行时，改变操作列的数据，当只有一行时，只显示"增加",否则都有
------------------------------------------------------------*/
function changeAct(tableId){
	newId=getLastNewId(tableId);
	var ids = jQuery('#'+tableId).jqGrid('getDataIDs');
	for(var i=0;i < ids.length;i++){
		var cl = ids[i];
		ae = "<a href='#' class='small-button-bg' onclick=\"myAddRow(newId,'"+cl+"','"+tableId+"');\"><span class='ui-icon ui-icon-plusthick'></span></a>";
		de = "<a href='#' class='small-button-bg' onclick=\"deleteFormTableData('"+tableId+"','"+cl+"','"+deleteUrl+"');\" ><span class='ui-icon  ui-icon-minusthick'></span></a>"; 
		if(ids.length>1){
			jQuery('#'+tableId).jqGrid('setRowData',ids[i],{act:ae+'&nbsp;&nbsp;'+de});
		 }else{
			 jQuery('#'+tableId).jqGrid('setRowData',ids[i],{act:ae});
		}
	}
}

function myAddRow(newId,cl,tableId){
	saveRowWhenAdd(lastsel,tableId);
	if(!hasEdit){
		jQuery('#'+tableId).jqGrid('addRowData','new_'+newId,{},'after',cl);
		jQuery('#'+tableId).jqGrid('editRow','new_'+newId,true,editFun,function(){},'',{},function(){hasEdit=false;},function(){},function(){hasEdit=false;});
		changeAct(tableId);
	}
}

function formatFun(cellvalue, options, rowObject ){
	return getShowValue(cellvalue, options);
}

function getShowValue(value, options){
	var valueset=options.colModel.valueset;
	if(typeof(valueset)!='undefined'){
		for(var p in valueset){
			if(p==value&&typeof(valueset[p])!='function'){if(valueset[p]==""){return '&nbsp;';}else{return valueset[p];}}
		}
	}
	if(typeof(value)=='undefined'||value==""){return '&nbsp;';}else{return value;}
}
//列表中显示复选框的形式
function formatCheckbox(ts1,cellval,opts,rwdat,_act){
	var v="";
	if(ts1=="true"){
		v="<input type=\"checkbox\" disabled=\"disabled\" checked=\"checked\" value=\"true\"/>";
	}else{
		v="<input type=\"checkbox\" disabled=\"disabled\" value=\"false\"/>";
	}
	return v;
}

function unFormatCheckbox(cellvalue,options,cell){
	return $('input', cell).attr('value');
}

function unFormatCurrentDate(cellvalue,options,cell){
	if($(cell).html()=="&nbsp;"){
		return formate_date(new Date());
	}
	return $(cell).html();
}
function unFormatCurrentTime(cellvalue,options,cell){
	if($(cell).html()=="&nbsp;"){
		return formate_date_second(new Date());
	}
return $(cell).html();
}

function unFormatUserName(cellvalue,options,cell){
	if($(cell).html()=="&nbsp;"){
		return $("#_user_name").val();
	}
	return $(cell).html();
}
function unFormatLoginName(cellvalue,options,cell){
	if($(cell).html()=="&nbsp;"){
		return $("#_login_name").val();
	}
	return $(cell).html();
}


/*---------------------------------------------------------
函数名称:formate_date_second
参          数:无
功          能:格式化列表中的日期为yyyy-mm-dd hh:MM:ss
------------------------------------------------------------*/
function formate_date_second(date){
	var minute=date.getMinutes();
	var hour=date.getHours();
	var second=date.getSeconds();
	if(second<10){
		second="0"+second;
	}
	if(minute<10){
		minute="0"+minute;
	}
	if(hour<10){
		hour="0"+hour;
	}
	return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+hour+":"+minute+":"+second ; 
}
/*---------------------------------------------------------
函数名称:formate_date
参          数:无
功          能:格式化列表中的日期为yyyy-mm-dd
------------------------------------------------------------*/
function formate_date(date){
	var minute=date.getMinutes();
	var hour=date.getHours();
	var second=date.getSeconds();
	if(second<10){
		second="0"+second;
	}
	if(minute<10){
		minute="0"+minute;
	}
	if(hour<10){
		hour="0"+hour;
	}
	return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate(); 
}

//重写(单行保存前处理行数据)
function $processRowData(data){
	return data;
}
//重写(在请求中附加额外的参数)
function $getExtraParams(){
	return {};
}
//重写(点击单元格触发的事件)
function $onCellClick(rowid,iCol,cellcontent,e){
	
}

/**
 * 可编辑表格所用到的选项
 */
var editableGridOptions={
	gridId:'',
	deleteUrl:'',
	rowSortable:true,
	sortUrl:'',
	havePage:true,
	extraParams:{}
};
/**
 * 表格对象
 */
var editableGrid;
/**
 * rowId表示行ID
 * originalIndex表示某一行所在表格的原始下标
 * newIndex表示某一行拖动后所在表格的新下标
 */
var rowId, originalIndex, newIndex;
/**
 * 可拖动的选择权限
 */
var sortableOptions = {
	start : function(event, ui) {
		rowId = ui.item.attr('id');
		originalIndex = editableGrid.jqGrid("getInd", rowId);
	},
	stop : function(event, ui) {
		newIndex = editableGrid.jqGrid("getInd", rowId);
		var params={};
		params=$.extend(true,params,editableGridOptions.extraParams,{
			"originalIndex" : originalIndex,
			"newIndex" : newIndex
		});
		$.post(editableGridOptions.sortUrl, params);
	}
};

function makeEditable(editable) {
	if (editable) {
		editing = false;
		if(editableGridOptions.rowSortable)
			jQuery('#'+editableGridOptions.gridId+' tbody').sortable('enable');
		lastsel=null;
	} else {
		editing = true;
		if(editableGridOptions.rowSortable)
			jQuery('#'+editableGridOptions.gridId+' tbody').sortable('disable');
	}

}

/**
 * 编辑下一行
 * @param rowId
 * @return
 */
function editNextRow(rowId) {
	var ids = editableGrid.jqGrid("getDataIDs");
	var index = editableGrid.jqGrid("getInd", rowId);
	index++;
	if (index > ids.length) {// 当前编辑行是最后一行
		editableGrid.resetSelection();
		addRow(true);
	} else {
		editableGrid.resetSelection();
		editRow(ids[index - 1]);
		editableGrid.setSelection(ids[index - 1],true);
	}
	
}

/**
 * 保存后走的方法
 * @param rowId
 * @param data
 * @return
 */
function afterSaveRow(rowId, data) {
	// 必须加括号才能转换为对象
	var jsonData = eval("(" + data.responseText + ")");
	if (rowId == 0) {// 新纪录删除了再增加
		editableGrid.jqGrid('delRowData', rowId);
		editableGrid.jqGrid('addRowData', jsonData.id, jsonData, "last");
	} else {// 更新已有记录
		editableGrid.jqGrid('setRowData', jsonData.id, jsonData);
	}
	editNextRow(jsonData.id);
}

/**
 * 编辑时需要的参数
 */
var editParams={
		keys : true,
		oneditfunc : function(rowId){
			editFun(rowId);
			$oneditfunc(rowId);
		},
		successfunc: function( response ) {
	        return $successfunc(response);
	    },
		aftersavefunc : function(rowId, data) {
				afterSaveRow(rowId, data);
				$aftersavefunc(rowId,data);
		},
		afterrestorefunc : function(rowId) {
			makeEditable(true);
			$afterrestorefunc(rowId);
		},
		restoreAfterError:false,
		extraparam:editableGridOptions.extraParams
	};

function $successfunc(response){
	return true;
}

function $oneditfunc(rowId){
	
}
function $aftersavefunc(rowId,data){
	
}
function $afterrestorefunc(rowId){
	
}
function $ondblClickRow(rowId,iRow,iCol,e){
	
}
function $addGridOption(jqGridOption){
	
}
function $gridComplete(){
	
}
function $loadBeforeSend(xhr, settings){
	
}


/**
 * 新建一行
 * @param byEnter
 * @return
 */
function addRow(byEnter) {
	if (byEnter == undefined) {
		byEnter = false;
	}
	if ((!editing && !byEnter) || byEnter) {
//		取消对其他行的选中，新增行会自动被选中
		editableGrid.resetSelection();
		editableGrid.jqGrid('addRow', {
			rowID : "0",
			position : "last",
			addRowParams : editParams
		});
		//新增一行时，当前选中行的id为0
		lastsel=0;
		makeEditable(false);
	}else{
		alert("请先完成编辑！");
	}
}

/**
 * 编辑行
 * @param rowId
 * @return
 */
function editRow(rowId,iRow,iCol,e) {
	if('null'!=rowId){
		if(rowId==lastsel){
	//		双击当前正在编辑的行，不用做任何特殊处理
			return;
		}else{
			editableGrid.jqGrid("restoreRow", lastsel);
		}
		
		lastsel = rowId;
		editableGrid.jqGrid("editRow", rowId, editParams);
		focusOnClickedCell(rowId,e);
		makeEditable(false);
		$ondblClickRow(rowId,iRow,iCol,e);
	}
}

function focusOnClickedCell(rowId,e){
	var p=editableGrid[0].p;
	if((p.multiselect&&$.inArray(rowId,p.selarrrow)<0)||rowId!=p.selrow){
		//如果行没有选中
		editableGrid.jqGrid('setSelection',rowId,true);
	}
	$('input,select',e.target).focus();
}
/**
 * 删除
 * @param rowId
 * @return
 */
function delRow(rowId) {
	if (editing) {
		alert("请先完成编辑！");
		return;
	}
	var ids = editableGrid.getGridParam('selarrrow');
	if (ids.length < 1) {
		alert("请选中需要删除的记录！");
		return;
	}
	var params={};
	params=$.extend(true,params,editableGridOptions.extraParams,{
		deleteIds : ids.join(',')
	});
	$.post(editableGridOptions.deleteUrl, params, function(data) {
		// ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
		if (ids.length>0) {
			editableGrid.jqGrid().trigger("reloadGrid"); 
		}
		if(data!=''&&data!=null){
			alert(data);
		}
	});
}

/**
 * 初始化可编辑表格
 * @param options
 * @return
 */
function initJqGrid(options){
	var params=$getExtraParams();
	options.extraParams=$.extend(true,options.extraParams,params);
	editableGridOptions=$.extend(true,editableGridOptions,options);
	editableGrid=$('#'+editableGridOptions.gridId);
	$addGridOption(jqGridOption);
	if(editableGridOptions.havePage){
		editableGrid.jqGrid(jqGridOption).navGrid('#'+editableGridOptions.gridId+'_pager',{edit:false,add:false,del:false,search:false});
	}else{
		editableGrid.jqGrid(jqGridOption);
	}
	//若没有在mms中填写拖动后保存的Url，则该表格不可拖动
	if(editableGridOptions.sortUrl!=''){
		editableGrid.jqGrid('sortableRows',sortableOptions);
	}
}

/**
 * 动态列导出
 * @param options
 * @return
 */
function dynamicExportExcel(url){
	var col=jQuery("#"+editableGridOptions.gridId).jqGrid("getGridParam", "colNames");
	var coll=jQuery("#"+editableGridOptions.gridId).jqGrid("getGridParam", "postData");
	var dynameicColumnName="";
	for(var i=0;i<col.length;i++){
		if(col[i].indexOf("<input")==-1){
			if(dynameicColumnName!=''){
				dynameicColumnName+=",";
			}
			dynameicColumnName+=col[i];
		}
	}
	var exportForm = $("form[name='_dynamic_export_form']");
	if(exportForm.length == 0){
		addExportForm(dynameicColumnName, coll);
	}else{
		exportForm.remove();
		addExportForm(dynameicColumnName, coll);
	}
	$("#_dynamic_export_form").attr("action",url);
	$("#_dynamic_export_form").submit();
}

/**
 * 动态列导出在页面body中追加FORM
 * @param options
 * @return
 */
function addExportForm(dynameicColumnName, coll){
	var body = $("body");
	$(body).append("<form id =\"_dynamic_export_form\" name=\"_dynamic_export_form\" method=\"post\">"
			+"<input type='hidden' name='_list_code' value='"+$(coll).attr('_list_code')+"'/>"
			+"<textarea name='_dynamic_column_name'>"+dynameicColumnName+"</textarea>"
			+"</form>"
			);
}

/**
 * 验证跳转输入框（该输入框中只能输入小于等于总页数的数字，否则清空）
 * @param options
 * @return
 */
function validatePageInput(){
	$(".ui-pg-input").bind('keyup', function() {
		var value=$(".ui-pg-input").attr("value");
		value=value.replace(/[^\d]/g,'');
		$(".ui-pg-input").attr("value",value);
		if($(".ui-pg-input").attr("value") != ''){
			var totalPageNum=$("#sp_1_"+editableGridOptions.gridId+"_pager").html();
			if(parseInt(totalPageNum)<parseInt($(".ui-pg-input").attr("value")))$(".ui-pg-input").attr("value",'');
		}
	});
}

/**
 * 在页面中插入翻页信息
 * @param options
 * @return
 */
function savePageInfo(){
	var names = editableGrid.jqGrid("getGridParam", "prmNames");
	var values = editableGrid.jqGrid("getGridParam", "postData");
	var pageSizeName = names['rows'];
	var pageNoName = names['page'];
	var orderByName = names['sort'];
	var orderName = names['order'];
	if($("#___pageSizeName").attr("id")=="___pageSizeName"){
		if("true"==$("#___signPageInfo").attr("value")){
			$("#___pageSizeName").attr("value",$("#___pageSizeName").attr("value"));
			$("#___pageNoName").attr("value",$("#___pageNoName").attr("value"));
			$("#___orderByName").attr("value",$("#___orderByName").attr("value"));
			$("#___orderName").attr("value",$("#___orderName").attr("value"));
		}else{
			$("#___pageSizeName").attr("value",values[pageSizeName]);
			$("#___pageNoName").attr("value",values[pageNoName]);
			$("#___orderByName").attr("value",values[orderByName]);
			$("#___orderName").attr("value",values[orderName]);
		}
		$("#___signPageInfo").attr("value",false);
	}else{
		var body = $("body");
		$(body).append("<input type='hidden' id='___pageSizeName' value='"+values[pageSizeName]+"'/>"
				+"<input type='hidden' id='___pageNoName' value='"+values[pageNoName]+"'/>"
				+"<input type='hidden' id='___orderByName' value='"+values[orderByName]+"'/>"
				+"<input type='hidden' id='___orderName' value='"+values[orderName]+"'/>"
				+"<input type='hidden' id='___signPageInfo' value='false'/>"
				);
	}
}

/**
 * 返回时设置页面状态
 * @param options
 * @return
 */
function setPageState(){
	$("#___signPageInfo").attr("value",true);
}

/**
 * 修改GridPage信息
 * @param options
 * @return
 */
function updateGridPageInfo(xhr, settings){
	if('true'==$("#___signPageInfo").attr("value")){
		var names = editableGrid.jqGrid("getGridParam", "prmNames");
		var pageSizeName = names['rows'];
		var pageNoName = names['page'];
		var orderByName = names['sort'];
		var orderName = names['order'];
		var prm = {};
		prm[pageSizeName]=$("#___pageSizeName").attr("value");
		prm[pageNoName]=$("#___pageNoName").attr("value");
		prm[orderByName]=$("#___orderByName").attr("value");
		prm[orderName]=$("#___orderName").attr("value");
		var prmts = settings.data.split('&');
		var prmt;
		var newPrmts = '';
		for(var i=0;i<prmts.length;i++){
			if(i!=0) newPrmts+='&';
			prmt = prmts[i].split('=');
			if(prmt[0]==pageSizeName){
				newPrmts = (newPrmts+pageSizeName+'='+$("#___pageSizeName").attr("value"));
			}else if(prmt[0]==pageNoName){
				newPrmts = (newPrmts+pageNoName+'='+$("#___pageNoName").attr("value"));
			}else if(prmt[0]==pageSizeName){
				newPrmts = (newPrmts+pageSizeName+'='+$("#___orderByName").attr("value"));
			}else if(prmt[0]==orderName){
				newPrmts = (newPrmts+orderName+'='+$("#___orderName").attr("value"));
			}else{
				newPrmts += prmts[i];
			}
		}
		settings.data = newPrmts;
	}
	
}