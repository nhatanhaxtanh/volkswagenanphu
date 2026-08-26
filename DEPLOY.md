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
# Trên VPS (thư mục production hiện tại: ~/volkswagenanphu)
git clone <your-repo> volkswagenanphu
cd volkswagenanphu

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

## Cấu hình domain + HTTPS

> ⚠️ **Không dùng `certbot --standalone`.** Standalone tự bind port 80, nhưng
> port đó do container `vw_nginx` giữ, nên mọi lần auto-renew sẽ fail âm thầm và
> cert hết hạn sau 90 ngày. Dùng webroot như dưới đây — renew không đụng port 80.

```bash
# Cài Certbot
apt install certbot

# Thư mục chứa ACME challenge (đã mount vào vw_nginx qua docker-compose.yml)
mkdir -p /var/www/certbot
docker compose up -d nginx

# Lấy SSL certificate
certbot certonly --webroot -w /var/www/certbot \
  --cert-name yourdomain.com \
  -d yourdomain.com -d www.yourdomain.com
```

`nginx/nginx.conf` đã có sẵn `location /.well-known/acme-challenge/` trong
server block port 80 để phục vụ challenge. Không xoá location đó.

Cert nằm ở `/etc/letsencrypt/live/<domain>/`, được mount thẳng vào container —
không cần copy vào `nginx/ssl/`.

### Deploy hook (bắt buộc)

nginx chỉ đọc cert lúc khởi động. Certbot renew thành công nhưng không reload thì
nginx vẫn phục vụ cert cũ cho tới khi hết hạn:

```bash
cat > /etc/letsencrypt/renewal-hooks/deploy/reload-nginx.sh <<'EOF'
#!/bin/sh
docker exec vw_nginx nginx -s reload
EOF
chmod +x /etc/letsencrypt/renewal-hooks/deploy/reload-nginx.sh
```

### Kiểm tra auto-renew

```bash
certbot renew --dry-run              # phải báo success
systemctl list-timers | grep certbot # phải thấy certbot.timer
```

`--dry-run` success là bằng chứng duy nhất cho biết cert sẽ tự gia hạn.

### Lưu ý: không cài nginx trên host

VPS chỉ được có một nginx là container `vw_nginx`. Nếu nginx host chạy, nó sẽ
chiếm port 80/443 và phục vụ trang "Welcome to nginx!" thay cho website:

```bash
systemctl mask nginx   # chặn nginx host khởi động dưới mọi hình thức
```

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
