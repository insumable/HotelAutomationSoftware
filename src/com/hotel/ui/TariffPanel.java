package com.hotel.ui;

import com.hotel.manager.*;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

public class TariffPanel extends JPanel {
    private final TariffManager tm = new TariffManager();

    private final JSpinner spMonth = new JSpinner(new SpinnerNumberModel(
            LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner spYear  = new JSpinner(new SpinnerNumberModel(
            LocalDate.now().getYear(), 2000, 2100, 1));
    private final JLabel   lblOcc  = new JLabel("—");

    private final JComboBox<BedType>    cbBed     = new JComboBox<>(BedType.values());
    private final JComboBox<AmenityType> cbAmenity = new JComboBox<>(AmenityType.values());
    private final JTextField tfPct  = new JTextField(6);

    private final JTextArea taHistory = new JTextArea(12, 42);

    public TariffPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setBackground(UIConstants.BG);
        buildOccupancy();
        buildTariff();
        buildHistory();
    }

    private void buildOccupancy() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(UIConstants.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "📊 Monthly Occupancy Report",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        spMonth.setPreferredSize(new Dimension(60, 28));
        spYear.setPreferredSize(new Dimension(80, 28));
        p.add(lbl("Month:")); p.add(spMonth);
        p.add(lbl("Year:"));  p.add(spYear);

        JButton btn = UIConstants.primaryBtn("Compute");
        btn.setPreferredSize(new Dimension(120, 32));
        btn.addActionListener(e -> {
            int m = (int) spMonth.getValue();
            int y = (int) spYear.getValue();
            double occ = tm.computeOccupancy(m, y);
            lblOcc.setText(String.format("Average Occupancy: %.2f%%  (Month %02d / %d)", occ, m, y));
            lblOcc.setForeground(occ >= 70 ? UIConstants.SUCCESS : UIConstants.PRIMARY);
        });
        p.add(btn);
        lblOcc.setFont(UIConstants.BOLD_FONT);
        lblOcc.setForeground(UIConstants.PRIMARY);
        p.add(lblOcc);
        add(p, BorderLayout.NORTH);
    }

    private void buildTariff() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(UIConstants.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "💰 Tariff Revision",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        p.add(lbl("Bed Type:")); p.add(cbBed);
        p.add(lbl("Amenity:")); p.add(cbAmenity);
        p.add(lbl("Change %:"));
        tfPct.setToolTipText("Positive = increase, Negative = decrease");
        p.add(tfPct);

        JButton btn = UIConstants.accentBtn("Apply Revision");
        btn.addActionListener(e -> applyRevision());
        p.add(btn);

        add(p, BorderLayout.CENTER);
    }

    private void buildHistory() {
        taHistory.setFont(UIConstants.MONO_FONT);
        taHistory.setEditable(false);
        taHistory.setBackground(new Color(252, 252, 248));
        refreshHistory();

        JScrollPane sp = new JScrollPane(taHistory);
        sp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "📜 Revision History",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.SOUTH);
    }

    private void applyRevision() {
        BedType     bt = (BedType)    cbBed.getSelectedItem();
        AmenityType at = (AmenityType) cbAmenity.getSelectedItem();
        double pct;
        try { pct = Double.parseDouble(tfPct.getText().trim()); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid percentage.", "Error",
                    JOptionPane.ERROR_MESSAGE); return;
        }
        int count = tm.applyRevision(bt, at, pct);
        JOptionPane.showMessageDialog(this,
                count + " room(s) updated by " + pct + "%.", "Revision Applied",
                JOptionPane.INFORMATION_MESSAGE);
        refreshHistory();
    }

    private void refreshHistory() {
        taHistory.setText("");
        for (String entry : tm.getRevisionHistory())
            taHistory.append(entry + "\n");
        if (tm.getRevisionHistory().isEmpty())
            taHistory.setText("No revisions yet.");
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t); l.setFont(UIConstants.LABEL_FONT); return l;
    }
}
