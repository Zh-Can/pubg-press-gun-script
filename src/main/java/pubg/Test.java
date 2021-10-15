package pubg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;

public class Test {

	
	
	public static void main(String[] args) throws Exception {

//		ImgUtil.gray(new File("C:\\Users\\zby03\\Desktop\\1.jpg"), new File("C:\\Users\\zby03\\Desktop\\2.jpg"));
		
		test(new File("C:\\Users\\zby03\\Desktop\\1.jpg"));
	}

	
	private static void test(File file) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(file);
		BufferedImage grayImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
		for (int i = 0; i < bufferedImage.getWidth(); i++) {
			for (int j = 0; j < bufferedImage.getHeight(); j++) {
				int color = bufferedImage.getRGB(i, j);
				int r = (color >> 16) & 0xff;
				int g = (color >> 8 ) & 0xff;
				int b = color & 0xff;
				int newPixel = colorToRGB(255, 0xff - r,0xff - g, 0xff - b);
				grayImage.setRGB(i, j, newPixel);
			}
		}
		ImageIO.write(grayImage, "jpg",  new File("C:\\Users\\zby03\\Desktop\\2.jpg"));
	}
	private static int colorToRGB(int alpha, int red, int green, int blue) {
		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;
		return newPixel;
	}
	
//	private static void updateNum() throws Exception{
//		List<Entity> query = Db.use().query("select * from gpdata where data like '%.%'");
//		for (Entity entity : query) {
//			String data = entity.getStr("data");
//			String[] split = data.split("\\|");
//			List<String> list = new ArrayList<>();
//			for (String str : split) {
//				if (str.contains(".")) {
//					list.add(Float.toString(Math.round(Float.parseFloat(str))).replace(".0", ""));
//				} else {
//					list.add(str);
//				}
//			}
//			System.out.println();
//			data = list.toString().replace("[", "").replace("]", "").replace(", ", "|");
//			Db.use().execute("update gpdata set data = ? where id = ?", data, entity.getStr("id"));
//		}
//	}
	
	
//	private static void impData() throws Exception{
//		List<Entity> query = Db.use().query("select name from gun order by id asc");
//		for (Entity entity : query) {
//			System.out.println(entity.getStr("name"));
//			ExcelUtil.readBySax("C:\\Users\\zby03\\Desktop\\导出数据.xls", "sheet_" + entity.getStr("name"), new RowHandler() {
//
//				@Override
//				public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
//					try {
//						if (rowIndex >= 1) {
//							Db.use().insert(Entity.create("gpdata")
//									.set("id", format(rowList.get(1).toString()))
//									.set("data", rowList.get(2))
//									.set("info", rowList.get(3))
//							);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//				private String format(String str) {
//					return str.substring(0, str.length() - 1) + (Integer.parseInt(str.substring(str.length() - 1)) - 1);
//				}
//
//			});
//		}
//	}
}
