package com.xinchi.common;

public enum FileFolder {
	USER_ID("userIdFileFolder"), SUPPLIER_FILE("supplierFileFolder"), AGENCY_FILE(
			"agencyFileFolder"), SYSTEM_GUIDE_FILE("systemGuideFolder"), VOUCHER("voucherFileFolder"), CLIENT_CONFIRM(
					"clientConfirmFileFolder"), CLIENT_RECEIVED_VOUCHER("clientReceivedVoucherFolder"), CLIENT_FINAL(
							"clientFinalFileFolder"), CLIENT_FINAL_VOUCHER(
									"clientFinalVoucherFileFolder"), CLIENT_EMPLOYEE_HEAD(
											"clientEmployeeHeadFolder"), CLIENT_EMPLOYEE_MIN_HEAD(
													"clientEmployeeMinHeadFolder"), SUPPLIER_RECEIVED_VOUCHER(
															"supplierReceivedVoucherFolder");
	private String value;

	public String value() {
		return value;
	}

	private FileFolder(String value) {
		this.value = value;
	}

	public static void main(String s[]) {
		System.out.println(FileFolder.CLIENT_FINAL.value());
	}
}
