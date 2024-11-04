import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerFrame extends JFrame implements ActionListener {

    private final JButton btnChangeSchema, btnNormal;

    // DB 정보 저장
    private final String url;
    private final String acct;
    private final String pw;

    public ManagerFrame(String url, String acct, String pw) {
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
        JLabel lblTitle = new JLabel("스키마 변경 페이지");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        loginPanel1.add(lblTitle);

        this.add(loginPanel1, BorderLayout.NORTH);


//        // Login Panel2
//        JPanel loginPanel2 = new JPanel();
//        loginPanel2.setLayout(new GridBagLayout());
//
//        // DB 계정
//        JLabel lblMgrSsn = new JLabel("Mgr_ssn: ");
//        lblMgrSsn.setFont(font1);
//        this.txtMgrSsn = new JTextField(15);
//        txtMgrSsn.setFont(font2);
//
//        // 간격 조정
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        loginPanel2.add(lblMgrSsn, gbc);
//        gbc.gridx = 1;
//        loginPanel2.add(txtMgrSsn, gbc);
//
//        this.add(loginPanel2, BorderLayout.CENTER);


        // Login Panel3
        JPanel loginPanel3 = new JPanel();
        loginPanel3.setLayout(new FlowLayout());

        // 확인 버튼
        this.btnChangeSchema = new JButton("스키마 변경");
        btnChangeSchema.setFont(font1);
        btnChangeSchema.addActionListener(this);

        loginPanel3.add(btnChangeSchema);

        this.btnNormal = new JButton("일반 사용자");
        btnNormal.setFont(font1);
        btnNormal.addActionListener(this);

        loginPanel3.add(btnNormal);

        this.add(loginPanel3, BorderLayout.SOUTH);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnChangeSchema) {
            try (Connection conn = DriverManager.getConnection(url, acct, pw)) {

                conn.close();
                JOptionPane.showMessageDialog(this, "스키마 변경 성공!");
                new ManagerFrame(url, acct, pw);
                this.setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "스키마 변경 실패.");
            }
        }

        if (e.getSource() == btnNormal) {
            new MainFrame(url, acct, pw);
            this.setVisible(false);
        }

    }
}
