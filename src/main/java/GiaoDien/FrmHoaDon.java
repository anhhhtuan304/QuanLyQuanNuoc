package GiaoDien;

import KetNoi.DBConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmHoaDon extends JFrame {

    JTextField txtMaHoaDon, txtNgayLap, txtNhanVien;
    JComboBox<String> cboBan, cboDoUong;
    JSpinner spnSoLuong;
    JLabel lblDonGia, lblTong;
    JTable table;
    DefaultTableModel model;

    // Tên đồ uống -> {mã, đơn giá, tồn kho}, đọc từ bảng DoUong
    private final Map<String, Object[]> menu = new LinkedHashMap<>();

    public FrmHoaDon() {
        setTitle("QUẢN LÝ HÓA ĐƠN");
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taiThucDon();

        Color xanh = new Color(35, 145, 90);
        Color nen = new Color(235, 248, 240);

        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setBackground(nen);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(main);

        JLabel title = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(xanh);
        title.setPreferredSize(new Dimension(0, 60));

        txtMaHoaDon = new JTextField();
        txtMaHoaDon.setEditable(false);
        txtNgayLap = new JTextField();
        txtNgayLap.setEditable(false);
        txtNhanVien = new JTextField();

        String[] dsBan = new String[10];
        for (int i = 0; i < dsBan.length; i++) dsBan[i] = "Bàn " + (i + 1);
        cboBan = new JComboBox<>(dsBan);
        cboDoUong = new JComboBox<>(menu.keySet().toArray(new String[0]));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        lblDonGia = new JLabel();
        lblDonGia.setFont(new Font("Arial", Font.BOLD, 14));
        lblDonGia.setForeground(xanh);

        JPanel form = new JPanel(new GridLayout(4, 4, 15, 15));
        form.setBackground(nen);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        addField(form, "Mã hóa đơn:", txtMaHoaDon);
        addField(form, "Ngày lập:", txtNgayLap);
        addField(form, "Bàn:", cboBan);
        addField(form, "Nhân viên:", txtNhanVien);
        addField(form, "Đồ uống:", cboDoUong);
        addField(form, "Số lượng:", spnSoLuong);
        addField(form, "Đơn giá:", lblDonGia);
        form.add(new JLabel());
        form.add(new JLabel());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        buttons.setBackground(nen);

        JButton btnThem = new JButton("THÊM MÓN");
        JButton btnXoa = new JButton("XÓA MÓN");
        JButton btnMoi = new JButton("LÀM MỚI");
        JButton btnThanhToan = new JButton("THANH TOÁN");

        for (JButton b : new JButton[]{btnThem, btnXoa, btnMoi, btnThanhToan}) {
            b.setBackground(xanh);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Arial", Font.BOLD, 13));
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setPreferredSize(new Dimension(130, 36));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttons.add(b);
        }
        btnThanhToan.setBackground(new Color(255, 143, 0));

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setBackground(nen);
        header.add(title, BorderLayout.NORTH);
        header.add(form, BorderLayout.CENTER);
        header.add(buttons, BorderLayout.SOUTH);
        main.add(header, BorderLayout.NORTH);

        String[] columns = {"STT", "Tên đồ uống", "Đơn giá", "Số lượng", "Thành tiền"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(165, 214, 167));
        table.getTableHeader().setBackground(xanh);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        DefaultTableCellRenderer tien = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value instanceof Number ? String.format("%,d", ((Number) value).longValue()) : "");
            }
        };
        tien.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(tien);
        table.getColumnModel().getColumn(4).setCellRenderer(tien);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        lblTong = new JLabel("TỔNG TIỀN: 0 VNĐ", SwingConstants.RIGHT);
        lblTong.setFont(new Font("Arial", Font.BOLD, 22));
        lblTong.setForeground(new Color(200, 60, 40));
        lblTong.setBorder(new EmptyBorder(5, 10, 5, 10));
        main.add(lblTong, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        cboDoUong.addActionListener(e -> capNhatDonGia());

        btnThem.addActionListener(e -> {
            String ten = (String) cboDoUong.getSelectedItem();
            if (ten == null) {
                JOptionPane.showMessageDialog(this, "Chưa có đồ uống nào trong CSDL!");
                return;
            }
            int gia = (Integer) menu.get(ten)[1];
            int ton = (Integer) menu.get(ten)[2];
            int sl = (Integer) spnSoLuong.getValue();

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).equals(ten)) {
                    int slMoi = (Integer) model.getValueAt(i, 3) + sl;
                    if (slMoi > ton) {
                        JOptionPane.showMessageDialog(this, "Không đủ hàng! Còn lại: " + ton);
                        return;
                    }
                    model.setValueAt(slMoi, i, 3);
                    model.setValueAt(slMoi * gia, i, 4);
                    capNhatTong();
                    return;
                }
            }
            if (sl > ton) {
                JOptionPane.showMessageDialog(this, "Không đủ hàng! Còn lại: " + ton);
                return;
            }
            model.addRow(new Object[]{model.getRowCount() + 1, ten, gia, sl, gia * sl});
            capNhatTong();
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Hãy chọn món cần xóa!");
                return;
            }
            model.removeRow(row);
            for (int i = 0; i < model.getRowCount(); i++) model.setValueAt(i + 1, i, 0);
            capNhatTong();
        });

        btnMoi.addActionListener(e -> lamMoi());
        btnThanhToan.addActionListener(e -> thanhToan());

        capNhatDonGia();
        lamMoi();
    }

    // ===== CSDL =====
    private void taiThucDon() {
        menu.clear();
        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Không kết nối được SQL Server!");
                return;
            }
            try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT MaDoUong, TenDoUong, DonGia, SoLuong FROM DoUong ORDER BY MaDoUong")) {
                while (rs.next()) {
                    menu.put(rs.getNString("TenDoUong"), new Object[]{
                        rs.getString("MaDoUong"), rs.getInt("DonGia"), rs.getInt("SoLuong")});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
        }
    }

    private String taoMaHoaDon() {
        String sql = "SELECT ISNULL(MAX(MaHoaDon), 0) + 1 FROM HoaDon";
        try (Connection c = DBConnection.getConnection()) {
            if (c != null) {
                try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                    if (rs.next()) return String.format("HD%04d", rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
        }
        return "HD0001";
    }

    private void thanhToan() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn chưa có món nào!");
            return;
        }
        if (txtNhanVien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!");
            return;
        }

        long tong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tong += ((Number) model.getValueAt(i, 4)).longValue();
        }

        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Không kết nối được SQL Server!");
                return;
            }
            c.setAutoCommit(false);   // dùng transaction: lỗi thì hủy hết
            try {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO HoaDon(MaHoaDon, Ban, NhanVien, TongTien) VALUES (?,?,?,?)")) {
                    ps.setString(1, txtMaHoaDon.getText());
                    ps.setNString(2, (String) cboBan.getSelectedItem());
                    ps.setNString(3, txtNhanVien.getText().trim());
                    ps.setInt(4, (int) tong);
                    ps.executeUpdate();
                }

                try (PreparedStatement ct = c.prepareStatement(
                        "INSERT INTO ChiTietHoaDon(MaHoaDon, MaDoUong, DonGia, SoLuong) VALUES (?,?,?,?)");
                     PreparedStatement kho = c.prepareStatement(
                        "UPDATE DoUong SET SoLuong = SoLuong - ? WHERE MaDoUong = ? AND SoLuong >= ?")) {

                    for (int i = 0; i < model.getRowCount(); i++) {
                        String ten = (String) model.getValueAt(i, 1);
                        String maDU = (String) menu.get(ten)[0];
                        int gia = (Integer) model.getValueAt(i, 2);
                        int sl = (Integer) model.getValueAt(i, 3);

                        ct.setString(1, txtMaHoaDon.getText());
                        ct.setString(2, maDU);
                        ct.setInt(3, gia);
                        ct.setInt(4, sl);
                        ct.executeUpdate();

                        kho.setInt(1, sl);
                        kho.setString(2, maDU);
                        kho.setInt(3, sl);
                        if (kho.executeUpdate() == 0) {
                            throw new SQLException("Không đủ hàng cho món: " + ten);
                        }
                    }
                }
                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại: " + ex.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Thanh toán thành công!\n"
                    + "Hóa đơn: " + txtMaHoaDon.getText() + "\n"
                    + cboBan.getSelectedItem() + "\n"
                    + lblTong.getText());
            lamMoi();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
        }
    }

    // ===== TIỆN ÍCH =====
    private void addField(JPanel panel, String label, Component field) {
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Arial", Font.BOLD, 13));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lb);
        panel.add(field);
    }

    private void capNhatDonGia() {
        String ten = (String) cboDoUong.getSelectedItem();
        if (ten != null) {
            lblDonGia.setText(String.format("%,d VNĐ  (còn %d)",
                    (Integer) menu.get(ten)[1], (Integer) menu.get(ten)[2]));
        }
    }

    private void capNhatTong() {
        long tong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tong += ((Number) model.getValueAt(i, 4)).longValue();
        }
        lblTong.setText(String.format("TỔNG TIỀN: %,d VNĐ", tong));
    }

    private void lamMoi() {
        taiThucDon();   // cập nhật lại tồn kho sau mỗi hóa đơn
        cboDoUong.setModel(new DefaultComboBoxModel<>(menu.keySet().toArray(new String[0])));
        txtMaHoaDon.setText(taoMaHoaDon());
        txtNgayLap.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        txtNhanVien.setText("");
        cboBan.setSelectedIndex(0);
        spnSoLuong.setValue(1);
        model.setRowCount(0);
        capNhatDonGia();
        capNhatTong();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmHoaDon().setVisible(true));
    }
}