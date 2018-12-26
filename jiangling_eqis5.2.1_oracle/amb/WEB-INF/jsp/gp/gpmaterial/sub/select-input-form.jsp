<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="tabs-1">
	<table style="width:100%;margin: auto;" class="form-table-border-left">
<!-- 		<tr style="background-color: CornflowerBlue;color: white;font-size: 16px;font-weight: bold;">
	        <td></td>
	    </tr> -->
	    <tr>
	        <td class="form-table-border-left" style="border:0px;table-layout:fixed;">
	        	<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr >	
						<td  style="text-align: center;">序号</td>
	               		<td  style="text-align: center;">表单编号</td>
						<td  style="text-align: center;">供应商名称</td>
						<td  style="text-align: center;">供应商编码</td>
						<td  style="text-align: center;">物料名称</td>
						<td  style="text-align: center;">物料编码</td>
					</tr>
					<s:iterator value="_gpMaterialSubs" var="gpMaterialSubs" id="gpMaterialSubs" status="status">
	                <tr class="gpMaterialSubs">
	                    <td style="text-align:center;" rn=true></td>
	                    <td style="text-align: center;">
	                    	<input type="hidden" style="width: 66%;border:none;background: none;" type="text" placeholder="表单编号" fieldName="formNo" id="formNo" name="formNo" value="${gpMaterialSubs.gpMaterial.formNo}" title="${gpMaterialSubs.gpMaterial.formNo}"  />
	                    	<span ><a target='_blank' href='${gpctx}/gpmaterial/input.htm?id=${gpMaterialSubs.gpMaterial.id}'>${gpMaterialSubs.gpMaterial.formNo}</a></span>
	                    </td>
	                    <td  style="text-align: center;">
	                    	<input type="hidden" style="width: 66%;border:none;background: none;" type="text" placeholder="供应商名称" fieldName="supplierName" id="supplierName" name="supplierName" value="${gpMaterialSubs.gpMaterial.supplierName}" title="${gpMaterialSubs.gpMaterial.supplierName}" />
				        	<span>${gpMaterialSubs.gpMaterial.supplierName}</span>
				        </td>
				        <td  style="text-align: center;">
	                    	<input type="hidden" style="width: 66%;border:none;background: none;" type="text" placeholder="供应商编码" fieldName="supplierCode" id="supplierCode" name="supplierCode" value="${gpMaterialSubs.gpMaterial.supplierCode}" title="${gpMaterialSubs.gpMaterial.supplierCode}" />
				        	<span>${gpMaterialSubs.gpMaterial.supplierCode}</span>
				        </td>
	                    <td  style="text-align: center;">
	                    	<input type="hidden" style="width: 66%;border:none;background: none;" type="text" placeholder="物料名称" fieldName="materialName" id="materialName" name="materialName" value="${gpMaterialSubs.gpMaterial.materialName}" title="${gpMaterialSubs.gpMaterial.materialName}" />
				       		<span>${gpMaterialSubs.gpMaterial.materialName}</span>
				        </td>
				        <td  style="text-align: center;">
				        	<input type="hidden" style="width: 66%;border:none;background: none;" type="text" placeholder="物料编码" fieldName="materialCode" id="materialCode" name="materialCode" value="${gpMaterialSubs.gpMaterial.materialCode}" title="${gpMaterialSubs.gpMaterial.materialCode}" />
	                    	<span>${gpMaterialSubs.gpMaterial.materialCode}</span>
	                    </td> 
	                </tr>
	                </s:iterator>
	            </table>
	        </td>
	    </tr>
	</table>
</div>