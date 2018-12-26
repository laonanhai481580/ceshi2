<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="tabs-1">
	<table style="width:100%;margin: auto;" class="form-table-border-left">
<!-- 		<tr style="background-color: CornflowerBlue;color: white;font-size: 16px;font-weight: bold;">
	        <td></td>
	    </tr> -->
	    <tr>
	        <td style="padding:0px;">
	        	<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr >	
						<td  style="text-align: center;">序号</td>
						<td  style="text-align: center;">设备名称</td>
						<td  style="text-align: center;">规格型号</td>
						<td  style="text-align: center;">厂商</td>
						<td  style="text-align: center;">出厂编号</td>
						<td  style="text-align: center;">保管人</td>
						<td  style="text-align: center;">仪器管理编号</td>
						<td  style="text-align: center;">备注</td>
					</tr>
	                <s:iterator value="_newEquipmentSublists" var="newEquipmentSublist" id="newEquipmentSublist" status="status">
	                <tr class="newEquipmentSublists">
	                    <td style="text-align:center;" rn=true></td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="设备名称" fieldName="deviceName" id="deviceName" name="deviceName" value="${newEquipmentSublist.deviceName}" title="${newEquipmentSublist.deviceName}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="规格型号" fieldName="modelSpecification" id="modelSpecification" name="modelSpecification" value="${newEquipmentSublist.modelSpecification}" title="${newEquipmentSublist.modelSpecification}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="厂商" fieldName="manufacturer" id="manufacturer" name="manufacturer" value="${newEquipmentSublist.manufacturer}" title="${newEquipmentSublist.manufacturer}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="出厂编号" fieldName="factoryNumber" id="factoryNumber" name="factoryNumber" value="${newEquipmentSublist.factoryNumber}" title="${newEquipmentSublist.factoryNumber}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="保管人" fieldName="preserver" id="preserver" name="preserver" value="${newEquipmentSublist.preserver}" title="${newEquipmentSublist.preserver}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="仪器管理编号" fieldName="nstrumentNumber" id="nstrumentNumber" name="nstrumentNumber" value="${newEquipmentSublist.nstrumentNumber}" title="${newEquipmentSublist.nstrumentNumber}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="备注" fieldName="remark" id="remark" name="remark" value="${newEquipmentSublist.remark}" title="${newEquipmentSublist.remark}" />
	                    </td>
	                    	        				        				        				  
	                </tr>
	                </s:iterator>
	            </table>
	        </td>
	    </tr>
	</table>
</div>