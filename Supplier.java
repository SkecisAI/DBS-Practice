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

public class Supplier extends JFrame implements ActionListener {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	int queryNums = -1;

	Connection myConnection = null; // 连接
	String querySqlAll = "SELECT * FROM Supplier";

	JLabel jlSupplierInfo = new JLabel("供货商信息");
	JLabel jlNoResult = new JLabel("查询无果 !");

	JTable jtSupplier = new JTable();
	JTable jtQueryResult = new JTable();
	JTable jtAddSupplier = new JTable();

	DefaultTableModel supplierModel;
	DefaultTableModel resultModel;
	DefaultTableModel insertModel;

	JButton jbAdd = new JButton("添加");
	JButton jbQuery = new JButton("查询");
	JButton jbDelete = new JButton("x删除");
	JButton jbModify = new JButton("*修改");
	JComboBox<Object> comboBox;
	JTextField jtfQueryCond = new JTextField();
	JPanel jpMain = new JPanel();
	JScrollPane jspTable;
	JScrollPane jspTable2;
	JScrollPane jspTable3;

	Vector<Vector<Object>> vtSupplierData = new Vector<>();
	Vector<Vector<Object>> vtQueryResult = new Vector<>();
	Vector<Vector<Object>> vtInsertColumn = new Vector<>();
	Vector<Object> tableHeader = new Vector<>();

	public Supplier(Connection conn) {
		this.myConnection = conn; // 数据库连接
		this.setBounds(300, 200, 700, 400);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("供货商信息");
		jpMain.setLayout(null);

		jlSupplierInfo.setFont(new Font("宋体", Font.BOLD, 20));
		jlSupplierInfo.setBounds(290, 0, 150, 50);

		tableHeader.add("供货商id");
		tableHeader.add("药品id");
		tableHeader.add("供货商名");
		tableHeader.add("联系电话");
		this.readEmployeeData(); // 读取数据库到表格
		jtSupplier.setModel(supplierModel);

		jspTable = new JScrollPane(jtSupplier);
		jspTable.setBounds(15, 40, 660, 200);

		jbAdd.setBounds(15, 250, 80, 40);
		jbAdd.setFont(new Font("黑体", Font.BOLD, 20));
		jbAdd.addActionListener(this);

		jbQuery.setBounds(15, 300, 150, 40);
		jbQuery.setFont(new Font("黑体", Font.BOLD, 20));
		jbQuery.addActionListener(this);

		DefaultComboBoxModel<Object> comboBoxModel = new DefaultComboBoxModel<Object>();
		comboBoxModel.addElement("供货商id");
		comboBoxModel.addElement("药品id");
		comboBoxModel.addElement("供货商名");
		comboBox = new JComboBox<Object>(comboBoxModel);
		comboBox.setBounds(170, 300, 100, 40);
		comboBox.setFont(new Font("黑体", Font.BOLD, 15));

		jtfQueryCond.setFont(new Font("黑体", Font.BOLD, 18));
		jtfQueryCond.setBounds(280, 300, 100, 40);
		
		jbModify.setBounds(450, 300, 100, 40);
		jbModify.setFont(new Font("黑体", Font.BOLD, 20));
		jbModify.addActionListener(this);
		jbDelete.setBounds(560, 300, 100, 40);
		jbDelete.setFont(new Font("黑体", Font.BOLD, 20));
		jbDelete.addActionListener(this);
		
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
		rowData.add("");
		rowData.add("");
		rowData.add("");
		vtInsertColumn.add(rowData);
		insertModel = new DefaultTableModel(vtInsertColumn, tableHeader);
		jtAddSupplier = new JTable(insertModel);

		jspTable3 = new JScrollPane(jtAddSupplier);
		jspTable3.setBounds(100, 250, 580, 40);

		jpMain.add(jbAdd);
		jpMain.add(jbQuery);
		jpMain.add(jbModify);
		jpMain.add(jbDelete);
		jpMain.add(jlSupplierInfo);
		jpMain.add(jlNoResult);
		jpMain.add(jspTable);
		jpMain.add(jspTable2);
		jpMain.add(jspTable3);
		jpMain.add(comboBox);
		jpMain.add(jtfQueryCond);

		this.add(jpMain);
	}

