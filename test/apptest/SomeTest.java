package apptest;

import java.io.IOException;

import com.xinchi.common.SimpletinyString;

public class SomeTest {

	public static void main(String[] args) throws IOException {
		String a = "N,N";
		String b = SimpletinyString.replaceCharFromLeft(a, "Y");
		System.out.println(b);
	}
}
