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
	         <td >供应商名称</td>
	         <td><input id="supplierName" name="supplierName" value="${supplierName}"/>  </td>
	         <td>供应商代表</td>
	         <td><input id="supplierDeputy" name="supplierDeputy" value="${supplierDeputy }"/></td>
	     </tr>
	         <tr> 
	         <td >评鉴日期</td>
	         <td><input id="evaluateDate" name="evaluateDate" isDate="true"  value="<s:date name='evaluateDate' format="yyyy-MM-dd"/>"/>  </td>
	         <td>评鉴人员</td>
	         <td><input id="evaluateMan" name="evaluateMan" value="${evaluateMan }"/>
	             <input id="evaluateManLog" name="evaluateManLog" value="${evaluateManLog }" type="hidden"/>
	         </td>
	     </tr>
	    <tr>
	        <td>不合格项</td>
	         <td colspan='3'><input id="unqualifiedItem" name="unqualifiedItem" value="${unqualifiedItem}" style="width:98%;"/></td>
	     </tr>
	     <tr>
	        <td>原因分析</td>
	         <td colspan='3'><input id="causeAnalysis" name="causeAnalysis" value="${causeAnalysis}" style="width:98%;"/></td>
	     </tr>
	     <tr>
	        <td>临时对策</td>
	         <td colspan='3'><input id="tempSituation" name="tempSituation" value="${tempSituation}" style="width:98%;"/></td>
	     </tr>
	     <tr>
	        <td rowspan='2'>长期对策</td>
	         <td colspan='3'><input id="longSituation" name="longSituation" value="${longSituation}" style="width:98%;"/></td>
	     </tr>
	     <tr>
	         <td colspan='3'><input isFile="true" type="hidden" id="situationFile" name="situationFile" value="${situationFile}" /></td>
	     </tr>
	      <tr>
	        <td>问题关闭确认</td>
	         <td colspan='3'><input id="closeCheck" name="closeCheck" value="${closeCheck}" style="width:98%;"/></td>
	     </tr>
	</tbody> 	 
</table>
