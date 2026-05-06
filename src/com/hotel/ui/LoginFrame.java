package com.hotel.ui;

import com.hotel.manager.Database;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private final JTextField     tfUser  = new JTextField(16);
    private final JPasswordField pfPass  = new JPasswordField(16);
    private final JLabel         lblMsg  = new JLabel(" ");

    public LoginFrame() {
        super("Hotel Automation Software – Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.PRIMARY);

        // ── header ────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 20, 20, 20));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("🏨", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("HOTEL AUTOMATION", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConstants.ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Software", SwingConstants.CENTER);
        sub.setFont(UIConstants.LABEL_FONT);
        sub.setForeground(new Color(200, 200, 200));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(sub);

        // ── form ──────────────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(new EmptyBorder(28, 40, 28, 40));
        form.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        JLabel signIn = new JLabel("Sign In");
        signIn.setFont(UIConstants.TITLE_FONT);
        signIn.setForeground(UIConstants.PRIMARY);
        form.add(signIn, gbc);

        gbc.gridwidth=1; gbc.gridy=1; gbc.gridx=0;
        form.add(lbl("Username"), gbc);
        gbc.gridx=1;
        styleTF(tfUser);
        form.add(tfUser, gbc);

        gbc.gridy=2; gbc.gridx=0;
        form.add(lbl("Password"), gbc);
        gbc.gridx=1;
        styleTF(pfPass);
        form.add(pfPass, gbc);

        gbc.gridy=3; gbc.gridx=0; gbc.gridwidth=2;
        lblMsg.setForeground(UIConstants.DANGER);
        lblMsg.setFont(UIConstants.LABEL_FONT);
        form.add(lblMsg, gbc);

        gbc.gridy=4;
        JButton btnLogin = UIConstants.primaryBtn("Login");
        btnLogin.addActionListener(e -> doLogin());
        form.add(btnLogin, gbc);

        // Hint panel
        gbc.gridy=5;
        JLabel hint = new JLabel("<html><font color='gray' size='2'>" +
            "Demo – admin/admin123 · reception/rec123 · catering/cat123 · manager/mgr123</font></html>");
        form.add(hint, gbc);

        // Enter key triggers login
        ActionListener loginAction = e -> doLogin();
        tfUser.addActionListener(loginAction);
        pfPass.addActionListener(loginAction);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void doLogin() {
        String user = tfUser.getText().trim();
        String pass = new String(pfPass.getPassword());
        User u = Database.get().getUsers().get(user);
        if (u != null && u.getPassword().equals(pass)) {
            dispose();
            new MainFrame(u).setVisible(true);
        } else {
            lblMsg.setText("Invalid username or password.");
            pfPass.setText("");
        }
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.BOLD_FONT);
        return l;
    }

    private void styleTF(JTextField tf) {
        tf.setFont(UIConstants.LABEL_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                new EmptyBorder(4,8,4,8)));
    }
}
