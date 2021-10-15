package pubg.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

import cn.hutool.core.util.HexUtil;
/**
 * 飞易来芯片
 */
public interface MSDK extends Library {

	public static void main(String[] args) {
		MSDK mSdk = MSDK.msdk;
		System.out.println(HexUtil.toBigInteger("0329").intValue()+ "---" + HexUtil.toBigInteger("1202").intValue());
		int handle = mSdk.GTQiKnPag(HexUtil.toBigInteger("0329").intValue(),HexUtil.toBigInteger("1202").intValue());
		mSdk.cQb5SeO3o8qB(handle, 101, 101);
	}
	MSDK msdk = (MSDK)Native.load("markBox-F", MSDK.class);

	// 打开设备（指定设备ID）
	int GTQiKnPag(int vid, int pid);
	/**
	 * M_Close
	 * @param paramInt1 handle
	 * @return
	 */
	int D8dE6wlJNnD(int paramInt1);
	/**
	 * M_MoveR2
	 * @param paramInt1 handle
	 * @param paramInt2 x
	 * @param paramInt3 y
	 * @return
	 */
	int cQb5SeO3o8qB(int paramInt1, int paramInt2, int paramInt3);

}
