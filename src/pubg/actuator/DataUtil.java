package pubg.actuator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import pubg.APP;
import pubg.entity.GunData;
import pubg.listener.MouseListener;

public class DataUtil {

	public static void main(String[] args) {
		System.out.println(getGunData("M416-00000"));
	}

	/**
	 * 获取武器数据
	 * 
	 * @param id
	 * @return
	 */
	public static GunData getGunData(String id) {
		System.out.println(id);
		GunData gunData = new GunData();
		try {
			List<GunData> query = Db.use().query("select * from gpdata where id = ?", GunData.class, id);
			if (CollUtil.isNotEmpty(query)) {
				String type = Db.use().queryOne("select type from gun where name = ?", id.split("-")[0]).getStr("type");
				gunData = query.get(0);
				gunData.setType(type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gunData;
	}

	/**
	 * debug-获取压枪数据
	 * 
	 * @param name
	 * @return
	 */
	public static void getGPData(String id) {
		try {
			GunData gunData = new GunData();
			List<GunData> query = Db.use().query("select * from gpdata where id = ?", GunData.class, id);
			String type = Db.use().queryOne("select type from gun where name = ?", id.split("-")[0]).getStr("type");
			gunData = query.get(0);
			gunData.setType(type);
			String data = gunData.getData();
			if (StrUtil.isNotBlank(data)) {
				String[] datas = data.split("\\|");
				MouseListener.debug_gunType = gunData.getType();
				MouseListener.debug_data = Stream.of(gunData.getData().split("\\|")).map(Integer::parseInt).toArray(Integer[]::new);
				
				System.out.println(Arrays.toString(datas));
				APP.data0.setValue(Integer.parseInt(datas[0]));
				APP.data1.setValue(Integer.parseInt(datas[1]));
				APP.data2.setValue(Integer.parseInt(datas[2]));
				APP.data3.setValue(Integer.parseInt(datas[3]));
				APP.data4.setValue(Integer.parseInt(datas[4]));
				APP.data5.setValue(Integer.parseInt(datas[5]));
				APP.data6.setValue(Integer.parseInt(datas[6]));
				APP.data7.setValue(Integer.parseInt(datas[7]));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取基础数据
	 * 
	 * @param name
	 * @return String[]
	 */
	public static String[] getBasicData(String name) {
		String[] arr = new String[] {};
		try {
			List<String> list = new ArrayList<>();
			List<Entity> query = Db.use().query("select name from " + name + " order by id");
			for (Entity entity : query) {
				list.add(entity.getStr("name"));
			}
			arr = ArrayUtil.toArray(list, String.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * 更新数据
	 * 
	 * @param id
	 * @param data
	 */
	public static void updateData(String id, String data) {
		try {
			Db.use().execute("update gpdata set data = ? where id = ?", data.replace(" ", ""), id);
			Integer[] array = Stream.of(APP.data0.getValue(), APP.data1.getValue(), APP.data2.getValue(),
					APP.data3.getValue(), APP.data4.getValue(), APP.data5.getValue(), APP.data6.getValue(), APP.data7.getValue())
					.toArray(Integer[]::new);
			System.out.println(Arrays.toString(array));
			MouseListener.debug_data = array;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
