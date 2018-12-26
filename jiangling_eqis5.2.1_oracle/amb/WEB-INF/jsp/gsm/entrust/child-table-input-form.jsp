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
						<td  style="text-align: center;">管理编号</td>
						<td  style="text-align: center;">设备名称</td>
						<td  style="text-align: center;">规格型号</td>
						<td  style="text-align: center;">安装地点</td>
						<td  style="text-align: center;">责任人</td>
						<td  style="text-align: center;">测量范围</td>
						<td  style="text-align: center;">制造商</td>
						<td  style="text-align: center;">机身编号</td>
						<td  style="text-align: center;">校准日期</td>
						<td  style="text-align: center;">校准方式</td>
						<td  style="text-align: center;">使用参数标准</td>
						<td  style="text-align: center;">状态</td>
						<td  style="text-align: center;">确认人</td>
					</tr>
	                <s:iterator value="_entrustSublists" var="entrustSublist" id="entrustSublist" status="status">
	                <tr class="entrustSublists">
	                    <td style="text-align:center;" rn=true></td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="管理编号" fieldName="managerAssets" id="managerAssets" name="managerAssets" value="${entrustSublist.managerAssets}" title="${entrustSublist.managerAssets}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="设备名称" fieldName="equipmentName" id="deviceName" name="equipmentName" value="${entrustSublist.equipmentName}" title="${entrustSublist.equipmentName}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="规格型号" fieldName="equipmentModel" id="equipmentModel" name="equipmentModel" value="${entrustSublist.equipmentModel}" title="${entrustSublist.equipmentModel}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="安装地点" fieldName="address" id="address" name="address" value="${entrustSublist.address}" title="${entrustSublist.address}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="责任人" fieldName="dutyMan" id="dutyMan" name="dutyMan" value="${entrustSublist.dutyMan}" title="${entrustSublist.dutyMan}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="测量范围" fieldName="measuringRange" id="measuringRange" name="measuringRange" value="${entrustSublist.measuringRange}" title="${entrustSublist.measuringRange}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="制造商" fieldName="manufacturer" id="manufacturer" name="manufacturer" value="${entrustSublist.manufacturer}" title="${entrustSublist.manufacturer}" />
	                    </td>
	                    <td style="text-align: center;">
	                    	<input  style="width: 66%;" type="text" placeholder="机身编号" fieldName="factoryNumber" id="factoryNumber" name="factoryNumber" value="${entrustSublist.factoryNumber}" title="${entrustSublist.factoryNumber}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="校准日期" fieldName="calibrationDate" id="calibrationDate" name="calibrationDate" value="${entrustSublist.calibrationDate}" title="${entrustSublist.calibrationDate}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="校准方式" fieldName="checkMethod" id="checkMethod" name="checkMethod" value="${entrustSublist.checkMethod}" title="${entrustSublist.checkMethod}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="使用参数标准" fieldName="parameterStandard" id="parameterStandard" name="parameterStandard" value="${entrustSublist.parameterStandard}" title="${entrustSublist.parameterStandard}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="状态" fieldName="measurementState" id="measurementState" name="measurementState" value="${entrustSublist.measurementState}" title="${entrustSublist.measurementState}" />
				        </td>
				        <td style="text-align: center;">
				        	<input  style="width: 66%;" type="text" placeholder="确认人" fieldName="confirmor" id="confirmor" name="confirmor" value="${entrustSublist.confirmor}" title="${entrustSublist.confirmor}" />
				        </td>				        				        				        				  
	                </tr>
	                </s:iterator>
	            </table>
	        </td>
	    </tr>
	</table>
</div>