package pubg.jna;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 * 模拟鼠标
 * @author Can
 *
 */
public class Simulate {

	// 自动化对象
	private static Robot robot;

	public static void main(String[] args) {
		m_move(10, 100);
	}

	// 模拟移动
	public static void m_move(int x, int y) {
		try {
			Point mousepoint = MouseInfo.getPointerInfo().getLocation();
			x += mousepoint.x;
			y += mousepoint.y;
			robot = new Robot();
			robot.mouseMove(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
