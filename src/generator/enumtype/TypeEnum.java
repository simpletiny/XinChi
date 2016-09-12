package generator.enumtype;

public enum TypeEnum {

	BIT("java.lang.Boolean", "bit", "bit"),TEXT("java.lang.String", "varchar", "text"),
	LONGTEXT("java.lang.String", "varchar", "longtext"),
	LONGBLOB("java.lang.String", "blog", "longblob"),CHAR("java.lang.String", "char", "char"),
	VARCHAR("java.lang.String", "varchar", "varchar"),
	TIMESTAMP("java.sql.Timestamp", "timestamp", "timestamp"),INT("java.lang.Integer", "integer", "int"),
	DATE("java.sql.Date","date","date"),DATETIME("java.sql.Timestamp", "timestamp", "timestamp");
	private String columnType;
	
	private String type;
	
	private String dbtype;
	
	private TypeEnum(String type,String columnType, String dbtype) {
		this.columnType = columnType;
		this.type = type;
		this.dbtype = dbtype;
	}

	public static String getJavaType(String columnType){
		for(TypeEnum type:TypeEnum.values()){
			if(columnType.equals(type.getDbtype()))
				return type.getType();
		}
		return null;
	}
	
	public static String getJdbcType(String columnType){
		for(TypeEnum type:TypeEnum.values()){
			if(columnType.equals(type.getDbtype()))
				return type.getColumnType();
		}
		return null;
	}
	
	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	
	public static void main(String[] args) {
		System.out.println(getJdbcType("int"));
	}
	
	
}
