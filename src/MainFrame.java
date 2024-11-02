import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainFrame extends JFrame implements ActionListener {

    private final JCheckBox chkName, chkSsn, chkBdate, chkAddress, chkSex, chkSalary, chkSupervisor, chkDepartment;
    private final JButton btnSearch;
    private final JTable employeeTable;
    private final JLabel lblSelectedCount, lblSelectedEmployees;
    private final String url;
    private final String acct;
    private final String pw;

    public MainFrame(String url, String acct, String pw) throws SQLException {
        this.setTitle("Information System");
        this.setSize(1800, 1200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        this.url = url;
        this.acct = acct;
        this.pw = pw;

        Font font = new Font("Malgun Gothic", Font.BOLD, 18);

        // Parent Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        // Search Panel1
        JPanel searchPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lblSearchRange = new JLabel("검색 범위");
        lblSearchRange.setFont(font);
        JComboBox<String> searchCategory = new JComboBox<>(new String[]{"전체", "이름", "주민번호", "생일", "주소", "성별", "연봉", "상사이름", "부서"});
        searchCategory.setFont(font);
        searchPanel1.add(lblSearchRange);
        searchPanel1.add(searchCategory);

        // Search Panel2
        JPanel searchPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        chkName = new JCheckBox("Name");
        chkSsn = new JCheckBox("Ssn");
        chkBdate = new JCheckBox("Bdate");
        chkAddress = new JCheckBox("Address");
        chkSex = new JCheckBox("Sex");
        chkSalary = new JCheckBox("Salary");
        chkSupervisor = new JCheckBox("Supervisor");
        chkDepartment = new JCheckBox("Department");

        chkName.setFont(font);
        chkSsn.setFont(font);
        chkBdate.setFont(font);
        chkAddress.setFont(font);
        chkSex.setFont(font);
        chkSalary.setFont(font);
        chkSupervisor.setFont(font);
        chkDepartment.setFont(font);

        // Default selection
        chkName.setSelected(true);
        chkSsn.setSelected(true);
        chkBdate.setSelected(true);
        chkAddress.setSelected(true);
        chkSex.setSelected(true);
        chkSalary.setSelected(true);
        chkSupervisor.setSelected(true);
        chkDepartment.setSelected(true);

        JLabel lblSearchItems = new JLabel("검색 항목");
        lblSearchItems.setFont(font);
        searchPanel2.add(lblSearchItems);
        searchPanel2.add(chkName);
        searchPanel2.add(chkSsn);
        searchPanel2.add(chkBdate);
        searchPanel2.add(chkAddress);
        searchPanel2.add(chkSex);
        searchPanel2.add(chkSalary);
        searchPanel2.add(chkSupervisor);
        searchPanel2.add(chkDepartment);

        btnSearch = new JButton("검색");
        btnSearch.setFont(font);
        btnSearch.addActionListener(this);
        searchPanel2.add(btnSearch);

        searchPanel.add(searchPanel1);
        searchPanel.add(searchPanel2);
        this.add(searchPanel, BorderLayout.NORTH);

        // Initialize the table
        employeeTable = new JTable();
        employeeTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        employeeTable.setRowHeight(30); // Increase row height for better visibility
        employeeTable.getTableHeader().setFont(font);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        this.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        lblSelectedCount = new JLabel("입력수 : 0");
        lblSelectedCount.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        lblSelectedEmployees = new JLabel("선택한 직원: ");
        lblSelectedEmployees.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));

        JButton btnUpdate = new JButton("UPDATE");
        btnUpdate.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        bottomPanel.add(lblSelectedCount);
        bottomPanel.add(lblSelectedEmployees);
        bottomPanel.add(btnUpdate);

        this.add(bottomPanel, BorderLayout.SOUTH);
        this.setVisible(true);

        // Initial data fetch
        fetchData();
    }

    private void fetchData() {
        try {
            Connection conn = DriverManager.getConnection(url, acct, pw);
            StringBuilder sql = new StringBuilder("SELECT ");
            boolean firstField = true;

            // Build the SQL query based on selected checkboxes
            if (chkName.isSelected()) {
                sql.append("E.Fname, E.Minit, E.Lname");
                firstField = false;
            }
            if (chkSsn.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("E.Ssn");
                firstField = false;
            }
            if (chkBdate.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("E.Bdate");
                firstField = false;
            }
            if (chkAddress.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("E.Address");
                firstField = false;
            }
            if (chkSex.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("E.Sex");
                firstField = false;
            }
            if (chkSalary.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("E.Salary");
                firstField = false;
            }
            if (chkSupervisor.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("S.Fname, S.Minit, S.Lname");
                firstField = false;
            }
            if (chkDepartment.isSelected()) {
                if (!firstField) sql.append(", ");
                sql.append("Dname");
            }

            // Ensure at least one field is selected
            if (firstField) {
                JOptionPane.showMessageDialog(this, "At least one field must be selected.");
                return;
            }

            // Complete the SQL statement
            sql.append(" FROM EMPLOYEE E ")
                    .append("JOIN DEPARTMENT ON E.Dno = Dnumber ")
                    .append("JOIN EMPLOYEE S ON E.Super_ssn = S.Ssn");

            // Log the SQL query for debugging
            System.out.println("Executing query: " + sql.toString());

            // Prepare and execute the query
            PreparedStatement p = conn.prepareStatement(sql.toString());
            ResultSet r = p.executeQuery();

            // Fetch data from ResultSet and populate the table
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("선택");
            if (chkName.isSelected()) model.addColumn("NAME");
            if (chkSsn.isSelected()) model.addColumn("SSN");
            if (chkBdate.isSelected()) model.addColumn("BDATE");
            if (chkAddress.isSelected()) model.addColumn("ADDRESS");
            if (chkSex.isSelected()) model.addColumn("SEX");
            if (chkSalary.isSelected()) model.addColumn("SALARY");
            if (chkSupervisor.isSelected()) model.addColumn("SUPERVISOR");
            if (chkDepartment.isSelected()) model.addColumn("DEPARTMENT");

            while (r.next()) {
                Object[] row = new Object[model.getColumnCount()];
                int index = 0;
                int i = 1;
                row[index++] = false; // Default value for "선택" column
                if (chkName.isSelected()) row[index++] = r.getString(i++) + " " + r.getString(i++) + " " + r.getString(i++);
                if (chkSsn.isSelected()) row[index++] = r.getString(i++);
                if (chkBdate.isSelected()) row[index++] = r.getString(i++);
                if (chkAddress.isSelected()) row[index++] = r.getString(i++);
                if (chkSex.isSelected()) row[index++] = r.getString(i++);
                if (chkSalary.isSelected()) row[index++] = r.getDouble(i++);
                if (chkSupervisor.isSelected()) row[index++] = r.getString(i++) + " " + r.getString(i++) + " " + r.getString(i++);
                if (chkDepartment.isSelected()) row[index] = r.getString(i);
                model.addRow(row);
            }

            // Update the table model
            employeeTable.setModel(model);
            lblSelectedCount.setText("입력수 : " + model.getRowCount());

            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching data: " + ex.getMessage());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            fetchData(); // Fetch data when the search button is clicked
        }
    }
}
