import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField txtDBAccount, txtDBName;
    private final JPasswordField txtDBPassword;

    public LoginFrame() {
        this.setTitle("DataBase Basic Project");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        Font font1 = new Font("Malgun Gothic", Font.BOLD, 18);
        Font font2 = new Font("Malgun Gothic", Font.PLAIN, 18);

        // Login Panel1
        JPanel loginPanel1 = new JPanel();
        loginPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // title 명시
        JLabel lblTitle = new JLabel("Database Login");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        loginPanel1.add(lblTitle);

        this.add(loginPanel1, BorderLayout.NORTH);


        // Login Panel2
        JPanel loginPanel2 = new JPanel();
        loginPanel2.setLayout(new GridBagLayout());

        // DB 계정
        JLabel lblDBAccount = new JLabel("DB Account: ");
        lblDBAccount.setFont(font1);
        this.txtDBAccount = new JTextField(15);
        txtDBAccount.setFont(font2);

        // DB 비밀번호
        JLabel lblDBPassword = new JLabel("DB Password: ");
        lblDBPassword.setFont(font1);
        this.txtDBPassword = new JPasswordField(15);
        txtDBPassword.setFont(font2);

        // DB 이름 (mydb)
        JLabel lblDBName = new JLabel("DB Name: ");
        lblDBName.setFont(font1);
        this.txtDBName = new JTextField(15);
        txtDBName.setFont(font2);

        // 간격 조정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel2.add(lblDBAccount, gbc);
        gbc.gridx = 1;
        loginPanel2.add(txtDBAccount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel2.add(lblDBPassword, gbc);
        gbc.gridx = 1;
        loginPanel2.add(txtDBPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel2.add(lblDBName, gbc);
        gbc.gridx = 1;
        loginPanel2.add(txtDBName, gbc);

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
        String acct = txtDBAccount.getText();
        char[] p = txtDBPassword.getPassword();
        String pw = new String(p);
        String dbname = txtDBName.getText();

        if (acct.isEmpty() || pw.isEmpty() || dbname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/" + dbname + "?serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(url, acct, pw)) {
            JOptionPane.showMessageDialog(this, "DB 접속 성공!");
            conn.close();
            new MainFrame(url, acct, pw);
            this.setVisible(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB 연결에 실패했습니다.");
        }
    }
}
