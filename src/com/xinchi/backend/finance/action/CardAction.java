package com.xinchi.backend.finance.action;

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

	public CardBean getCard() {
		return card;
	}

	public void setCard(CardBean card) {
		this.card = card;
	}

}
