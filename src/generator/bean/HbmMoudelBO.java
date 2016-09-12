package generator.bean;


import java.util.List;

public class HbmMoudelBO {

	private String packageName;
	
	private String clzssName;
	
	private String tableName;
	
	private List<ColumnBO> columnList;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClzssName() {
		return clzssName;
	}

	public void setClzssName(String clzssName) {
		this.clzssName = clzssName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnBO> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnBO> columnList) {
		this.columnList = columnList;
	}
	
}
