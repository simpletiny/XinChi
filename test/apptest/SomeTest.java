package apptest;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author Administrator 通过aop拦截后执行具体操作
 */
@Aspect
@Component
public class SomeTest {

	public static void main(String[] args) {

		File srcfile = new File("d://test.png");
		File distfile = new File("d://test1.png");

		distfile.delete();

	}

}