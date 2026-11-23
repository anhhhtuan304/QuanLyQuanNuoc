package GiaoDien;

import KetNoi.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FrmDoUong extends JFrame {

    JTextField txtMa, txtTen, txtLoaiDoUong, txtGia, txtSoLuong;
    JTable table;
    DefaultTableModel model;

    public FrmDoUong() {
        setTitle("QUẢN LÝ ĐỒ UỐNG");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color xanh = new Color(35, 145, 90);
        Color nen = new Color(235, 248, 240);

        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setBackground(nen);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(main);

        JLabel title = new JLabel("QUẢN LÝ ĐỒ UỐNG", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(xanh);
        title.setPreferredSize(new Dimension(0, 60));

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtLoaiDoUong = new JTextField();
        txtGia = new JTextField();
        txtSoLuong = new JTextField();

        JPanel form = new JPanel(new GridLayout(3, 4, 15, 15));
        form.setBackground(nen);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        addField(form, "Mã đồ uống:", txtMa);
        addField(form, "Tên đồ uống:", txtTen);
        addField(form, "Loại đồ uống:", txtLoaiDoUong);
        addField(form, "Đơn giá:", txtGia);
        addField(form, "Số lượng:", txtSoLuong);
        form.add(new JLabel());
        form.add(new JLabel());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        buttons.setBackground(nen);

        JButton btnThem = new JButton("THÊM");
        JButton btnSua = new JButton("SỬA");
        JButton btnLoaiDoUong = new JButton("XÓA");
        JButton btnTim = new JButton("TÌM KIẾM");
        JButton btnMoi = new JButton("LÀM MỚI");

        for (JButton b : new JButton[]{btnThem, btnSua, btnLoaiDoUong, btnTim, btnMoi}) {
            b.setBackground(xanh);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Arial", Font.BOLD, 13));
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setPreferredSize(new Dimension(110, 36));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttons.add(b);
        }

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setBackground(nen);
        header.add(title, BorderLayout.NORTH);
        header.add(form, BorderLayout.CENTER);
        header.add(buttons, BorderLayout.SOUTH);
        main.add(header, BorderLayout.NORTH);

        String[] columns = {"Mã", "Tên đồ uống", "Loại", "Đơn giá", "Số lượng"};
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
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnLoaiDoUong.addActionListener(e -> xoa());
        btnTim.addActionListener(e -> timKiem());
        btnMoi.addActionListener(e -> {
            clear();
            taiDuLieu("");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                txtMa.setText(model.getValueAt(r, 0).toString());
                txtTen.setText(model.getValueAt(r, 1).toString());
                txtLoaiDoUong.setText(model.getValueAt(r, 2) == null ? "" : model.getValueAt(r, 2).toString());
                txtGia.setText(model.getValueAt(r, 3).toString());
                txtSoLuong.setText(model.getValueAt(r, 4).toString());
                txtMa.setEditable(false);   // không cho đổi khóa chính khi sửa
            }
        });

        taiDuLieu("");
    }

    // ===== CÁC HÀM LÀM VIỆC VỚI CSDL =====
    private void taiDuLieu(String tuKhoa) {
        model.setRowCount(0);
        String sql = "SELECT MaDoUong, TenDoUong, LoaiDoUong, DonGia, SoLuong FROM DoUong "
                + "WHERE TenDoUong LIKE ? OR MaDoUong LIKE ? ORDER BY MaDoUong";
        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                loiKetNoi();
                return;
            }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setNString(1, "%" + tuKhoa + "%");
                ps.setNString(2, "%" + tuKhoa + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("MaDoUong"), rs.getNString("TenDoUong"),
                            rs.getNString("LoaiDoUong"), rs.getInt("DonGia"), rs.getInt("SoLuong")
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            baoLoi(ex);
        }
    }

    private void them() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã và tên đồ uống!");
            return;
        }
        int gia, sl;
        try {
            gia = Integer.parseInt(txtGia.getText().trim());
            sl = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá và số lượng phải là số nguyên!");
            return;
        }
        String sql = "INSERT INTO DoUong(MaDoUong, TenDoUong, LoaiDoUong, DonGia, SoLuong) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                loiKetNoi();
                return;
            }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, txtMa.getText().trim());
                ps.setNString(2, txtTen.getText().trim());
                ps.setNString(3, txtLoaiDoUong.getText().trim());
                ps.setInt(4, gia);
                ps.setInt(5, sl);
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            clear();
            taiDuLieu("");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("PRIMARY KEY")) {
                JOptionPane.showMessageDialog(this, "Mã đồ uống đã tồn tại!");
            } else {
                baoLoi(ex);
            }
        }
    }

    private void sua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn dòng cần sửa!");
            return;
        }
        int gia, sl;
        try {
            gia = Integer.parseInt(txtGia.getText().trim());
            sl = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá và số lượng phải là số nguyên!");
            return;
        }
        String sql = "UPDATE DoUong SET TenDoUong=?, LoaiDoUong=?, DonGia=?, SoLuong=? WHERE MaDoUong=?";
        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                loiKetNoi();
                return;
            }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setNString(1, txtTen.getText().trim());
                ps.setNString(2, txtLoaiDoUong.getText().trim());
                ps.setInt(3, gia);
                ps.setInt(4, sl);
                ps.setString(5, txtMa.getText().trim());
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
            clear();
            taiDuLieu("");
        } catch (SQLException ex) {
            baoLoi(ex);
        }
    }

    private void xoa() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn dòng cần xóa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa đồ uống này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection c = DBConnection.getConnection()) {
            if (c == null) {
                loiKetNoi();
                return;
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM DoUong WHERE MaDoUong=?")) {
                ps.setString(1, txtMa.getText().trim());
                ps.executeUpdate();
            }
            clear();
            taiDuLieu("");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("REFERENCE")) {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa: đồ uống này đã có trong hóa đơn!");
            } else {
                baoLoi(ex);
            }
        }
    }

    private void timKiem() {
        taiDuLieu(txtTen.getText().trim());
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy đồ uống!");
        }
    }

    // ===== TIỆN ÍCH =====
    private void addField(JPanel panel, String label, JTextField field) {
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Arial", Font.BOLD, 13));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lb);
        panel.add(field);
    }

    private void clear() {
        txtMa.setText("");
        txtTen.setText("");
        txtLoaiDoUong.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        txtMa.setEditable(true);
        table.clearSelection();
    }

    private void loiKetNoi() {
        JOptionPane.showMessageDialog(this, "Không kết nối được SQL Server!");
    }

    private void baoLoi(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDoUong().setVisible(true));
    }
}