<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ include file="/common/taglibs.jsp"%>
<table width="200" border="1" >
  <tr>
    <td>日期</td>
    <s:iterator value="categorieslist" > 
    <td> <s:property/></td>
    </s:iterator>
  </tr>
  <tr>
    <td>检查批数</td>
   <s:iterator value="data1" > 
    <td> <s:property/></td>
    </s:iterator>
  </tr>
  <tr>
    <td>合格批数</td>
    <s:iterator value="data2" > 
    <td> <s:property/></td>
    </s:iterator>
  </tr>
  <tr>
    <td>合格率</td>
     <s:iterator value="data3" > 
    <td> <s:property/></td>
    </s:iterator>
    </tr>
</table>
