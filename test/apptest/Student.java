package apptest;

public class Student {
	private String type;
	private Info infor = new Info();
	
	public class Info{
		private String name;
		private String sex;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Info getInfor() {
		return infor;
	}


	public void setInfor(Info infor) {
		this.infor = infor;
	}
	
}
