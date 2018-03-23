package generator.enumtype;

public enum TypeEnum {

	BIT("Boolean", "bit", "bit"), TEXT("String", "varchar", "text"), LONGTEXT("String", "varchar", "longtext"), LONGBLOB("String", "blog", "longblob"), CHAR(
			"String", "char", "char"), VARCHAR("String", "varchar", "varchar"), TINYINT("Integer", "integer", "tinyint"), TIMESTAMP("java.sql.Timestamp",
			"timestamp", "timestamp"), INT("Integer", "integer", "int"), DATE("java.sql.Date", "date", "date"), DATETIME("java.sql.Timestamp",
			"timestamp", "timestamp"), DECIMAL("java.math.BigDecimal", "decimal", "decimal"), BIGINT("int", "bigint", "bigint"), MEDIUMTEXT("String",
			"mediumtext", "mediumtext"),FLOAT("float","float","float");
	private String columnType;

	private String type;

	private String dbtype;

	private TypeEnum(String type, String columnType, String dbtype) {
		this.columnType = columnType;
		this.type = type;
		this.dbtype = dbtype;
	}

	public static String getJavaType(String columnType) {
		for (TypeEnum type : TypeEnum.values()) {
			if (columnType.equals(type.getDbtype()))
				return type.getType();
		}
		return null;
	}

	public static String getJdbcType(String columnType) {
		for (TypeEnum type : TypeEnum.values()) {
			if (columnType.equals(type.getDbtype()))
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
		System.out.println(getJdbcType("bigint"));
	}

}
