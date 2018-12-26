<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	.searchli{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	.searchli select{
		width:175px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
	.ui-jqgrid tr.jqgrow td 
	{
		/* jqGrid cell content wrap  */
		white-space: normal !important;
	        height :auto;
	}
	
	th.ui-th-column div
	{
		/* jqGrid columns name wrap  */
		white-space:normal !important;
		height:auto !important;
		padding:0px;
	}
-->
</style>
<div class="ui-layout-center">
	<form action="post" onsubmit="return false" id="customSearchForm">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="window.customSearchView.search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
			 	<button class='btn' type="button" onclick="window.customSearchView.exportGrid();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<button class='btn' type="reset" onclick="setTimeout(function(){$('select[isMulti]').multiselect('uncheckAll');},10);"  id="resetSearchBtn"><span><span><b class="btn-icons btn-icons-redo"></b>重置查询条件</span></span></button>
				<button class='btn' onclick="window.customSearchView.toggleSearchTable();" type="button" id="showOrHideSearchBtn"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>
			 	<span style="margin-left:4px;color:red;" id="message">
			 		${errorMessage}
			 	</span>
			 	<div style="float:right;">
			 		<#if isManager>
			 			<button class='btn' type="button" onclick="window.customSearchView.saveGridColumnWidth();"><span><span><b class="btn-icons btn-icons-save"></b>保存列宽</span></span></button>
			 			<button class='btn' type="button" onclick="window.customSearchView.editGridSetting();"><span><span><b class="btn-icons btn-icons-edit"></b>修改配置</span></span></button>
			 		</#if>
			 	</div>
			 </div>
			<div id="opt-content">
				<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
					<tr>
						<td style="padding-left:6px;padding-bottom:4px;">
							<ul id="searchUl"></ul>
						</td>
					</tr>
				</table>
				<div id="tableDiv" style="margin-top:8px;"></div>
			</div>
		</div>
	</form>
	<form id="exportForm" name="exportForm" method="post" action=""></form>
</div>
<script type="text/javascript">
	<#if errorMessage!=''>
	     return;
	</#if>
	function contentResize(){
		$("#opt-content").height($(".ui-layout-center").height()-40);
		if(window.customSearchView.grids&&window.customSearchView.grids.length>0){
			var offset = $("#tableDiv").offset();
			var width = $(window).width() - offset.left - 12;
			var height = offset.top - $("#opt-content").offset().top;
			height = $("#opt-content").height() - height - 35;
			var headerHeight = $("#tableDiv").find(".ui-jqgrid-hbox").height();
			height -= headerHeight - 20;
			if(window.customSearchView.rowList&&window.customSearchView.rowList.length>0){
				height -= 24;
			}
			/**if(window.customSearchView.groupHeaders&&window.customSearchView.groupHeaders.length>0){
				height -= 24;
			}*/
			if(window.customSearchView.totalColumnNames&&window.customSearchView.totalColumnNames.length>0){
				height -= 24;
			}
			$("#" + window.customSearchView._gridId).jqGrid("setGridWidth",width);
			$("#" + window.customSearchView._gridId).jqGrid("setGridHeight",height);
		}
	}
	//添加格式化方法
	<#list methodMap?keys as key>
		window.customSearchView.formatters['${key}'] = function(value,o,rowObject){
			${methodMap[key]}
		}
	</#list>
	$(function(){
		//初始化
		var methodJson = ${methodJson};
		var colModels = ${colModels};
		for(var i=0;i<colModels.length;i++){
			var model = colModels[i];
			if(methodJson[model.name]){
				var formatterName = methodJson[model.name];
				model.formatter = window.customSearchView.formatters[formatterName];
			}
		}
		window.customSearchView.init({
			webRoot:'${webRoot}',
			customSearchCode:'${customSearchCode}',//自定义查询定义的编码
			searchInputs:${initSearchs},
			selectListMap:${selectListMap},
			colModels:colModels,
			groupHeaders:${groupHeaders},
			sortBy : '${sortBy}',
			sortByType : '${sortByType}',
			rowList : [${rowList}],
			isShowSearchSet:'${isShowSearchSet}',//是否显示查询条件
			isDefaultSearch:'${isDefaultSearch}',//初始化时调用查询方法
			initComplete:function(gridId){ //初始化完成后调用的方法
				${initCompleteMethod}
			},
			loadComplete:function(gridId){ //初始化完成后调用的方法
				${loadCompleteMethod}
			},
			totalFormatter:function(totalValueJson){ //初始化完成后调用的方法
				${totalFormatterMethod}
			}
		});
	});
</script>

