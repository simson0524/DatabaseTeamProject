import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManagerLoginFrame extends JFrame implements ActionListener {

    private final JTextField txtMgrSsn;

    // DB 정보 저장
    private final String url;
    private final String acct;
    private final String pw;

    public ManagerLoginFrame(String url, String acct, String pw) {
        this.setTitle("DataBase Basic Project");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        Font font1 = new Font("Malgun Gothic", Font.BOLD, 18);
        Font font2 = new Font("Malgun Gothic", Font.PLAIN, 18);

        // DB 정보 저장
        this.url = url;
        this.acct = acct;
        this.pw = pw;


        // Login Panel1
        JPanel loginPanel1 = new JPanel();
        loginPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // title 명시
        JLabel lblTitle = new JLabel("관리자 로그인");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        loginPanel1.add(lblTitle);

        this.add(loginPanel1, BorderLayout.NORTH);


        // Login Panel2
        JPanel loginPanel2 = new JPanel();
        loginPanel2.setLayout(new GridBagLayout());

        // DB 계정
        JLabel lblMgrSsn = new JLabel("Mgr_ssn: ");
        lblMgrSsn.setFont(font1);
        this.txtMgrSsn = new JTextField(15);
        txtMgrSsn.setFont(font2);

        // 간격 조정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel2.add(lblMgrSsn, gbc);
        gbc.gridx = 1;
        loginPanel2.add(txtMgrSsn, gbc);

        this.add(loginPanel2, BorderLayout.CENTER);


        // Login Panel3
        JPanel loginPanel3 = new JPanel();
        loginPanel3.setLayout(new FlowLayout());

        // 확인 버튼
        JButton btnLogin = new JButton("OK");
        btnLogin.setFont(font1);
        btnLogin.addActionListener(this);

        loginPanel3.add(btnLogin);

        this.add(loginPanel3, BorderLayout.SOUTH);


        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection conn = DriverManager.getConnection(url, acct, pw)) {

            String query = "SELECT EXISTS (SELECT 1 FROM DEPARTMENT WHERE Mgr_ssn = ?)";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, this.txtMgrSsn.getText());

            ResultSet r = p.executeQuery();
            if (r.next() && r.getInt(1) == 1) {
                // 존재하면 성공 메시지와 화면 전환
                JOptionPane.showMessageDialog(this, "관리자 로그인 성공!");
                new ManagerFrame(url, acct, pw);
                this.setVisible(false);
            } else {
                // 존재하지 않을 경우 실패 메시지
                JOptionPane.showMessageDialog(this, "잘못된 관리자 번호입니다.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "데이터베이스 오류가 발생했습니다.");
        }
    }

}
