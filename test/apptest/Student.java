package apptest;

import java.util.List;

public class Student {
	private String name;
	private int age;
	private List<String> scores;
	private String sex;

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
		return this.name.equals(other.name) && this.age == other.age;
	}

	// @Override
	// public int hashCode() {
	// int result = 17;
	// result = 31 * result + name.hashCode();
	// result = 31 * result + age;
	// return result;
	// }

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
}
