# Hướng dẫn Deploy lên VPS

## Cấu trúc dự án

```
Knguyen/
├── frontend/          # Next.js 16 (React, shadcn/ui, Framer Motion)
├── backend/           # Spring Boot 3.4 (PostgreSQL, JWT, Gmail SMTP)
├── nginx/             # Cấu hình nginx CŨ, không còn dùng (giữ để quay về khi cần)
├── docker-compose.yml
└── .env.example       # Biến môi trường mẫu
```

## Yêu cầu VPS

- Docker + Docker Compose
- Stack proxy `edge` (Caddy) đang chạy ở `/root/edge` và network `edge` đã tạo (xem mục Domain + HTTPS)
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

- **Website**: `https://volkswagenanphu.vn`
- **Admin**: `https://volkswagenanphu.vn/admin/login`
- **API**: `https://volkswagenanphu.vn/api/models`

## Domain + HTTPS (proxy chung Caddy)

VPS chạy chung VW và HPPM. **VW không giữ cổng 80/443.** Cổng 80/443 do container
`edge_caddy` (stack `/root/edge`, không thuộc dự án nào) giữ; Caddy tự cấp và gia hạn
Let's Encrypt, tự chuyển http → https. Bản gốc của `Caddyfile` nằm trong repo HPPM
(`github.com/nhatanhaxtanh/hppmweb`, `deploy/edge/Caddyfile`), bản trên VPS phải giống bản đó.

```
Internet ──80/443──> edge_caddy ──(network edge)──> vw_frontend:3000 / vw_backend:8080 (/api/*)
                                        └─────────> hppm-backend
```

- `vw_frontend`, `vw_backend` tham gia network `edge` (`external: true`); `vw_postgres` chỉ ở `vw_network`.
- Domain VW (`volkswagenanphu.vn`, `www` → redirect về apex) khai báo trong `/root/edge/Caddyfile`.
  Đổi domain/route thì sửa file đó (dùng `nano`, không dùng `vim`) rồi:
  ```bash
  cd /root/edge
  docker compose exec caddy caddy validate --config /etc/caddy/Caddyfile
  docker compose exec caddy caddy reload --config /etc/caddy/Caddyfile
  ```
- ⚠️ **Không bao giờ dừng stack `/root/edge`** khi deploy VW, và không thêm `ports: 80/443` vào compose VW.
  `docker compose down/up` ở `/root/volkswagenanphu` chỉ ảnh hưởng VW.
- Không cần certbot cho VW nữa. Không cài nginx trên host (`systemctl mask nginx`).

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
