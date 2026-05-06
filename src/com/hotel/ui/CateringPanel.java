package com.hotel.ui;

import com.hotel.manager.*;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class CateringPanel extends JPanel {
    private final CateringManager    cm  = new CateringManager();
    private final ReservationManager rm  = new ReservationManager();

    private final JTextField tfToken = new JTextField(10);
    private final JTextField tfItem  = new JTextField(18);
    private final JTextField tfQty   = new JTextField(5);
    private final JTextField tfPrice = new JTextField(8);

    private final JTextField tfSearch = new JTextField(12);

    private final String[] COLS = {"Order#","Token","Item","Qty","Unit Price","Total","Time"};
    private final DefaultTableModel model = new DefaultTableModel(COLS, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    private final JLabel lblTotal = new JLabel("Catering total: ₹ 0.00");

    public CateringPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setBackground(UIConstants.BG);
        buildForm();
        buildTable();
        refreshAll();
    }

    private void buildForm() {
        JPanel top = new JPanel(new GridLayout(1, 2, 10, 0));
        top.setOpaque(false);

        // ── add order ──────────────────────────────────────────────────────
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBackground(UIConstants.PANEL_BG);
        addPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "🍽 Log Food Order",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,6,5,6); g.anchor = GridBagConstraints.WEST;

        Object[][] rows = {
            {"Token *", tfToken}, {"Item Name *", tfItem},
            {"Quantity *", tfQty}, {"Unit Price (₹) *", tfPrice}
        };
        int r = 0;
        for (Object[] row : rows) {
            g.gridy=r; g.gridx=0; g.fill=GridBagConstraints.NONE;
            addPanel.add(lbl(row[0].toString()), g);
            g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
            addPanel.add((Component)row[1], g);
            r++;
        }
        JButton btnAdd = UIConstants.primaryBtn("Add Order");
        btnAdd.addActionListener(e -> addOrder());
        g.gridy=r; g.gridx=0; g.gridwidth=2; g.fill=GridBagConstraints.NONE;
        addPanel.add(btnAdd, g);

        // ── search ─────────────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(UIConstants.PANEL_BG);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "🔍 View Orders by Token",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints sg = new GridBagConstraints();
        sg.insets = new Insets(6,6,6,6); sg.anchor = GridBagConstraints.WEST;
        sg.gridy=0; sg.gridx=0; searchPanel.add(lbl("Token"), sg);
        sg.gridx=1; sg.fill=GridBagConstraints.HORIZONTAL;
        searchPanel.add(tfSearch, sg);

        JButton btnSearch = UIConstants.accentBtn("Filter");
        JButton btnAll    = UIConstants.primaryBtn("Show All");
        btnSearch.addActionListener(e -> filterByToken());
        btnAll.addActionListener(e -> refreshAll());

        sg.gridy=1; sg.gridx=0; sg.gridwidth=2; sg.fill=GridBagConstraints.NONE;
        JPanel br = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        br.setOpaque(false); br.add(btnSearch); br.add(btnAll);
        searchPanel.add(br, sg);

        sg.gridy=2; sg.gridx=0; sg.gridwidth=2;
        lblTotal.setFont(UIConstants.BOLD_FONT);
        lblTotal.setForeground(UIConstants.PRIMARY);
        searchPanel.add(lblTotal, sg);

        top.add(addPanel);
        top.add(searchPanel);
        add(top, BorderLayout.NORTH);
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
                "📜 Order History",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.CENTER);
    }

    private void addOrder() {
        String token = tfToken.getText().trim();
        String item  = tfItem.getText().trim();
        if (token.isEmpty() || item.isEmpty()) {
            msg("Token and Item Name are required.", true); return;
        }
        int qty; double price;
        try {
            qty   = Integer.parseInt(tfQty.getText().trim());
            price = Double.parseDouble(tfPrice.getText().trim());
        } catch (NumberFormatException ex) {
            msg("Quantity must be integer; Price must be a number.", true); return;
        }
        CateringOrder order = cm.addFoodOrder(token, item, qty, price);
        if (order == null) {
            msg("Token not found or guest already checked out.", true);
        } else {
            msg("Order #" + order.getOrderId() + " added.\nTotal: ₹" + order.getLineTotal(), false);
            tfItem.setText(""); tfQty.setText(""); tfPrice.setText("");
            refreshAll();
        }
    }

    private void refreshAll() {
        model.setRowCount(0);
        double total = 0;
        for (CateringOrder o : cm.getOrdersByToken("")) {
            appendRow(o); total += o.getLineTotal();
        }
        // show all
        model.setRowCount(0);
        for (Guest g : rm.getActiveGuests()) {
            for (CateringOrder o : cm.getOrdersByToken(g.getTokenNumber())) {
                appendRow(o); total += o.getLineTotal();
            }
        }
        lblTotal.setText(String.format("All orders total: ₹ %,.2f", total));
    }

    private void filterByToken() {
        String tok = tfSearch.getText().trim();
        model.setRowCount(0);
        List<CateringOrder> orders = cm.getOrdersByToken(tok);
        double total = 0;
        for (CateringOrder o : orders) { appendRow(o); total += o.getLineTotal(); }
        lblTotal.setText(String.format("Token [%s] total: ₹ %,.2f", tok, total));
    }

    private void appendRow(CateringOrder o) {
        model.addRow(new Object[]{
            o.getOrderId(), o.getTokenNumber(), o.getFoodItemName(),
            o.getQuantity(),
            String.format("₹ %,.2f", o.getUnitPrice()),
            String.format("₹ %,.2f", o.getLineTotal()),
            o.getOrderTimestamp().toLocalTime().toString().substring(0, 5)
        });
    }

    private void msg(String text, boolean err) {
        JOptionPane.showMessageDialog(this, text, "Catering",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT); return l;
    }
}
