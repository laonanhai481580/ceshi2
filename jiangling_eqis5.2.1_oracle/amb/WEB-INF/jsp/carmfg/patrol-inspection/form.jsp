<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
	<input type="hidden" name="checkItemStrs" value=""></input>
	<input type="hidden" name="patrolItemStrs" value=""></input>
	<input type="hidden" name="inspectionPoint" value="${inspectionPoint}"></input>
	<input type="hidden" name="id" value="${id}"></input>
	<input type="hidden" name="saveMode" id="saveMode"></input>
	<input type="hidden" name="imgFileId" id="imgFileId"></input>
	<input type="hidden" name="params.savetype" value="input"></input>
	<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
		<caption style="height: 25px"><h2>工艺纪律检查报告</h2></caption>
		<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}</caption>
		<tr>
			<td style="width:40%;padding:0px;text-align:center;position:relative;" rowspan="6" colspan="2">
				<span style="position:absolute;">
				<a href='' class ='cloud-zoom' rel="tintOpacity:0.5 ,smoothMove:5,zoomWidth:400,zoomHeight:400,adjustY:-4, adjustX:10">
			    	<img id="img" alt='' style=""/>
			    </a>
			    </span>
			</td>
			<td style="width:10%;">事业部</td>
			<td style="width:20%">
				<input name="businessUnitCode" value="${businessUnitCode}" type="hidden"></input>
				<input name="businessUnitName" value="${businessUnitName}" type="hidden"></input>
				<span>${businessUnitName}</span>
			</td>
			<td style="width:10%">工段</td>
			<td style="width:20%">
				<input name="section" value="${section}" type="hidden"></input>
				<span>${section}</span>
			</td>
		</tr>
		<tr>
			<td>生产线</td>
			<td>
				<input name="productionLine"  value="${productionLine}" type="hidden"></input>
				<span>${productionLine}</span>
			</td>
			<td>班别</td>
			<td>
				<input name="workGroupType" value="${workGroupType}" type="hidden"></input>
				<span>${workGroupType}</span>
			</td>
		</tr>
		<tr>
			<td>工序</td>
			<td>
				<input name="workProcedure" id="workProcedure" value="${workProcedure}" type="hidden"></input>
				<span>${workProcedure}</span>
			</td>
			<td><span style="color:red">*</span>产品编码</td>
			<td>
				<input style="float:left;" name="checkBomCode" id="checkBomCode" value="${checkBomCode}" hisValue="${checkBomCode}" hisSearch="${checkBomCode}" class="{required:true,messages:{required:'产品型号不能为空'}}"></input>
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkBomClick(this)" href="javascript:void(0);" title="选择产品型号"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>
		</tr>
		<tr>
			<td>产品型号</td>
			<td>
				<input name="checkBomModel"  value="${checkBomModel}" type="hidden"></input>
				<span>${checkBomModel}</span>
			</td>
			<td>产品名称</td>
			<td>
				<input name="checkBomName"  value="${checkBomName}" id="checkBomName" type="hidden"></input>
				<span>${checkBomName}</span>
			</td>
		</tr>
		<tr>
			<td>客户编码</td>
			<td>
				<input style="float:left;" readonly="readonly" id="customerCode" name="customerCode" value="${customerCode}"></input>
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="customerClick(this)" href="javascript:void(0);" title="选择客户"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>
			<td>客户名称</td>
			<td>
				<input id="customerName" name="customerName" value="${customerName}" type="hidden"></input>
				<span>${customerName}</span>
			</td>
		</tr>
		<tr>
			<td>交货日期</td>
			<td>
				<input id="inspectionDate" name="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd" />"></input>
			</td>
			<td><span style="color:red;width:10%">*</span>出货数量</td>
			<td>
				<input id="stockAmount" name="stockAmount" value="${stockAmount}"  class="{required:true,digits:true,messages:{required:'出货数量不能为空',digits:'必须是整数!'}}"></input>
			</td>
		</tr>
		<tr>
			<td>消防编号</td>
			<td>
				<input name="fireControlNo" value="${fireControlNo}"></input>
			</td>
			<td>是否3C产品</td>
			<td>
				<input name="is3C" value="${is3C}" type="hidden"></input>
				<span>${is3C}</span>
			</td>
			<td>是否标准件</td>
			<td>
				<input name="isStandard" value="${isStandard}" type="hidden"></input>
				<span>${isStandard}</span>
			</td>
		</tr>
		<tr>
			<td>是否关键件</td>
			<td>
				<input name="iskeyComponent" value="${iskeyComponent}" type="hidden"></input>
				<span>${iskeyComponent}</span>
			</td>
			<td>是否量产</td>
			<td>
				<input type="radio" name="isBatchProduct" value="是" <s:if test="%{isBatchProduct==\"是\"}">checked="checked"</s:if>/>是
				<input type="radio" name="isBatchProduct" value="否" <s:if test="%{isBatchProduct==\"否\"}">checked="checked"</s:if>/>否
			</td>
			<td>检验标准版本</td>
			<td>
				<input name="standardVersion" value="${standardVersion}" type="hidden"></input>
				<span>${standardVersion}</span>
			</td>
		</tr>
		<!-- <tr>
			<td>
				<input type="radio" name="acquisitionMethod" id="acquisitionMethod" value="on" checked="checked"  onclick="selectMethod(this)">在线采集</input>
				<input type="radio" name="acquisitionMethod" id="acquisitionMethod" value="off" onclick="selectMethod(this)">离线采集</input>
			</td>
			<td>
				<div id="td1" style="display: none;">Excel模板（下载模板填写数据后上传）</div>
			</td>
			<td colspan="2">
				<div id="mya" style="display: none;"></div>
			</td>
			<td>
				<input type="hidden" name="hisinspectionDatas" value='${inspectionDatas}'></input>
				<input type="hidden" name="inspectionDatas" id="inspectionDatas" value='${inspectionDatas}'></input>
				<button  class='btn' type="button" onclick="uploadInspectionDatas();" id="uploadBtn" style="display: none;"><span><span><b class="btn-icons btn-icons-upload"></b>上传离线数据</span></span></button>
			</td>
			<td id="showInspectionDatas"></td>
		</tr> -->
		<tr>
			<td colspan="6" style="padding:0px;" id="checkItemsParent">
				<div style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px;">
					<%@ include file="check-items.jsp"%>
				checkItem.getInspectionLevel()==null?"":(levelMap.containsKey(checkItem.getInspectionLevel())?levelMap.get(checkItem.getInspectionLevel()):checkItem.getInspectionLevel())ons-search"></b>选择不良项目</span></span></button>
			</td>
		</checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()tem">
				${badItemStrs}
				</div>
			</td>
		</tr> -->checkItem.getSpecifications()==null?"":checkItem.getSpecifications()er-top:0px;">
				<span>${patrolTimes}</span>
				<input type="hidden" name="patrolTimes" id="patrolTimes"   value="${patrolTimes}" ></input>
			</td>
			<td>合格次数</td>
			<td>
				<span>${patrolQualifiedTimes}</span>
				<input type="hidden" name="patrolQualifiedTimes" id="patrolQualifiedTimes"  value="${patrolQualifiedTimes}"></input>
			</td>
			<td>不合格次数</td>
			<td>
				<span>${patrolUnqualifiedTimes}</span>
				<input type="hidden" name="patrolUnqualifiedTimes" id="patrolUnqualifiedTimes" value="${patrolUnqualifiedTimes}"></input>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
				<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
				<button  class='btn' type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			</td>
			<td colspan="5" id="showAttachmentFiles">
			</td>
		</tr>
		<tr>
			<td style="border-left:0px;"><span style="color:red">*</span>检验员</td>
			<td >
				<input name="inspector" id="inspector" value="${inspector}"  readonly="readonly" onclick="selectObj('选择检验人员','inspector','inspector')" class="{required:true,messages:{required:'检验员不能为空'}}"></input>
			</td>
			<td>
				审核员
			</td>
			<td colspan="3">
				<input name="auditMan" id="auditMan" value="${auditMan}"  readonly="readonly" onclick="selectObj('选择审核人员','auditMan','auditMan')"></input>
			</td>
		</tr>
	</table>
</form>
			