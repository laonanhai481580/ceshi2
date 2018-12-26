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
		width:70px;
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
-->
</style>
<div class="ui-layout-center">
	<form action="post" onsubmit="return false" id="form">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
			 	<button class='btn' type="button" onclick="window.chartView.search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
			 	<button class='btn' type="button" onclick="window.chartView.exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<button class='btn' type="reset" onclick="setTimeout(function(){$('select[isMulti]').multiselect('uncheckAll');},10);"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				<button class='btn' onclick="window.chartView.toggleSearchTable();" type="button" id="showOrHideSearchBtn"><span><span><b class="btn-icons btn-icons-search"></b>隐藏查询</span></span></button>
			 	<span style="margin-left:4px;color:red;" id="message">
			 		${errorMessage}
			 	</span>
			 	<div style="float:right;">
			 		<#if isManager>
			 			<button class='btn' type="button" onclick="window.chartView.editChartSetting();"><span><span><b class="btn-icons btn-icons-edit"></b>修改配置</span></span></button>
			 		</#if>
			 	</div>
			 </div>
			<div id="opt-content">
				<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
					<tr>
						<td style="padding-left:6px;padding-bottom:4px;">
							<ul id="searchUl">
						 	</ul>
						 </td>
					</tr>
				</table>
				<table id="totalTable" class="form-table-outside-border" style="width:100%;">
					<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计对象</caption>
					<tr>
						<td id="totalParent">
						</td>
					</tr>
				</table>
				<table id="groupTable" class="form-table-outside-border" style="width:100%;">
					<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
					<tr>
						<td id="groupParent">
						</td>
					</tr>
				</table>
				<div id="chartDiv" style="padding-top:8px;">
				</div>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	function contentResize(){
		if(window.chartView.cacheResult){
			window.chartView.createChart(window.chartView.cacheResult);
		}
	}
	//tooltip显示格式化
	<#list formatterStrs as format>
	   window._seriesTooltipFormatter${format.seriesId}=function(){
			${format.function};
		} 
	</#list>
	$(function(){
		<#if (errorMessage != "")>
			return;
		</#if>
		//初始化
		window.chartView.init({
			webRoot:'${webRoot}',
			chartDefinitionCode:'${chartDefinitionCode}',//流程定义的编码
			searchInputs:${initSearchs},
			selectListMap:${selectListMap},
			groupSets:${groupSets},
			totalSets:${totalSets},
			isShowSearchSet:'${isShowSearchSet}',//是否显示查询条件
			isShowTotalType:'${isShowTotalType}',//是否显示统计对象
			isShowGroupType:'${isShowGroupType}',//是否显示分组条件
			isDefaultSearch:'${isDefaultSearch}',//初始化时调用查询方法
			is3D:'${is3D}'//是否3D效果显示
		});
	});
</script>

