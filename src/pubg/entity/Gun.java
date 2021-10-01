package pubg.entity;

public class Gun {
	/**
	 * 武器
	 */
	private int gun = 0;
	/**
	 * 武器类型(0步枪，1连狙)
	 */
	private int type = 0;
	/**
	 * 枪口
	 */
	private int muzzle = 0;
	/**
	 * 握把
	 */
	private int grip = 0;

	/**
	 * 瞄准镜
	 */
	private int sight = 0;
	/**
	 * 枪托
	 */
	private int buttstock = 0;
	/**
	 * 姿势（0站1蹲2趴）
	 */
	private int posture = 0;
	/**
	 * 压枪数据
	 */
	private PGData pgData;

	/**
	 * 弹夹
	 */
//    private String clip;

	public int getGun() {
		return gun;
	}

	public void setGun(int gun) {
		this.gun = gun;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMuzzle() {
		return muzzle;
	}

	public void setMuzzle(int muzzle) {
		this.muzzle = muzzle;
	}

	public int getGrip() {
		return grip;
	}

	public void setGrip(int grip) {
		this.grip = grip;
	}

	public int getSight() {
		return sight;
	}

	public void setSight(int sight) {
		this.sight = sight;
	}

	public int getButtstock() {
		return buttstock;
	}

	public void setButtstock(int buttstock) {
		this.buttstock = buttstock;
	}

	public PGData getPgData() {
		return pgData;
	}

	public void setPgData(PGData pgData) {
		this.pgData = pgData;
	}

	public int getPosture() {
		return posture;
	}

	public void setPosture(int posture) {
		this.posture = posture;
	}

}
