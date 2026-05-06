package com.hotel.ui;

import java.awt.*;

public class UIConstants {
    public static final Color PRIMARY    = new Color(26,  43,  76);   // deep navy
    public static final Color ACCENT     = new Color(212, 175, 55);   // gold
    public static final Color BG         = new Color(245, 245, 245);
    public static final Color PANEL_BG   = Color.WHITE;
    public static final Color SUCCESS    = new Color(34, 139, 34);
    public static final Color DANGER     = new Color(180, 30,  30);

    public static final Font TITLE_FONT  = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font LABEL_FONT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BOLD_FONT   = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font MONO_FONT   = new Font("Monospaced", Font.PLAIN, 13);

    public static javax.swing.JButton primaryBtn(String text) {
        javax.swing.JButton b = new javax.swing.JButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(BOLD_FONT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 36));
        return b;
    }

    public static javax.swing.JButton accentBtn(String text) {
        javax.swing.JButton b = primaryBtn(text);
        b.setBackground(ACCENT);
        b.setForeground(PRIMARY);
        return b;
    }

    public static javax.swing.JButton dangerBtn(String text) {
        javax.swing.JButton b = primaryBtn(text);
        b.setBackground(DANGER);
        return b;
    }
}
