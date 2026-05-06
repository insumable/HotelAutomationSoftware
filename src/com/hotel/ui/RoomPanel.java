package com.hotel.ui;

import com.hotel.manager.RoomManager;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class RoomPanel extends JPanel {
    private final RoomManager rm = new RoomManager();

    private final String[] COLS = {"Room#", "Floor", "Bed", "Amenity", "Tariff/Night", "Status"};
    private final DefaultTableModel model = new DefaultTableModel(COLS, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    // add-room fields
    private final JTextField tfNo     = new JTextField(5);
    private final JTextField tfFloor  = new JTextField(3);
    private final JTextField tfTariff = new JTextField(8);
    private final JComboBox<BedType>    cbBed     = new JComboBox<>(BedType.values());
    private final JComboBox<AmenityType> cbAmenity = new JComboBox<>(AmenityType.values());

    // status update
    private final JTextField tfStatusRoom = new JTextField(5);
    private final JComboBox<RoomStatus> cbStatus = new JComboBox<>(RoomStatus.values());

    public RoomPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setBackground(UIConstants.BG);
        buildTable();
        buildForms();
        refreshTable();
    }

    private void buildTable() {
        table.setFont(UIConstants.LABEL_FONT);
        table.setRowHeight(26);
        table.getTableHeader().setFont(UIConstants.BOLD_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(220, 230, 255));
        table.setGridColor(new Color(220, 220, 220));

        // colour status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String val = v.toString();
                setHorizontalAlignment(CENTER);
                if ("AVAILABLE".equals(val))   { setForeground(UIConstants.SUCCESS); }
                else if ("OCCUPIED".equals(val)){ setForeground(UIConstants.DANGER);  }
                else                            { setForeground(Color.GRAY); }
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        add(sectionPanel("🛏 Room Catalogue", sp), BorderLayout.CENTER);
    }

    private void buildForms() {
        JPanel south = new JPanel(new GridLayout(1, 2, 10, 0));
        south.setOpaque(false);

        // ── add room ──────────────────────────────────────────────────────
        JPanel addPanel = formCard("➕ Add Room");
        addPanel.add(lbl("Room #")); addPanel.add(tfNo);
        addPanel.add(lbl("Floor"));  addPanel.add(tfFloor);
        addPanel.add(lbl("Bed"));    addPanel.add(cbBed);
        addPanel.add(lbl("Amenity"));addPanel.add(cbAmenity);
        addPanel.add(lbl("Tariff")); addPanel.add(tfTariff);
        JButton btnAdd = UIConstants.primaryBtn("Add Room");
        btnAdd.addActionListener(e -> addRoom());
        addPanel.add(new JLabel()); addPanel.add(btnAdd);

        // ── update status ─────────────────────────────────────────────────
        JPanel statusPanel = formCard("🔄 Update Room Status");
        statusPanel.add(lbl("Room #")); statusPanel.add(tfStatusRoom);
        statusPanel.add(lbl("Status")); statusPanel.add(cbStatus);
        JButton btnStatus = UIConstants.accentBtn("Update Status");
        btnStatus.addActionListener(e -> updateStatus());
        statusPanel.add(new JLabel()); statusPanel.add(btnStatus);

        south.add(addPanel);
        south.add(statusPanel);
        add(south, BorderLayout.SOUTH);
    }

    private void addRoom() {
        try {
            int    no     = Integer.parseInt(tfNo.getText().trim());
            int    floor  = Integer.parseInt(tfFloor.getText().trim());
            double tariff = Double.parseDouble(tfTariff.getText().trim());
            BedType     bt = (BedType)    cbBed.getSelectedItem();
            AmenityType at = (AmenityType) cbAmenity.getSelectedItem();
            if (rm.addRoom(no, bt, at, tariff, floor)) {
                msg("Room " + no + " added.", false);
                refreshTable();
            } else {
                msg("Room number already exists.", true);
            }
        } catch (NumberFormatException ex) {
            msg("Invalid input. Check room#, floor, tariff.", true);
        }
    }

    private void updateStatus() {
        try {
            int rn = Integer.parseInt(tfStatusRoom.getText().trim());
            RoomStatus st = (RoomStatus) cbStatus.getSelectedItem();
            if (rm.setStatus(rn, st)) {
                msg("Status updated for room " + rn, false);
                refreshTable();
            } else {
                msg("Room not found.", true);
            }
        } catch (NumberFormatException ex) {
            msg("Enter a valid room number.", true);
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Room> rooms = rm.getAllRooms();
        for (Room r : rooms) {
            model.addRow(new Object[]{
                r.getRoomNumber(), r.getFloorNumber(),
                r.getBedType(), r.getAmenityType(),
                String.format("₹ %,.2f", r.getTariffPerNight()),
                r.getStatus()
            });
        }
    }

    private void msg(String text, boolean err) {
        JOptionPane.showMessageDialog(this, text, "Room Management",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    // ── helpers ───────────────────────────────────────────────────────────
    private JPanel sectionPanel(String title, Component content) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(UIConstants.PANEL_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(10,10,10,10)));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UIConstants.TITLE_FONT);
        lbl.setForeground(UIConstants.PRIMARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private JPanel formCard(String title) {
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.setBackground(UIConstants.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)), title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        return p;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT);
        return l;
    }
}
