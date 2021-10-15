package pubg;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.setting.Setting;
import pubg.actuator.Actuator;
import pubg.actuator.DataUtil;
import pubg.entity.GunData;
import pubg.jna.MSDK;
import pubg.jna.YSDK;
import pubg.listener.KeyboardListener;
import pubg.listener.MouseListener;
import javax.swing.DropMode;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JSpinner;

public class APP {

	private JFrame frame;
	Setting cfg = new Setting(new File("config.setting"), Charset.defaultCharset(), false);
	
	// 姿势（0站，1蹲，2趴）
	public static JLabel posture;
	
	// 一号武器位
	// 1号枪
	public static JComboBox gunSelect1;
	// 1号枪-枪口
	public static JComboBox muzzleSelect1;
	// 1号枪-瞄具
	public static JComboBox sightSelect1;
	// 1号枪-握把
	public static JComboBox gripSelect1;
	// 1号枪-枪托
	public static JComboBox buttSelect1;
	
	// 2号枪
	public static JComboBox gunSelect2;
	// 1号枪-枪口
	public static JComboBox muzzleSelect2;
	// 1号枪-瞄具
	public static JComboBox sightSelect2;
	// 1号枪-握把
	public static JComboBox gripSelect2;
	// 1号枪-枪托
	public static JComboBox buttSelect2;
	
	// 状态（开启/未开启）
	public static JLabel status;
	// 芯片（无/飞易来/易键鼠）
	public static JLabel chip;
	
	
	// debug
	public static JCheckBox debug;
	public static JComboBox debug_postureSelect;
	public static JComboBox debug_gunSelect;
	public static JComboBox debug_sightSelect;
	public static JComboBox debug_muzzleSelect;
	public static JComboBox debug_gripSelect;
	public static JComboBox debug_buttSelect;
	public static JLabel debug_msg;

	public static JSpinner data0;
	public static JSpinner data1;
	public static JSpinner data2;
	public static JSpinner data3;
	public static JSpinner data4;
	public static JSpinner data5;
	public static JSpinner data6;
	public static JSpinner data7;
	
	// 武器1ID
	public static String id1 = "M416-00000";
	// 武器2ID
	public static String id2 = "M416-00000";
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					APP window = new APP();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public APP() {
		initialize();
		basicDataInit();
		chipInit(cfg.getInt("chip"));
		Actuator.data1 = Stream.of(DataUtil.getGunData("M416-00000").getData().split("\\|")).map(Integer::parseInt).toArray(Integer[]::new);
		Actuator.data2 = Actuator.data1;
		MouseListener.data = Actuator.data1;
		
		ThreadUtil.execAsync(new Runnable() {
			
			@Override
			public void run() {
				KeyboardListener.add();
				KeyboardListener.run = true;
			}
		});
	}

