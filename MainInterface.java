package database_homework;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainInterface extends JFrame implements ActionListener, MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Connection myConnection;  // 连接
	final String MY_DB_URL = "jdbc:mysql://localhost:3306/pharmacy_ms?useSSL=false";  // 药店数据库地址
	final ImageIcon arrowIcon = new ImageIcon(MainInterface.class.getResource("arrow1.png"));
	final ImageIcon arrowIcon2 = new ImageIcon(MainInterface.class.getResource("arrow2.png"));
	final ImageIcon loadingIcon = new ImageIcon(MainInterface.class.getResource("loading.gif"));

	JLabel jlSystemName = new JLabel("·药店管理系统·");
	JLabel[] jlImg = new JLabel[5];
	JLabel[] jlImgRight = new JLabel[5];
//	JLabel jlRight = new JLabel();
	JLabel jlLoading = new JLabel();
	
	JButton jbEmployeeInfo = new JButton("1.员工信息");
	JButton jbMedicineInfo = new JButton("2.库存药品信息");
	JButton jbCounterInfo = new JButton("3.柜台药品信息");
	JButton jbSaleInfo = new JButton("4.销售信息");
	JButton jbSupplierInfo = new JButton("5.供货商信息");
	JButton jbInitDatabase = new JButton("> Start <");
	
	JPanel jpMainPanel = new JPanel();
	
	Employee myEmployee;  // 员工信息界面
	Medicine myMedicine;  // 药品库存信息
	Counter myCounter;    // 柜台药品信息
	Sale mySale;          // 销售记录信息
	Supplier mySupplier;  // 供应商信息
	
	public MainInterface() {
		this.setVisible(true);
		this.setBounds(600, 200, 600, 700);
		this.setResizable(false);
		this.setTitle("药店管理系统");
		jpMainPanel.setLayout(null);
		jpMainPanel.setBackground(new Color(255, 255, 255));
		
		for (int i = 0; i < jlImg.length; i++) {
			jlImg[i] = new JLabel();
			jlImg[i].setIcon(arrowIcon);
			jlImg[i].setFont(new Font("黑体", Font.BOLD, 20));
			jlImg[i].setBounds(50, 90+i*100, 208, 103);
			jlImg[i].setVisible(false);
			
			jlImgRight[i] = new JLabel();
			jlImgRight[i].setIcon(arrowIcon2);
			jlImgRight[i].setFont(new Font("黑体", Font.BOLD, 20));
			jlImgRight[i].setBounds(455, 90+i*100, 208, 103);
			jlImgRight[i].setVisible(false);
		}
//		jlRight.setIcon(arrowIcon2);
//		jlRight.setBounds(170, 575, 208, 103);
//		jlRight.setVisible(false);
		jlLoading.setIcon(loadingIcon);
		jlLoading.setBounds(195, 575, 228, 103);
		jlLoading.setVisible(false);
		
		jlSystemName.setFont(new Font("楷体", Font.BOLD, 35));
		jlSystemName.setBounds(155, 10, 340, 50);
		jpMainPanel.add(jlSystemName);
		
		jbEmployeeInfo.setFont(new Font("黑体", Font.PLAIN, 20));
		jbEmployeeInfo.setBounds(150, 100, 300, 75);
		jbEmployeeInfo.setEnabled(false);
//		jbEmployeeInfo.setHorizontalAlignment(SwingConstants.LEFT);
		jbEmployeeInfo.addActionListener(this);
		jbEmployeeInfo.addMouseListener(this);
		
		jbMedicineInfo.setFont(new Font("黑体", Font.PLAIN, 20));
		jbMedicineInfo.setBounds(150, 200, 300, 75);
		jbMedicineInfo.setEnabled(false);
		jbMedicineInfo.addActionListener(this);
		jbMedicineInfo.addMouseListener(this);
		
		jbCounterInfo.setFont(new Font("黑体", Font.PLAIN, 20));
		jbCounterInfo.setBounds(150, 300, 300, 75);
		jbCounterInfo.setEnabled(false);
		jbCounterInfo.addActionListener(this);
		jbCounterInfo.addMouseListener(this);
		
		jbSaleInfo.setFont(new Font("黑体", Font.PLAIN, 20));
		jbSaleInfo.setBounds(150, 400, 300, 75);
		jbSaleInfo.setEnabled(false);
		jbSaleInfo.addActionListener(this);
		jbSaleInfo.addMouseListener(this);
		
		jbSupplierInfo.setFont(new Font("黑体", Font.PLAIN, 20));
		jbSupplierInfo.setBounds(150, 500, 300, 75);
		jbSupplierInfo.setEnabled(false);
		jbSupplierInfo.addActionListener(this);
		jbSupplierInfo.addMouseListener(this);
		
		jbInitDatabase.setFont(new Font("黑体", Font.PLAIN, 20));
		jbInitDatabase.setBounds(40, 600, 130, 50);
//		jbInitDatabase.setBackground(new Color(220,20,60)); // 红色
		jbInitDatabase.setBackground(new Color(127,255,170));
		jbInitDatabase.addActionListener(this);
		jbInitDatabase.addMouseListener(this);
		
		for (int i = 0; i < jlImg.length; i++) {
			jpMainPanel.add(jlImg[i]);
			jpMainPanel.add(jlImgRight[i]);
		}
//		jpMainPanel.add(jlRight);
		jpMainPanel.add(jlLoading);
		jpMainPanel.add(jbInitDatabase);
		jpMainPanel.add(jbEmployeeInfo);
		jpMainPanel.add(jbMedicineInfo);
		jpMainPanel.add(jbCounterInfo);
		jpMainPanel.add(jbSaleInfo);
		jpMainPanel.add(jbSupplierInfo);
		this.add(jpMainPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jbInitDatabase) {
			this.jbEmployeeInfo.setEnabled(true);
			this.jbMedicineInfo.setEnabled(true);
			this.jbCounterInfo.setEnabled(true);
			this.jbSaleInfo.setEnabled(true);
			this.jbSupplierInfo.setEnabled(true);
//			this.jbInitDatabase.setBackground(new Color(127,255,170)); 绿色
			this.jbInitDatabase.setBackground(new Color(255,255,255)); // 白色
			this.jbInitDatabase.setText("Running");
			this.jbInitDatabase.setFont(new Font("黑体", Font.ITALIC, 20));
			this.jbInitDatabase.setBorderPainted(false);
			this.connectDatabase();  // 连接数据库
			this.initDatabase();  // 初始化数据库
			jbInitDatabase.setEnabled(false);
			jlLoading.setVisible(true);
		}else if (e.getSource()==jbEmployeeInfo) {  // 员工信息数据库
			this.myEmployee = new Employee(this.myConnection);
		}else if (e.getSource()==jbMedicineInfo) {  // 药品库存数据库 
			this.myMedicine = new Medicine(this.myConnection);
		}else if (e.getSource()==jbCounterInfo) {  // 柜台药品数据库
			this.myCounter = new Counter(this.myConnection);
		}else if (e.getSource()==jbSaleInfo) {  // 销售记录数据库
			this.mySale = new Sale(this.myConnection);
		}else if (e.getSource()==jbSupplierInfo) {  // 供货商数据库
			this.mySupplier = new Supplier(this.myConnection);
		}
	}
	
	public void initDatabase() {		
			String deleteString = "DELETE FROM %s WHERE %s = '%s'";
			deleteMyRow(deleteString, "employee", "e_id", "9999");
			deleteMyRow(deleteString, "medicine", "m_id", "9999");
			deleteMyRow(deleteString, "counter_log", "counter_log_id", "99999999");
			deleteMyRow(deleteString, "sale_log", "sale_id", "99999999");	
			deleteMyRow(deleteString, "supplier", "s_id", "9999");
	}
	
	public void deleteMyRow(String deleteString, String name, String key, String value) {
		try {
			deleteString = String.format(deleteString, name, key, value);
			Statement stmt = this.myConnection.createStatement();
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
			stmt.executeUpdate(deleteString);
			stmt.execute("SET FOREIGN_KEY_CHECKS =1");
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connectDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.myConnection = DriverManager.getConnection(this.MY_DB_URL, "root", "root");
		} catch (Exception e) {
			System.out.println("连接数据库时发生错误！");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainInterface mainInterface = new MainInterface();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==jbEmployeeInfo) {
			showArrowLable(0);
		}else if (e.getSource()==jbMedicineInfo) {
			showArrowLable(1);
		}else if (e.getSource()==jbCounterInfo) {
			showArrowLable(2);
		}else if (e.getSource()==jbSaleInfo) {
			showArrowLable(3);
		}else if (e.getSource()==jbSupplierInfo) {
			showArrowLable(4);
		}else if (e.getSource()==jbInitDatabase) {
//			this.jlRight.setVisible(true);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==jbEmployeeInfo) {
			hideArrowLable(0);
		}else if (e.getSource()==jbMedicineInfo) {
			hideArrowLable(1);
		}else if (e.getSource()==jbCounterInfo) {
			hideArrowLable(2);
		}else if (e.getSource()==jbSaleInfo) {
			hideArrowLable(3);
		}else if (e.getSource()==jbSupplierInfo) {
			hideArrowLable(4);
		}else if (e.getSource()==jbInitDatabase) {
//			this.jlRight.setVisible(false);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void showArrowLable(int seq) {
		this.jlImg[seq].setVisible(true);
		this.jlImgRight[seq].setVisible(true);
	}
	
	public void hideArrowLable(int seq) {
		this.jlImg[seq].setVisible(false);
		this.jlImgRight[seq].setVisible(false);
	}
}
