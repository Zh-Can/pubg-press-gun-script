package pubg.entity;

public class GunData {
	/**
	 * M416-00001
	 */
	private String id;

	/**
	 * 压枪数据
	 */
	private String data;

	/**
	 * 压枪备注
	 */
	private String info;
	/**
	 * 武器类型
	 */
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "GunData [id=" + id + ", data=" + data + ", info=" + info + ", type=" + type + "]";
	}

	
}