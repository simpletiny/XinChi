package apptest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SomeTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String sales_name = "123,1231,4123,";
		sales_name = sales_name.substring(0,sales_name.length()-1);
		
		System.out.println(sales_name);
	}
}
