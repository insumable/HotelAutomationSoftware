package com.hotel.ui;

import com.hotel.manager.FreqGuestManager;
import com.hotel.model.FrequentGuest;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class FreqGuestPanel extends JPanel {
    private final FreqGuestManager fgm = new FreqGuestManager();

    private final JTextField tfId      = new JTextField(10);
    private final JTextField tfName    = new JTextField(18);
    private final JTextField tfContact = new JTextField(14);
    private final JTextField tfRate    = new JTextField(6);

    private final String[] COLS = {"Freq.ID","Name","Contact","Discount %","Visits"};
    private final DefaultTableModel model = new DefaultTableModel(COLS, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public FreqGuestPanel() {
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
                "⭐ Register / Update Frequent Guest",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6); g.anchor = GridBagConstraints.WEST;

        Object[][] rows = {
            {"Freq. Guest ID *", tfId},   {"Full Name *", tfName},
            {"Contact *", tfContact},     {"Discount % *", tfRate}
        };
        int r = 0;
        for (int i = 0; i < rows.length; i += 2) {
            g.gridy=r; g.gridx=0; g.fill=GridBagConstraints.NONE;
            form.add(lbl(rows[i][0].toString()), g);
            g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
            form.add((Component)rows[i][1], g);
            if (i+1 < rows.length) {
                g.gridx=2; g.fill=GridBagConstraints.NONE;
                form.add(lbl(rows[i+1][0].toString()), g);
                g.gridx=3; g.fill=GridBagConstraints.HORIZONTAL;
                form.add((Component)rows[i+1][1], g);
            }
            r++;
        }
        JButton btnReg = UIConstants.primaryBtn("Register");
        JButton btnUpd = UIConstants.accentBtn("Update Discount");
        btnReg.addActionListener(e -> register());
        btnUpd.addActionListener(e -> updateDiscount());

        g.gridy=r; g.gridx=0; g.gridwidth=4; g.fill=GridBagConstraints.NONE;
        JPanel br = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        br.setOpaque(false); br.add(btnReg); br.add(btnUpd);
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
                "📋 Frequent Guest Registry",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.CENTER);
    }

    private void register() {
        String id   = tfId.getText().trim();
        String name = tfName.getText().trim();
        String con  = tfContact.getText().trim();
        if (id.isEmpty()||name.isEmpty()||con.isEmpty()) {
            msg("ID, Name and Contact are required.", true); return;
        }
        double rate;
        try { rate = Double.parseDouble(tfRate.getText().trim()); }
        catch (NumberFormatException ex) { msg("Invalid discount rate.", true); return; }

        if (fgm.register(id, name, con, rate)) {
            msg("Frequent guest [" + id + "] registered.", false);
            refresh();
        } else {
            msg("ID already exists. Use 'Update Discount' to change rate.", true);
        }
    }

    private void updateDiscount() {
        String id = tfId.getText().trim();
        double rate;
        try { rate = Double.parseDouble(tfRate.getText().trim()); }
        catch (NumberFormatException ex) { msg("Invalid discount rate.", true); return; }
        if (fgm.updateDiscount(id, rate)) {
            msg("Discount updated for [" + id + "].", false); refresh();
        } else {
            msg("Frequent guest ID not found.", true);
        }
    }

    private void refresh() {
        model.setRowCount(0);
        for (FrequentGuest fg : fgm.getAllProfiles())
            model.addRow(new Object[]{
                fg.getFreqGuestId(), fg.getGuestName(),
                fg.getContactNumber(),
                String.format("%.1f%%", fg.getDiscountRate()),
                fg.getTotalVisits()
            });
    }

    private void msg(String t, boolean err) {
        JOptionPane.showMessageDialog(this, t, "Frequent Guest",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT); return l;
    }
}
