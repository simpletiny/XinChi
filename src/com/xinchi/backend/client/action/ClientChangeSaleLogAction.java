package com.xinchi.backend.client.action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xinchi.common.BaseAction;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientChangeSaleLogAction extends BaseAction {

	private static final long serialVersionUID = 5508493895827658436L;

}