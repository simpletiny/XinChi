package ${hbmMoudelVO.packageName};

import java.io.Serializable;
import com.xinchi.common.SupperBO;

public class ${hbmMoudelVO.clzssName?cap_first} extends SupperBO implements Serializable {
	private static final long serialVersionUID = 1L;
		
	<#list hbmMoudelVO.columnList as columnVO>
		 
		  private ${columnVO.type} ${columnVO.propertyName};
		  
	</#list>
	

}
