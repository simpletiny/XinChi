package com.xinchi.backend.accounting.service;

public interface AccountingService {

	public String updateRelatedPaid(String related_pk, String status);

	public String updatePaid(String pk,  String status);

}
