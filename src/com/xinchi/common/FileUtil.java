package com.xinchi.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xinchi.tools.PropertiesUtil;

public class FileUtil {

	public static void saveFile(String fileName, String destFolderStr, String subFolder) {

		String tempFolder = PropertiesUtil.getProperty("tempUploadFolder");
		String fileFolder = PropertiesUtil.getProperty(destFolderStr);
		File sourceFile = new File(tempFolder + File.separator + fileName);
		File destfile = new File(fileFolder + File.separator + subFolder + File.separator + fileName);
		try {
			FileUtils.copyFile(sourceFile, destfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceFile.delete();
	}

	public static void deleteFile(String fileName, String destFolderStr, String subFolder) {
		String fileFolder = PropertiesUtil.getProperty(destFolderStr);
		File destfile = new File(fileFolder + File.separator + subFolder + File.separator + fileName);
		if (destfile.exists())
			destfile.delete();
	}

	public static void reduceImg(File src, File dest, int width, int height, Float rate) {
		try {
			if (!src.exists())
				return;

			if (rate != null && rate > 0) {
				int[] results = getImgWidthHeight(src);
				if (results == null || results[0] == 0 || results[1] == 0) {
					return;
				} else {
					width = (int) (results[0] * rate);
					height = (int) (results[1] * rate);
				}
			}

			Image srcImg = ImageIO.read(src);

			BufferedImage tag = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

			if (!dest.getParentFile().exists())
				dest.getParentFile().mkdirs();

			FileOutputStream out = new FileOutputStream(dest);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}

	/**
	 * 获取图片宽度和高度
	 * 
	 * @param 图片路径
	 * @return 返回图片的宽度
	 */
	private static int[] getImgWidthHeight(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = ImageIO.read(is);
			result[0] = src.getWidth(null);
			result[1] = src.getHeight(null);
			is.close();
		} catch (Exception ef) {
			ef.printStackTrace();
		}

		return result;
	}

	public static void toZip(List<File> srcFiles, OutputStream out) {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[2048];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
