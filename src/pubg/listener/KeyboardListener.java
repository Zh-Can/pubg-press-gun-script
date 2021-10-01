package pubg.listener;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import pubg.actuator.Actuator;

/**
 * 键盘监听器
 */
public class KeyboardListener {


	public static boolean run = true;
	
	public static void main(String[] args) {
		KeyboardListener.add();
	}
	
	public static void add() {
		// Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails 
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input

//		System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");
//		
//		for (Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
//			System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
//		}
		
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
		
			@Override 
			public void keyPressed(GlobalKeyEvent event) {
//				System.out.println(event);
//				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
//					run = false;
//				}
				switch (event.getVirtualKeyCode()) {
				case GlobalKeyEvent.VK_HOME://按下F9 开启
					Actuator.pressHome();
					break;
				case GlobalKeyEvent.VK_END://按下F10 关闭
					Actuator.pressEnd();
					break;
				case GlobalKeyEvent.VK_1://按下1 - 切1号位枪
					Actuator.press1();
					break;
				case GlobalKeyEvent.VK_2://按下2 - 切2号位枪
					Actuator.press2();
					break;
				case GlobalKeyEvent.VK_X://按下x - 收枪/放枪
					break;
				case GlobalKeyEvent.VK_C://按下c - 蹲下/站起
					Actuator.pressC();
					break;
				case GlobalKeyEvent.VK_Z://按下z
					Actuator.pressZ();
					break;
				case GlobalKeyEvent.VK_SPACE://按下空格 - 站起
					Actuator.pressSpace();
					break;
				}
				
			}
			
			@Override 
			public void keyReleased(GlobalKeyEvent event) {
//				System.out.println(event); 
			}
		});
		
		try {
			while(run) { 
				Thread.sleep(128); 
			}
		} catch(InterruptedException e) { 
			//Do nothing
		} finally {
			keyboardHook.shutdownHook(); 
		}
	}

}
