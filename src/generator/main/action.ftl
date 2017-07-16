package ${pac}.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ${vo};
import ${pac}.service.${clazzName?cap_first}Service;


@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ${clazzName?cap_first}Action extends BaseAction{
	
	
}