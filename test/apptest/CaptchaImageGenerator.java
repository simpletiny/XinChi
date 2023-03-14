package apptest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CaptchaImageGenerator {

	private static final int CAPTCHA_IMAGE_WIDTH = 150;
	private static final int CAPTCHA_IMAGE_HEIGHT = 50;
	private static final int CAPTCHA_CODE_LENGTH = 6;

	public static void generateCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		String captchaCode = generateCaptchaCode(CAPTCHA_CODE_LENGTH);
		session.setAttribute("captchaCode", captchaCode);

		BufferedImage captchaImage = new BufferedImage(CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		Graphics graphics = captchaImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT);

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 25));
		graphics.drawString(captchaCode, 30, 35);

		response.setContentType("image/jpeg");
		ServletOutputStream outputStream = response.getOutputStream();
		ImageIO.write(captchaImage, "jpeg", outputStream);
		outputStream.close();
	}

	public static void generateImage(String file_path) throws Exception {
		OutputStream out = new FileOutputStream(file_path);
		String captchaCode = generateCaptchaCode(CAPTCHA_CODE_LENGTH);
		BufferedImage captchaImage = new BufferedImage(CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		Graphics graphics = captchaImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT);

		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 25));
		graphics.drawString(captchaCode, 30, 35);

		ImageIO.write(captchaImage, "jpeg", out);
		out.close();
	}

	private static String generateCaptchaCode(int captchaCodeLength) {
		StringBuilder captchaCode = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < captchaCodeLength; i++) {
			int randomIndex = random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length());
			captchaCode.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(randomIndex));
		}
		return captchaCode.toString();
	}
}
