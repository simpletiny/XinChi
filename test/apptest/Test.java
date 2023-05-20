package apptest;

public class Test {
	private int age;
	private long dew;

	public void copyFromStudent(Student s) {
		this.age = s.getAge();
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getDew() {
		return dew;
	}

	public void setDew(long dew) {
		this.dew = dew;
	}
}