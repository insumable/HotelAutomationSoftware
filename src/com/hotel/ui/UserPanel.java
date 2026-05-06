package com.hotel.ui;

import com.hotel.manager.Database;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private final JTextField     tfUser = new JTextField(14);
    private final JPasswordField pfPass = new JPasswordField(14);
    private final JComboBox<UserRole> cbRole = new JComboBox<>(UserRole.values());

    private final String[] COLS = {"Username", "Role"};
    private final DefaultTableModel model = new DefaultTableModel(COLS, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public UserPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setBackground(UIConstants.BG);
        buildForm();
        buildTable();
        refresh();
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "👥 Create User Account",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,8,6,8); g.anchor = GridBagConstraints.WEST;

        g.gridy=0; g.gridx=0; form.add(lbl("Username *"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; form.add(tfUser, g);
        g.gridx=2; g.fill=GridBagConstraints.NONE; form.add(lbl("Password *"), g);
        g.gridx=3; g.fill=GridBagConstraints.HORIZONTAL; form.add(pfPass, g);

        g.gridy=1; g.gridx=0; g.fill=GridBagConstraints.NONE; form.add(lbl("Role"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        cbRole.setFont(UIConstants.LABEL_FONT);
        form.add(cbRole, g);

        JButton btnAdd = UIConstants.primaryBtn("Create User");
        JButton btnDel = UIConstants.dangerBtn("Delete User");
        btnAdd.addActionListener(e -> addUser());
        btnDel.addActionListener(e -> deleteUser());

        g.gridy=1; g.gridx=2; g.gridwidth=2; g.fill=GridBagConstraints.NONE;
        JPanel br = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        br.setOpaque(false); br.add(btnAdd); br.add(btnDel);
        form.add(br, g);

        add(form, BorderLayout.NORTH);
    }

    private void buildTable() {
        table.setFont(UIConstants.LABEL_FONT);
        table.setRowHeight(24);
        table.getTableHeader().setFont(UIConstants.BOLD_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "📋 User Accounts",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.CENTER);
    }

    private void addUser() {
        String user = tfUser.getText().trim();
        String pass = new String(pfPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            msg("Username and Password required.", true); return;
        }
        if (Database.get().getUsers().containsKey(user)) {
            msg("Username already exists.", true); return;
        }
        UserRole role = (UserRole) cbRole.getSelectedItem();
        Database.get().getUsers().put(user, new User(user, pass, role));
        msg("User [" + user + "] created.", false);
        tfUser.setText(""); pfPass.setText("");
        refresh();
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row < 0) { msg("Select a row to delete.", true); return; }
        String username = model.getValueAt(row, 0).toString();
        if ("admin".equals(username)) { msg("Cannot delete the admin account.", true); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user [" + username + "]?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Database.get().getUsers().remove(username);
            refresh();
        }
    }

    private void refresh() {
        model.setRowCount(0);
        for (User u : Database.get().getUsers().values())
            model.addRow(new Object[]{ u.getUsername(), u.getRole() });
    }

    private void msg(String t, boolean err) {
        JOptionPane.showMessageDialog(this, t, "User Management",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT); return l;
    }
}
