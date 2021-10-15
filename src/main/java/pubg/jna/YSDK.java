package pubg.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

import cn.hutool.core.util.HexUtil;
/**
 * 易键鼠芯片
 * @author Can
 *
 */
public interface YSDK extends Library {

	public static void main(String[] args) {
		YSDK ysdk = YSDK.ysdk;
		int openDeviceEx = ysdk.D_OpenVidPid(HexUtil.toBigInteger("C216").intValue(), HexUtil.toBigInteger("0301").intValue());
		System.out.println(openDeviceEx);
		ysdk.M_Move(openDeviceEx, 100, 100);

	}

	YSDK ysdk = (YSDK) Native.load("YSDK", YSDK.class);

	// 打开设备（指定设备ID）
	int D_OpenVidPid(int VID, int PID);

	// 设备_关闭（硬件位置）
	void D_Close(int handle);

	// 鼠标_相对移动
	void M_Move(int handle, int x, int y);
	

}
