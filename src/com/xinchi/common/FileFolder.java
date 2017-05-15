package com.xinchi.common;

public enum FileFolder {
	USER_ID("userIdFileFolder"), SUPPLIER_FILE("supplierFileFolder"), AGENCY_FILE("agencyFileFolder"),SYSTEM_GUIDE_FILE("systemGuideFolder");
	private String value;

	public String value() {
		return value;
	}

	private FileFolder(String value) {
		this.value = value;
	}

	// public static void main(String s[]){
	// System.out.println(FileFolder.valueOf("USER_ID").value);
	// }
}
