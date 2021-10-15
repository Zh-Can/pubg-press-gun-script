package pubg.listener;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import pubg.APP;
import pubg.actuator.Actuator;
import pubg.jna.MSDK;

public class MouseListener {
	
	private static final Log log = LogFactory.get();
	// 是否秒准中
	private static boolean isAim = false;
	// 是否射击
	private static boolean isShoot = false;

	// 是否运行
	public static boolean run = true;

	// 枪类型（0连发1单发）
	public static String gunType = "0";
	public static Integer[] data = {};
	
	// debug
	public static String debug_gunType = "0";
	public static Integer[] debug_data = {};

	public static void main(String[] args) {
		// 注册 按键绑定
		int handle = MSDK.msdk.GTQiKnPag(HexUtil.toBigInteger("0329").intValue(), HexUtil.toBigInteger("1202").intValue());
		System.out.println(handle);
		MouseListener.run = true;
		MouseListener.add();
	}

	public static void add() {
		// Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
		GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode

//		System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");
//
//		for (Entry<Long, String> mouse : GlobalMouseHook.listMice().entrySet()) {
//			System.out.format("%d: %s\n", mouse.getKey(), mouse.getValue());
//		}

		mouseHook.addMouseListener(new GlobalMouseAdapter() {

			// 鼠标按键按下
			@Override
			public void mousePressed(GlobalMouseEvent event) {
//				System.out.println(event.getButton());

				if (event.getButton() == 1) {
					isShoot = true;
				}
				if (event.getButton() == 2) {
					isAim = true;
				}
				if ((event.getButtons() & GlobalMouseEvent.BUTTON_LEFT) != GlobalMouseEvent.BUTTON_NO
						&& (event.getButtons() & GlobalMouseEvent.BUTTON_RIGHT) != GlobalMouseEvent.BUTTON_NO) {
//					System.out.println("同时按下了鼠标右键和鼠标左键，即开始射击");

					if (APP.debug.isSelected()) {
						
						// 连发武器
						if ("0".equals(debug_gunType)) {
							if (isAim == true) {
//								System.out.println(isAim + "," + isShoot);
								// 必须异步执行,否则会卡在这里
								ThreadUtil.execAsync(new Runnable() {
									@Override
									public void run() {
										int magNum = 40;
										int n = 0;
										while (isShoot == true && isAim == true && magNum > 0) {
											if (magNum == 40) {
												n = debug_data[0];
											}
											if (magNum < 40 && magNum >= 34) {
												n = debug_data[1];
											}
											if (magNum < 34 && magNum >= 27) {
												n = debug_data[2];
											}
											if (magNum < 27 && magNum >= 19) {
												n = debug_data[3];
											}
											if (magNum < 19 && magNum >= 9) {
												n = debug_data[4];
											}
											if (magNum < 9) {
												n = debug_data[5];
											}
											for (int i = 0; i < debug_data[6]; i++) {
												Actuator.move(n);
												ThreadUtil.safeSleep(debug_data[7] / debug_data[6] - 1);
											}
											magNum--;
										}
									}
								});
							}
						} else {
							// 连狙
							// 必须异步执行,否则会卡在这里
							ThreadUtil.execAsync(new Runnable() {
								@Override
								public void run() {
									int n = 0;
									while (isShoot == true && isAim == true) {
										n = debug_data[0];
										for (int i = 0; i < debug_data[6]; i++) {
											Actuator.move(n);
											ThreadUtil.safeSleep(data[7] / debug_data[6] - 1);
										}
									}
								}
							});
						}
					} else {
						// 连发武器
						if ("0".equals(gunType)) {
							if (isAim == true) {
//								System.out.println(isAim + "," + isShoot);
								// 必须异步执行,否则会卡在这里
								ThreadUtil.execAsync(new Runnable() {
									@Override
									public void run() {
										int magNum = 40;

										int n = 0;
										while (isShoot == true && isAim == true && magNum > 0) {
											log.info("{}", magNum);
											if (magNum == 40) {
												n = data[0];
											}
											if (magNum < 40 && magNum >= 34) {
												n = data[1];
											}
											if (magNum < 34 && magNum >= 27) {
												n = data[2];
											}
											if (magNum < 27 && magNum >= 19) {
												n = data[3];
											}
											if (magNum < 19 && magNum >= 9) {
												n = data[4];
											}
											if (magNum < 9) {
												n = data[5];
											}
											for (int i = 0; i < data[6]; i++) {
												Actuator.move(n);
												ThreadUtil.safeSleep(data[7] / data[6] - 1);
											}
											--magNum;
										}
									}
								});
							}
						} else {
							// 连狙
							// 必须异步执行,否则会卡在这里
							ThreadUtil.execAsync(new Runnable() {
								@Override
								public void run() {
									int n = 0;
									while (isShoot == true && isAim == true) {
										n = data[0];
										for (int i = 0; i < data[6]; i++) {
											Actuator.move(n);
											ThreadUtil.safeSleep(data[7] / data[6] - 1);
										}
									}
								}
							});
						}
					}
				}

			}

			// 鼠标按键抬起
			@Override
			public void mouseReleased(GlobalMouseEvent event) {
//				System.out.println(event);
				if (event.getButton() == 1) {
					isShoot = false;
				}
				if (event.getButton() == 2) {
					isAim = false;
				}
			}
		});

		try {
			while (run) {
				Thread.sleep(128);
			}
		} catch (InterruptedException e) {
			// Do nothing
		} finally {
			mouseHook.shutdownHook();
		}
	}

}
