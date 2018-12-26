package com.ambition.product;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.norteksoft.product.util.PropUtils;

public class SubSystemEntranceRedirectFilter implements Filter {
	
	public void destroy() { }

	public void doFilter(ServletRequest req, ServletResponse rep,
			FilterChain chan) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) rep;
//		String userName = request.getParameter("userName");
//		System.out.println("userName=====================" + userName + "======");
//		String name1 = new String(userName.getBytes("ISO-8859-1"),"UTF-8");
//		System.out.println("afterUserName-ISO-8859-1=====================" + name1 + "======");
//		String name2 = new String(userName.getBytes("GBK"),"UTF-8");
//		System.out.println("afterUserName-GBK=====================" + name2 + "======");
//		String name4 = new String(userName.getBytes("ISO-8859-1"),"GBK");
//		System.out.println("afterUserName-ISO-8859-1 TO GBK=====================" + name4 + "======");
//		String name3 = new String(userName.getBytes("gb2312"),"UTF-8");
//		System.out.println("afterUserName-gb2312 TO UTF-8=====================" + name3 + "======");
//		String name5 = new String(userName.getBytes("UTF-8"),"GBK");
//		System.out.println("afterUserName-UTF-8 TO GBK=====================" + name5 + "======");
//		String name6 = new String(userName.getBytes("UTF-8"),"ISO-8859-1");
//		System.out.println("afterUserName-UTF-8 TO ISO-8859-1=====================" + name6 + "======");
//		String name7 = new String(userName.getBytes("Unicode"),"UTF-8");
//		System.out.println("afterUserName-Unicode TO UTF-8=====================" + name7 + "======");
//		String name8 = new String(new String(userName.getBytes("UTF-8"),"ISO-8859-1").getBytes("ISO-8859-1"),"GBK");
//		System.out.println("afterUserName-UTF-8 TO ISO-8859-1 to GBK=====================" + name8 + "======");
//		String name9 = flex.messaging.util.URLDecoder.decode(userName,"GBK");
//		System.out.println("afterUserName-Decoder TO GBK=====================" + name9 + "======");
//		String contentType = request.getContentType();
//		System.out.println("afterUserName-contentType:=====================" + contentType + "======");
//		String getCharacterEncoding = request.getCharacterEncoding();
//		System.out.println("afterUserName-getCharacterEncoding:=====================" + getCharacterEncoding + "======");
//		String getRequestURI = request.getRequestURI();
//		System.out.println("afterUserName-getRequestURI:=====================" + getRequestURI + "======");
//		String getQueryString = request.getQueryString();
//		System.out.println("afterUserName-getQueryString:=====================" + getQueryString + "======");
//		String getQueryString1 = new String(getQueryString.getBytes("UTF-8"),"gb2312");
//		System.out.println("afterUserName-getQueryString1:=====================" + getQueryString1 + "======");
//		String name10 = new String(userName.getBytes(),"gb2312");
//		System.out.println("afterUserName-getQueryString1:=====================" + name10 + "======");
//		String name11 = new String(userName.getBytes(),"iso-8859-1");
//		System.out.println("afterUserName-name11:=====================" + name11 + "======");
//		userName = new String(userName.getBytes("ISO-8859-1"),"UTF-8");
		//解决SWFUPLOAD上传组件在非IE浏览器使用时报302错误的BUG
		String requestJSessionId = request.getParameter("JSESSIONID");
		if(StringUtils.isNotEmpty(requestJSessionId)){
			Cookie[] cookies = request.getCookies();
			Cookie jssionCookie = null;
			if(cookies != null){
				for(Cookie cookie : cookies){
					if("JSESSIONID".equals(cookie.getName())){
						cookie.setValue(requestJSessionId);
						jssionCookie = cookie;
					}
				}
			}
			if(jssionCookie == null){
				jssionCookie = new Cookie("JSESSIONID",requestJSessionId);
			}
			response.addCookie(jssionCookie);
		}
		
		String url=request.getRequestURI();
		if (this.isSubSystem(url)) {
			if(url.endsWith("/")){
				url=url.substring(0,url.lastIndexOf("/"));
			}
			String systemCode=url.substring(url.lastIndexOf("/")+1);
			String redirectUrl=PropUtils.getProp("redirectUrl.properties",systemCode);
			response.sendRedirect(url+redirectUrl);
		}else{
			chan.doFilter(req, rep);
		}
	}

	public void init(FilterConfig arg0) throws ServletException { }
	
	public boolean isSubSystem(String url){
		return (url.endsWith("/mfg")||url.endsWith("/carmfg/"))||
				(url.endsWith("/iqc")||url.endsWith("/iqc/"))||
				(url.endsWith("/goal")||url.endsWith("/goal/"))||
				(url.endsWith("/improve")||url.endsWith("/improve/"))||
				(url.endsWith("/market")||url.endsWith("/market/"))||
				(url.endsWith("/cost")||url.endsWith("/cost/"))||
				(url.endsWith("/test")||url.endsWith("/test/"))||
				(url.endsWith("/device")||url.endsWith("/device/"))||
				(url.endsWith("/gsm")||url.endsWith("/gsm/"))||
				(url.endsWith("/supplier")||url.endsWith("/supplier/"))||
				(url.endsWith("/audit")||url.endsWith("/audit/"))||
				(url.endsWith("/mesplan")||url.endsWith("/mesplan/"))||
				(url.endsWith("/spc")||url.endsWith("/spc/"))||
				(url.endsWith("/produceplan")||url.endsWith("/produceplan/"))||
				(url.endsWith("/QEvaluation")||url.endsWith("/QEvaluation/"))||
				(url.endsWith("/monitor")||url.endsWith("/monitor/"))||
				(url.endsWith("/claim")||url.endsWith("/claim/"))||
				(url.endsWith("/procertify")||url.endsWith("/procertify/"))||
				(url.endsWith("/qmis")||url.endsWith("/qmis/"))||
				(url.endsWith("/ams")||url.endsWith("/ams/"))||
				(url.endsWith("/ascend")||url.endsWith("/ascend/"))||
				(url.endsWith("/temp")||url.endsWith("/temp/"))||
				(url.endsWith("/satisfied")||url.endsWith("/satisfied/"));
				
	}
}

