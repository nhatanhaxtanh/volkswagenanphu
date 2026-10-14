# Volkswagen An Phú (volkswagenanphu.vn)

Website đại lý Volkswagen An Phú: trang giới thiệu xe, tin tức, đăng ký lái thử/liên hệ (gửi email),
trang quản trị `/admin`.

## Cấu trúc

- `backend/`: Spring Boot 3.4, Java 21, PostgreSQL, JWT, Gmail SMTP. Package `com.vwsaigon`
  (controller / service / repository / entity / dto / security / config).
  - API công khai và route admin khai báo trong `config/SecurityConfig.java`.
  - Ảnh upload lưu ở `/app/uploads` (volume `uploads_data`), phục vụ qua `/api/uploads/**`.
  - Upload tối đa 2MB/ảnh (`spring.servlet.multipart.*` trong `application.properties`).
  - Dockerfile dùng image `maven:3.9-eclipse-temurin-21`, **không** `apk add maven` (từng treo 30+ phút trên VPS).
- `frontend/`: Next.js 16. **Không nằm trong git**: chỉ có trên VPS (`/root/volkswagenanphu/frontend`,
  untracked). Gọi backend phía server qua `BACKEND_URL`, phía trình duyệt qua `/api`.
- `nginx/`: cấu hình nginx **cũ, không còn dùng**, giữ để quay về khi cần.
- `docker-compose.yml`: `vw_postgres`, `vw_backend`, `vw_frontend`. Không có proxy, không mở cổng 80/443.
- `DEPLOY.md`: hướng dẫn deploy chi tiết.

## Hạ tầng production

VPS `103.200.20.69` chạy chung 2 dự án: **VW** (`/root/volkswagenanphu`) và **HPPM** (`/root/hppmweb`,
chỉ backend + Postgres).

```
Internet ──80/443──> edge_caddy ──(network edge)──> vw_frontend:3000   (volkswagenanphu.vn/*)
                                        ├─────────> vw_backend:8080    (volkswagenanphu.vn/api/*, giữ prefix /api)
                                        └─────────> hppm-backend:8080  (api.hppm.vn)
www.volkswagenanphu.vn ──301──> volkswagenanphu.vn
```

- Cổng 80/443 do **Caddy** (`edge_caddy`, stack `/root/edge`) giữ, không thuộc dự án nào. Caddy tự cấp/gia hạn
  Let's Encrypt. Bản gốc `Caddyfile` nằm trong repo HPPM (`github.com/nhatanhaxtanh/hppmweb`,
  `deploy/edge/Caddyfile`); bản trên VPS và trong repo phải giống nhau.
- Network `edge` tạo tay (`docker network create edge`), compose VW khai báo `external: true`.
  `vw_frontend`, `vw_backend` ở cả `vw_network` và `edge`; `vw_postgres` chỉ ở `vw_network`.
- Đến 14/10/2026 đã chuyển xong từ `vw_nginx` sang Caddy (trước đó nginx nằm trong compose VW,
  nên `docker compose down` ở VW làm sập luôn HPPM, như sự cố 13/10/2026).

## Quy tắc quan trọng

- **Không bao giờ dừng stack `/root/edge`** khi làm việc với VW (HPPM sập theo).
- **Không thêm `ports: 80/443` hay service nginx** vào compose VW.
- Gọi nhau bằng tên container (`vw_*`) vì network `edge` dùng chung với dự án khác.
  **Ngoại lệ**: frontend gọi backend qua alias `vw-backend` (khai báo trên `vw_network`), vì Tomcat trả
  **400** khi Host header có dấu `_` (`vw_backend:8080`). Caddy không bị vì nó chuyển Host gốc.
- Sửa `Caddyfile` trên VPS bằng `nano`, không `vim` (file mount đơn lẻ, vim ghi file mới nên container đọc bản cũ).
- Không cài nginx trên host (`systemctl mask nginx`). Không cần certbot cho VW.
- Không `docker compose down -v` (xoá volume database và ảnh upload).

## Lệnh thường dùng (trên VPS)

```bash
# Deploy VW
cd /root/volkswagenanphu
git pull && docker compose config -q
docker compose up -d --build --remove-orphans

# Tắt / bật tạm VW (Caddy trả 502 cho domain VW, HPPM không ảnh hưởng)
docker compose stop
docker compose start

# Đổi domain/route VW: sửa Caddyfile, validate rồi reload (lỗi cấu hình thì Caddy giữ bản cũ)
cd /root/edge
cp Caddyfile Caddyfile.bak-$(date +%F) && nano Caddyfile
docker compose exec caddy caddy validate --config /etc/caddy/Caddyfile
docker compose exec caddy caddy reload --config /etc/caddy/Caddyfile
docker compose logs -f caddy

# Kiểm tra từ bên trong
docker network inspect edge --format '{{range .Containers}}{{.Name}} {{end}}'
docker exec vw_frontend wget -qO- http://vw-backend:8080/api/models | head -c 200
curl https://api.hppm.vn/actuator/health

# Backup database
docker exec vw_postgres pg_dump -U vwsaigon vwsaigon > backup.sql
```

Tắt hẳn domain VW (không trả gì, kể cả 502): comment 2 khối VW trong `/root/edge/Caddyfile`, reload Caddy.

## Việc còn lại

- Khi web chạy ổn vài ngày: xoá `/etc/letsencrypt/renewal-hooks/deploy/reload-vw-nginx.sh`,
  `systemctl disable --now certbot.timer` (nếu không còn ai dùng certbot). Giữ `/etc/letsencrypt` một thời gian.
- Trên VPS còn file sửa tay chưa commit: `nginx/nginx.conf` (thêm block `api.hppm.vn`, không còn dùng) và các file `.bak`.
