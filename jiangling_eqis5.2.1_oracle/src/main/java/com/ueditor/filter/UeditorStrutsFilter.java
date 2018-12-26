package com.ueditor.filter;



import java.io.IOException;    
import javax.servlet.FilterChain;    
import javax.servlet.ServletException;    
import javax.servlet.ServletRequest;    
import javax.servlet.ServletResponse;    
import javax.servlet.http.HttpServletRequest;    
  
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;  
  
public class UeditorStrutsFilter extends StrutsPrepareAndExecuteFilter {
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String url = request.getRequestURI();

		if ("/amb/UEditor/jsp/controller.jsp"
				.equals(url)) {
			chain.doFilter(req, res);
		} else {
			super.doFilter(req, res, chain);
		}
	}
	 /** 
     * ueditor编辑器中的图片上传和文件上传 
     * @param url 
     * @return 
     */  
    private boolean decideURI(String url){  
        if(url.endsWith("imageUp.jsp")){  
            return true;  
        }else if(url.endsWith("fileUp.jsp")){  
            return true;  
        }//此处可能需要修改  
        return false;  
    }  
      
}