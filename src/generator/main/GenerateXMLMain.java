package generator.main;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import generator.bean.ColumnBO;
import generator.bean.HbmMoudelBO;
import generator.enumtype.TypeEnum;
import generator.util.DatabaseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

public class GenerateXMLMain {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {

		/*List<String> tablelist = DatabaseUtil.getTableList();
		for (String table : tablelist) {
			createXmlAndBOByTabName(table);
		}*/
		String table = "supplier";
		createXmlAndBOByTabName(table);
	}
	
	public static void createXmlAndBOByTabName(String table){
		
		String[] excludeColumn = { 
				"create_time","update_time"};
		HbmMoudelBO hbmMoudelVO = null;
		Map<String, HbmMoudelBO> rootMap = new HashMap<String, HbmMoudelBO>();
		List<ColumnBO> list = null;
		String packagePath = "com.xinchi.bean";
		hbmMoudelVO = new HbmMoudelBO();
		list = new ArrayList<ColumnBO>();
		String[] strs = table.split("_");
		String clzssName = "";
		for (String str : strs) {
			clzssName += Character.toUpperCase(str.charAt(0))
					+ str.substring(1);
		}
		// 配置类名 包名和表名
		hbmMoudelVO.setClzssName(clzssName);
		hbmMoudelVO.setTableName(table);
		hbmMoudelVO.setPackageName(packagePath);
		List<ColumnBO> columnlist = DatabaseUtil.getColumnList(table);
		for (ColumnBO vo : columnlist) {
			if (!ArrayUtils.contains(excludeColumn, vo.getColumnName())) {
				vo.setColumnName(vo.getColumnName());
				vo.setPropertyName(vo.getColumnName());
				vo.setType(TypeEnum.getJavaType(vo.getColumnType()));
				list.add(vo);
			}
		}
		hbmMoudelVO.setColumnList(list);
		rootMap.put("hbmMoudelVO", hbmMoudelVO);
		String ftl = "hbm.ftl";
		String ftl2 = "model.ftl";
		String hbmFileName =Character.toLowerCase(hbmMoudelVO
				.getClzssName().replace("Model", "").charAt(0))
				+ hbmMoudelVO.getClzssName().replace("Model", "")
						.substring(1).replace("Bean", "") + "Mapper.xml";
		try {
			// 生成映射文件
			getHbmXml(rootMap, hbmFileName, ftl);
			// 生成java bean类
			getModel(rootMap, hbmMoudelVO.getClzssName() + ".java", ftl2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	private static void getHbmXml(Map<String, HbmMoudelBO> rootMap,
			String fileName, String ftl) throws Exception {
		String path = getClzssPath();
		String modelPackage = path.replace("generator/main/",
				"com/xinchi/bean/mapper/");
		generateFile(rootMap, fileName, ftl, path, modelPackage);
	}

	private static void getModel(Map<String, HbmMoudelBO> rootMap,
			String fileName, String ftl) throws Exception {
		String path = getClzssPath();
		String modelClassPackage = path.replace("generator/main/",
				"com/xinchi/bean/");
		generateFile(rootMap, fileName, ftl, path, modelClassPackage);
	}

	private static void generateFile(Map<String, HbmMoudelBO> rootMap,
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

	/**
	 * 当前类路径
	 * 
	 * @return
	 */
	private static String getClzssPath() {
		String path = GenerateXMLMain.class.getResource("").getPath();
		path = path.replace("WebRoot/WEB-INF/classes", "src");
		return path;
	}

}
