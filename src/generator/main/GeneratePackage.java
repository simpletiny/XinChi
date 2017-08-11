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
	public static final String clazzName = "innerTransfer";
	public static final String desc = "内转明细";
	public static final String voName = "InnerTransferBean";
	public static final String baseFolder = "src/com/xinchi/backend/finance";
	public static final String basePackage = "com.xinchi.backend.finance";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new File(baseFolder + "/action/").mkdirs();
		new File(baseFolder + "/action/" + StringUtils.capitalize(clazzName) + "Action.java").createNewFile();
		new File(baseFolder + "/service/impl/").mkdirs();
		new File(baseFolder + "/service/" + StringUtils.capitalize(clazzName) + "Service.java").createNewFile();
		new File(baseFolder + "/service/impl/" + StringUtils.capitalize(clazzName) + "ServiceImpl.java").createNewFile();
		new File(baseFolder + "/dao/impl").mkdirs();
		new File(baseFolder + "/dao/" + StringUtils.capitalize(clazzName) + "DAO.java").createNewFile();
		new File(baseFolder + "/dao/impl/" + StringUtils.capitalize(clazzName) + "DAOImpl.java").createNewFile();

		Map<String, String> map = new HashMap<String, String>();
		map.put("clazzName", clazzName);
		map.put("pac", basePackage);
		map.put("desc", desc);
		map.put("voName", voName);
		map.put("vo", "com.xinchi.bean." + voName);
		// // //生成action
		getAction(clazzName, map, "action.ftl");
		// //SERVICE
		generateFile2(map, StringUtils.capitalize(clazzName) + "Service.java", "service.ftl", getClzssPath(), baseFolder + "/service/");
		generateFile2(map, StringUtils.capitalize(clazzName) + "ServiceImpl.java", "serviceImpl.ftl", getClzssPath(), baseFolder + "/service/impl/");
		// DAO
		generateFile2(map, StringUtils.capitalize(clazzName) + "Dao.java", "dao.ftl", getClzssPath(), baseFolder + "/dao/");
		generateFile2(map, StringUtils.capitalize(clazzName) + "DaoImpl.java", "daoImpl.ftl", getClzssPath(), baseFolder + "/dao/impl/");

	}

	private static void getAction(String clazzName, Map<String, String> map, String ftl) throws Exception {
		String path = getClzssPath();
		String modelPackage = baseFolder + "/action/";
		generateFile2(map, StringUtils.capitalize(clazzName) + "Action.java", ftl, path, modelPackage);
	}

	private static void generateFile2(Map<String, String> rootMap, String fileName, String ftl, String path, String modelPackage) throws IOException,
			UnsupportedEncodingException, FileNotFoundException, TemplateException {
		File file = new File(path);
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(file);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template template = cfg.getTemplate(ftl, "UTF-8");
		Writer out = new OutputStreamWriter(new FileOutputStream(modelPackage + fileName), "UTF-8");
		template.process(rootMap, out);
		out.flush();
	}

	private static String getClzssPath() {
		String path = GenerateXMLMain.class.getResource("").getPath();
		path = path.replace("WebRoot/WEB-INF/classes", "src");
		return path;
	}

}
