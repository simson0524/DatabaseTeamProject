import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsertDataFrame extends JFrame implements ActionListener {

    private final JTextField txtFirstName, txtMiddleInit, txtLastName, txtSsn, txtBirthdate, txtAddress, txtSalary, txtSuperSsn, txtDno;
    private final JComboBox<String> txtSex;

    // DB 정보 저장
    private final String url;
    private final String acct;
    private final String pw;

    public InsertDataFrame(String url, String acct, String pw) {
        this.setTitle("DataBase Basic Project");
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        Font font1 = new Font("Malgun Gothic", Font.BOLD, 18);
        Font font2 = new Font("Malgun Gothic", Font.PLAIN, 18);

        // DB 정보 저장
        this.url = url;
        this.acct = acct;
        this.pw = pw;

        // Insert Panel1
        JPanel insertPanel1 = new JPanel();
        insertPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // title 명시
        JLabel lblTitle = new JLabel("새로운 직원 정보 추가");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        insertPanel1.add(lblTitle);

        this.add(insertPanel1, BorderLayout.NORTH);


        // Insert Panel2
        JPanel insertPanel2 = new JPanel();
        insertPanel2.setLayout(new GridBagLayout());

        // Fname
        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setFont(font1);
        this.txtFirstName = new JTextField(15);
        txtFirstName.setFont(font2);

        // Minit
        JLabel lblMiddleInit = new JLabel("Middle Init: ");
        lblMiddleInit.setFont(font1);
        this.txtMiddleInit = new JTextField(15);
        txtMiddleInit.setFont(font2);

        // Lname
        JLabel lblLastName = new JLabel("Last Name: ");
        lblLastName.setFont(font1);
        this.txtLastName = new JTextField(15);
        txtLastName.setFont(font2);

        // Ssn
        JLabel lblSsn = new JLabel("Ssn(고유한 값): ");
        lblSsn.setFont(font1);
        this.txtSsn = new JTextField(15);
        txtSsn.setFont(font2);

        // Bdate
        JLabel lblBirthdate = new JLabel("Birth Date(yyyy-mm-dd): ");
        lblBirthdate.setFont(font1);
        this.txtBirthdate = new JTextField(15);
        txtBirthdate.setFont(font2);

        // Address
        JLabel lblAddress = new JLabel("Address: ");
        lblAddress.setFont(font1);
        this.txtAddress = new JTextField(15);
        txtAddress.setFont(font2);

        // Sex
        JLabel lblSex = new JLabel("Sex: ");
        lblSex.setFont(font1);
        this.txtSex = new JComboBox<>(new String[]{"M", "F"});
        txtSex.setPreferredSize(new Dimension(txtFirstName.getPreferredSize().width, txtSex.getPreferredSize().height));
        txtSex.setFont(font2);

        // Salary
        JLabel lblSalary = new JLabel("Salary: ");
        lblSalary.setFont(font1);
        this.txtSalary = new JTextField(15);
        txtSalary.setFont(font2);

        // Super_ssn
        JLabel lblSuperSsn = new JLabel("Super_ssn(빈 칸 또는 존재하는 Ssn): ");
        lblSuperSsn.setFont(font1);
        this.txtSuperSsn = new JTextField(15);
        txtSuperSsn.setFont(font2);

        // Dno
        JLabel lblDno = new JLabel("Dno(존재하는 부서 1, 4, 5): ");
        lblDno.setFont(font1);
        this.txtDno = new JTextField(15);
        txtDno.setFont(font2);

        // 간격 조정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        insertPanel2.add(lblFirstName, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        insertPanel2.add(lblMiddleInit, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtMiddleInit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        insertPanel2.add(lblLastName, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtLastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        insertPanel2.add(lblSsn, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtSsn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        insertPanel2.add(lblBirthdate, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtBirthdate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        insertPanel2.add(lblAddress, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        insertPanel2.add(lblSex, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtSex, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        insertPanel2.add(lblSalary, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtSalary, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        insertPanel2.add(lblSuperSsn, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtSuperSsn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        insertPanel2.add(lblDno, gbc);
        gbc.gridx = 1;
        insertPanel2.add(txtDno, gbc);

        this.add(insertPanel2, BorderLayout.CENTER);


        // Insert Panel3
        JPanel insertPanel3 = new JPanel();
        insertPanel3.setLayout(new FlowLayout());

        // 확인 버튼
        JButton btnInsert = new JButton("OK");
        btnInsert.setFont(font1);
        btnInsert.addActionListener(this);

        insertPanel3.add(btnInsert);

        this.add(insertPanel3, BorderLayout.SOUTH);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
            // ---------- 준호 영역 ----------



            // txtFirstName, txtMiddleInit, txtLastName, txtSsn, txtBirthdate, txtAddress, txtSalary, txtSuperSsn, txtDno, txtSex - 자료형은 맨 위 참고
            // 이 변수들 get~~ 함수로 가져와서 사용

            // 설명: 사용자가 이름, Ssn, 생일, 주소, 등을 입력하고 OK 버튼을 누르면 DB에 저장하는 로직 필요 -> 해당 로직을 여기에서 구현




            // ---------- 준호 영역 ----------
            conn.close();
            JOptionPane.showMessageDialog(this, "정보 추가 성공!");
            new MainFrame(url, acct, pw);
            this.setVisible(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "정보를 추가하지 못했습니다. 데이터 형식을 확인하세요.");
        }

    }
}
