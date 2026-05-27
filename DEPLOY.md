# Hướng dẫn Deploy lên VPS

## Cấu trúc dự án

```
Knguyen/
├── frontend/          # Next.js 16 (React, shadcn/ui, Framer Motion)
├── backend/           # Spring Boot 3.4 (PostgreSQL, JWT, Gmail SMTP)
├── nginx/             # Reverse proxy config
├── docker-compose.yml
└── .env.example       # Biến môi trường mẫu
```

## Yêu cầu VPS

- Docker + Docker Compose
- Mở port 80 (và 443 nếu dùng HTTPS)
- Ít nhất 2GB RAM

## Bước 1: Cấu hình Gmail App Password

1. Đăng nhập Gmail → [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
2. Tạo App Password cho "Mail"
3. Lưu lại mật khẩu 16 ký tự (dạng `xxxx xxxx xxxx xxxx`)

## Bước 2: Clone và cấu hình

```bash
# Trên VPS
git clone <your-repo> vwsaigon
cd vwsaigon

# Tạo file .env từ template
cp .env.example .env
nano .env   # Điền đầy đủ thông tin
```

Nội dung `.env`:
```
JWT_SECRET=<chuỗi random dài 32+ ký tự>
GMAIL_USERNAME=your-gmail@gmail.com
GMAIL_APP_PASSWORD=xxxx-xxxx-xxxx-xxxx
NOTIFICATION_EMAIL=email-nhan-thong-bao@gmail.com
ADMIN_USERNAME=admin
ADMIN_PASSWORD=<mật khẩu mạnh>
```

## Bước 3: Deploy

```bash
# Build và chạy tất cả services
docker compose --env-file .env up -d --build

# Kiểm tra logs
docker compose logs -f

# Kiểm tra từng service
docker compose ps
```

## Bước 4: Kiểm tra

- **Website**: `http://your-vps-ip`
- **Admin**: `http://your-vps-ip/admin/login`
- **API**: `http://your-vps-ip/api/models`

## Cấu hình domain + HTTPS (tùy chọn)

```bash
# Cài Certbot
apt install certbot

# Lấy SSL certificate
certbot certonly --standalone -d yourdomain.com

# Copy certificates
mkdir -p nginx/ssl
cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem nginx/ssl/
cp /etc/letsencrypt/live/yourdomain.com/privkey.pem nginx/ssl/
```

Sau đó cập nhật `nginx/nginx.conf` để dùng HTTPS.

## Quản lý

```bash
# Xem logs backend
docker compose logs backend -f

# Restart một service
docker compose restart backend

# Update code
git pull
docker compose up -d --build

# Backup database
docker exec vw_postgres pg_dump -U vwsaigon vwsaigon > backup.sql
```

## Development local

### Frontend
```bash
cd frontend
cp .env.local.example .env.local
npm install
npm run dev   # chạy tại http://localhost:3000
```

### Backend
```bash
# Chạy PostgreSQL local (cần Docker)
docker run -d -p 5432:5432 -e POSTGRES_DB=vwsaigon -e POSTGRES_USER=vwsaigon -e POSTGRES_PASSWORD=vwsaigon_password postgres:16-alpine

# Điền thông tin Gmail vào application.properties
cd backend
./mvnw spring-boot:run   # chạy tại http://localhost:8080
```

## Tài khoản admin mặc định

- **Username**: `admin` (hoặc giá trị `ADMIN_USERNAME` trong .env)
- **Password**: `Admin@2024!` (hoặc giá trị `ADMIN_PASSWORD` trong .env)

⚠️ Đổi mật khẩu ngay sau lần đăng nhập đầu tiên tại `/admin/settings`
