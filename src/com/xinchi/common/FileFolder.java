package com.xinchi.common;

public enum FileFolder {
	USER_ID("userIdFileFolder"), SUPPLIER_FILE("supplierFileFolder"),
	AGENCY_FILE("agencyFileFolder"),SYSTEM_GUIDE_FILE("systemGuideFolder"),
	VOUCHER("voucherFileFolder"),CLIENT_CONFIRM("clientConfirmFileFolder"),CLIENT_RECEIVED_VOUCHER("clientReceivedVoucherFolder");
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
