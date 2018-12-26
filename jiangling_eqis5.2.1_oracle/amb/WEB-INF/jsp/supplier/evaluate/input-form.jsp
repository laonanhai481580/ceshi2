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
	         <td>事业群</td>
	         <td><input id="businessUnitName" name="businessUnitName" value="${businessUnitName }"/></td>
	         <td>供应商分类</td>
	         <td>
	              <s:select list="supplierTypes" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="supplierType"
				  id="supplierType"
				  emptyOption="false"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	          </td>
	         <td>紧急程度</td>
	         <td>
	             <s:select list="urgencyLevels" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="urgencyLevel"
				  id="urgencyLevel"
				  emptyOption="false"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	     </tr>
	     <tr>
	         <td>制单人</td>
	         <td><input id="formCreator" name="formCreator" value="${formCreator}"/></td>
	         <td>制单日期</td>
	         <td><input id="formCreateDate" name="formCreateDate" isDate="true" value="<s:date name='formCreateDate' format="yyyy-MM-dd"/>"/></td>
	         <td>表单编号</td>
	         <td>${formNo }</td>
	     </tr>
	     <tr>
	         <td>部门</td>
	         <td><input id="department" name="department" value="${department }"/></td>
	         <td>公司</td>
	         <td><input id="company" name="company" value="${company }"/></td>
	         <td>评价日期</td>
	         <td><input id="evaluateDate" name="evaluateDate" isDate="true" value="<s:date name='evaluateDate' format="yyyy-MM-dd"/>"/></td>
	     </tr>
	     <tr>
	         <td>供应商名称</td>
	         <td colspan="5"><input id="supplierName" style="width:98%;" name="supplierName" value="${supplierName }"/></td>
	     </tr>
	     <tr>
	         <td>提供产品/服务范围</td>
	         <td colspan="5"><input id="supplierServices" style="width:98%;" name="supplierServices" value="${supplierServices }"/></td>
	     </tr>
	     <tr>
	         <td>公司地址</td>
	         <td colspan="5"><input id="supplierCompanyAddress" style="width:98%;" name="supplierCompanyAddress" value="${supplierCompanyAddress }"/></td>
	     </tr>
	     <tr>
	         <td>营业执照号码</td>
	         <td><input id="businessLicenseCode" name="businessLicenseCode" value="${businessLicenseCode }"/></td>
	         <td>法人代表</td>
	         <td><input id="legalRepresentative" name="legalRepresentative" value="${legalRepresentative }"/></td>
	         <td>业务负责人/电话</td>
	         <td><input id="supportTelephone" name="supportTelephone" value="${supportTelephone }"/></td>
	     </tr>
	     <tr>
	         <td>品质负责人/电话</td>
	         <td><input id="qualitySupportTelephone" name="qualitySupportTelephone" value="${qualitySupportTelephone }"/></td>
	         <td>公司电话</td>
	         <td><input id="companyTelephone" name="companyTelephone" value="${companyTelephone }"/></td>
	         <td>传真</td>
	         <td><input id="companyFax" name="companyFax" value="${companyFax }"/></td>
	     </tr>
	     <tr>
	         <td>备注</td>
	         <td colspan="5"><input id="remark" style="width:98%;" name="remark" value="${remark }"/></td>
	     </tr>
	     <tr>
	         <td>基本信息</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="baseInfoFile" name="baseInfoFile" value="${baseInfoFile}"/></td>
	     </tr>
	     <tr>
	         <td>三证</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="threePapersFile" name="threePapersFile" value="${threePapersFile }"/></td>
	     </tr>
	     <tr>
	         <td>廉洁协议</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="integrityAgreementFile" name="integrityAgreementFile" value="${integrityAgreementFile }"/></td>
	     </tr>
	     <tr>
	         <td>样品评估表</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="sampleEvaluateFile" name="sampleEvaluateFile" value="${sampleEvaluateFile }"/></td>
	     </tr>
	     <tr>
	         <td>审厂资料</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="factoryAuditFile" name="factoryAuditFile" value="${factoryAuditFile }"/></td>
	     </tr>
	     <tr>
	         <td>采购框架协议</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="purchasingFile" name="purchasingFile" value="${purchasingFile }"/></td>
	     </tr>
	     <tr>
	         <td>供应商竞争力分析表</td>
	         <td colspan="5"><input type="hidden" isFile="true" id="supplierAnalyzeFile" name="supplierAnalyzeFile" value="${supplierAnalyzeFile }"/></td>
	     </tr>
	      <tr>
	         <td>采购中心审核</td>
	         <td>
	             <input  style="float: left;"  hiddenInputId="purchaseCheckerLog" isUser="true" id="purchaseChecker" name="purchaseChecker" value="${purchaseChecker }"/>
	             <input type="hidden" name="purchaseCheckerLog" id="purchaseCheckerLog"  value="${purchaseCheckerLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseChecker','purchaseCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	             <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>采购中心评价</td>
	         <td><input id="purchaseEvaluate" name="purchaseEvaluate" value="${purchaseEvaluate }"/></td>
	         <td>采购中心部门长</td>
	         <td>
	         <input style="float: left;"  hiddenInputId="bossPurchaseLog" isUser="true" id="bossPurchase" name="bossPurchase" value="${bossPurchase }"/>
	         <input type="hidden" name="bossPurchaseLog" id="bossPurchaseLog"  value="${bossPurchaseLog}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossPurchase','bossPurchaseLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	     </tr>
	      <tr>
	         <td>工程研发部评价人</td>
	         <td>
		           <input style="float: left;" isUser="true"  hiddenInputId="developCheckerLog" id="developChecker" name="developChecker" value="${developChecker }"/>
		          <input type="hidden" name="developCheckerLog" id="developCheckerLog"  value="${developCheckerLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('developChecker','developCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>工程研发部审核方式</td>
	         <td><input id="developAuditType" name="developAuditType" value="${developAuditType }"/></td>
	         <td>工程研发部审核等级</td>
	         <td><input id="developAuditLevel" name="developAuditLevel" value="${developAuditLevel }"/></td>
	     </tr>
	      <tr>
	         <td>工程研发部部门长</td>
	         <td>
		         <input style="float: left;" isUser="true" hiddenInputId="bossDevelopCheckerLog"  id="bossDevelopChecker" name="bossDevelopChecker" value="${bossDevelopChecker }"/>
		          <input type="hidden" name="bossDevelopCheckerLog" id="bossDevelopCheckerLog"  value="${bossDevelopCheckerLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossDevelopChecker','bossDevelopCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>QPA分数</td>
	         <td><input id="developQpaScore" name="developQpaScore" value="${developQpaScore }"/></td>
	         <td>QSA分数</td>
	         <td><input id="developQsaScore" name="developQsaScore" value="${developQsaScore }"/></td>
	     </tr>
	      <tr>
	         <td>品质中心QS评价人</td>
	         <td>
	             <input style="float: left;" isUser="true"  hiddenInputId="qualityQsCheckerLog"  id="qualityQsChecker" name="qualityQsChecker" value="${qualityQsChecker }"/>
	             <input type="hidden" name="qualityQsCheckerLog" id="qualityQsCheckerLog"  value="${qualityQsCheckerLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qualityQsChecker','qualityQsCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>品质中心QS审核方式</td>
	         <td><input id="qualityQsAuditType" name="qualityQsAuditType" value="${qualityQsAuditType }"/></td>
	         <td>品质中心QS审核等级</td>
	         <td><input id="qualityQsAuditLevel" name="qualityQsAuditLevel" value="${qualityQsAuditLevel }"/></td>
	     </tr>
	       <tr>
	         <td>品质中心QS部门长审核</td>
	         <td colspan="3">
	             <input style="float: left;" isUser="true"  hiddenInputId="bossQsQualityLog"  id="bossQsQuality" name="bossQsQuality" value="${bossQsQuality }"/>
	             <input type="hidden" name="bossQsQualityLog" id="bossQsQualityLog"  value="${bossQsQualityLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossQsQuality','bossQsQualityLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>CSR分数</td>
	         <td><input id="csrScore" name="csrScore" value="${csrScore }"/></td>
	     </tr>
	     <tr>
	         <td>品质中心SQE评价人</td>
	         <td>
	             <input style="float: left;"  isUser="true" hiddenInputId="qualitySqeCheckerLog"   id="qualitySqeChecker" name="qualitySqeChecker" value="${qualitySqeChecker }"/>
	             <input type="hidden" name="qualitySqeCheckerLog" id="qualitySqeCheckerLog"  value="${qualitySqeCheckerLog}" />
	              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qualitySqeChecker','qualitySqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	              <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>品质中心SQE审核方式</td>
	         <td><input id="qualitySqeAuditType" name="qualitySqeAuditType" value="${qualitySqeAuditType }"/></td>
	         <td>品质中心SQ审核等级</td>
	         <td><input id="qualitySqeAuditLevel" name="qualitySqeAuditLevel" value="${qualitySqeAuditLevel }"/></td>
	     </tr>
	     <tr>
	         <td>品质中心SQE部门长审批</td>
	         <td colspan="3">
	            <input style="float: left;"  isUser="true" hiddenInputId="bossSqeQualityLog" id="bossSqeQuality" name="bossSqeQuality" value="${bossSqeQuality }"/>
	            <input type="hidden" name="bossSqeQualityLog" id="bossSqeQualityLog"  value="${bossSqeQualityLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossSqeQuality','bossSqeQualityLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>QPA分数</td>
	         <td><input id="qualityQpaScore" name="qualityQpaScore" value="${qualityQpaScore }"/></td>
	     </tr>
	      <tr>
	         <td>采购管理部评价人</td>
	         <td>
	             <input style="float: left;" isUser="true"  hiddenInputId="purManagerCheckerLog" id="purchaseManageChecker" name="purchaseManageChecker" value="${purchaseManageChecker }"/>
	             <input type="hidden" name="purManagerCheckerLog" id="purManagerCheckerLog"  value="${purManagerCheckerLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseManageChecker','purManagerCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>采购管理部审核方式</td>
	         <td><input id="purchaseManageType" name="purchaseManageType" value="${purchaseManageType }"/></td>
	         <td>采购管理部审核等级</td>
	         <td><input id="purchaseManageLevel" name="purchaseManageLevel" value="${purchaseManageLevel }"/></td>
	     </tr>
	     <tr>
	         <td>采购管理部(经理或以上)</td>
	         <td colspan="3">
	              <input style="float: left;" isUser="true"  hiddenInputId="purManagerLog"  id="purchaseManageManager" name="purchaseManageManager" value="${purchaseManageManager }"/>
	              <input type="hidden" name="purManagerLog" id="purManagerLog"  value="${purManagerLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('purchaseManageManager','purManagerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td>QSA分数</td>
	         <td><input id="purchaseQsaScore" name="purchaseQsaScore" value="${purchaseQsaScore }"/></td>
	     </tr>
	      <tr>
	         <td>品质中心部门长审批 </td>
	         <td >
	              <input style="float: left;" isUser="true"  hiddenInputId="bossQualityLog" id="bossQuality" name="bossQuality" value="${bossQuality }"/>
	              <input type="hidden" name="bossQualityLog" id="bossQualityLog"  value="${bossQualityLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossQuality','bossQualityLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	          <td>采购管理部部门长</td>
	         <td >
	             <input style="float: left;"  isUser="true" hiddenInputId="bossPurchaseManageLog" id="bossPurchaseManage" name="bossPurchaseManage" value="${bossPurchaseManage }"/>
	             <input type="hidden" name="bossPurchaseManageLog" id="bossPurchaseManageLog"  value="${bossPurchaseManageLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossPurchaseManage','bossPurchaseManageLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	          </td>
	          <td>集团COO</td>
	         <td >
	              <input  style="float: left;"  isUser="true" hiddenInputId="bossCooLog" id="bossCoo" name="bossCoo" value="${bossCoo }"/>
	              <input type="hidden" name="bossCooLog" id="bossCooLog"  value="${bossCooLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('bossCoo','bossCooLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	     </tr>
	      <tr>
	         <td>集团总经理</td>
	         <td >
	            <input style="float: left;" isUser="true"  hiddenInputId="managerLog" id="manager" name="manager" value="${manager }"/>
	            <input type="hidden" name="managerLog" id="managerLog"  value="${managerLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('manager','managerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a> 
	           </td>
	          <td>集团高级副总裁</td>
	         <td >
	            <input style="float: left;" isUser="true" hiddenInputId="superManagerLog"  id="superManager" name="superManager" value="${superManager }" />
	            <input type="hidden" name="superManagerLog" id="superManagerLog"  value="${superManagerLog}" />
	            <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('superManager','superManagerLog')" href="javascript:void(0);" title="<s:text name='清空'/>">
	            <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	          </td>
	         <td></td>
	         <td></td>
	     </tr>
	</tbody> 	 
</table>
