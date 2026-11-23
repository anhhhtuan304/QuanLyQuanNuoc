
package GiaoDien;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmQuanLyQuanNuoc extends JFrame {

    private final Color GREEN = new Color(35, 145, 90);
    private final Color DARK_GREEN = new Color(24, 105, 65);
    private final Color LIGHT_GREEN = new Color(235, 248, 240);
    private final Color WHITE = Color.WHITE;

    public FrmQuanLyQuanNuoc() {
        setTitle("QUẢN LÝ QUÁN NƯỚC");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 600));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_GREEN);
        setContentPane(mainPanel);

        // THANH TIÊU ĐỀ
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN);
        header.setPreferredSize(new Dimension(1150, 75));
        header.setBorder(new EmptyBorder(10, 25, 10, 25));

        JLabel title = new JLabel("QUẢN LÝ QUÁN NƯỚC");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel("HỆ THỐNG QUẢN LÝ BÁN HÀNG");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(WHITE);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        mainPanel.add(header, BorderLayout.NORTH);

        // MENU BÊN TRÁI
        JPanel menu = new JPanel();
        menu.setBackground(DARK_GREEN);
        menu.setPreferredSize(new Dimension(220, 600));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel menuTitle = new JLabel("DANH MỤC");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 17));
        menuTitle.setForeground(WHITE);
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(menuTitle);
        menu.add(Box.createVerticalStrut(25));

        addMenuButton(menu, "Trang chủ");
        addMenuButton(menu, "Quản lý đồ uống");
        addMenuButton(menu, "Quản lý hóa đơn");
        addMenuButton(menu, "Thống kê doanh thu");
        addMenuButton(menu, "Thoát");

        mainPanel.add(menu, BorderLayout.WEST);

        // KHU VỰC NỘI DUNG
        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setBackground(LIGHT_GREEN);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel welcome = new JLabel("TỔNG QUAN HỆ THỐNG");
        welcome.setFont(new Font("Arial", Font.BOLD, 23));
        welcome.setForeground(DARK_GREEN);

        content.add(welcome, BorderLayout.NORTH);

        // CÁC Ô THỐNG KÊ
        JPanel statistics = new JPanel(new GridLayout(2, 2, 20, 20));
        statistics.setOpaque(false);

        statistics.add(createCard("TỔNG ĐỒ UỐNG", "05", "Sản phẩm hiện có"));
        statistics.add(createCard("HÓA ĐƠN", "00", "Hóa đơn hôm nay"));
        statistics.add(createCard("DOANH THU", "0 VNĐ", "Doanh thu hôm nay"));
        statistics.add(createCard("TRẠNG THÁI", "HOẠT ĐỘNG", "Hệ thống đang sẵn sàng"));

        content.add(statistics, BorderLayout.CENTER);

        // CHÂN TRANG
        JLabel footer = new JLabel(
                "PHẦN MỀM QUẢN LÝ QUÁN NƯỚC - PHIÊN BẢN 1.0",
                SwingConstants.CENTER
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        content.add(footer, BorderLayout.SOUTH);

        mainPanel.add(content, BorderLayout.CENTER);
    }

    // TẠO NÚT MENU
        // TẠO NÚT MENU
    private void addMenuButton(JPanel menu, String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(GREEN);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(190, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(e -> moForm(text));   // <-- dòng mới
        menu.add(button);
        menu.add(Box.createVerticalStrut(12));
    }
        // MỞ FORM TƯƠNG ỨNG VỚI NÚT MENU
    private void moForm(String ten) {
        switch (ten) {
            case "Quản lý đồ uống":
                new FrmDoUong().setVisible(true);
                break;
            case "Quản lý hóa đơn":
                new FrmHoaDon().setVisible(true);
                break;
            case "Thống kê doanh thu":
                JOptionPane.showMessageDialog(this, "Chức năng đang phát triển!");
                break;
            case "Thoát":
                if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
            default:
                break;
        }
    }

    // TẠO THẺ THỐNG KÊ
    private JPanel createCard(String heading, String value, String description) {
        JPanel card = new JPanel(new GridLayout(3, 1, 5, 5));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 230, 215)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel(heading);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(DARK_GREEN);

        JLabel number = new JLabel(value);
        number.setFont(new Font("Arial", Font.BOLD, 26));
        number.setForeground(GREEN);

        JLabel note = new JLabel(description);
        note.setFont(new Font("Arial", Font.PLAIN, 13));
        note.setForeground(Color.GRAY);

        card.add(label);
        card.add(number);
        card.add(note);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmQuanLyQuanNuoc().setVisible(true);
        });
    }
}