	public void readEmployeeData() { // 读取数据到表格
		DefaultTableModel model = (DefaultTableModel) jtSupplier.getModel();
		int row_count = model.getRowCount();
		for (int i = row_count - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		try {
			Statement stmt = this.myConnection.createStatement();
			ResultSet rs = stmt.executeQuery(this.querySqlAll);
			while (rs.next()) {
				Vector<Object> rowData = new Vector<>();
				rowData.add(rs.getString("s_id"));
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("s_name"));
				rowData.add(rs.getString("tel"));
				vtSupplierData.add(rowData); // 增加一条记录
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.supplierModel = new DefaultTableModel(this.vtSupplierData, this.tableHeader) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false; // 设置不可编辑
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbAdd) {
			String queryString = "INSERT INTO supplier " + "VALUES ('%s', '%s', '%s', '%s')";
			String[] insertStrings = { "9999", "1001", "万能公司", "99999999999" };
			for (int i = 0; i < 4; i++) {
				insertStrings[i] = this.jtAddSupplier.getValueAt(0, i).toString();
			}
			if (this.jtAddSupplier.getValueAt(0, 0) == "") {
				queryString = String.format(queryString, "9999", "1001", "万能公司", "99999999999");
			} else {
				queryString = String.format(queryString, insertStrings[0], insertStrings[1], insertStrings[2],
						insertStrings[3]);
			}
			try {
				Statement stmt = this.myConnection.createStatement();
				stmt.executeUpdate(queryString);
				stmt.close();
				JOptionPane.showMessageDialog(null, "插入成功 ^_^  重新载入数据库！");
				this.readEmployeeData();
//				this.dispose();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "插入记录失败! x_x\nInfo: " + e1.getMessage(), "发生错误",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == jbQuery) { // 查询
			String queryString = "SELECT * FROM supplier WHERE %s='%s'";
			String queryCondString = comboBox.getSelectedItem().toString();
			// 清空表格
			DefaultTableModel model = (DefaultTableModel) jtQueryResult.getModel();
			int row_count = model.getRowCount();
			for (int i = row_count - 1; i >= 0; i--) {
				model.removeRow(i);
			}

			if (queryCondString == "供货商id") {
				queryString = String.format(queryString, "s_id", jtfQueryCond.getText());
				queryNums = executeSQL(queryString);
			} else if (queryCondString == "药品id") {
				queryString = String.format(queryString, "m_id", jtfQueryCond.getText());
				queryNums = executeSQL(queryString);
			} else if (queryCondString == "供货商名") {
				queryString = String.format(queryString, "s_name", jtfQueryCond.getText());
				queryNums = executeSQL(queryString);
			}
		}else if (e.getSource()==jbDelete) {  // 删除
			if (queryNums == 1) {
				int selectRowId = jtQueryResult.getSelectedRow();
				if (selectRowId != -1) {  // 选中行
					String deleteString = "DELETE FROM supplier WHERE s_id = '%s'";
					deleteString = String.format(deleteString, jtQueryResult.getValueAt(selectRowId, 0));
					try {
						Statement stmt = this.myConnection.createStatement();
						stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
						stmt.executeUpdate(deleteString);
						stmt.execute("SET FOREIGN_KEY_CHECKS =1");
						stmt.close();
						JOptionPane.showMessageDialog(null, "删除成功 ^_^  重新载入数据库！");
						this.readEmployeeData();
//						this.dispose();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "删除失败! x_x\nInfo: " + e1.getMessage(), "发生错误",
								JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "没有选中的行 -_-！", null, JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(null, "没有查询结果 -_-！", null, JOptionPane.WARNING_MESSAGE);
			}
		}else if (e.getSource()==jbModify) {  // 修改
			if (queryNums == 1) {
				int selectRowId = jtQueryResult.getSelectedRow();
				if (selectRowId != -1) {  // 选中了行
					String modifyString = "UPDATE supplier SET %s='%s'\r\n" + 
							"WHERE s_id='%s' AND m_id = '%s'";
					int selectColId = jtQueryResult.getSelectedColumn();
					String[] headerNameStrings = {"s_id","m_id","s_name", "tel"};
					if (selectColId >= 2) {
						String inputValue = JOptionPane.showInputDialog("请输入您新的数据");
						modifyString = String.format(modifyString, headerNameStrings[selectColId], inputValue,
								jtQueryResult.getValueAt(selectRowId, 0), jtQueryResult.getValueAt(selectRowId, 1));
						try {
							Statement stmt = this.myConnection.createStatement();
							stmt.executeUpdate(modifyString);
							stmt.close();
							JOptionPane.showMessageDialog(null, "修改成功 ^_^  重新载入数据库！");
							this.readEmployeeData();
//							this.dispose();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "修改失败! x_x\nInfo: " + e2.getMessage(), "发生错误",
									JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(null, "禁止修改此列（外键约束）！请联系管理员。", null, JOptionPane.WARNING_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "没有选中的行 -_-！", null, JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(null, "没有查询结果 -_-！", null, JOptionPane.WARNING_MESSAGE);
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
				rowData.add(rs.getString("s_id"));
				rowData.add(rs.getString("m_id"));
				rowData.add(rs.getString("s_name"));
				rowData.add(rs.getString("tel"));
				this.vtQueryResult.add(rowData);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (c == 0) { // 查询无果
			this.jspTable2.setVisible(false);
			this.setBounds(300, 200, 700, 400);
			JOptionPane.showMessageDialog(null, "oh no!   查询无果 ...");
			return -1;
		} else {
			this.jspTable2.setVisible(true);

			this.resultModel = new DefaultTableModel(this.vtQueryResult, this.tableHeader) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false; // 设置不可编辑
				}
			};
			this.jtQueryResult.setModel(this.resultModel);
			this.jtQueryResult.updateUI();
			this.setBounds(300, 200, 700, 500);
			return 1;
		}
	}
}
