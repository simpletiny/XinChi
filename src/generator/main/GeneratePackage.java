package generator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.IoUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratePackage {

	public static final String clazzName="client";
	public static final String desc="客户";
	public static final String voName="ClientBean";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		 URL url = GeneratePackage.class.getResource("/");
		 String path1=url.getPath().replace("WebRoot/WEB-INF/classes/", "src/com/xinchi");
		 String path2=path1+"/backend";
		 new File(path2+"/"+clazzName.toLowerCase()+"/action/").mkdirs();
		 new File(path2+"/"+clazzName.toLowerCase()+"/action/"+StringUtils.capitalize(clazzName)+"Action.java").createNewFile();
		 new File(path2+"/"+clazzName.toLowerCase()+"/service/impl/").mkdirs();
		 new File(path2+"/"+clazzName.toLowerCase()+"/service/"+StringUtils.capitalize(clazzName)+"Service.java").createNewFile();
		 new File(path2+"/"+clazzName.toLowerCase()+"/service/impl/"+StringUtils.capitalize(clazzName)+"ServiceImpl.java").createNewFile();
		 new File(path2+"/"+clazzName.toLowerCase()+"/dao/impl").mkdirs();
		 new File(path2+"/"+clazzName.toLowerCase()+"/dao/"+StringUtils.capitalize(clazzName)+"DAO.java").createNewFile();
		 new File(path2+"/"+clazzName.toLowerCase()+"/dao/impl/"+StringUtils.capitalize(clazzName)+"DAOImpl.java").createNewFile();
		
		Map<String,String> map=new HashMap<String, String>();
		map.put("clazzName", clazzName);
		map.put("pac","com.xinchi.backend."+ clazzName.toLowerCase());
		map.put("desc", desc);
		map.put("vo", "com.xinchi.bean."+ voName);
//		//生成action
		getAction(clazzName,map,"action.ftl");
//		//SERVICE
		generateFile2(map, StringUtils.capitalize(clazzName)+"Service.java", "service.ftl", getClzssPath(), path2+"/"+clazzName.toLowerCase()+"/service/");
		generateFile2(map, StringUtils.capitalize(clazzName)+"ServiceImpl.java", "serviceImpl.ftl", getClzssPath(), path2+"/"+clazzName.toLowerCase()+"/service/impl/");
//		DAO
		generateFile2(map, StringUtils.capitalize(clazzName)+"Dao.java", "dao.ftl", getClzssPath(), path2+"/"+clazzName.toLowerCase()+"/dao/");
		generateFile2(map, StringUtils.capitalize(clazzName)+"DaoImpl.java", "daoImpl.ftl", getClzssPath(), path2+"/"+clazzName.toLowerCase()+"/dao/impl/");
		
	}

	private static void getAction(String clazzName,Map<String,String> map,String ftl) throws Exception {
		String path = getClzssPath();
		String modelPackage = path.replace("generator/main/",
				"com/xinchi/backend/"+clazzName.toLowerCase()+"/action/");
		generateFile2(map, StringUtils.capitalize(clazzName)+"Action.java", ftl, path, modelPackage);
	}

	private static void generateFile2(Map<String, String> rootMap,
			String fileName, String ftl, String path, String modelPackage)
			throws IOException, UnsupportedEncodingException,
			FileNotFoundException, TemplateException {
		File file = new File(path);
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(file);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template template = cfg.getTemplate(ftl, "UTF-8");
		Writer out = new OutputStreamWriter(new FileOutputStream(modelPackage
				+ fileName), "UTF-8");
		template.process(rootMap, out);
		out.flush();
	}
	
	private static String getClzssPath() {
		String path = GenerateXMLMain.class.getResource("").getPath();
		path = path.replace("WebRoot/WEB-INF/classes", "src");
		return path;
	}

}
