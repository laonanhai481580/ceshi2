var lastsel;var newId;var hasEdit=false;
//fieldName1:集合字段名  fieldName1=[{},{},...];fieldName2='[{},{},...]'
/*---------------------------------------------------------
函数名称:getSubTableDatas
参          数:无
功          能:保存时获得子表数据,其格式为:
		carUseApplication=[{"id":"52521","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"汤祁中的用车申请"},{"id":"76991","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"76835","useType":"短途","deptCheckResult":"张君正","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"何庆的用车申请"},{"id":"75200","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"76235","useType":"短途","deptCheckResult":"","officeCheckResult":"王滨","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"91716","useType":"短途","deptCheckResult":"","officeCheckResult":"","leaderCheckResult":"","isDirectAssign":"否","applicationTheme":"王滨的用车申请"},{"id":"108380","useType":"短途","deptCheckResult":"","officeCheckResult":"","leaderCheckResult":"","isDirectAssign":"是","applicationTheme":"王滨的用车申请"}];aa=[{"a":"1","b":"2"}]
------------------------------------------------------------*/
function getSubTableDatas(formId){
	$("#"+formId).find("input[name='subTableVals']").remove();
	var controls=$("input[pluginType='STANDARD_LIST_CONTROL']");
	var results="";
	for(var i=0;i<controls.length;i++){
		var collectionObj="";
		var tableId="tb_"+$(controls[i]).attr("id");
		//保存时有一行正编辑，取值有问题，需要保存当前正编辑的列表中的值并去掉编辑状态
		saveRowWhenAdd(lastsel,tableId);
		hasEdit=false;//编辑状态设为false
		
		collectionObj=$(controls[i]).attr("name")+"=";
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
							objVal=objVal+"\""+p+"\":{\"value\":\""+result[1]+"\",\"datatype\":\""+result[0]+"\"},";
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
		
		if(collectionObj!="")results=results+collectionObj+";";
	}
	if(results.indexOf(";")>=0)results=results.substring(0,results.length-1);
	$("#"+formId).append("<input name='subTableVals' value='"+results+"'/>");
}
/*---------------------------------------------------------
函数名称:saveRowWhenAdd
参          数:无
功          能:保存行
------------------------------------------------------------*/
function saveRowWhenAdd(lastsel,tableId){
	return jQuery('#'+tableId).jqGrid('saveRow',lastsel);
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
		ae = "<a href='#' class='small_btn' onclick=\"myAddRow(newId,'"+cl+"','"+tableId+"');\"><span>增加</span></a>";
		de = "<a href='#' class='small_btn' onclick=\"jQuery('#"+tableId+"').jqGrid('delRowData','"+cl+"');hasEdit=false;\" ><span>删除</span></a>"; 
		if(ids.length>1){
			jQuery('#'+tableId).jqGrid('setRowData',ids[i],{act:ae+de});
		 }else{
			 jQuery('#'+tableId).jqGrid('setRowData',ids[i],{act:ae});
		}
	}
}

function myAddRow(newId,cl,tableId){
	if(!hasEdit||(hasEdit&&saveRowWhenAdd(lastsel,tableId))){
		jQuery('#'+tableId).jqGrid('addRowData','new_'+newId,{},'before',cl);
		jQuery('#'+tableId).jqGrid('editRow','new_'+newId,true,editFun,function(){},'',{},function(){hasEdit=false;});
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

function restoreOtherTable(currentTableId){
	var controls=$("input[pluginType='STANDARD_LIST_CONTROL']");
	for(var i=0;i<controls.length;i++){
		var tableId="tb_"+$(controls[i]).attr("id");
		if(tableId!=currentTableId){
			jQuery('#'+tableId).jqGrid('restoreRow',lastsel);hasEdit=false;
		}
	}
}

//key:value,根据value获得key;mms中配的为准
function getkeyByValue(currenProperty,currentValue,colModel){
	var result=new Array();
	for(var i=0;i<colModel.length;i++){
		if(currenProperty==colModel[i].name){
			result[0]=colModel[i].mydatatype;
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

function deleteFormTableData(tableId,rowid,deleteUrl){
	if(rowid.indexOf("new_")<0){
		var url=deleteUrl;
		if(url.indexOf("?")<0){
			url=url+"?id="+rowid;
		}else{
			url=url+rowid;
		}
		if(deleteUrl==""){
			alert("请在系统构建平台中配删除数据的url");
		}else{
			jQuery("#"+tableId).jqGrid('delRowData',"'"+rowid+"'");
			hasEdit=false;
			$.ajax({
				type: "POST",
				url: url,
				success: function(data){
				}
			}); 
		}
	}else{
		jQuery("#"+tableId).jqGrid('delRowData',"'"+rowid+"'");
		hasEdit=false;
	}
}