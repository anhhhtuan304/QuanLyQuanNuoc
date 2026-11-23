USE QuanLyQuanNuoc;

INSERT INTO DoUong
(TenDoUong, LoaiDoUong, DonGia, SoLuong, MoTa)
VALUES
(N'Trà sữa truyền thống', N'Trà sữa', 25000, 50, N'Trà sữa'),
(N'Trà đào', N'Trà trái cây', 30000, 40, N'Trà đào'),
(N'Cà phê sữa', N'Cà phê', 20000, 30, N'Cà phê sữa'),
(N'Bạc xỉu', N'Cà phê', 25000, 25, N'Bạc xỉu'),
(N'Trà chanh', N'Trà trái cây', 15000, 60, N'Trà chanh');

SELECT * FROM DoUong;