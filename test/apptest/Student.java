package apptest;

import java.util.List;

public class Student {
	private String name;
	private String age;
	private List<String> scores;
	private String sex;

	public String getName() {
		return name;
	}

	public String getAge() {
		return age;
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

	public void setAge(String age) {
		this.age = age;
	}

	public void setScores(List<String> scores) {
		this.scores = scores;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
