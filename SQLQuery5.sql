USE master;
GO

CREATE LOGIN javauser
WITH PASSWORD = 'Java@123456';
GO

USE QuanLyQuanNuoc;
GO

CREATE USER javauser FOR LOGIN javauser;
GO

ALTER ROLE db_owner ADD MEMBER javauser;
GO