package com.hotel.ui;

import com.hotel.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(User user) {
        super("Hotel Automation Software");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 700);
        setMinimumSize(new Dimension(860, 600));
        setLocationRelativeTo(null);
        buildUI(user);
    }

    private void buildUI(User user) {
        // ── title bar ─────────────────────────────────────────────────────
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(UIConstants.PRIMARY);
        titleBar.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel title = new JLabel("🏨  Hotel Automation Software");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIConstants.ACCENT);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JLabel userLbl = new JLabel("👤 " + user.getUsername()
                        + "  [" + user.getRole().name().replace('_',' ') + "]");
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(UIConstants.LABEL_FONT);

        JButton btnLogout = UIConstants.accentBtn("Logout");
        btnLogout.setPreferredSize(new Dimension(90, 30));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        right.add(userLbl);
        right.add(btnLogout);
        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(right, BorderLayout.EAST);

        // ── tabs ──────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setFont(UIConstants.BOLD_FONT);
        tabs.setBackground(UIConstants.BG);

        UserRole role = user.getRole();

        if (role == UserRole.RECEPTIONIST || role == UserRole.SYSTEM_ADMIN) {
            tabs.addTab("📋 Rooms",       new RoomPanel());
            tabs.addTab("🛎 Reservation", new ReservationPanel());
            tabs.addTab("💳 Billing",     new BillingPanel());
        }
        if (role == UserRole.CATERING_MANAGER || role == UserRole.SYSTEM_ADMIN) {
            tabs.addTab("🍽 Catering",    new CateringPanel());
        }
        if (role == UserRole.HOTEL_MANAGER || role == UserRole.SYSTEM_ADMIN) {
            tabs.addTab("📊 Occupancy",   new TariffPanel());
        }
        if (role == UserRole.SYSTEM_ADMIN) {
            tabs.addTab("⭐ Freq. Guests", new FreqGuestPanel());
            tabs.addTab("👥 Users",        new UserPanel());
        }

        // layout
        JPanel root = new JPanel(new BorderLayout());
        root.add(titleBar, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
    }
}
