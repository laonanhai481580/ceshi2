<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="tabs-1">
	<table style="width:100%;margin: auto;" class="form-table-border-left">
<!-- 		<tr style="background-color: CornflowerBlue;color: white;font-size: 16px;font-weight: bold;">
	        <td></td>
	    </tr> -->
	    <tr>
	        <td style="padding:0px;">
	        	<table class="form-table-border-left" style="border: 0px; width: 100%;">
				<tr>
						<td  style="text-align: center; border-top: 0px; border-left: 0px;width:10%;">操作</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:30%;">设备名称</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:30%;">仪器名称</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:30%;">规格型号</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:30%;">品牌</td>
						<td  style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;width:30%;">备注</td>
					
					</tr>
					 <s:iterator value="_borrowRecordSublists" var="borrowRecordSublist" id="borrowRecordSublist" status="status">
						<tr class="borrowRecordSublists">
							<td style="text-align:center;" rn=true></td>
							<td style="text-align: center;">
		                    	<input  style="width: 66%;" type="text" placeholder="仪器管理编号" fieldName="managerAssets" id="managerAssets" name="managerAssets" value="${borrowRecordSublist.managerAssets}" title="${borrowRecordSublist.managerAssets}" />
		                    </td>
		                    <td style="text-align: center;">
		                    	<input  style="width: 66%;" type="text" placeholder="仪器名称" fieldName="equipmentName" id="equipmentName" name="equipmentName" value="${borrowRecordSublist.equipmentName}" title="${borrowRecordSublist.equipmentName}" />
					        </td>
					         <td style="text-align: center;">
		                    	<input  style="width: 66%;" type="text" placeholder="规格型号" fieldName="equipmentModel" id="equipmentModel" name="equipmentModel" value="${borrowRecordSublist.equipmentModel}" title="${borrowRecordSublist.equipmentModel}" />
					        </td>
					         <td style="text-align: center;">
		                    	<input  style="width: 66%;" type="text" placeholder="品牌" fieldName="brand" id="brand" name="brand" value="${borrowRecordSublist.brand}" title="${borrowRecordSublist.brand}" />
					        </td>
					        <td style="text-align: center;">
					        	<input  style="width: 66%;" type="text" placeholder="备注" fieldName="remark" id="remark" name="remark" value="${borrowRecordSublist.remark}" title="${borrowRecordSublist.remark}" />
		                    </td>
							
						</tr>
					</s:iterator>
			</table>
	        </td>
	    </tr>
	</table>
</div>