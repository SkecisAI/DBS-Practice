package database_homework;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Counter extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	Connection myConnection = null;  // 连接
	String querySqlAll = "SELECT * FROM Counter";
	
	JLabel jlCounterInfo = new JLabel("柜台药品信息");
	JLabel jlDay = new JLabel("日*");
	JLabel jlMonth = new JLabel("月*");
	JLabel jlStat = new JLabel("<-统计结果->");
	
    JTable jtCounterMedicine = new JTable();
    JTable jtQueryResult = new JTable();
    JTable jtAddCounterLog = new JTable();
    JTable jtMname = new JTable();
    JTable jtDate = new JTable();
    JTable jtLog = new JTable();
    JTable jtAmountMoney = new JTable();
    
    DefaultTableModel counterMedicineModel;
    DefaultTableModel resultModel;
    DefaultTableModel insertModel;
    DefaultTableModel mnameModel;
    DefaultTableModel dateModel;
    DefaultTableModel logModel;
    DefaultTableModel amountMoneyModel;
    
    JButton jbAdd = new JButton("入柜");
    JButton jbQuery = new JButton("柜台查询 ->");
    JButton jbQueryLog = new JButton("<- 入柜记录查询*");
    JComboBox<Object> comboBox;
	JComboBox<Object> comboBoxYear1;
	JComboBox<Object> comboBoxDay1;
    JTextField jtfQueryCond = new JTextField();
    JPanel jpMain = new JPanel();
    JScrollPane jspTable;
    JScrollPane jspTable2;
    JScrollPane jspTable3;
    JScrollPane jspMname;
    JScrollPane jspDate;
    JScrollPane jspLog;
    JScrollPane jspAmountMoney;
    
    Vector<Vector<Object>> vtCounterMedicine = new Vector<>();
    Vector<Vector<Object>> vtQueryResult = new Vector<>();
    Vector<Vector<Object>> vtInsertColumn = new Vector<>();
    Vector<Vector<Object>> vtMname = new Vector<>();
    Vector<Vector<Object>> vtLog = new Vector<>();
    Vector<Vector<Object>> vtAmountMoney = new Vector<>();
    Vector<Object> tableHeader = new Vector<>();
    Vector<Object> insertHeader = new Vector<>();
    Vector<Object> mNameHeader = new Vector<>();
    Vector<Object> dateHeader = new Vector<>();
    Vector<Object> logHeader = new Vector<>();
    Vector<Object> amountMoneyHeader;
    
    public Counter(Connection conn)
    {
    	this.myConnection = conn;  // 数据库连接
        this.setBounds(300, 200, 700, 400);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("柜台药品信息");
        jpMain.setLayout(null);
        
        jlCounterInfo.setFont(new Font("宋体", Font.BOLD, 20));
        jlCounterInfo.setBounds(290, 0, 150, 50);
        
        jlStat.setFont(new Font("宋体", Font.BOLD, 15));
        jlStat.setBounds(310, 435, 150, 50);

        tableHeader.add("药品id");
        tableHeader.add("借款状态");
        tableHeader.add("柜台名称");
        tableHeader.add("数量");
        
        mNameHeader.add("药品id");
        mNameHeader.add("借款状态");
        mNameHeader.add("柜台名称");
        mNameHeader.add("数量");
        mNameHeader.add("品名");
        
        dateHeader.add("药品id");
        dateHeader.add("借款状态");
        dateHeader.add("柜台名称");
        dateHeader.add("数量");
        dateHeader.add("有效期");
        
        logHeader.add("流水号id");
        logHeader.add("药品id");
        logHeader.add("记录人id");
        logHeader.add("入柜日期");
        logHeader.add("柜台名称");
        logHeader.add("入柜数量");
        
		// 第一行月
		DefaultComboBoxModel<Object> comboBoxYearModel1 = new DefaultComboBoxModel<Object>();
		for (int i = 1; i <= 12; i++) {
			comboBoxYearModel1.addElement(String.valueOf(i));
		}
		comboBoxYear1 = new JComboBox<Object>(comboBoxYearModel1);
		comboBoxYear1.setBounds(370, 300, 80, 20);
		comboBoxYear1.setFont(new Font("黑体", Font.BOLD, 15));
		jlMonth.setBounds(460, 285, 50, 50);
		// 第二行日
		DefaultComboBoxModel<Object> comboBoxDayModel1 = new DefaultComboBoxModel<Object>();
		for (int i = 1; i <= 31; i++) {
			comboBoxDayModel1.addElement(String.valueOf(i));
		}
		comboBoxDay1 = new JComboBox<Object>(comboBoxDayModel1);
		comboBoxDay1.setBounds(370, 325, 80, 20);
		comboBoxDay1.setFont(new Font("黑体", Font.BOLD, 15));
		jlDay.setBounds(460, 310, 50, 50);
        
        this.readEmployeeData();  // 读取数据库到表格
        jtCounterMedicine.setModel(counterMedicineModel);
        jspTable = new JScrollPane(jtCounterMedicine);
        jspTable.setBounds(15, 40, 660, 200);
        
        jbAdd.setBounds(15, 250, 80, 40);
        jbAdd.setFont(new Font("黑体", Font.BOLD, 20));
        jbAdd.addActionListener(this);
        
        jbQuery.setBounds(15, 300, 150, 40);
        jbQuery.setFont(new Font("黑体", Font.BOLD, 15));
        jbQuery.addActionListener(this);
        
        jbQueryLog.setBounds(500, 300, 180, 40);
        jbQueryLog.setFont(new Font("黑体", Font.BOLD, 15));
        jbQueryLog.addActionListener(this);
        
        DefaultComboBoxModel<Object> comboBoxModel = new DefaultComboBoxModel<Object>();
        comboBoxModel.addElement("-药品id*");
        comboBoxModel.addElement("日期前*");
        comboBoxModel.addElement("记录人id*");
        comboBoxModel.addElement("流水号id*");
        comboBoxModel.addElement("-品名");
        comboBoxModel.addElement("-结款状态");
        comboBoxModel.addElement("-柜台名称");
        comboBoxModel.addElement("有效期");
        comboBoxModel.addElement("全部记录*");
        comboBox = new JComboBox<Object>(comboBoxModel);
        comboBox.setBounds(170, 300, 100, 40);
        comboBox.setFont(new Font("黑体", Font.BOLD, 15));
        
        jtfQueryCond.setFont(new Font("黑体", Font.BOLD, 18));
        jtfQueryCond.setBounds(280, 300, 80, 40);
        
        jspTable2 = new JScrollPane(jtQueryResult);
        jspTable2.setBounds(15, 350, 660, 100);
        jspTable2.setVisible(false);
        
        insertHeader.add("流水号id");
        insertHeader.add("药品id");
        insertHeader.add("记录人id");
        insertHeader.add("入柜日期");
        insertHeader.add("柜台名称");
        insertHeader.add("入柜数量");
	    Vector<Object> rowData = new Vector<>();
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    vtInsertColumn.add(rowData);
	    insertModel = new DefaultTableModel(vtInsertColumn, insertHeader);
	    jtAddCounterLog = new JTable(insertModel);

	    
	    jspTable3 = new JScrollPane(jtAddCounterLog);
	    jspTable3.setBounds(100, 250, 580, 40);
	    
	    jspMname = new JScrollPane(jtMname);
        jspMname.setBounds(15, 350, 660, 100);
        jspMname.setVisible(false);
        
	    jspDate = new JScrollPane(jtDate);
        jspDate.setBounds(15, 350, 660, 100);
        jspDate.setVisible(false);
        
	    jspLog = new JScrollPane(jtLog);
        jspLog.setBounds(15, 350, 660, 100);
        jspLog.setVisible(false);
        
        jspAmountMoney = new JScrollPane(jtAmountMoney);
        jspAmountMoney.setBounds(15, 470, 660, 100);
        jspLog.setVisible(false);
        
        jpMain.add(jbAdd);
        jpMain.add(jbQuery);
        jpMain.add(jbQueryLog);
        jpMain.add(jlCounterInfo);
        jpMain.add(jlStat);
        jpMain.add(jlMonth);
        jpMain.add(jlDay);
        jpMain.add(jspTable);
        jpMain.add(jspTable2);
        jpMain.add(jspTable3);
        jpMain.add(jspMname);
        jpMain.add(jspDate);
        jpMain.add(jspLog);
        jpMain.add(jspAmountMoney);
        jpMain.add(comboBox);
        jpMain.add(comboBoxDay1);
        jpMain.add(comboBoxYear1);
        jpMain.add(jtfQueryCond);
        
        this.add(jpMain);
    }
    
    public void readEmployeeData() {  // 读取数据到表格
		DefaultTableModel model = (DefaultTableModel) jtCounterMedicine.getModel();
		int row_count = model.getRowCount();
		for (int i = row_count - 1; i >= 0; i--) {
			model.removeRow(i);
		}
    	try {
    		Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(this.querySqlAll);
    		while (rs.next()) {
    		    Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("settle_status"));
				rowData.add(rs.getString("c_name"));
				rowData.add(rs.getString("amount"));
				vtCounterMedicine.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	this.counterMedicineModel = new DefaultTableModel(this.vtCounterMedicine, this.tableHeader) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column)
            {
                return false;  // 设置不可编辑
            }
    	};
	}
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource()==jbAdd) {
			String queryString = "INSERT INTO counter_log " + 
					"VALUES ('%s', '%s', %s, '%s', '%s', %s)";
		    String[] insertStrings = {"99999999", "9999", "9999", "2099-01-01", "A", "999"};
    		for (int i = 0; i < 6; i++) {
    			insertStrings[i] = this.jtAddCounterLog.getValueAt(0, i).toString();
			}
    		if (this.jtAddCounterLog.getValueAt(0, 0)=="") {
				queryString = String.format(queryString, "99999999", "9999", "9999", "2099-01-01", "A", "999");
			}else {
	    		queryString = String.format(queryString,
	    				insertStrings[0],
	    				insertStrings[1],
	    				insertStrings[2],
	    				insertStrings[3],
	    				insertStrings[4],
	    				insertStrings[5],
	    				insertStrings[6]);
			}
    		try {
				Statement stmt = this.myConnection.createStatement();
	    		stmt.executeUpdate(queryString);
	    		stmt.close();
	    		JOptionPane.showMessageDialog(null, "插入成功 ^_^  重新载入数据库！");  
	    		this.readEmployeeData();
//	    		this.dispose();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, 
						"插入记录失败! x_x\nInfo: "+	e1.getMessage(), 
						"标题",JOptionPane.ERROR_MESSAGE);
			}
		}else if (e.getSource()==jbQuery) {   // 查询
			String queryString = "SELECT * FROM counter WHERE %s='%s'";
			String queryCondString = comboBox.getSelectedItem().toString();
    		// 清空表格
			DefaultTableModel model = (DefaultTableModel) jtQueryResult.getModel();
			int row_count = model.getRowCount();
			for (int i = row_count - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			DefaultTableModel model1 = (DefaultTableModel) jtMname.getModel();
			int row_count1 = model1.getRowCount();
			for (int i = row_count1 - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			DefaultTableModel model2 = (DefaultTableModel) jtDate.getModel();
			int row_count2 = model2.getRowCount();
			for (int i = row_count2 - 1; i >= 0; i--) {
				model2.removeRow(i);
			}
			DefaultTableModel model3 = (DefaultTableModel) jtAmountMoney.getModel();
			int row_count3 = model3.getRowCount();
			for (int i = row_count3 - 1; i >= 0; i--) {
				model3.removeRow(i);
			}
			
			int sign = 0;
			if (queryCondString == "-药品id*") {
				queryString = String.format(queryString, "m_id", jtfQueryCond.getText());
				sign = executeSQL(queryString);
				if (sign == 1) {
					statInfo("药品id", "m_id");  // 统计数量
				}
			}else if (queryCondString == "-品名") {
				queryString = "SELECT counter.m_id, counter.settle_status, counter.c_name, counter.amount, medicine.m_name\r\n" + 
						"FROM counter, medicine\r\n" + 
						"WHERE counter.m_id = medicine.m_id AND %s='%s'";
				queryString = String.format(queryString, " medicine.m_name", jtfQueryCond.getText());
				sign = executeOtherSQL(queryString, "m_name");
				if (sign == 1) {
					statInfo("品名", "m_name");  // 统计数量
				}
			}else if (queryCondString == "-结款状态") {
				queryString = String.format(queryString, "settle_status", jtfQueryCond.getText());
				sign = executeSQL(queryString); 
				if (sign == 1) {
					statInfo("结款状态", "settle_status");  // 统计数量
				}
			}else if (queryCondString == "-柜台名称") {
				queryString = String.format(queryString, "c_name", jtfQueryCond.getText());
				sign = executeSQL(queryString);
				if (sign == 1) {
					statInfo("柜台名称", "c_name");  // 统计数量
				}
			}else if (queryCondString == "有效期") {
				queryString = "SELECT counter.m_id, counter.settle_status, counter.c_name, counter.amount, medicine.expiry_date\r\n" + 
						"FROM counter, medicine\r\n" + 
						"WHERE counter.m_id = medicine.m_id AND medicine.expiry_date <= '2020-%s-%s'";
				queryString = String.format(queryString, 
						comboBoxYear1.getSelectedItem().toString(), 
						comboBoxDay1.getSelectedItem().toString());
				executeOtherSQL(queryString, "expiry_date");
			}
		}else if (e.getSource()==jbQueryLog) {
			String queryString = "SELECT *\r\n" + 
					"FROM counter_log\r\n" + 
					"WHERE %s='%s' AND counter_date <= '2019-%s-%s'";
			String queryCondString = comboBox.getSelectedItem().toString();
			
    		// 清空表格
			DefaultTableModel model = (DefaultTableModel) jtLog.getModel();
			int row_count = model.getRowCount();
			for (int i = row_count - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			if (queryCondString == "-药品id*") {
				queryString = String.format(queryString, "m_id", jtfQueryCond.getText(), 
						comboBoxYear1.getSelectedItem().toString(), 
						comboBoxDay1.getSelectedItem().toString());
				logExecuteSQL(queryString, "m_id");
			}else if (queryCondString == "记录人id*") {
				queryString = String.format(queryString, "recorder_id", jtfQueryCond.getText(), 
						comboBoxYear1.getSelectedItem().toString(), 
						comboBoxDay1.getSelectedItem().toString());
				logExecuteSQL(queryString, "recorder_id");
			}else if (queryCondString == "日期前*") {
				queryString = "SELECT *\r\n" + 
						"FROM counter_log\r\n" + 
						"WHERE counter_date <= '2019-%s-%s'";
				queryString = String.format(queryString, 
						comboBoxYear1.getSelectedItem().toString(), 
						comboBoxDay1.getSelectedItem().toString());
				logExecuteSQL(queryString, "counter_date");
			}else if (queryCondString == "流水号id*") {
				queryString = String.format(queryString, "counter_log_id", jtfQueryCond.getText(), 
						comboBoxYear1.getSelectedItem().toString(), 
						comboBoxDay1.getSelectedItem().toString());
				logExecuteSQL(queryString, "counter_log_id");
			}else if (queryCondString == "全部记录*") {
				queryString = "SELECT *\r\n" + 
						"FROM counter_log";
				logExecuteSQL(queryString, "all");
			}
		}
    }

    public int executeSQL(String sqlString) {
    	int c = 0;
		try {
			Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(sqlString);
    		while (rs.next()) {
    			c++;
    		    Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("settle_status"));
				rowData.add(rs.getString("c_name"));
				rowData.add(rs.getString("amount"));
				this.vtQueryResult.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		if (c == 0) {  // 查询无果
			this.jspTable2.setVisible(false);
			this.jspMname.setVisible(false);
			this.jspDate.setVisible(false);
	        this.jspLog.setVisible(false);
	        this.setBounds(300, 200, 700, 400);
			JOptionPane.showMessageDialog(null, "oh no!   查询无果 ...");
			return -1;
		}else {
			this.jspMname.setVisible(false);
			this.jspDate.setVisible(false);
	        this.jspLog.setVisible(false);
			this.jspTable2.setVisible(true);
			
	    	this.resultModel = new DefaultTableModel(this.vtQueryResult, this.tableHeader) {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column)
	            {
	                return false;  // 设置不可编辑
	            }
	    	};
	        this.jtQueryResult.setModel(this.resultModel);
	        this.jtQueryResult.updateUI();
	        this.setBounds(300, 200, 700, 500);
	        return 1;
		}
	}
    public int executeOtherSQL(String sqlString, String additionalString) {
    	int c = 0;
		try {
			Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(sqlString);
    		while (rs.next()) {
    			c++;
    		    Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("settle_status"));
				rowData.add(rs.getString("c_name"));
				rowData.add(rs.getString("amount"));
				if (additionalString == "m_name") {
					rowData.add(rs.getString("m_name"));
				}else if (additionalString == "expiry_date") {
					rowData.add(rs.getString("expiry_date"));
				}
				this.vtQueryResult.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (c == 0) {  // 查询无果
			this.jspTable2.setVisible(false);
			this.jspMname.setVisible(false);
			this.jspDate.setVisible(false);
	        this.jspLog.setVisible(false);
	        this.setBounds(300, 200, 700, 400);
			JOptionPane.showMessageDialog(null, "oh no!   查询无果 ...");
			return -1;
		}else {
			if (additionalString == "m_name") {
				this.jspTable2.setVisible(false);
				this.jspDate.setVisible(false);
		        this.jspLog.setVisible(false);
				this.jspMname.setVisible(true);
		    	this.mnameModel = new DefaultTableModel(this.vtQueryResult, this.mNameHeader) {
		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int column)
		            {
		                return false;  // 设置不可编辑
		            }
		    	};
		    	this.jtMname.setModel(this.mnameModel);
		        this.jtMname.updateUI();
			}
			else {
				this.jspTable2.setVisible(false);
				this.jspMname.setVisible(false);
		        this.jspLog.setVisible(false);
				this.jspDate.setVisible(true);
		    	this.dateModel = new DefaultTableModel(this.vtQueryResult, this.dateHeader) {
		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int column)
		            {
		                return false;  // 设置不可编辑
		            }
		    	};
		    	this.jtDate.setModel(this.dateModel);
		        this.jtDate.updateUI();
			}
	        this.setBounds(300, 200, 700, 500);
			return 1;
		}
	}

    public void logExecuteSQL(String sqlString, String additionalString) {
    	int c = 0;
		try {
			Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(sqlString);
    		while (rs.next()) {
    			c++;
    		    Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("counter_log_id"));
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("recorder_id"));
				rowData.add(rs.getString("counter_date"));
				rowData.add(rs.getString("c_name"));
				rowData.add(rs.getString("counter_amount"));
				this.vtLog.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		if (c == 0) {  // 查询无果
			this.jspTable2.setVisible(false);
			this.jspMname.setVisible(false);
			this.jspDate.setVisible(false);
	        this.jspLog.setVisible(false);
	        this.setBounds(300, 200, 700, 400);
			JOptionPane.showMessageDialog(null, "oh no!   查询无果 ...");
		}else {
			this.jspMname.setVisible(false);
			this.jspDate.setVisible(false);
			this.jspTable2.setVisible(false);
	        this.jspLog.setVisible(true);
			
	    	this.logModel = new DefaultTableModel(this.vtLog, this.logHeader) {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column)
	            {
	                return false;  // 设置不可编辑
	            }
	    	};
	        this.jtLog.setModel(this.logModel);
	        this.jtLog.updateUI();
	        this.setBounds(300, 200, 700, 500);
		}
	}

    public void statInfo(String headerString, String queryString) {
    	String myqueryString = "SELECT counter.%s, sum(amount), sum(medicine.retail_price)\r\n" + 
    			"FROM counter, medicine\r\n" + 
    			"WHERE counter.m_id = medicine.m_id AND counter.%s='%s'\r\n" + 
    			"GROUP BY counter.%s";
    	myqueryString = String.format(myqueryString, queryString, queryString, jtfQueryCond.getText(), queryString);
    	if (queryString == "m_name") {
			myqueryString = "SELECT m_name, sum(amount), sum(medicine.retail_price)\r\n" + 
					"FROM counter, medicine\r\n" + 
					"WHERE counter.m_id = medicine.m_id AND m_name='%s'\r\n" + 
					"GROUP BY counter.m_id";
			myqueryString = String.format(myqueryString, jtfQueryCond.getText());
		}
    	amountMoneyHeader = new Vector<>();
        amountMoneyHeader.add(headerString);
        amountMoneyHeader.add("剩余数量");
        amountMoneyHeader.add("总金额");
        
		try {
			Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(myqueryString);
    		while (rs.next()) {
    		    Vector<Object> rowData = new Vector<>();
				if (queryString == "m_id") {
					rowData.add(rs.getString("m_id"));
				}else if (queryString == "m_name") {
					rowData.add(rs.getString("m_name"));
				}else if (queryString == "settle_status") {
					rowData.add(rs.getString("settle_status"));
				}else if (queryString == "c_name") {
					rowData.add(rs.getString("c_name"));
				}
				rowData.add(rs.getString("sum(amount)"));
				rowData.add(rs.getString("sum(medicine.retail_price)"));
				this.vtAmountMoney.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		this.jspAmountMoney.setVisible(true);
    	this.amountMoneyModel = new DefaultTableModel(this.vtAmountMoney, this.amountMoneyHeader) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column)
            {
                return false;  // 设置不可编辑
            }
    	};
    	this.jtAmountMoney.setModel(this.amountMoneyModel);
        this.jtAmountMoney.updateUI();
        this.setBounds(300, 200, 700, 620);
	}
}
