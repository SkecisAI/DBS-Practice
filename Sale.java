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

public class Sale extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection myConnection = null;  // 连接
	String querySqlAll = "SELECT * FROM sale_log";
	
	JLabel jlSaleInfo = new JLabel("销售记录信息");
	JLabel jlNoResult = new JLabel("查询无果 !");
	JLabel jlDay = new JLabel("日*");
	JLabel jlMonth = new JLabel("月*");
	JLabel jlTo = new JLabel("至");
	JLabel jlStat = new JLabel("<-统计结果->");
	JLabel jlSort = new JLabel("排序依据");
	
    JTable jtSale = new JTable();
    JTable jtQueryResult = new JTable();
    JTable jtAddSale = new JTable();
    JTable jtStat = new JTable();
    
    DefaultTableModel saleModel;
    DefaultTableModel resultModel;
    DefaultTableModel insertModel;
    DefaultTableModel statModel;
    
    JButton jbAdd = new JButton("添加");
    JButton jbQuery = new JButton("查询(统计)");
    JComboBox<Object> comboBox;
	JComboBox<Object> comboBoxYear1;
	JComboBox<Object> comboBoxDay1;
	JComboBox<Object> comboBoxYear2;
	JComboBox<Object> comboBoxDay2;
	JComboBox<Object> comboBoxSort;
    JTextField jtfQueryCond = new JTextField();
    JPanel jpMain = new JPanel();
    JScrollPane jspTable;
    JScrollPane jspTable2;
    JScrollPane jspTable3;
    JScrollPane jspStat;
    
    Vector<Vector<Object>> vtSaleData = new Vector<>();
    Vector<Vector<Object>> vtQueryResult = new Vector<>();
    Vector<Vector<Object>> vtInsertColumn = new Vector<>();
    Vector<Vector<Object>> vtStat = new Vector<>();
    Vector<Object> tableHeader = new Vector<>();
    Vector<Object> statHeader = new Vector<>();    

    
    public Sale(Connection conn)
    {
    	this.myConnection = conn;  // 数据库连接
        this.setBounds(300, 200, 700, 400);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("销售记录");
        jpMain.setLayout(null);
        
        jlSaleInfo.setFont(new Font("宋体", Font.BOLD, 20));
        jlSaleInfo.setBounds(290, 0, 150, 50);
        jlStat.setFont(new Font("宋体", Font.BOLD, 15));
        jlStat.setBounds(310, 435, 150, 50);

        tableHeader.add("流水号id");
        tableHeader.add("药品id");
        tableHeader.add("销售员id");
        tableHeader.add("销售日期");
        tableHeader.add("销售数量");
        
        
        this.readEmployeeData();  // 读取数据库到表格
        jtSale.setModel(saleModel);
        jspTable = new JScrollPane(jtSale);
        jspTable.setBounds(15, 40, 660, 200);
        
        jbAdd.setBounds(15, 250, 80, 40);
        jbAdd.setFont(new Font("黑体", Font.BOLD, 20));
        jbAdd.addActionListener(this);
        
        jbQuery.setBounds(15, 300, 150, 40);
        jbQuery.setFont(new Font("黑体", Font.BOLD, 20));
        jbQuery.addActionListener(this);
        
        DefaultComboBoxModel<Object> comboBoxModel = new DefaultComboBoxModel<Object>();
        comboBoxModel.addElement("药品id");
        comboBoxModel.addElement("品名*");
        comboBoxModel.addElement("营业员id");
        comboBoxModel.addElement("类型");
        comboBox = new JComboBox<Object>(comboBoxModel);
        comboBox.setBounds(170, 300, 80, 40);
        comboBox.setFont(new Font("黑体", Font.BOLD, 15));
        
        DefaultComboBoxModel<Object> comboBoxSortModel = new DefaultComboBoxModel<Object>();
        comboBoxSortModel.addElement("数量");
        comboBoxSortModel.addElement("金额");
        comboBoxSort = new JComboBox<Object>(comboBoxSortModel);
        comboBoxSort.setBounds(620, 330, 60, 25);
        comboBoxSort.setFont(new Font("黑体", Font.BOLD, 15));
        jlSort.setBounds(620, 300, 70, 25);
        jlSort.setFont(new Font("黑体", Font.BOLD, 12));
        
        // 第一行月
 		DefaultComboBoxModel<Object> comboBoxYearModel1 = new DefaultComboBoxModel<Object>();
 		for (int i = 1; i <= 12; i++) {
 			comboBoxYearModel1.addElement(String.valueOf(10));
 		}
 		comboBoxYear1 = new JComboBox<Object>(comboBoxYearModel1);
 		comboBoxYear1.setBounds(370, 300, 80, 20);
 		comboBoxYear1.setFont(new Font("黑体", Font.BOLD, 15));

 		DefaultComboBoxModel<Object> comboBoxYearModel2 = new DefaultComboBoxModel<Object>();
 		for (int i = 1; i <= 12; i++) {
 			comboBoxYearModel2.addElement(String.valueOf(10));
 		}
 		comboBoxYear2 = new JComboBox<Object>(comboBoxYearModel2);
 		comboBoxYear2.setBounds(490, 300, 80, 20);
 		comboBoxYear2.setFont(new Font("黑体", Font.BOLD, 15));
 		jlMonth.setBounds(580, 285, 50, 50);
 		jlMonth.setFont(new Font("黑体", Font.BOLD, 15));
 		jlTo.setBounds(460, 300, 50, 50);
 		jlTo.setFont(new Font("黑体", Font.BOLD, 20));
 		// 第二行日
 		DefaultComboBoxModel<Object> comboBoxDayModel1 = new DefaultComboBoxModel<Object>();
 		for (int i = 1; i <= 31; i++) {
 			comboBoxDayModel1.addElement(String.valueOf(i));
 		}
 		comboBoxDay1 = new JComboBox<Object>(comboBoxDayModel1);
 		comboBoxDay1.setBounds(370, 330, 80, 20);
 		comboBoxDay1.setFont(new Font("黑体", Font.BOLD, 15));
 		
 		DefaultComboBoxModel<Object> comboBoxDayModel2 = new DefaultComboBoxModel<Object>();
 		for (int i = 1; i <= 31; i++) {
 			comboBoxDayModel2.addElement(String.valueOf(i));
 		}
 		comboBoxDay2 = new JComboBox<Object>(comboBoxDayModel2);
 		comboBoxDay2.setBounds(490, 330, 80, 20);
 		comboBoxDay2.setFont(new Font("黑体", Font.BOLD, 15));
 		jlDay.setBounds(580, 315, 50, 50);
 		jlDay.setFont(new Font("黑体", Font.BOLD, 15));
        
        jtfQueryCond.setFont(new Font("黑体", Font.BOLD, 18));
        jtfQueryCond.setBounds(260, 300, 100, 40);
        
        jlNoResult.setBounds(370, 300, 200, 50);
        jlNoResult.setFont(new Font("黑体", Font.BOLD, 20));
        jlNoResult.setVisible(false);
        
        jspTable2 = new JScrollPane(jtQueryResult);
        jspTable2.setBounds(15, 350, 660, 100);
        jspTable2.setVisible(false);
        
	    Vector<Object> rowData = new Vector<>();
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    rowData.add("");
	    vtInsertColumn.add(rowData);
	    insertModel = new DefaultTableModel(vtInsertColumn, tableHeader);
	    jtAddSale = new JTable(insertModel);
	    
	    jspTable3 = new JScrollPane(jtAddSale);
	    jspTable3.setBounds(100, 250, 580, 40);
	    
        jspStat = new JScrollPane(jtStat);
        jspStat.setBounds(15, 470, 660, 100);
        jspStat.setVisible(false);
        
        jpMain.add(jbAdd);
        jpMain.add(jbQuery);
        jpMain.add(jlSaleInfo);
        jpMain.add(jlNoResult);
		jpMain.add(jlDay);
		jpMain.add(jlMonth);
		jpMain.add(jlTo);
		jpMain.add(jlStat);
		jpMain.add(jlSort);
        jpMain.add(jspTable);
        jpMain.add(jspTable2);
        jpMain.add(jspTable3);
        jpMain.add(jspStat);
        jpMain.add(comboBox);
		jpMain.add(comboBoxDay1);
		jpMain.add(comboBoxYear1);
		jpMain.add(comboBoxDay2);
		jpMain.add(comboBoxYear2);
		jpMain.add(comboBoxSort);
        jpMain.add(jtfQueryCond);
        
        this.add(jpMain);
    }
    
    public void readEmployeeData() {  // 读取数据到表格
		DefaultTableModel model = (DefaultTableModel) jtSale.getModel();
		int row_count = model.getRowCount();
		for (int i = row_count - 1; i >= 0; i--) {
			model.removeRow(i);
		}
    	try {
    		Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(this.querySqlAll);
    		while (rs.next()) {
    		    Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("sale_id"));
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("saler_id"));
				rowData.add(rs.getString("sale_date"));
				rowData.add(rs.getString("sale_amount"));
				vtSaleData.add(rowData);  // 增加一条记录
			}
    		rs.close();
    		stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	this.saleModel = new DefaultTableModel(this.vtSaleData, this.tableHeader) {
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
			String queryString = "INSERT INTO sale_log " + 
					"VALUES ('%s', '%s', '%s', '%s', %s)";
		    String[] insertStrings = {"99999999", "9999", "9999", "2099-01-01", "99"};
    		for (int i = 0; i < 5; i++) {
    			insertStrings[i] = this.jtAddSale.getValueAt(0, i).toString();
			}
    		if (this.jtAddSale.getValueAt(0, 0)=="") {
				queryString = String.format(queryString, "99999999", "9999", "9999", "2099-01-01", "99");
			}else {
	    		queryString = String.format(queryString,
	    				insertStrings[0],
	    				insertStrings[1],
	    				insertStrings[2],
	    				insertStrings[3],
	    				insertStrings[4]);
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
			String queryString = "SELECT * FROM sale_log WHERE %s='%s' AND sale_date>='2019-%s-%s' AND sale_date <='2019-%s-%s'";
			String queryCondString = comboBox.getSelectedItem().toString();
    		// 清空表格
			DefaultTableModel model = (DefaultTableModel) jtQueryResult.getModel();
			int row_count = model.getRowCount();
			for (int i = row_count - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			DefaultTableModel model1 = (DefaultTableModel) jtStat.getModel();
			int row_count1 = model1.getRowCount();
			for (int i = row_count1 - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			
    		int sign = 0;
			if (queryCondString == "药品id") {
				queryString = String.format(queryString, "m_id", jtfQueryCond.getText(), comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
						comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString());
				sign = executeSQL(queryString);
				if (sign == 1) {
					statInfo("药品id", "m_id");  // 统计数量
				}
			}else if (queryCondString=="品名*") {
				queryString = "SELECT sale_log.*\r\n" + 
						"FROM medicine, sale_log\r\n" + 
						"WHERE sale_log.m_id=medicine.m_id AND sale_date>='2019-%s-%s' AND sale_date <='2019-%s-%s' AND \r\n" + 
						"      %s='%s'";
				queryString = String.format(queryString, comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
						comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString(), "m_name", jtfQueryCond.getText());
				sign = executeSQL(queryString);
//				if (sign == 1) {
//					statInfo("品名", "m_name");  // 统计数量
//				}
			}else if (queryCondString=="营业员id") {
				queryString = String.format(queryString, "saler_id", jtfQueryCond.getText(), comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
						comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString());
				sign = executeSQL(queryString);
				if (sign == 1) {
					statInfo("营业员id", "saler_id");  // 统计数量
				}
			}else if (queryCondString=="类型"){
				queryString = "SELECT sale_log.*\r\n" + 
						"FROM medicine, sale_log\r\n" + 
						"WHERE sale_log.m_id=medicine.m_id AND sale_date>='2019-%s-%s' AND sale_date <='2019-%s-%s' AND \r\n" + 
						"      %s='%s'";
				queryString = String.format(queryString, comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
						comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString(), "type", jtfQueryCond.getText());
				sign = executeSQL(queryString);
				if (sign == 1) {
					statInfo("类型", "type");  // 统计数量
				}
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
				rowData.add(rs.getString("sale_id"));
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("saler_id"));
				rowData.add(rs.getString("sale_date"));
				rowData.add(rs.getString("sale_amount"));
				this.vtQueryResult.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		if (c == 0) {  // 查询无果
			this.jspTable2.setVisible(false);
	        this.setBounds(300, 200, 700, 400);
			JOptionPane.showMessageDialog(null, "oh no!   查询无果 ...");
			return -1;
		}else {
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

    public void statInfo(String headerString, String queryString) {
    	String myqueryString = "SELECT sale_log.%s, sale_log.m_id, sum(sale_amount), sum(retail_price), sum(retail_price - trade_price), sum(retail_price - trade_price) / sum(trade_price)\r\n" + 
    			"FROM sale_log, medicine\r\n" + 
    			"WHERE sale_log.m_id=medicine.m_id AND sale_date>='2019-%s-%s' AND sale_date <='2019-%s-%s' AND\r\n" + 
    			"      sale_log.%s='%s'\r\n" + 
    			"GROUP BY sale_log.m_id\r\n" + 
    			"ORDER BY sum(%s) DESC";
    	if (comboBoxSort.getSelectedItem().toString()=="数量") {
        	myqueryString = String.format(myqueryString, queryString, comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
    				comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString(), queryString, jtfQueryCond.getText(), "sale_amount");
		}else {
			myqueryString = String.format(myqueryString, queryString, comboBoxYear1.getSelectedItem().toString(), comboBoxDay1.getSelectedItem().toString(),
    				comboBoxYear2.getSelectedItem().toString(), comboBoxDay2.getSelectedItem().toString(), queryString, jtfQueryCond.getText(), "retail_price");
		}
    	statHeader = new Vector<>();
        statHeader.add(headerString);
        statHeader.add("药品id");
        statHeader.add("销售数量");
        statHeader.add("销售金额");
        statHeader.add("利润");
        statHeader.add("利润率");
        
		try {
			Statement stmt = this.myConnection.createStatement();
    		ResultSet rs = stmt.executeQuery(myqueryString);
    		while (rs.next()) {
    		    Vector<Object> rowData = new Vector<>();
				if (queryString == "m_id") {
					rowData.add(rs.getString("m_id"));
				}else if (queryString == "saler_id") {
					rowData.add(rs.getString("saler_id"));
				}else if (queryString == "type") {
					rowData.add(rs.getString("type"));
				}
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("sum(sale_amount)"));
				rowData.add(rs.getString("sum(retail_price)"));
				rowData.add(rs.getString("sum(retail_price - trade_price)"));
				rowData.add(rs.getString("sum(retail_price - trade_price) / sum(trade_price)"));
				this.vtStat.add(rowData);
			}
    		rs.close();
    		stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		this.jspStat.setVisible(true);
    	this.statModel = new DefaultTableModel(this.vtStat, this.statHeader) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column)
            {
                return false;  // 设置不可编辑
            }
    	};
    	this.jtStat.setModel(this.statModel);
        this.jtStat.updateUI();
        this.setBounds(300, 200, 700, 620);
	}
}
