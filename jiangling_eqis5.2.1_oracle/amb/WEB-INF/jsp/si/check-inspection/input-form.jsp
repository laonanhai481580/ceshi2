<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 24px;">SI检验报告</caption>
		<input type="hidden" name= "zibiao" id="zibiao" value=""/>
		<input type="hidden" name="id" id="id" value="${id}" />
		<input type="hidden" name="canEdit" id="canEdit" value="${canEdit}" />
			<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:10%">事业部</td>
			<td>
				<s:select list="businessUnits" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  name="businessUnitCode"></s:select>
			</td>
			<td style="width:10%">检验日期<span style="color:red">*</span></td>
			<td style="width:25%">
				<input name="inspectionDate" isDate=true id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:'必填'}}" ></input>
			</td>
			<td style="width:10%">客户型号</td>
			<td>
				<input id="customerModel" name="customerModel" value="${customerModel}" />
			</td>
		</tr>
		<tr>
			<td style="width:10%">机型<span style="color:red">*</span></td>
			<td>
				<input id="machineType" name="machineType" value="${machineType}" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td style="width:10%">客户<span style="color:red">*</span></td>
			<td>
				<input id="customer" name="customer" value="${customer}" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td style="width:10%">批次号</td>
			<td style="">
				<input id="batchNo" name="batchNo" value="${batchNo}" />
			</td>
		</tr>
		<tr>
			<td style="width:10%">投入数<span style="color:red">*</span></td>
			<td>
				<input id="stockAmount" name="stockAmount" value="${stockAmount}" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td style="width:10%">检验Lot数<span style="color:red">*</span></td>
			<td>
				<input id="inspectionLotAmount" name="inspectionLotAmount" value="${inspectionLotAmount}" class="{required:true,messages:{required:'必填'}}" onchange="lrrChange();"/>
			</td>
			<td style="width:10%">Pass Lot数<span style="color:red">*</span></td>
			<td style="">
				<input id="passLotAmount" name="passLotAmount" value="${passLotAmount}" class="{required:true,messages:{required:'必填'}}" onchange="lrrChange();"/>
			</td>								
		</tr>
		<tr>
			<td style="width:10%">Reject Lot数	</td>
			<td>
				<input id="rejectLotAmount" name="rejectLotAmount" value="${rejectLotAmount}" readonly="readonly" />
			</td>
			<td style="width:10%">批退率</td>
			<td>
				<input id="lrrRate" name="lrrRate" value="${lrrRate}" readonly="readonly" />%
			</td>
			<td style="width:10%">检验结果</td>
			<td style="">
				<input id="inspectionConclusion" name="inspectionConclusion" value="${inspectionConclusion}" readonly="readonly" />
			</td>																
		</tr>								
		<tr>
			<td colspan="6" style="padding: 0px;" >
			<div style="overflow-x: scroll; overflow-y: hidden; overflow: auto; ">
				<table class="form-table-border-left" style="border: 0px; table-layout: fixed;">
					 <tr>
					 <td style="text-align:center;width:8%;">操作</td>
					 <td style="text-align:center;width:8%;">序号</td>
					 <td style="text-align:center;width:12%;">不良分类</td>
					 <td style="text-align:center;width:12%;">不良项目</td>
					 <td style="text-align:center;width:12%;">不良数量</td>
					 <td style="text-align:center;width:12%;">标准</td>
					 <td style="text-align:center;width:12%;">实际测量值</td>
					 <td style="text-align:center;width:12%;">S/N号</td>
					 <td style="text-align:center;">备注</td>
				 </tr>
			 <s:iterator value="_siCheckItems" var="items" id="items" status="ss">
				 <tr name="siCheckItems" zbtr1=true>
					 <td style="text-align:center;">
						<a id="aBtn" class="small-button-bg" addBtn="true" zba=true style="margin-left:2px;" onclick="addRowHtml(this)" href="#"  title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
						<a id="aBtn" class="small-button-bg" delBtn="true" zba=true style="margin-left:2px;" onclick="removeRowHtml(this)" href="#" title="删除"><span  class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					</td> 
					<td style="background:#E8F2FE;border-top:0px;text-align: center;" target='rowNum'>
						<s:property value="#ss.count"/>
						<input style="width:90%;" type="hidden"  id="id_<s:property value="#ss.count"/>" value="${items.id}"  />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="defectionTypeName" id="defectionTypeName_<s:property value="#ss.count"/>" value="${defectionTypeName}"  readonly="readonly" onclick="defectionCodeClick(this);"/>		
						<input type="hidden"  name="defectionTypeNo" id="defectionTypeNo_<s:property value="#ss.count"/>" value="${defectionTypeNo}" />			
					</td>										
					<td style="background:#E8F2FE;border-top:0px;text-align: center;">
						<input style="width:90%;" type="text"  name="defectionCodeName" id="defectionCodeName_<s:property value="#ss.count"/>" value="${defectionCodeName}"   readonly="readonly" onclick="defectionCodeClick(this);"/>
						<input type="hidden"  name="defectionCodeNo" id="defectionCodeNo_<s:property value="#ss.count"/>" value="${defectionCodeNo}" />	
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="value" id="value_<s:property value="#ss.count"/>" value="${value}" class="{number:true,messages:{number:'必须为数字'}}" onchange="unAmountChange();"/>
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="standard" id="standard_<s:property value="#ss.count"/>" value="${standard}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  id="actualValue_<s:property value="#ss.count"/>"  name="actualValue" value="${actualValue}" />
					</td>
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  id="snCode_<s:property value="#ss.count"/>"  name="snCode" value="${snCode}" />
					</td>										
					<td style="background:#E8F2FE;border-top:0px;">
						<input style="width:90%;"  name="remark" id="remark_<s:property value="#ss.count"/>" value="${remark}" />
					</td>
				</tr>
			</s:iterator>	
			</table></div>
			</td>
		</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">							
		<tr>
			<td>
				<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
				<button  class='btn' type="button" onclick="uploadFiles('showAttachmentFiles','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			</td>
			<td colspan="7" id="showAttachmentFiles">
			</td>
		</tr>
		<tr>
			<td style="width:10%">外观检验数<span style="color:red">*</span></td>
			<td>
				<input id="appearanceInspectionAmount" name="appearanceInspectionAmount" value="${appearanceInspectionAmount}" class="{required:true,messages:{required:'必填'}}"  onchange="unRateChange();" />
			</td>		
			<td style="width:10%">外观不良数</td>
			<!-- style="border:none;text-align: center;background: none;" -->
			<td>
				<input id="appearanceUnAmount" name="appearanceUnAmount" value="${appearanceUnAmount}" class="{number:true}" readonly="readonly" />
			</td>
			<td style="width:10%">不良率</td>
			<td>
				<input id="appearanceAmountRate" name="appearanceAmountRate" value="${appearanceAmountRate}"  readonly="readonly"/>%
			</td>			
			<td style="width:10%">外观判定</td>
			<td>
				<s:select list="conclusions" 
					  listKey="value" 
					  listValue="value"
					  theme="simple"
					  cssStyle="{required:true}"
					  name="appearanceConclusion"
					  id="appearanceConclusion"
					  onchange="conclusionChange(this)"></s:select>
			</td>
			<td>检验员</td>
			<td><input id="appearanceMan" name="appearanceMan" value="${appearanceMan}" /></td>			
		</tr>
		<tr>
			<td style="width:10%">尺寸检验数<span style="color:red">*</span></td>
			<td>
				<input id="sizeInspectionAmount" name="sizeInspectionAmount" value="${sizeInspectionAmount}" class="{required:true,messages:{required:'必填'}}" onchange="unRateChange();"/>
			</td>			
		    <td style="width:10%">尺寸不良数</td>
			<td>
				<input id="sizeUnAmount" name="sizeUnAmount" value="${sizeUnAmount}" class="{number:true}" readonly="readonly"/>
			</td>
			<td style="width:10%">不良率</td>
			<td>
				<input id="sizeAmountRate" name="sizeAmountRate" value="${sizeAmountRate}"  readonly="readonly"/>%
			</td>			
			<td style="width:10%">尺寸判定</td>
			<td>
				<s:select list="conclusions" 
					  listKey="value" 
					  listValue="value"
					  theme="simple"
					  cssStyle="{required:true}"
					  name="sizeConclusion"
					  id="sizeConclusion"
					  onchange="conclusionChange(this)"></s:select>
			</td>	
			<td>检验员</td>
			<td><input id="sizeMan" name="sizeMan" value="${sizeMan}" /></td>	
		</tr>
		<tr>
			<td style="width:10%">功能检验数<span style="color:red">*</span></td>
			<td>
				<input id="functionInspectionAmount" name="functionInspectionAmount" value="${functionInspectionAmount}" class="{required:true,messages:{required:'必填'}}" onchange="unRateChange();"/>
			</td>		
		   	<td style="width:10%">功能不良数</td>
			<td style="">
				<input id="functionUnAmount" name="functionUnAmount" value="${functionUnAmount}"class="{number:true}" readonly="readonly"/>
			</td>
			<td style="width:10%">不良率</td>
			<td>
				<input id="functionAmountRate" name="functionAmountRate" value="${functionAmountRate}"  readonly="readonly"/>%
			</td>				
			<td style="width:10%">功能判定</td>
			<td>
				<s:select list="conclusions" 
					  listKey="value" 
					  listValue="value"
					  theme="simple"
					  cssStyle="{required:true}"
					  name="functionConclusion"
					  id="functionConclusion"
					  onchange="conclusionChange(this)"></s:select>									
			</td>
			<td>检验员</td>
			<td><input id="functionMan" name="functionMan" value="${functionMan}" /></td>	
		</tr>						
	</table>