	/**
	 * 芯片初始化
	 */
	public static void chipInit(Integer type) {
		
		if (type != null) {
			
			if (type == 1) {
				// 飞易来
				int handle = MSDK.msdk.GTQiKnPag(HexUtil.toBigInteger("0329").intValue(), HexUtil.toBigInteger("1202").intValue());
				if (handle > 0) {
					Actuator.handle = handle;
					Actuator.chipType = 1;
					APP.chip.setText("飞易来芯片");
					APP.chip.setForeground(new Color(0, 0, 255));
					return;
				}
			} else {
				// 模拟
				Actuator.chipType = 0;
				APP.chip.setText("模拟鼠标");
				APP.chip.setForeground(new Color(0, 0, 0));
			}
			
			if (type == 2) {
				// 易键鼠
				int handle = 0;
				handle = YSDK.ysdk.D_OpenVidPid(HexUtil.toBigInteger("C216").intValue(), HexUtil.toBigInteger("0301").intValue());
				if (handle > 0) {
					Actuator.handle = handle;
					Actuator.chipType = 2;
					APP.chip.setText("易键鼠芯片");
					APP.chip.setForeground(new Color(0, 0, 255));
					return;
				}
			} else {
				// 模拟
				Actuator.chipType = 0;
				APP.chip.setText("模拟鼠标");
				APP.chip.setForeground(new Color(0, 0, 0));
			}
			
		} else {
			// 模拟
			Actuator.chipType = 0;
			APP.chip.setText("模拟鼠标");
			APP.chip.setForeground(new Color(0, 0, 0));
			System.out.println(APP.chip.getText());
		}
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\zby03\\Desktop\\test\\bitbug_favicon.ico"));
		frame.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//关闭窗口，关闭芯片连接
				if (Actuator.chipType == 1) {
					MSDK.msdk.D8dE6wlJNnD(Actuator.handle);
				}
				if (Actuator.chipType == 2) {
					YSDK.ysdk.D_Close(Actuator.handle);
				}
			}
		});
		frame.setTitle(RandomUtil.randomString(32));
		frame.setBackground(new Color(255, 255, 255));
		frame.setResizable(false);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int w = 758;
		int h = 554;
		int x = (int) (toolkit.getScreenSize().getWidth() - w) / 2;
		int y = (int) (toolkit.getScreenSize().getHeight() - h) / 2;
		frame.setBounds(x, y, 847, 532);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		
		JPanel dubug_panel = new JPanel();
		dubug_panel.setBackground(new Color(255, 255, 255));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dubug_panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
				.addComponent(dubug_panel, GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
		);
		dubug_panel.setLayout(null);
		
		debug = new JCheckBox("调试模式");
		debug.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		debug.setBackground(new Color(255, 255, 255));
		debug.setBounds(6, 6, 95, 37);
		dubug_panel.add(debug);
		
		JLabel degbug_postureLabel = new JLabel("姿势");
		degbug_postureLabel.setVerticalAlignment(SwingConstants.TOP);
		degbug_postureLabel.setHorizontalAlignment(SwingConstants.CENTER);
		degbug_postureLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		degbug_postureLabel.setBounds(114, 69, 54, 23);
		dubug_panel.add(degbug_postureLabel);
		
		debug_postureSelect = new JComboBox();
		debug_postureSelect.setBounds(178, 69, 101, 23);
		dubug_panel.add(debug_postureSelect);
		
		JLabel debug_gunLabel = new JLabel("武器");
		debug_gunLabel.setVerticalAlignment(SwingConstants.TOP);
		debug_gunLabel.setHorizontalAlignment(SwingConstants.CENTER);
		debug_gunLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		debug_gunLabel.setBounds(114, 114, 54, 23);
		dubug_panel.add(debug_gunLabel);
		
		debug_gunSelect = new JComboBox();
		debug_gunSelect.setBounds(178, 114, 101, 23);
		dubug_panel.add(debug_gunSelect);
		
		JLabel debug_sightLabel = new JLabel("瞄具");
		debug_sightLabel.setVerticalAlignment(SwingConstants.TOP);
		debug_sightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		debug_sightLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		debug_sightLabel.setBounds(114, 161, 54, 23);
		dubug_panel.add(debug_sightLabel);
		
		debug_sightSelect = new JComboBox();
		debug_sightSelect.setBounds(178, 161, 101, 23);
		dubug_panel.add(debug_sightSelect);
		
		debug_muzzleSelect = new JComboBox();
		debug_muzzleSelect.setBounds(178, 208, 101, 23);
		dubug_panel.add(debug_muzzleSelect);
		
		JLabel debug_muzzleLabel = new JLabel("枪口");
		debug_muzzleLabel.setVerticalAlignment(SwingConstants.TOP);
		debug_muzzleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		debug_muzzleLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		debug_muzzleLabel.setBounds(114, 208, 54, 23);
		dubug_panel.add(debug_muzzleLabel);
		
		JLabel debug_gripLabel = new JLabel("握把");
		debug_gripLabel.setVerticalAlignment(SwingConstants.TOP);
		debug_gripLabel.setHorizontalAlignment(SwingConstants.CENTER);
		debug_gripLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		debug_gripLabel.setBounds(114, 254, 54, 23);
		dubug_panel.add(debug_gripLabel);
		
		debug_gripSelect = new JComboBox();
		debug_gripSelect.setBounds(178, 254, 101, 23);
		dubug_panel.add(debug_gripSelect);
		
		JLabel debug_buttLabel = new JLabel("枪托");
		debug_buttLabel.setVerticalAlignment(SwingConstants.TOP);
		debug_buttLabel.setHorizontalAlignment(SwingConstants.CENTER);
		debug_buttLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		debug_buttLabel.setBounds(114, 300, 54, 23);
		dubug_panel.add(debug_buttLabel);
		
		debug_buttSelect = new JComboBox();
		debug_buttSelect.setBounds(178, 300, 101, 23);
		dubug_panel.add(debug_buttSelect);
		
		JLabel debug_dataLabel0 = new JLabel("40");
		debug_dataLabel0.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel0.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel0.setBounds(11, 362, 40, 17);
		dubug_panel.add(debug_dataLabel0);
		
		JLabel debug_dataLabel1 = new JLabel("40-34");
		debug_dataLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel1.setBounds(62, 362, 40, 17);
		dubug_panel.add(debug_dataLabel1);
		
		JLabel debug_dataLabel3 = new JLabel("27-19");
		debug_dataLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel3.setBounds(164, 362, 40, 17);
		dubug_panel.add(debug_dataLabel3);
		
		JLabel debug_dataLabel2 = new JLabel("34-27");
		debug_dataLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel2.setBounds(113, 362, 40, 17);
		dubug_panel.add(debug_dataLabel2);
		
		JLabel debug_dataLabel5 = new JLabel("9");
		debug_dataLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel5.setBounds(266, 362, 40, 17);
		dubug_panel.add(debug_dataLabel5);
		
		JLabel debug_dataLabel4 = new JLabel("19-9");
		debug_dataLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel4.setBounds(215, 362, 40, 17);
		dubug_panel.add(debug_dataLabel4);
		
		JLabel debug_dataLabel7 = new JLabel("time");
		debug_dataLabel7.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel7.setBounds(368, 362, 40, 17);
		dubug_panel.add(debug_dataLabel7);
		
		JLabel debug_dataLabel6 = new JLabel("count");
		debug_dataLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		debug_dataLabel6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		debug_dataLabel6.setBounds(317, 362, 40, 17);
		dubug_panel.add(debug_dataLabel6);
		
		JButton getDataButton = new JButton("获取数据");
		getDataButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String id = debug_gunSelect.getSelectedItem().toString()+ "-" + debug_muzzleSelect.getSelectedIndex() + debug_gripSelect.getSelectedIndex()
				 + debug_buttSelect.getSelectedIndex() + debug_postureSelect.getSelectedIndex() + debug_sightSelect.getSelectedIndex();
				DataUtil.getGPData(id);
				ThreadUtil.execAsync(new Runnable() {
					@Override
					public void run() {
						debug_msg.setText("获取成功");
						ThreadUtil.safeSleep(1000);
						debug_msg.setText("");
					}
				});
			}
		});
		getDataButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		getDataButton.setBounds(6, 445, 107, 37);
		dubug_panel.add(getDataButton);
		
		JButton updateDataButton = new JButton("更新数据");
		updateDataButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String id = debug_gunSelect.getSelectedItem().toString()+ "-" + debug_muzzleSelect.getSelectedIndex() + debug_gripSelect.getSelectedIndex()
				 + debug_buttSelect.getSelectedIndex() + debug_postureSelect.getSelectedIndex() + debug_sightSelect.getSelectedIndex();
				String data = data0.getValue() + "|" + data1.getValue() + "|" + data2.getValue() + "|" + data3.getValue() + "|" +
						data4.getValue() + "|" + data5.getValue() + "|" + data6.getValue() + "|" + data7.getValue();
				DataUtil.updateData(id, data);
				ThreadUtil.execAsync(new Runnable() {
					@Override
					public void run() {
						debug_msg.setText("更新成功");
						ThreadUtil.safeSleep(1000);
						debug_msg.setText("");
					}
				});
			}
		});
		updateDataButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		updateDataButton.setBounds(303, 445, 105, 37);
		dubug_panel.add(updateDataButton);
		
		data7 = new JSpinner();
		data7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data7.setBounds(371, 389, 45, 22);
		dubug_panel.add(data7);
		
		data5 = new JSpinner();
		data5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data5.setBounds(267, 389, 45, 22);
		dubug_panel.add(data5);
		
		data4 = new JSpinner();
		data4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data4.setBounds(215, 389, 45, 22);
		dubug_panel.add(data4);
		
		data3 = new JSpinner();
		data3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data3.setBounds(163, 389, 45, 22);
		dubug_panel.add(data3);
		
		data2 = new JSpinner();
		data2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data2.setBounds(111, 389, 45, 22);
		dubug_panel.add(data2);
		
		data1 = new JSpinner();
		data1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data1.setBounds(59, 389, 45, 22);
		dubug_panel.add(data1);
		
		data0 = new JSpinner();
		data0.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data0.setBounds(7, 389, 45, 22);
		dubug_panel.add(data0);
		
		data6 = new JSpinner();
		data6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		data6.setBounds(319, 389, 45, 22);
		dubug_panel.add(data6);
		
		debug_msg = new JLabel("");
		debug_msg.setBounds(185, 458, 54, 15);
		dubug_panel.add(debug_msg);
		panel.setLayout(null);
		
		JLabel title = new JLabel("PUBG压枪1.0");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
		title.setBounds(10, 46, 391, 59);
		panel.add(title);
		
		JLabel postureLabel = new JLabel("姿势：");
		postureLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		postureLabel.setBounds(150, 115, 54, 34);
		panel.add(postureLabel);
		
		posture = new JLabel("站");
		posture.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		posture.setBounds(217, 115, 54, 34);
		panel.add(posture);
		
		JLabel label1 = new JLabel("1号武器位");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		label1.setBounds(10, 159, 193, 34);
		panel.add(label1);
		
		JLabel label2 = new JLabel("2号武器位");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		label2.setBounds(208, 159, 193, 34);
		panel.add(label2);
		
		gunSelect1 = new JComboBox();
		gunSelect1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId1();
				}
			}
		});
		gunSelect1.setBounds(74, 203, 101, 23);
		panel.add(gunSelect1);
		
		JLabel gunLable1 = new JLabel("武器");
		gunLable1.setHorizontalAlignment(SwingConstants.CENTER);
		gunLable1.setVerticalAlignment(SwingConstants.TOP);
		gunLable1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		gunLable1.setBounds(10, 203, 54, 23);
		panel.add(gunLable1);
		
		JLabel gunLable2 = new JLabel("武器");
		gunLable2.setHorizontalAlignment(SwingConstants.CENTER);
		gunLable2.setVerticalAlignment(SwingConstants.TOP);
		gunLable2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		gunLable2.setBounds(236, 203, 54, 23);
		panel.add(gunLable2);
		
		gunSelect2 = new JComboBox();
		gunSelect2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId2();
				}
			}
		});
		gunSelect2.setBounds(300, 203, 101, 23);
		panel.add(gunSelect2);
		
		JLabel sightLabel2 = new JLabel("瞄具");
		sightLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		sightLabel2.setVerticalAlignment(SwingConstants.TOP);
		sightLabel2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		sightLabel2.setBounds(236, 248, 54, 23);
		panel.add(sightLabel2);
		
		sightSelect2 = new JComboBox();
		sightSelect2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId2();
				}
			}
		});
		sightSelect2.setBounds(300, 248, 101, 23);
		panel.add(sightSelect2);
		
		JLabel sightLabel1 = new JLabel("瞄具");
		sightLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		sightLabel1.setVerticalAlignment(SwingConstants.TOP);
		sightLabel1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		sightLabel1.setBounds(10, 248, 54, 23);
		panel.add(sightLabel1);
		
		sightSelect1 = new JComboBox();
		sightSelect1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId1();
				}
			}
		});
		sightSelect1.setBounds(74, 248, 101, 23);
		panel.add(sightSelect1);
		
		JLabel muzzleLabel2 = new JLabel("枪口");
		muzzleLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		muzzleLabel2.setVerticalAlignment(SwingConstants.TOP);
		muzzleLabel2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		muzzleLabel2.setBounds(236, 295, 54, 23);
		panel.add(muzzleLabel2);
		
		muzzleSelect2 = new JComboBox();
		muzzleSelect2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId2();
				}
			}
		});
		muzzleSelect2.setBounds(300, 295, 101, 23);
		panel.add(muzzleSelect2);
		
		JLabel muzzleLabel1 = new JLabel("枪口");
		muzzleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		muzzleLabel1.setVerticalAlignment(SwingConstants.TOP);
		muzzleLabel1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		muzzleLabel1.setBounds(10, 295, 54, 23);
		panel.add(muzzleLabel1);
		
		muzzleSelect1 = new JComboBox();
		muzzleSelect1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId1();
				}
			}
		});
		muzzleSelect1.setBounds(74, 295, 101, 23);
		panel.add(muzzleSelect1);
		
		JLabel gripLabel2 = new JLabel("握把");
		gripLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		gripLabel2.setVerticalAlignment(SwingConstants.TOP);
		gripLabel2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		gripLabel2.setBounds(236, 342, 54, 23);
		panel.add(gripLabel2);
		
		gripSelect2 = new JComboBox();
		gripSelect2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId2();
				}
			}
		});
		gripSelect2.setBounds(300, 342, 101, 23);
		panel.add(gripSelect2);
		
		JLabel gripLabel1 = new JLabel("握把");
		gripLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		gripLabel1.setVerticalAlignment(SwingConstants.TOP);
		gripLabel1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		gripLabel1.setBounds(10, 342, 54, 23);
		panel.add(gripLabel1);
		
		gripSelect1 = new JComboBox();
		gripSelect1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId1();
				}
			}
		});
		gripSelect1.setBounds(74, 342, 101, 23);
		panel.add(gripSelect1);
		
		JLabel buttLabel2 = new JLabel("枪托");
		buttLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		buttLabel2.setVerticalAlignment(SwingConstants.TOP);
		buttLabel2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		buttLabel2.setBounds(236, 388, 54, 23);
		panel.add(buttLabel2);
		
		buttSelect2 = new JComboBox();
		buttSelect2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId2();
				}
			}
		});
		buttSelect2.setBounds(300, 388, 101, 23);
		panel.add(buttSelect2);
		
		JLabel buttLabel1 = new JLabel("枪托");
		buttLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		buttLabel1.setVerticalAlignment(SwingConstants.TOP);
		buttLabel1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		buttLabel1.setBounds(10, 388, 54, 23);
		panel.add(buttLabel1);
		
		buttSelect1 = new JComboBox();
		buttSelect1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					changeId1();
				}
			}
		});
		buttSelect1.setBounds(74, 388, 101, 23);
		panel.add(buttSelect1);
		
		JLabel statusLabel = new JLabel("状态(Home开启 / End关闭)：");
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		statusLabel.setBounds(10, 463, 193, 17);
		panel.add(statusLabel);
		
		JLabel chipLabel = new JLabel("芯片识别(无/飞易来/易键鼠)：");
		chipLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chipLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		chipLabel.setBounds(11, 446, 193, 17);
		panel.add(chipLabel);
		
		chip = new JLabel("未初始化");
		chip.setHorizontalAlignment(SwingConstants.LEFT);
		chip.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		chip.setBounds(208, 446, 109, 17);
		panel.add(chip);
		
		status = new JLabel("未启动");
		status.setHorizontalAlignment(SwingConstants.LEFT);
		status.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		status.setBounds(208, 463, 109, 17);
		panel.add(status);
		frame.getContentPane().setLayout(groupLayout);
		
	}
	
	private static void basicDataInit() {
		String[] guns = DataUtil.getBasicData("gun");
		String[] muzzles = DataUtil.getBasicData("muzzle");
		String[] sights = DataUtil.getBasicData("sight");
		String[] grips = DataUtil.getBasicData("grip");
		String[] butts = DataUtil.getBasicData("butt");
		String[] postures = DataUtil.getBasicData("posture");
		
		gunSelect1.setModel(new DefaultComboBoxModel(guns));
		gunSelect2.setModel(new DefaultComboBoxModel(guns));
		
		muzzleSelect1.setModel(new DefaultComboBoxModel(muzzles));
		muzzleSelect2.setModel(new DefaultComboBoxModel(muzzles));
		
		sightSelect1.setModel(new DefaultComboBoxModel(sights));
		sightSelect2.setModel(new DefaultComboBoxModel(sights));
		
		gripSelect1.setModel(new DefaultComboBoxModel(grips));
		gripSelect2.setModel(new DefaultComboBoxModel(grips));
		
		buttSelect1.setModel(new DefaultComboBoxModel(butts));
		buttSelect2.setModel(new DefaultComboBoxModel(butts));
		
		
		//debug
		debug_gunSelect.setModel(new DefaultComboBoxModel(guns));
		debug_sightSelect.setModel(new DefaultComboBoxModel(sights));
		debug_muzzleSelect.setModel(new DefaultComboBoxModel(muzzles));
		debug_gripSelect.setModel(new DefaultComboBoxModel(grips));
		debug_buttSelect.setModel(new DefaultComboBoxModel(butts));
		debug_postureSelect.setModel(new DefaultComboBoxModel(postures));
		
	}
	
	public static void changeId1() {
		String postureId = "0";
		switch (posture.getText()) {
			case "站": postureId = "0";break;
			case "蹲": postureId = "1";break;
			case "趴": postureId = "2";break;
			default: postureId = "0";break;
		}
		id1 = gunSelect1.getSelectedItem().toString()+ "-" + muzzleSelect1.getSelectedIndex() + gripSelect1.getSelectedIndex()
		 + buttSelect1.getSelectedIndex() + postureId + sightSelect1.getSelectedIndex();
//		System.out.println(id1);
		GunData gunData = DataUtil.getGunData(id1);
		System.out.println(gunData);
		Actuator.type1 = gunData.getType();
		Actuator.data1 = Stream.of(gunData.getData().split("\\|")).map(Integer::parseInt).toArray(Integer[]::new);
		if (Actuator.gunInt == 1) {
			MouseListener.data = Actuator.data1;
		} else {
			MouseListener.data = Actuator.data2;
		}
	}
	public static void changeId2() {
		String postureId = "0";
		switch (posture.getText()) {
		case "站": postureId = "0";break;
		case "蹲": postureId = "1";break;
		case "趴": postureId = "2";break;
		default: postureId = "0";break;
		}
		id2 = gunSelect2.getSelectedItem().toString()+ "-" + muzzleSelect2.getSelectedIndex() + gripSelect2.getSelectedIndex()
		+ buttSelect2.getSelectedIndex() + postureId + sightSelect2.getSelectedIndex();
//		System.out.println(id2);
		GunData gunData = DataUtil.getGunData(id2);
		
		Actuator.type2 = gunData.getType();
		Actuator.data2 = Stream.of(gunData.getData().split("\\|")).map(Integer::parseInt).toArray(Integer[]::new);
		if (Actuator.gunInt == 1) {
			MouseListener.data = Actuator.data1;
		} else {
			MouseListener.data = Actuator.data2;
		}
	}
}
