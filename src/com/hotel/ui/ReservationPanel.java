package com.hotel.ui;

import com.hotel.manager.*;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationPanel extends JPanel {
    private final ReservationManager resMgr = new ReservationManager();

    private final JTextField tfName    = new JTextField(18);
    private final JTextField tfContact = new JTextField(14);
    private final JTextField tfId      = new JTextField(14);
    private final JTextField tfNights  = new JTextField(5);
    private final JTextField tfAdv     = new JTextField(8);
    private final JTextField tfFGId    = new JTextField(10);
    private final JComboBox<BedType>    cbBed     = new JComboBox<>(BedType.values());
    private final JComboBox<AmenityType> cbAmenity = new JComboBox<>(AmenityType.values());

    private final String[] COLS = {"Token","Guest","Room","Bed","Amenity","Nights","Status"};
    private final DefaultTableModel tableModel = new DefaultTableModel(COLS, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public ReservationPanel() {
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
                "🛎 New Reservation / Check-In",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,6,5,6);
        g.anchor = GridBagConstraints.WEST;

        Object[][] rows = {
            {"Guest Name *", tfName},      {"Contact *", tfContact},
            {"ID Proof *",   tfId},        {"Stay Nights *", tfNights},
            {"Advance (₹)",  tfAdv},       {"Freq. Guest ID", tfFGId},
            {"Bed Type",     cbBed},       {"Amenity",  cbAmenity}
        };

        int r = 0;
        for (int i = 0; i < rows.length; i += 2) {
            g.gridy = r;
            g.gridx = 0; g.fill = GridBagConstraints.NONE; form.add(lbl(rows[i][0].toString()), g);
            g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; form.add((Component)rows[i][1], g);
            if (i+1 < rows.length) {
                g.gridx = 2; g.fill = GridBagConstraints.NONE; form.add(lbl(rows[i+1][0].toString()), g);
                g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL; form.add((Component)rows[i+1][1], g);
            }
            r++;
        }

        JButton btnBook = UIConstants.primaryBtn("Book & Check-In");
        JButton btnClear = UIConstants.accentBtn("Clear");
        btnBook.addActionListener(e -> doBooking());
        btnClear.addActionListener(e -> clearForm());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnBook); btnRow.add(btnClear);

        g.gridy = r; g.gridx = 0; g.gridwidth = 4; g.fill = GridBagConstraints.NONE;
        form.add(btnRow, g);

        add(form, BorderLayout.NORTH);
    }

    private void buildTable() {
        styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "📋 Active Reservations",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.CENTER);
    }

    private void doBooking() {
        String name    = tfName.getText().trim();
        String contact = tfContact.getText().trim();
        String idProof = tfId.getText().trim();
        String fgId    = tfFGId.getText().trim().isEmpty() ? null : tfFGId.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || idProof.isEmpty()) {
            msg("Guest Name, Contact, and ID Proof are required.", true); return;
        }
        int nights; double adv;
        try {
            nights = Integer.parseInt(tfNights.getText().trim());
            adv    = tfAdv.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfAdv.getText().trim());
        } catch (NumberFormatException ex) {
            msg("Nights must be a whole number; Advance must be a number.", true); return;
        }

        BedType     bt = (BedType)    cbBed.getSelectedItem();
        AmenityType at = (AmenityType) cbAmenity.getSelectedItem();

        Guest g = resMgr.makeReservation(name, contact, idProof, LocalDateTime.now(),
                                         nights, adv, bt, at, fgId);
        if (g == null) {
            msg("Sorry — no " + bt + " " + at + " room is available right now.", true);
        } else {
            msg("✅ Booking confirmed!\n\nToken  : " + g.getTokenNumber()
                + "\nRoom   : " + g.getAssignedRoomNumber()
                + "\nGuest  : " + g.getGuestName()
                + "\nNights : " + nights, false);
            clearForm();
            refresh();
        }
    }

    private void clearForm() {
        tfName.setText(""); tfContact.setText(""); tfId.setText("");
        tfNights.setText(""); tfAdv.setText(""); tfFGId.setText("");
    }

    private void refresh() {
        tableModel.setRowCount(0);
        RoomManager rm = new RoomManager();
        for (Guest g : resMgr.getActiveGuests()) {
            Room room = rm.getRoom(g.getAssignedRoomNumber());
            tableModel.addRow(new Object[]{
                g.getTokenNumber(), g.getGuestName(),
                g.getAssignedRoomNumber(),
                room != null ? room.getBedType() : "-",
                room != null ? room.getAmenityType() : "-",
                g.getStayDurationNights(),
                "Active"
            });
        }
    }

    private void msg(String text, boolean err) {
        JOptionPane.showMessageDialog(this, text, "Reservation",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT);
        return l;
    }

    private void styleTable(JTable t) {
        t.setFont(UIConstants.LABEL_FONT);
        t.setRowHeight(24);
        t.getTableHeader().setFont(UIConstants.BOLD_FONT);
        t.getTableHeader().setBackground(UIConstants.PRIMARY);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setGridColor(new Color(220, 220, 220));
    }
}
