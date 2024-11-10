import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {

    private final JCheckBox chkName, chkSsn, chkBdate, chkAddress, chkSex, chkSalary, chkSupervisor, chkDepartment;
    private final JComboBox<String> searchCategory, searchCategory1, searchCategory2;
    private final JComboBox<String> groupCategory;
    private final JComboBox<String> updateCategory;
    private final JTextField txtSalary, txtUpdate;
    private final JButton btnSearch, btnManager, btnInsert, btnUpdate, btnDelete;
    private final JTable employeeTable;
    private final JLabel lblSelectGroup, lblSelectedCount, lblSelectedEmployees;

    // DB 정보 저장
    private final String url;
    private final String acct;
    private final String pw;

    private List<String> selectedEmployeeList = new ArrayList<>();
    private List<String> selectedSsnList = new ArrayList<>();

    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    public MainFrame(String url, String acct, String pw) {
        this.setTitle("DataBase Basic Project");
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        // DB 정보 저장
        this.url = url;
        this.acct = acct;
        this.pw = pw;

        Font font1 = new Font("Malgun Gothic", Font.BOLD, 18);
        Font font2 = new Font("Malgun Gothic", Font.PLAIN, 18);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        this.btnManager = new JButton("관리자 로그인");
        btnManager.setFont(font1);
        btnManager.addActionListener(this);


        // Search Panel1
        JPanel searchPanel1 = new JPanel();
        searchPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JLabel lblSearchRange = new JLabel("검색 범위");
        lblSearchRange.setFont(font1);
        this.searchCategory = new JComboBox<>(new String[]{"전체", "이름", "주민번호", "생일", "주소", "성별", "연봉", "상사이름", "부서"});
        searchCategory.setFont(font1);
        searchCategory.addActionListener(this);

        this.searchCategory1 = new JComboBox<>(new String[]{"M", "F"});
        this.txtSalary = new JTextField(10);
        this.searchCategory2 = new JComboBox<>(new String[]{"Headquarters", "Administration", "Research"});

        searchCategory1.setFont(font1);
        txtSalary.setFont(font1);
        searchCategory2.setFont(font1);

        searchCategory1.setVisible(false);
        txtSalary.setVisible(false);
        searchCategory2.setVisible(false);

        this.lblSelectGroup = new JLabel("그룹간 평균 월급");
        lblSelectGroup.setFont(font1);
        this.groupCategory = new JComboBox<>(new String[]{"그룹 없음", "성별", "부서", "상급자"});
        groupCategory.setFont(font1);
        groupCategory.addActionListener(this);

        searchPanel1.add(lblSearchRange);
        searchPanel1.add(searchCategory);
        searchPanel1.add(searchCategory1);
        searchPanel1.add(txtSalary);
        searchPanel1.add(searchCategory2);
        searchPanel1.add(lblSelectGroup);
        searchPanel1.add(groupCategory, BorderLayout.CENTER);

        searchPanel.add(searchPanel1, BorderLayout.WEST);
        searchPanel.add(btnManager, BorderLayout.EAST);


        // Search Panel2
        JPanel searchPanel2 = new JPanel();
        searchPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JLabel lblSearchItems = new JLabel("검색 항목");
        lblSearchItems.setFont(font1);
        this.chkName = new JCheckBox("Name");
        this.chkSsn = new JCheckBox("Ssn");
        this.chkBdate = new JCheckBox("Bdate");
        this.chkAddress = new JCheckBox("Address");
        this.chkSex = new JCheckBox("Sex");
        this.chkSalary = new JCheckBox("Salary");
        this.chkSupervisor = new JCheckBox("Supervisor");
        this.chkDepartment = new JCheckBox("Department");

        chkName.setFont(font1);
        chkSsn.setFont(font1);
        chkBdate.setFont(font1);
        chkAddress.setFont(font1);
        chkSex.setFont(font1);
        chkSalary.setFont(font1);
        chkSupervisor.setFont(font1);
        chkDepartment.setFont(font1);

        chkName.setSelected(true);
        chkSsn.setSelected(true);
        chkBdate.setSelected(true);
        chkAddress.setSelected(true);
        chkSex.setSelected(true);
        chkSalary.setSelected(true);
        chkSupervisor.setSelected(true);
        chkDepartment.setSelected(true);

        this.btnSearch = new JButton("검색");
        btnSearch.setFont(font1);
        btnSearch.addActionListener(this);

        searchPanel2.add(lblSearchItems);
        searchPanel2.add(chkName);
        searchPanel2.add(chkSsn);
        searchPanel2.add(chkBdate);
        searchPanel2.add(chkAddress);
        searchPanel2.add(chkSex);
        searchPanel2.add(chkSalary);
        searchPanel2.add(chkSupervisor);
        searchPanel2.add(chkDepartment);
        searchPanel2.add(btnSearch);

        mainPanel.add(searchPanel);
        mainPanel.add(searchPanel2);
        //this.add(searchPanel, BorderLayout.NORTH);


        // TABLE Panel
        JPanel tablePanel = new JPanel(new BorderLayout());

        this.employeeTable = new JTable();
        employeeTable.setFont(font2);
        employeeTable.setRowHeight(30);
        employeeTable.getTableHeader().setFont(font1);
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Employee Data", 0, 0, font1));

        JScrollPane scrollPane = new JScrollPane(employeeTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel); // temp
        //this.add(tablePanel, BorderLayout.CENTER);


        // Bottom Panel
        JPanel bottomPanel = new JPanel();


        // Bottom Panel1
        JPanel bottomPanel1 = new JPanel(new BorderLayout());

        this.lblSelectedEmployees = new JLabel("선택한 직원: ");
        lblSelectedEmployees.setFont(font1);

        this.btnInsert = new JButton("직원 추가");
        btnInsert.setFont(font1);
        btnInsert.addActionListener(this);

        bottomPanel1.add(lblSelectedEmployees, BorderLayout.WEST);
        bottomPanel1.add(btnInsert, BorderLayout.EAST);

        // Bottom Panel2
        JPanel bottomPanel2 = new JPanel(new BorderLayout());

        this.lblSelectedCount = new JLabel("직원수: 0");
        lblSelectedCount.setFont(font1);

        JPanel bottomPanel3 = new JPanel();
        bottomPanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel lblUpdateItem = new JLabel("수정: ");
        lblUpdateItem.setFont(font1);

        this.updateCategory = new JComboBox<>(new String[] {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"});
        updateCategory.setFont(font1);

        this.txtUpdate = new JTextField(15);
        txtUpdate.setFont(font2);

        this.btnUpdate = new JButton("수정");
        btnUpdate.setFont(font1);
        btnUpdate.addActionListener(this);

        bottomPanel3.add(lblUpdateItem);
        bottomPanel3.add(updateCategory);
        bottomPanel3.add(txtUpdate);
        bottomPanel3.add(btnUpdate);

        this.btnDelete = new JButton("직원 삭제");
        btnDelete.setFont(font1);
        btnDelete.addActionListener(this);

        bottomPanel2.add(lblSelectedCount, BorderLayout.WEST);
        bottomPanel2.add(bottomPanel3, BorderLayout.CENTER);
        bottomPanel2.add(btnDelete, BorderLayout.EAST);

        mainPanel.add(bottomPanel1); // temp
        mainPanel.add(bottomPanel2); // temp
        this.add(mainPanel, BorderLayout.NORTH); // temp

        // bottomPanel.add(bottomPanel1);
        // bottomPanel.add(bottomPanel2);
        // this.add(bottomPanel, BorderLayout.SOUTH);


        this.setVisible(true);

        fetchData();
    }

    // 테이블 모델 리스너 추가 메서드
    private void addTableModelListener() {
        employeeTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // 체크박스 열인지 확인
            if (column == 0) {
                Boolean isChecked = (Boolean) employeeTable.getModel().getValueAt(row, column);
                System.out.println("Row " + row + " 체크박스 상태: " + (isChecked ? "선택됨" : "선택 해제됨"));

                handleCheckboxChange(row, isChecked);
            }
        });
    }

    // 체크박스 변경 시 추가 작업을 처리하는 메서드
    private void handleCheckboxChange(int row, boolean isChecked) {
        String employeeName = (String) employeeTable.getModel().getValueAt(row, 1);
        String employeeSsn = (String) employeeTable.getModel().getValueAt(row, 2);
        if (isChecked) {
            selectedEmployeeList.add(employeeName);
            selectedSsnList.add(employeeSsn);
        } else {
            selectedEmployeeList.remove(employeeName);
            selectedSsnList.remove(employeeSsn);
        }

        String selectedEmployees = String.join(" ", selectedEmployeeList);
        lblSelectedEmployees.setText("선택된 직원 데이터: " + selectedEmployees);

        System.out.println("선택된 직원 데이터: " + selectedEmployees);
    }

    private void fetchData() {
        try {
            Connection conn = DriverManager.getConnection(url, acct, pw);

            // StringBuilder를 활용하여 SQL문 생성
            StringBuilder query = new StringBuilder("SELECT ");
            boolean firstField = true;

            if (chkName.isSelected()) {
                query.append("E.Fname, E.Minit, E.Lname");
                firstField = false;
            }
            if (chkSsn.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("E.Ssn");
                firstField = false;
            }
            if (chkBdate.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("E.Bdate");
                firstField = false;
            }
            if (chkAddress.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("E.Address");
                firstField = false;
            }
            if (chkSex.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("E.Sex");
                firstField = false;
            }
            if (chkSalary.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("E.Salary");
                firstField = false;
            }
            if (chkSupervisor.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("S.Fname, S.Minit, S.Lname");
                firstField = false;
            }
            if (chkDepartment.isSelected()) {
                if (!firstField) query.append(", ");
                query.append("Dname");
            }

            if (firstField) {
                JOptionPane.showMessageDialog(this, "적어도 하나의 필드는 선택해주세요.");
                return;
            }

            query.append(" FROM EMPLOYEE E ")
                    .append("JOIN DEPARTMENT ON E.Dno = Dnumber ")
                    .append("LEFT JOIN EMPLOYEE S ON E.Super_ssn = S.Ssn");

            if (searchCategory.getSelectedItem() == "전체") {
                flag1 = false;
                flag2 = false;
                flag3 = false;
            }
            if (flag1) query.append(" WHERE E.Sex = ?");
            if (flag2) query.append(" WHERE E.Salary > ?");
            if (flag3) query.append(" WHERE Dname = ?");

            System.out.println("Executing query: " + query.toString());

            PreparedStatement p = conn.prepareStatement(query.toString());

            if (flag1) {
                String sex = (String) searchCategory1.getSelectedItem();
                p.clearParameters();
                p.setString(1, sex);
            }
            if (flag2) {
                String salary = txtSalary.getText();
                p.clearParameters();
                p.setString(1, salary);
            }
            if (flag3) {
                String dName = (String) searchCategory2.getSelectedItem();
                p.clearParameters();
                p.setString(1, dName);
            }

            ResultSet r = p.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public Class<?> getColumnClass(int column) {
                    // 첫 번째 열이 체크박스 열인 경우 Boolean 타입으로 설정
                    return column == 0 ? Boolean.class : String.class;
                }
            };

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
                row[index++] = false;
                if (chkName.isSelected()) row[index++] = r.getString(i++) + " " + r.getString(i++) + " " + r.getString(i++);
                if (chkSsn.isSelected()) row[index++] = r.getString(i++);
                if (chkBdate.isSelected()) row[index++] = r.getString(i++);
                if (chkAddress.isSelected()) row[index++] = r.getString(i++);
                if (chkSex.isSelected()) row[index++] = r.getString(i++);
                if (chkSalary.isSelected()) row[index++] = r.getDouble(i++);
                if (chkSupervisor.isSelected()) {
                    if (r.getString(i) == null) {
                        i += 3;
                        row[index++] = "";
                    }
                    else row[index++] = r.getString(i++) + " " + r.getString(i++) + " " + r.getString(i++);
                }
                if (chkDepartment.isSelected()) row[index] = r.getString(i);
                model.addRow(row);
            }

            employeeTable.setModel(model);
            lblSelectedCount.setText("직원수 : " + model.getRowCount());

            if (conn != null) conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "데이터를 불러오는데 실패했습니다.");
        }
        addTableModelListener();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchCategory){
            searchCategory1.setVisible(false);
            txtSalary.setVisible(false);
            searchCategory2.setVisible(false);
            lblSelectGroup.setVisible(false);
            groupCategory.setVisible(false);

            searchCategory1.setSelectedIndex(0);
            txtSalary.setText("");
            searchCategory2.setSelectedIndex(0);
            groupCategory.setSelectedIndex(0);

            flag1 = false;
            flag2 = false;
            flag3 = false;

            String selected = (String) searchCategory.getSelectedItem();

            if (selected.equals("전체")) {
                lblSelectGroup.setVisible(true);
                groupCategory.setVisible(true);
            }
            else if (selected.equals("성별")) {
                flag1 = true;
                searchCategory1.setVisible(true);
            }
            else if (selected.equals("연봉")) {
                flag2 = true;
                txtSalary.setVisible(true);
            }
            else if (selected.equals("부서")) {
                flag3 = true;
                searchCategory2.setVisible(true);
            }
        }

        if (e.getSource() == btnManager) {
            new ManagerLoginFrame(url, acct, pw);
            this.setVisible(false);
        }

        if (e.getSource() == groupCategory) {
            String selected = (String) groupCategory.getSelectedItem();

            if (selected.equals("그룹 없음")) {
                fetchData();
            }
            else if (selected.equals("성별")) {
                try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
                    // ---------- 기영 영역 ----------






                    // ---------- 기영 영역 ----------
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "데이터를 불러오는데 실패했습니다.");
                }
            }
            else if (selected.equals("부서")) {
                try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
                    // ---------- 기영 영역 ----------






                    // ---------- 기영 영역 ----------
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "데이터를 불러오는데 실패했습니다.");
                }
            }
            else if (selected.equals("상급자")) {
                try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
                    // ---------- 기영 영역 ----------






                    // ---------- 기영 영역 ----------
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "데이터를 불러오는데 실패했습니다.");
                }
            }
        }

        if (e.getSource() == btnSearch) {
            fetchData();
        }

        if (e.getSource() == btnInsert) {
            new InsertDataFrame(url, acct, pw);
            this.setVisible(false);
        }

        if (e.getSource() == btnUpdate) {
            try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
                // ---------- 준호 영역 ----------



                // selectedSsnList의 원소들 사용 - 자료형은 맨 위 참고
                // List 내에 있는 Ssn들을 선택
                // updateCategory, txtUpdate 변수들 사용 - 자료형은 맨 위 참고
                // 이 변수들 get~~ 함수로 값을 가져와서 사용

                // 설명: 사용자가 수정하고자 하는 직원을 선택하고, 수정하고 싶은 필드(어트리뷰트)를 선택하고, 어떤 값으로 수정할지 선택한 후
                // UPDATE 버튼을 누르면 DB에서 삭제하는 로직 필요 -> 해당 로직을 여기에서 구현




                // ---------- 준호 영역 ----------
                conn.close();
                JOptionPane.showMessageDialog(this, "수정 성공!");
                new MainFrame(url, acct, pw);
                this.setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "수정에 실패했습니다. 데이터 형식을 확인하세요.");
            }
        }

        if (e.getSource() == btnDelete) {
            try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
                // ---------- 재혁님 영역 ----------



                // selectedSsnList의 원소들 사용 - 자료형은 맨 위 참고
                // List 내에 있는 Ssn들을 삭제하는 로직 구현

                // 설명: 사용자가 삭제하고자 하는 직원을 선택하고 삭제 버튼을 누르면 DB에서 삭제하는 로직 필요 -> 해당 로직을 여기에서 구현

                // SQL DELETE 문 작성
                String sql = "DELETE FROM EMPLOYEE WHERE Ssn = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    // 선택된 모든 Ssn에 대해 삭제 실행
                    for (String ssn : selectedSsnList) {
                        pstmt.setString(1, ssn);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                // 이거 왜 안올라가냐..


                // ---------- 재혁님 영역 ----------
                conn.close();
                JOptionPane.showMessageDialog(this, "삭제 성공!");
                new MainFrame(url, acct, pw);
                this.setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "삭제에 실패했습니다.");
            }
        }
    }
}
