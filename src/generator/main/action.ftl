package ${pac}.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ${vo};
import ${pac}.service.${clazzName?cap_first}Service;


@Controller
@Scope("prototype")
public class ${clazzName?cap_first}Action{
	private Logger logger=Logger.getLogger(${clazzName?cap_first}Action.class);
	
	private ${vo} bo;
	
	private List<${vo}> boList;
	@Autowired
	private I${clazzName?cap_first}Service ${clazzName?uncap_first}Service;
	@Autowired
	private IDictionaryFieldMapService dictFieldMapService;
	
	private Map<String, List<HsDictionaryDataBO>> dicts;
	/**
	 * 新增
	 * @return
	 */
	public String save${clazzName?cap_first}(){
		${clazzName?uncap_first}Service.insert(bo);
		return "SUCCESS";
	}
	
	public String update${clazzName?cap_first}(){
		${clazzName?uncap_first}Service.update(bo);
		return "SUCCESS";
	}
	
	public String del${clazzName?cap_first}(){
		${clazzName?uncap_first}Service.delete(bo.getId());
		return "SUCCESS";
	}
	
	public String find${clazzName?cap_first}(){
		bo=${clazzName?uncap_first}Service.selectByPrimaryKey(bo.getId());
		return "SUCCESS";
	}
	
	public String findAll${clazzName?cap_first}(){
		boList=${clazzName?uncap_first}Service.getAllByParam(bo);
		return "SUCCESS";
	}
	/**
	 * 表单验证
	 */
	public void validateAdd${clazzName?cap_first}(){
		
	}
	
	public ${vo} getBo() {
		return bo;
	}

	public void setBo(${vo} bo) {
		this.bo = bo;
	}
	
	public List<${vo}> getBoList() {
		return boList;
	}

	public void setBoList(List<${vo}> boList) {
		this.boList = boList;
	}
	
	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}

	public Map<String, List<HsDictionaryDataBO>> getDicts() {
		return dicts;
	}

	public void setDicts(Map<String, List<HsDictionaryDataBO>> dicts) {
		this.dicts = dicts;
	}
}