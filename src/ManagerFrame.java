import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerFrame extends JFrame implements ActionListener {

    private final JButton btnChangeSchema, btnNormal;
    private final JTextField txtMinSalary, txtMaxSalary;

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

        // DB 정보 저장
        this.url = url;
        this.acct = acct;
        this.pw = pw;

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JLabel lblTitle = new JLabel("스키마 변경 페이지");
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titlePanel.add(lblTitle);
        this.add(titlePanel, BorderLayout.NORTH);

        // Salary 입력 패널
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel lblMinSalary = new JLabel("최소 Salary:");
        lblMinSalary.setFont(font1);
        txtMinSalary = new JTextField(10);
        txtMinSalary.setFont(font1);

        JLabel lblMaxSalary = new JLabel("최대 Salary:");
        lblMaxSalary.setFont(font1);
        txtMaxSalary = new JTextField(10);
        txtMaxSalary.setFont(font1);

        inputPanel.add(lblMinSalary);
        inputPanel.add(txtMinSalary);
        inputPanel.add(lblMaxSalary);
        inputPanel.add(txtMaxSalary);

        this.add(inputPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnChangeSchema = new JButton("스키마 변경");
        btnChangeSchema.setFont(font1);
        btnChangeSchema.addActionListener(this);
        buttonPanel.add(btnChangeSchema);

        btnNormal = new JButton("일반 사용자");
        btnNormal.setFont(font1);
        btnNormal.addActionListener(this);
        buttonPanel.add(btnNormal);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnChangeSchema) {
            String minSalary = txtMinSalary.getText();
            String maxSalary = txtMaxSalary.getText();

            // 최소, 최대값 검증
            if (minSalary.isEmpty() || maxSalary.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Salary의 최소, 최대값을 모두 입력해주세요.");
                return;
            }

            int intMinSalary, intMaxSalary;
            try {
                intMinSalary = Integer.parseInt(minSalary);
                intMaxSalary = Integer.parseInt(maxSalary);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "입력값은 숫자여야 합니다.");
                return;
            }

            if (intMinSalary > intMaxSalary) {
                JOptionPane.showMessageDialog(this, "최소값은 최대값보다 클 수 없습니다.");
                return;
            }

            // 제약조건 설정         
            String addConstraintSQL = String.format(
                "ALTER TABLE EMPLOYEE MODIFY COLUMN Salary DECIMAL(10,2) CHECK (Salary >= %d AND Salary <= %d)",
                intMinSalary, intMaxSalary);

            try (Connection conn = DriverManager.getConnection(url, acct, pw);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(addConstraintSQL);

                JOptionPane.showMessageDialog(this, "스키마 변경 성공!");
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
