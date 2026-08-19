# Hero banner — file nguồn

Thư mục này giữ **file gốc** của media dùng cho hero banner. Nó nằm ngoài
build context của cả `frontend/` lẫn `backend/` nên không bị đóng vào Docker
image — chỉ để lưu trữ và version.

File thật mà website đang phục vụ nằm trong Docker volume `uploads_data`
trên VPS (`/app/uploads/hero/`), volume đó **không được backup tự động**.
Đây là bản sao dự phòng của những file đó.

## Danh sách

| File | Mô tả |
|---|---|
| `id-era-9x-master.mp4` | Bản gốc 1.6MB, 1280x720, 9.3s, h264 + audio aac. Dùng để encode lại khi cần. |
| `id-era-9x-web.mp4` | Bản 700KB đã nén, **chính là file đang chạy** trên slide "Mở đầu xu hướng" (`/api/uploads/hero/b99e688c-bb2d-4e49-8805-14436f90bb27.mp4`). |

## Nén lại cho web

Hero render bằng `<video muted>` nên audio track là dung lượng thừa — luôn bỏ
bằng `-an`. `+faststart` đẩy moov atom lên đầu file để video phát ngay thay vì
đợi tải xong.

```bash
ffmpeg -i id-era-9x-master.mp4 \
  -an -c:v libx264 -crf 28 -preset slow -pix_fmt yuv420p -movflags +faststart \
  id-era-9x-web.mp4
```

Giữ file web dưới **1MB** chừng nào `client_max_body_size` của nginx còn ở mặc
định — không thì upload sẽ bị chặn bằng 413 trước khi tới backend.
