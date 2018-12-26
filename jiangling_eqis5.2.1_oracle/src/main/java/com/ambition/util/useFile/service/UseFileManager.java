package com.ambition.util.useFile.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.dao.UseFileDao;
import com.ambition.util.useFile.entity.UseFile;
import com.ibm.icu.util.Calendar;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class UseFileManager {
	@Autowired
	private UseFileDao useFileDao;
	
	/**
	 * 根据编号获取文件
	 * @param id
	 * @return
	 */
	public UseFile getUseFile(Long id){
		return useFileDao.get(id);
	}
	/**
	 * 根据编号获取唯一文件
	 * @param id
	 * @return
	 */
	public UseFile findById(Long id){
		return useFileDao.findById(id);
	}
	/**
	 * 根据编号删除文件
	 * @param useFile
	 */
	public void delete(Long id){
		useFileDao.delete(id);
	}
	
	/**
	 * 删除
	 * @param useFile
	 */
	public void delete(UseFile useFile){
		useFileDao.delete(useFile);
	}
	
	/**
	 * 保存
	 * @param useFile
	 */
	public void save(UseFile useFile){
		useFileDao.save(useFile);
	}
	/**
	 * 保存文件 
	 * @param fileName
	 * @param blob
	 * @param isUse
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public UseFile saveFile(File file,String fileName) throws FileNotFoundException, IOException, SQLException{
		return saveFile(file,fileName,false);
	}
	
	/**
	 * 保存文件 
	 * @param fileName
	 * @param blob
	 * @param isUse
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public UseFile saveFile(File file,String fileName,Boolean isUse) throws FileNotFoundException, IOException, SQLException{
		UseFile useFile = new UseFile();
		useFile.setIsUse(isUse);
		useFile.setBlobValue(Hibernate.createBlob(new FileInputStream(file)));
		useFile.setFileSize(useFile.getBlobValue().length());
		useFile.setFileName(fileName);
		useFile.setCreatedTime(new Date());
		useFile.setCreator(ContextUtils.getUserName());
		useFile.setLastModifiedTime(useFile.getCreatedTime());
		useFile.setLastModifier(useFile.getCreator());
		useFile.setCompanyId(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")>-1&&!fileName.endsWith(".")){
			useFile.setType(fileName.substring(fileName.lastIndexOf(".")+1));
		}
		useFileDao.save(useFile);
		return useFile;
	}
	
	/**
	 * 使用文件
	 * @param fileIds
	 */
	public void useFiles(String fileIds){
		StringBuffer sb = new StringBuffer("0");
		for(String fileId : fileIds.split(",")){
			if(StringUtils.isNotEmpty(fileId)){
				sb.append("," + fileId);
			}
		}
		Query query = useFileDao.createQuery("update UseFile u set u.isUse = ?,u.lastModifiedTime = ? ,u.lastModifier = ? where u.id in ("+sb.toString()+")",true,new Date(),ContextUtils.getUserName());
		query.executeUpdate();
	}
	
	/**
	 * 使用文件
	 * @param fileIds
	 */
	public void cancelUseFiles(String fileIds){
		StringBuffer sb = new StringBuffer("0");
		for(String fileId : fileIds.split(",")){
			if(StringUtils.isNotEmpty(fileId)){
				sb.append("," + fileId);
			}
		}
		Query query = useFileDao.createQuery("update UseFile u set u.isUse = ?,u.lastModifiedTime = ?,u.lastModifier = ? where u.id in ("+sb.toString()+")",false,new Date(),ContextUtils.getUserName());
		query.executeUpdate();
	}
	
	public void updateFileName(Long id,String fileName){
		UseFile useFile = useFileDao.get(id);
		useFile.setFileName(fileName);
		useFile.setLastModifiedTime(useFile.getCreatedTime());
		useFile.setLastModifier(useFile.getCreator());
		useFileDao.save(useFile);
	}
	
	public List<Map<String,String>> parseFilesByString(String fileStr){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotEmpty(fileStr)){
			if(fileStr.startsWith("[")&&fileStr.endsWith("[")){
				JSONArray jsonArray = JSONArray.fromObject(fileStr);
				for(int i=0;i<jsonArray.size();i++){
					JSONObject json = jsonArray.getJSONObject(i);
					Map<String,String> obj = new HashMap<String, String>();
					for(Object key : json.keySet()){
						obj.put(key.toString(),json.getString(key.toString()));
					}
					result.add(obj);
				}
			}else{
				String files[] = fileStr.split("s~s");
				for(String file : files){
					if(StringUtils.isNotEmpty(file)){
						String[] strs = file.split("\\|~\\|");
						if(strs.length>1){
							Map<String,String> obj = new HashMap<String, String>();
							obj.put("id",strs[0]);
							obj.put("fileName",strs[1]);
							result.add(obj);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 转换文件集为前台可用的字符串
	 * @param useFiles
	 * @return
	 */
	public String parseFilesToStr(List<UseFile> useFiles){
		StringBuffer ids = new StringBuffer("");
		for(UseFile useFile : useFiles){
			if(ids.length()>0){
				ids.append("s~s");
			}
			ids.append(useFile.getId() + "|~|" + useFile.getFileName());
		}
		return ids.toString();
	}
	
	public void useAndCancelUseFiles(String cancelUseFiles,String useFiles){
		//如果结果一致,不执行
		if(cancelUseFiles != null && cancelUseFiles.equals(useFiles)){
			return;
		}
		List<Map<String,String>> files = parseFilesByString(cancelUseFiles);
		StringBuffer ids = new StringBuffer("");
		for(Map<String,String> file : files){
			ids.append("," + file.get("id"));
		}
		cancelUseFiles(ids.toString());
		files = parseFilesByString(useFiles);
		ids.delete(0,ids.length());
		for(Map<String,String> file : files){
			ids.append("," + file.get("id"));
		}
		useFiles(ids.toString());
	}
	
	/**
	  * 方法名: 复制文件
	  * <p>功能说明：</p>
	  * @param hisUseFiles
	  * @return
	 */
	public String copyFiles(String hisUseFiles){
		if(StringUtils.isEmpty(hisUseFiles)){
			return null;
		}
		List<Map<String,String>> fileMaps = parseFilesByString(hisUseFiles);
		List<UseFile> useFiles = new ArrayList<UseFile>();
		for(Map<String,String> fileMap : fileMaps){
			Long hisId = Long.valueOf(fileMap.get("id"));
			String hisFileName = fileMap.get("fileName");
			UseFile hisUseFile = getUseFile(hisId);
			UseFile useFile = new UseFile();
			useFile.setBlobValue(hisUseFile.getBlobValue());
			try {
				useFile.setFileSize(useFile.getBlobValue().length());
			} catch (SQLException e) {
				throw new AmbFrameException("保存文件失败",e);
			}
			useFile.setFileName(hisFileName);
			useFile.setCreatedTime(new Date());
			useFile.setCreator(ContextUtils.getUserName());
			useFile.setLastModifiedTime(useFile.getCreatedTime());
			useFile.setLastModifier(useFile.getCreator());
			useFile.setCompanyId(ContextUtils.getCompanyId());
			useFile.setType(hisUseFile.getType());
			useFileDao.save(useFile);
			useFiles.add(useFile);
		}
		return parseFilesToStr(useFiles);
	}
	/**
	 * 方法名: saveFile 
	 * <p>功能说明：保存文件(Blob通用)</p> 
	 * @return UseFile
	 * @throws
	 */
	public UseFile saveFile(Blob blob,String fileName,Boolean isUse) throws FileNotFoundException, IOException, SQLException{
		//把文件上传到硬盘
		Map<String,String> resultMap= saveFileToDisk(blob,fileName);
		return saveFile(blob.length(),fileName,true,resultMap.get("relativePath"),resultMap.get("newFileName"));
	}
	/**
	 * 方法名: saveFileToDisk 
	 * <p>功能说明：把文件上传到硬盘（传入Blob）</p> 
	 * @return String
	 * @throws
	 */
	public Map<String,String> saveFileToDisk(Blob blob,String fileName){
		String newFileName;
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		String relativePath = "/uploadFiles/"+calendar.get(Calendar.YEAR)+"/"+(month<10?("0"+""+month):month);
		String path = ServletActionContext.getServletContext().getRealPath(relativePath);
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		//判断上传文件是否有扩展名，一时间错作为新的文件名
		int index = fileName.lastIndexOf('.');
		String prefixName = UUID.randomUUID().toString();
		if(index != -1){
			newFileName = prefixName + fileName.substring(index);
		}else{
			newFileName = prefixName;
		}
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		//读取保存在临时目录下的上传文件，写入到新的文件中
		try {
			bis = new BufferedInputStream(blob.getBinaryStream());
			fos = new FileOutputStream(new File(dir,newFileName));
			bos = new BufferedOutputStream(fos);
			byte[] buf = new byte[4096];
			int len = -1;
			while((len = bis.read(buf))!=-1){
				bos.write(buf,0,len);
			}
		} catch (Exception e) {
			throw new AmbFrameException(e.getMessage());
		}finally{
			try{
				if(null != bis){
					bis.close();
				}
			}catch(IOException e){
				throw new AmbFrameException(e.getMessage());
			}
			try{
				if(null != bos){
					bos.close();
				}
			}catch(IOException e){
				throw new AmbFrameException(e.getMessage());
			}
			try{
				if(null != fis){
					fis.close();
				}
			}catch(IOException e){
				throw new AmbFrameException(e.getMessage());
			}
			try{
				if(null != fos){
					fos.close();
				}
			}catch(IOException e){
				throw new AmbFrameException(e.getMessage());
			}
		}
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("relativePath", relativePath);
		resultMap.put("newFileName", newFileName);
		return resultMap;
	}
	/**
	 * 方法名: saveFile 
	 * <p>功能说明：保存文件详细信息</p> 
	 * @return UseFile
	 * @throws
	 */
	public UseFile saveFile(Long fileLength,String fileName,Boolean isUse,String path,String newFileName) throws FileNotFoundException, IOException, SQLException{
		UseFile useFile = new UseFile();
		useFile.setIsUse(isUse);
		useFile.setPath(path);
		useFile.setFileSize(fileLength);
		useFile.setFileName(fileName);
		useFile.setRealFileName(newFileName);
		useFile.setCreatedTime(new Date());
		useFile.setCreator(ContextUtils.getUserName());
		useFile.setModifiedTime(useFile.getCreatedTime());
		useFile.setModifier(useFile.getCreator());
		useFile.setCompanyId(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")>-1&&!fileName.endsWith(".")){
			useFile.setType(fileName.substring(fileName.lastIndexOf(".")+1));
		}
		useFileDao.save(useFile);
		return useFile;
	}
	/**
	 * @throws UnsupportedEncodingException 
	 * 方法名: saveFileToDisk 
	 * <p>功能说明：把文件上传到硬盘（传入byte[]）</p> 
	 * @return String
	 * @throws
	 */
	public Map<String,String> saveFileToDisk(byte[] bytes,String fileName) throws UnsupportedEncodingException{
		String newFileName;
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		String relativePath = "/uploadFiles/"+calendar.get(Calendar.YEAR)+"/"+(month<10?("0"+""+month):month);
		String classpath = URLDecoder.decode(this.getClass().getClassLoader().getResource("/").getPath(), "UTF-8");
		String path = classpath.substring(0, classpath.lastIndexOf("/WEB-INF/classes/"))+relativePath;
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		//判断上传文件是否有扩展名，一时间错作为新的文件名
		int index = fileName.lastIndexOf('.');
		String prefixName = UUID.randomUUID().toString();
		if(index != -1){
			newFileName = prefixName + fileName.substring(index);
		}else{
			newFileName = prefixName;
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(dir,newFileName));
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);  
		} catch (Exception e) {
			throw new AmbFrameException(e.getMessage());
		}finally{
			if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e) {  
                	throw new AmbFrameException(e.getMessage());
                }  
            }
            if (fos != null) {  
                try {
                    fos.close();
                } catch (IOException e) {
                	throw new AmbFrameException(e.getMessage());
                }
            }  
		}
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("relativePath", relativePath);
		resultMap.put("newFileName", newFileName);
		return resultMap;
	}
}
