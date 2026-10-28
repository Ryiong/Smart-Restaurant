package com.example.SmartRestaurant.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Hóa đơn máy in nhiệt khổ 80mm bằng Java 2D (cần Java 16+ vì dùng record).
 * <p>
 * Giấy 80mm, vùng in thực tế ~72mm (576 dot ở 203dpi).
 * Đơn vị của Java Printing API là point (1/72 inch).
 * <p>
 * Chạy: java ReceiptDesign  -> receipt-preview.png
 * Muốn dùng lại bố cục ở nơi khác: gọi layout(g2d, true) trên Graphics2D bất kỳ.
 */
public class ReceiptDesign {

    private static final int WIDTH = 576;
    private static final int PADDING = 30;

    public static BufferedImage render() {
        int height = 1165;

        BufferedImage image = new BufferedImage(
                WIDTH, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, height);
        g.setColor(Color.BLACK);

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int y = 25;

        // HEADER
        y = centered(g, "NHÀ HÀNG", 36, true, y);
        y = centered(g, "SMARTRESTAURANT", 36, true, y);
        y = centered(g, "123 Phố Huế, Hai Bà Trưng, Hà Nội",
                20, false, y);
        y = centered(g, "ĐT: 024 1234 5678", 20, false, y);
        y += 14;

        line(g, y, false);
        y += 14;

        // TITLE
        y = centered(g, "HÓA ĐƠN THANH TOÁN", 32, true, y);
        y = centered(g, "Số: HD-20261021-0042", 24, false, y);
        y += 6;

        line(g, y, false);
        y += 12;

        // ORDER INFO
        y = pair(g, "Bàn:", "Bàn 07 - Tầng 1", 23, true, y);
        y = pair(g, "Thu ngân:", "Nguyễn Văn A", 23, false, y);
        y = pair(g, "Thời gian:", "21/10/2026 19:45", 23, false, y);
        y += 5;

        line(g, y, true);
        y += 12;

        // ITEM HEADER
        y = pair(g, "Món / SL x Đơn giá", "Thành tiền",
                21, true, y);

        // ITEMS
        y = item(g, "Phở bò tái nạm",
                "(Ít hành, không hành tây)",
                "2 x 65.000đ", "130.000đ", y);

        y = item(g, "Bún chả Hà Nội",
                null, "1 x 70.000đ", "70.000đ", y);

        y = item(g, "Nem rán (phần 4 cái)",
                null, "3 x 45.000đ", "135.000đ", y);

        y = item(g, "Trà đá",
                null, "4 x 5.000đ", "20.000đ", y);

        y = item(g, "Bia Hà Nội chai 330ml",
                "(Ướp lạnh)", "6 x 22.000đ", "132.000đ", y);

        y += 2;
        line(g, y, true);
        y += 10;

        // SUMMARY
        y = pair(g, "Tạm tính:", "487.000đ", 22, false, y);
        y = pair(g, "Giảm giá:", "-20.000đ", 22, false, y);
        y = pair(g, "VAT (8%):", "37.360đ", 22, false, y);
        y += 5;

        line(g, y, false);
        y += 8;

        y = pair(g, "TỔNG CỘNG", "504.360đ", 31, true, y);
        y += 4;

        line(g, y, false);
        y += 10;

        // PAYMENT
        y = pair(g, "Thanh toán (Tiền mặt):",
                "600.000đ", 22, false, y);
        y = pair(g, "Tiền thừa:",
                "95.640đ", 22, false, y);
        y += 10;

        line(g, y, true);
        y += 14;

        // FOOTER
        y = centered(g, "Cảm ơn quý khách!", 21, false, y);
        y = centered(g, "Hẹn gặp lại quý khách", 21, false, y);
        y = centered(g, "Wifi: smartrestaurant / 12345678",
                20, false, y);

        g.dispose();
        return image;
    }

    private static int centered(
            Graphics2D g, String text, int size,
            boolean bold, int y) {

        g.setFont(new Font("SansSerif",
                bold ? Font.BOLD : Font.PLAIN, size));

        FontMetrics fm = g.getFontMetrics();
        int baseline = y + fm.getAscent();

        g.drawString(text,
                (WIDTH - fm.stringWidth(text)) / 2,
                baseline);

        return baseline + fm.getDescent() + 3;
    }

    private static int pair(
            Graphics2D g, String left, String right,
            int size, boolean bold, int y) {

        Font font = new Font("SansSerif",
                bold ? Font.BOLD : Font.PLAIN, size);

        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int baseline = y + fm.getAscent();

        g.drawString(left, PADDING, baseline);
        g.drawString(right,
                WIDTH - PADDING - fm.stringWidth(right),
                baseline);

        return baseline + fm.getDescent() + 3;
    }

    private static int item(
            Graphics2D g, String name, String note,
            String detail, String amount, int y) {

        y = drawText(g, name, 22, true, PADDING, y);

        if (note != null) {
            y = drawText(g, note, 19, false, PADDING, y);
        }

        y = pair(g, detail, amount, 21, false, y);
        return y + 4;
    }

    private static int drawText(
            Graphics2D g, String text, int size,
            boolean bold, int x, int y) {

        g.setFont(new Font("SansSerif",
                bold ? Font.BOLD : Font.PLAIN, size));

        FontMetrics fm = g.getFontMetrics();
        int baseline = y + fm.getAscent();
        g.drawString(text, x, baseline);

        return baseline + fm.getDescent() + 2;
    }

    private static void line(
            Graphics2D g, int y, boolean dashed) {

        Stroke old = g.getStroke();

        if (dashed) {
            g.setStroke(new BasicStroke(
                    2, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10,
                    new float[]{9, 6}, 0));
        } else {
            g.setStroke(new BasicStroke(3));
        }

        g.drawLine(PADDING, y, WIDTH - PADDING, y);
        g.setStroke(old);
    }
}

