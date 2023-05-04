package apptest;

import java.math.BigDecimal;
import java.util.List;

public class Student {
	private String name;
	private int age;
	private List<String> scores;
	private String sex;
	private String id;
	private String cellphone_A;
	private String cellphone_B;
	private BigDecimal price;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Student)) {
			return false;
		}
		Student other = (Student) obj;
		return (this.name == other.name || (null != this.name && this.name.equals(other.name)))
				&& (this.id == other.id || (null != this.id && this.id.equals(other.id)))
				&& (this.sex == other.sex || (null != this.sex && this.sex.equals(other.sex)));
	}

	@Override
	public int hashCode() {
		int result = 17;
		if (null != name)
			result = 31 * result + name.hashCode();
		if (null != id)
			result = 31 * result + id.hashCode();
		if (null != sex)
			result = 31 * result + sex.hashCode();

		return result;
	}

	public String getName() {
		return name;
	}

	public List<String> getScores() {
		return scores;
	}

	public String getSex() {
		return sex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScores(List<String> scores) {
		this.scores = scores;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public String getCellphone_A() {
		return cellphone_A;
	}

	public String getCellphone_B() {
		return cellphone_B;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCellphone_A(String cellphone_A) {
		this.cellphone_A = cellphone_A;
	}

	public void setCellphone_B(String cellphone_B) {
		this.cellphone_B = cellphone_B;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
