import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField txtDBAccount, txtDBName;
    private final JPasswordField txtDBPassword;

    public LoginFrame() {
        this.setTitle("Information System");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));

        Font font = new Font("Malgun Gothic", Font.PLAIN, 18);

        JLabel lblTitle = new JLabel("Database Login");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        JLabel lblDBAccount = new JLabel("DB Account");
        lblDBAccount.setFont(font);
        txtDBAccount = new JTextField(15);
        txtDBAccount.setFont(font);

        JLabel lblDBPassword = new JLabel("DB Password");
        lblDBPassword.setFont(font);
        txtDBPassword = new JPasswordField(15);
        txtDBPassword.setFont(font);

        JLabel lblDBName = new JLabel("DB Name");
        lblDBName.setFont(font);
        txtDBName = new JTextField(15);
        txtDBName.setFont(font);

        JButton btnLogin = new JButton("OK");
        btnLogin.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        btnLogin.addActionListener(this);

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        p1.add(lblTitle);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        p2.add(lblDBAccount, gbc);
        gbc.gridx = 1;
        p2.add(txtDBAccount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        p2.add(lblDBPassword, gbc);
        gbc.gridx = 1;
        p2.add(txtDBPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        p2.add(lblDBName, gbc);
        gbc.gridx = 1;
        p2.add(txtDBName, gbc);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(btnLogin);

        this.add(p1, BorderLayout.NORTH);
        this.add(p2, BorderLayout.CENTER);
        this.add(p3, BorderLayout.SOUTH);
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
