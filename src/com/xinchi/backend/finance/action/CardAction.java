package com.xinchi.backend.finance.action;

import java.math.BigDecimal;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.backend.finance.service.CardService;
import com.xinchi.bean.CardBean;
import com.xinchi.common.BaseAction;
import com.xinchi.common.SimpletinyDataUtil;

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
	private BigDecimal sum_balance;

	public String searchCard() throws Exception {
		cards = cardService.getAllCardsByParam(card);
		sum_balance = cardService.selectSumBalance(SimpletinyDataUtil.getAccounts());
		return SUCCESS;
	}

	private List<String> accounts;

	public String searchAllAccounts() {
		accounts = cardService.getAllAccounts();
		return SUCCESS;
	}

	private String delete_flg;

	public String switchUseCard() {
		String[] pks = card_pks.split(",");
		for (String pk : pks) {
			CardBean current = cardService.selectByPk(pk);
			current.setDelete_flg(delete_flg);
			cardService.update(current);
		}
		resultStr = SUCCESS;
		return SUCCESS;
	}

	private String account;

	@JSON(serialize = false)
	public String getAccountBalance() {
		// try {
		// account = new String(account.getBytes("ISO-8859-1"), "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// return ERROR;
		// }
		resultStr = cardService.getAccountBalance(account);
		return SUCCESS;
	}

	private String card_pks;
	private String purpose;

	public String signCardPurpose() {
		String[] pks = card_pks.split(",");
		for (String pk : pks) {
			CardBean current = cardService.selectByPk(pk);
			current.setPurpose(purpose);
			cardService.update(current);
		}
		resultStr = SUCCESS;
		return SUCCESS;
	}

	public String searchCardsByPurpose() {
		cards = cardService.selectByPurpose(purpose);
		return SUCCESS;
	}

	public String checkAccount() {
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

	public String getCard_pks() {
		return card_pks;
	}

	public void setCard_pks(String card_pks) {
		this.card_pks = card_pks;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public BigDecimal getSum_balance() {
		return sum_balance;
	}

	public void setSum_balance(BigDecimal sum_balance) {
		this.sum_balance = sum_balance;
	}

	public String getDelete_flg() {
		return delete_flg;
	}

	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}

}
