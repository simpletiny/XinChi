package com.xinchi.backend.finance.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.CardService;
import com.xinchi.bean.CardBean;
import com.xinchi.common.BaseAction;

@Controller
@Scope("prototype")
public class CardAction extends BaseAction {
	private static final long serialVersionUID = 3408762081387739186L;
	@Autowired
	private CardService cardService;
	private CardBean card;

	public String createCard() {
		cardService.insert(card);
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private List<CardBean> cards;

	public String searchCard() {
		cards = cardService.getAllCardsByParam(null);
		return SUCCESS;
	}

	private List<String> accounts;

	public String searchAllAccounts() {
		accounts = cardService.getAllAccounts();
		return SUCCESS;
	}

	private String account;
	
	@JSON(serialize = false)
	public String getAccountBalance() {
//		try {
//			account = new String(account.getBytes("ISO-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return ERROR;
//		}
		resultStr = cardService.getAccountBalance(account);
		return SUCCESS;
	}
	public String checkAccount(){
		resultStr = cardService.checkAccount(account);
		return SUCCESS;
	}
	public CardBean getCard() {
		return card;
	}

	public void setCard(CardBean card) {
		this.card = card;
	}

	public List<CardBean> getCards() {
		return cards;
	}

	public void setCards(List<CardBean> cards) {
		this.cards = cards;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
