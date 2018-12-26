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
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-tabl">
	<tbody>
	   <tr>
	   <td rowspan='2' colspan='6'>
	   <input id="id" name="id" value='${id }' type="hidden"/>
	   <input  name="defectiveInfo" id="defectiveInfo" type="hidden"/>
	           <table class="form-table-border-left" style="width:100%;margin: auto;text-align:center;border:0px" id="defectiveInfoTable" >
	         <tbody>
				<%int bcount5=0; %>
				      <tr>
				        <td style="margin-left:2px;border:0px"></td>
				        <td style="margin-left:2px;border:0px"></td>
				         <td style="margin-left:1px;border:0px;text-align:center;">不良名称</td>
	                    <td style="margin-left:1px;border:0px;text-align:center;">不良代码</td>
	                    <td style="margin-left:1px;border:0px;text-align:center;">不良数量</td>
				        <td style="margin-left:1px;border:0px;text-align:center;">不良类别</td>
<!-- 	                    <td style="margin-left:1px;border:0px;text-align:center;">类别代码</td> -->
				      </tr>
				     <s:iterator value="oqcDefectiveItems" var="ingredient5" id="oqcDefectiveItems" status="st1">
						<tr class="oqcDefectiveItemsTr" name="oqcDefectiveItemsTr" >
								   <td style="text-align:left;border:0px;">
								     <a class="small-button-bg"  onclick="addRowHtml('oqcDefectiveItemsTotal','oqcDefectiveItemsTr',this)" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
									 <a class="small-button-bg" style="margin-left:0px;" onclick="removeRowHtml('oqcDefectiveItemsTotal','oqcDefectiveItemsTr',this)" title="删除"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
								   </td>
								   <td style="border-bottom:0px;text-align:center;border:0px;" ><%=bcount5+1%></td>
								      <td style="text-align:center;border:0px;">
								      <input type="text" style="width:80%;"  name="defectionCodeName" id="defectionCodeName_<%=bcount5%>" value="${defectionCodeName }"  /></td>
								   <td style="text-align:center;border:0px;">
								   <input type="text" style="width:80%;float:left;"  name=defectionCodeNo id="defectionCodeNo_<%=bcount5%>" value="${defectionCodeNo }"  />
								   </td>
								   <td style="text-align:center;border:0px;">
								   <input type="text" style="width:80%;float:left;"  name="defectionCodeValue" id="defectionCodeValue_<%=bcount5%>" value="${defectionCodeValue}"  />
								   </td>
								   <td style="text-align:center;border:0px;">
								       <input type="text" style="width:80%;float:left;"  name="defectionTypeName" id="defectionTypeName_<%=bcount5%>" value="${defectionTypeName}"  />
								   </td>
<!-- 								   <td style="text-align:center;border:0px;"> -->
<%-- 								   <input type="text" style="width:80%;float:left;"  name="defectionTypeCode" id="defectionTypeCode_<%=bcount5%>" value="${defectionTypeCode }"  /> --%>
<!-- 								   </td> -->
						</tr>
						<% bcount5++; %>
					</s:iterator>
				   <input type="hidden" name="oqcDefectiveItemsTotal" id="oqcDefectiveItemsTotal" value="<%=bcount5%>"/>
				</tbody>
	         </table>
	      </td>
	   
	   </tr>
	</tbody> 	 
</table>
