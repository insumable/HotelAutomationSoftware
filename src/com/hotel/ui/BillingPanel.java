package com.hotel.ui;

import com.hotel.manager.*;
import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class BillingPanel extends JPanel {
    private final BillingManager     bm  = new BillingManager();
    private final ReservationManager rm  = new ReservationManager();
    private final CateringManager    cm  = new CateringManager();

    private final JTextField   tfToken  = new JTextField(12);
    private final JTextArea    taResult = new JTextArea(18, 40);
    private final JComboBox<String> cbActive = new JComboBox<>();

    public BillingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setBackground(UIConstants.BG);
        buildUI();
    }

    private void buildUI() {
        // ── controls ──────────────────────────────────────────────────────
        JPanel ctrl = new JPanel(new GridBagLayout());
        ctrl.setBackground(UIConstants.PANEL_BG);
        ctrl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "💳 Guest Checkout & Bill",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.anchor = GridBagConstraints.WEST;

        g.gridy=0; g.gridx=0;
        ctrl.add(lbl("Select Active Guest"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        refreshDropdown();
        cbActive.setFont(UIConstants.LABEL_FONT);
        cbActive.setPreferredSize(new Dimension(260, 30));
        cbActive.addActionListener(e -> {
            String sel = (String) cbActive.getSelectedItem();
            if (sel != null && sel.contains("(")) {
                tfToken.setText(sel.substring(0, sel.indexOf(' ')));
            }
        });
        ctrl.add(cbActive, g);

        g.gridy=1; g.gridx=0; g.fill=GridBagConstraints.NONE;
        ctrl.add(lbl("Or enter Token"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        ctrl.add(tfToken, g);

        g.gridy=2; g.gridx=0; g.gridwidth=2; g.fill=GridBagConstraints.NONE;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnBill    = UIConstants.primaryBtn("Generate Bill & Checkout");
        JButton btnPreview = UIConstants.accentBtn("Preview Orders");
        JButton btnRefresh = UIConstants.primaryBtn("Refresh List");
        btnBill.addActionListener(e -> generateBill());
        btnPreview.addActionListener(e -> previewOrders());
        btnRefresh.addActionListener(e -> { refreshDropdown(); cbActive.revalidate(); });
        btnRow.add(btnBill); btnRow.add(btnPreview); btnRow.add(btnRefresh);
        ctrl.add(btnRow, g);

        add(ctrl, BorderLayout.NORTH);

        // ── bill display ──────────────────────────────────────────────────
        taResult.setFont(UIConstants.MONO_FONT);
        taResult.setEditable(false);
        taResult.setBackground(new Color(252, 252, 248));
        taResult.setBorder(new EmptyBorder(10, 12, 10, 12));
        taResult.setText("Generate a bill to see the itemised breakdown here.");

        JScrollPane sp = new JScrollPane(taResult);
        sp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180,180,180)),
                "🧾 Bill",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIConstants.BOLD_FONT, UIConstants.PRIMARY));
        add(sp, BorderLayout.CENTER);
    }

    private void generateBill() {
        String token = tfToken.getText().trim();
        if (token.isEmpty()) { msg("Enter or select a token.", true); return; }

        Bill bill = bm.generateBill(token);
        if (bill == null) {
            msg("Token not found or guest already checked out.", true);
        } else {
            taResult.setText(billText(bill, token));
            refreshDropdown();
        }
    }

    private void previewOrders() {
        String token = tfToken.getText().trim();
        if (token.isEmpty()) { msg("Enter or select a token.", true); return; }
        List<CateringOrder> orders = cm.getOrdersByToken(token);
        if (orders.isEmpty()) {
            taResult.setText("No catering orders for token: " + token);
        } else {
            StringBuilder sb = new StringBuilder("Catering Orders for Token: " + token + "\n");
            sb.append("─".repeat(50)).append("\n");
            double total = 0;
            for (CateringOrder o : orders) {
                sb.append(o).append("\n");
                total += o.getLineTotal();
            }
            sb.append("─".repeat(50)).append("\n");
            sb.append(String.format("Total: ₹ %,.2f\n", total));
            taResult.setText(sb.toString());
        }
    }

    private String billText(Bill b, String token) {
        Guest g = new ReservationManager().getGuestByToken(token);
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(50)).append("\n");
        sb.append("        HOTEL AUTOMATION SOFTWARE\n");
        sb.append("              INVOICE\n");
        sb.append("═".repeat(50)).append("\n\n");
        if (g != null) {
            sb.append(String.format("Guest  : %s\n", g.getGuestName()));
            sb.append(String.format("Token  : %s\n", g.getTokenNumber()));
            sb.append(String.format("Room   : %d\n", g.getAssignedRoomNumber()));
            sb.append(String.format("Nights : %d\n\n", g.getStayDurationNights()));
        }
        sb.append(b.getSummary()).append("\n\n");
        sb.append("═".repeat(50)).append("\n");
        sb.append("Thank you for staying with us!\n");
        return sb.toString();
    }

    private void refreshDropdown() {
        cbActive.removeAllItems();
        for (Guest g : rm.getActiveGuests())
            cbActive.addItem(g.getTokenNumber() + " (" + g.getGuestName() + ")");
    }

    private void msg(String text, boolean err) {
        JOptionPane.showMessageDialog(this, text, "Billing",
                err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(UIConstants.LABEL_FONT); return l;
    }
}
