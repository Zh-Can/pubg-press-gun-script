package pubg.actuator;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import pubg.APP;
import pubg.jna.MSDK;
import pubg.jna.Simulate;
import pubg.jna.YSDK;
import pubg.listener.MouseListener;

/**
 * 执行器
 * 
 * @author Can
 *
 */
public class Actuator {

	private final static Log log = LogFactory.get();

	// 芯片地址
	public static int handle;
	// 芯片类型（0无1飞易来2易键鼠）
	public static int chipType;
	
	// 当前选择武器位，默认1
	public static int gunInt = 1; 
	
	// 武器1类型
	public static String type1 = "0";
	// 武器2类型
	public static String type2 = "0";
	// 武器1压强数据
	public static Integer[] data1 = {};
	// 武器2压强数据
	public static Integer[] data2 = {};
	
	
	/**
	 * 当按下Home键时执行
	 * 
	 * 1.获取屏幕分辩率 2.注册飞易来MSdk 3.状态修改 4.注册鼠标监听
	 */
	public static void pressHome() {
		// 1.获取屏幕分辩率
//		Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
//		Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
//		String size = di.getSize().width + " x " + di.getSize().height;
//		App.screenSize.setText(size);
//		
		try {
//			 3.状态修改
			APP.status.setText("已开启");
//			 4.注册鼠标监听
			ThreadUtil.execAsync(new Runnable() {
				@Override
				public void run() {
					MouseListener.run = true;
					MouseListener.add();
				}
			});
		} catch (Exception e) {
			log.error(ExceptionUtil.stacktraceToString(e));
		}

	}

	/**
	 * 当按下End键时执行 1.状态修改 2.卸载鼠标监听
	 */
	public static void pressEnd() {
		// 1.状态修改
		APP.status.setText("未开启");
		// 2.卸载鼠标监听
		MouseListener.run = false;
	}

	/**
	 * 当按下1键时执行 当前武器置为1
	 */
	public static void press1() {
		gunInt = 1;
		MouseListener.data = data1;
		MouseListener.gunType = type1;
	}

	/**
	 * 当按下2键时执行 当前武器置为1
	 */
	public static void press2() {
		gunInt = 2;
		MouseListener.data = data2;
		MouseListener.gunType = type2;
	}

	/**
	 * 当按下C键时执行 当前姿势改为蹲或站 站0 蹲1 趴2
	 */
	public static void pressC() {
		switch (APP.posture.getText()) {
		case "站": APP.posture.setText("蹲");break;
		case "蹲": APP.posture.setText("站");break;
		default: APP.posture.setText("站");break;
		}
		APP.changeId1();
		APP.changeId2();
	}

	/**
	 * 当按下空格键时执行 当前姿势改为站 站0 蹲1 趴2
	 */
	public static void pressSpace() {
		switch (APP.posture.getText()) {
		case "站": APP.posture.setText("站");break;
		case "蹲": APP.posture.setText("站");break;
		case "趴": APP.posture.setText("站");break;
		default: APP.posture.setText("站");break;
		}
		APP.changeId1();
		APP.changeId2();
	}

	/**
	 * 当按下Z键时执行 当前姿势改为趴 站0 蹲1 趴2
	 */
	public static void pressZ() {
		// 如果当前是趴下，按Z后改为站
		// 如果当前不是趴下，按Z后改为趴
		switch (APP.posture.getText()) {
		case "站": APP.posture.setText("趴");break;
		case "蹲": APP.posture.setText("趴");break;
		case "趴": APP.posture.setText("站");break;
		default: APP.posture.setText("站");break;
		}
		APP.changeId1();
		APP.changeId2();
	}

	/**
	 * 鼠标模拟移动
	 * @param y
	 */
	public static void move(int y) {
		if (chipType == 0) {
			Simulate.m_move(0, y);
		}
		if (chipType == 1) {
			MSDK.msdk.cQb5SeO3o8qB(handle, 0, y);
		}
		if (chipType == 2) {
			YSDK.ysdk.M_Move(handle, 0, y);
		}
	}

}
