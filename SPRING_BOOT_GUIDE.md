# Smart Restaurant — Dữ liệu thử (MockAPI) và chuyển sang Spring Boot

Frontend chọn nguồn dữ liệu bằng **một dòng** trong `static/js/config.js`:

```js
export const API_MODE = 'local';   // 'local' | 'mockapi' | 'spring'
```

| Chế độ | Dữ liệu nằm ở đâu | Dùng khi |
|---|---|---|
| `local` | `localStorage` của trình duyệt | Chỉ xem/chỉnh giao diện, không cần mạng |
| `mockapi` | mockapi.io (chung cho mọi máy) | Demo cho nhiều người, test trên nhiều thiết bị |
| `spring` | Backend Spring Boot thật (`/api/...`) | Chạy thật |

Giao diện (`AreaManager`, `AdminIndex`...) **không đổi** giữa 3 chế độ — chúng chỉ gọi `areaService` và `revenueService`.

---

## 1. Dùng mockapi.io

### 1.1 Tạo dự án và 2 resource

1. Đăng ký https://mockapi.io → **New Project** (giữ prefix mặc định `/api/v1`).
2. Tạo resource **`areas`** với các field (xoá các field Faker mặc định như `name`/`avatar`/`createdAt` rồi tạo lại đúng kiểu):

   | Field | Kiểu |
   |---|---|
   | `name` | String |
   | `active` | Boolean |
   | `tables` | Object (mảng JSON) |

3. Tạo resource **`transactions`**:

   | Field | Kiểu |
   |---|---|
   | `at` | Number (epoch ms) |
   | `day` | String (`YYYY-MM-DD`) |
   | `tableName` | String |
   | `areaName` | String |
   | `method` | String (`cash` / `transfer`) |
   | `total` | Number |

4. mockapi.io tự sinh vài bản ghi giả khi tạo resource → **xoá hết** (hoặc bỏ qua: code đã lọc các bản ghi không có `tables`, nhưng xoá cho sạch).
5. Copy **API endpoint** của dự án (dạng `https://xxxxxxxx.mockapi.io/api/v1`) vào `config.js`:

   ```js
   export const API_MODE = 'mockapi';
   export const MOCKAPI_BASE = 'https://xxxxxxxx.mockapi.io/api/v1';
   ```

6. Mở trang Bàn & Khu vực: lần đầu danh sách rỗng nên frontend tự nạp 4 khu vực mẫu (đánh dấu bằng key `sr:mockapi:seeded` trong localStorage để không nạp lại). Muốn nạp lại: xoá hết bản ghi trên mockapi.io và `localStorage.removeItem('sr:mockapi:seeded')`.

> Gói free hiện chỉ cho ít resource (kiểm tra lại trên trang pricing của mockapi.io). Thiết kế này dùng đúng 2 resource.

### 1.2 Cách hoạt động và giới hạn

- mockapi.io chỉ có CRUD thuần (`GET/POST/PUT/DELETE /resource`), **không** có endpoint riêng như `/tables/{id}/open`. Vì vậy luật nghiệp vụ (mở bàn, gộp, chuyển, thanh toán, kiểm tra trùng tên...) vẫn chạy ở trình duyệt (`createAreaService` trong `AreaService.mock.js`), chỉ khác là lưu lên mockapi.io. Mỗi thao tác = đọc `areas` → sửa → chỉ gửi `PUT`/`POST`/`DELETE` cho khu vực thật sự thay đổi.
- **Không có giao dịch/khoá**: hai người cùng sửa một khu vực thì ai lưu sau sẽ ghi đè. Chấp nhận được khi demo, không dùng cho thật.
- **Không có xác thực, URL công khai**: ai biết URL đều đọc/ghi được. Chỉ để dữ liệu giả.
- Trang đăng nhập/đăng ký (`/api/auth/...`) vẫn là demo giao diện, không đi qua mockapi.
- Doanh thu các ngày trước hôm nay là số mẫu tạo ở client (`demoDay`); hôm nay và sau đó lấy từ resource `transactions`.

---

## 2. Chuyển sang Spring Boot thật

### 2.1 Checklist

1. **Backend**: viết các endpoint ở mục 2.3 (đúng đường dẫn, JSON, thông báo lỗi).
2. **Frontend**: `API_MODE = 'spring'`. Không cần sửa file nào khác.
3. **Đường dẫn trang**: xem mục 2.5 (đây là chỗ hay vướng nhất).
4. **Xác thực**: xem mục 2.6.
5. Xoá `localStorage` cũ (`sr:areas:v1`, `sr:revenue:v1`) khi kiểm tra lại.

### 2.2 Quy ước dữ liệu (bắt buộc đúng, nếu không UI sẽ lỗi)

- **`id` luôn là chuỗi** trong JSON (kể cả khi DB dùng `Long`). UI so sánh `===` với `dataset.id` (chuỗi); số sẽ làm không chọn được bàn.
- **Thời gian là epoch milliseconds** (`long`): `startedAt`, `reservedAt`, `reservedUntil`, `Tx.at`.
- **Tiền là số nguyên VNĐ** (`long`), không dùng `double`.
- **`status`** là một trong: `empty`, `serving`, `paying`, `reserved`, `merged` (chữ thường).
- Lỗi nghiệp vụ: HTTP 4xx kèm `{ "message": "..." }` (tiếng Việt, hiển thị nguyên văn bằng toast). Thành công không có nội dung: `204`.

```jsonc
// Area
{ "id": "1", "name": "Sân vườn", "active": true, "tables": [ /* Table */ ] }

// Table
{ "id": "11", "name": "Bàn A02", "capacity": 4, "status": "serving",
  "guests": 2, "startedAt": 1761300000000, "reservedAt": null, "reservedUntil": null,
  "orders": [ { "name": "Phở bò", "price": 95000, "qty": 2 } ],
  "mergedInto": null, "mergedIds": [] }

// Tx (hoá đơn)
{ "id": "501", "at": 1761300000000, "tableName": "Bàn A02", "areaName": "Sân vườn",
  "method": "cash", "total": 395000 }
```

### 2.3 Danh sách endpoint

| Hàm frontend | Method + đường dẫn | Body | Trả về |
|---|---|---|---|
| `listAreas` | `GET /api/areas` | — | `Area[]` (kèm `tables`) |
| `listMenu` | `GET /api/menu-items` | — | `[{ id, name, price }]` |
| `saveArea` (mới) | `POST /api/areas` | `{ name, tables:[{ name, capacity }] }` | `Area` |
| `saveArea` (sửa) | `PUT /api/areas/{id}` | `{ id, name, tables:[{ id?, name, capacity }] }` | `Area` |
| `deleteArea` | `DELETE /api/areas/{id}` | — | `204` |
| `setAreaActive` | `PATCH /api/areas/{id}/active` | `{ active }` | `Area` |
| `openTable` | `POST /api/tables/{id}/open` | `{ guests }` | `Table` |
| `addOrder` | `POST /api/tables/{id}/orders` | `{ items:[{ name, price, qty }] }` | `Table` |
| `requestPayment` | `POST /api/tables/{id}/request-payment` | — | `Table` |
| `cancelPayment` | `POST /api/tables/{id}/cancel-payment` | — | `Table` |
| `checkoutTable` | `POST /api/tables/{id}/checkout` | `{ method:"cash"\|"transfer", received }` | `{ total, change }` |
| `cancelReservation` | `POST /api/tables/{id}/cancel-reservation` | — | `204` |
| `moveTable` | `POST /api/tables/{id}/move` | `{ toId }` | `Table` (bàn đích) |
| `mergeTables` | `POST /api/tables/{id}/merge` | `{ primaryId }` | `Table` (bàn chính) |
| `revenueService.getToday` | `GET /api/revenue/today` | — | `{ date, total, count, cash, transfer, transactions:Tx[] }` (mới nhất trước) |
| `revenueService.getDaily` | `GET /api/revenue/daily?days=N` | — | `[{ date, total, count }]` tăng dần, đủ N ngày (ngày không có hoá đơn = 0), gồm hôm nay |

Sau mỗi thao tác ghi, UI **gọi lại `GET /api/areas`** nên giá trị trả về của các thao tác ghi chỉ cần đúng cấu trúc.

### 2.4 Luật nghiệp vụ backend phải kiểm tra (thông báo lấy từ bản mock)

| Thao tác | Điều kiện / thông báo lỗi (HTTP 400/404/409) |
|---|---|
| Lưu khu vực | Tên trống: `Vui lòng nhập tên khu vực`; trùng (không phân biệt hoa/thường): `Tên khu vực đã tồn tại`; tên bàn trống: `Tên bàn không được để trống`; trùng trong khu vực: `Tên bàn "X" bị trùng`; sức chứa không nguyên trong 1–50: `Sức chứa không hợp lệ`; xoá bàn đang dùng/đặt trước: `<tên> đang được sử dụng, không thể xoá` |
| Xoá / tạm ngưng khu vực | Còn bàn khác `empty`: `Khu vực còn bàn đang sử dụng hoặc đã đặt trước` |
| Mở bàn | Bàn phải `empty` hoặc `reserved`; `guests` nguyên ≥ 1 và ≤ (sức chứa bàn + các bàn đã gộp): `Bàn chỉ chứa tối đa N khách`. Kết quả: `serving`, `startedAt = now`, xoá đặt trước, `orders = []` |
| Đặt món | Bàn phải `serving`; cộng dồn món cùng tên + cùng giá |
| Yêu cầu thanh toán | Bàn `serving` và có ≥ 1 món: `Bàn chưa có món nào để thanh toán` → `paying` |
| Thanh toán | Bàn `paying`; tiền mặt mà `received < tổng`: `Số tiền khách đưa chưa đủ`. **Trong một transaction**: tạo hoá đơn, trả bàn + mọi bàn đã gộp về `empty` (xoá guests/orders/mergedIds/mergedInto) |
| Chuyển bàn | Nguồn `serving`/`paying`; đích phải `empty`: `Bàn đích không phải đang trống`; `guests` ≤ sức chứa đích + bàn đã gộp. Chuyển trạng thái, khách, giờ vào, món, danh sách gộp sang đích; nguồn về `empty` |
| Gộp bàn | Nguồn `serving`; đích `empty` hoặc `serving`; không gộp với chính nó. Cộng khách, giờ vào = nhỏ hơn, gộp món; nguồn (và các bàn nó đã gộp) thành `merged` với `mergedInto = đích`; đích có `mergedIds` |

### 2.5 Đường dẫn trang/tài nguyên khi chạy trong Spring Boot

Hiện HTML dùng đường dẫn tương đối kiểu Live Server (`../../static/js/...`, nav trỏ `/src/main/resources/templates/...`). Khi chạy bằng Spring Boot:

- Thư mục `static/` được phục vụ ở **gốc**: `static/js/a.js` → `/js/a.js`, `static/css/output.css` → `/css/output.css` (không có tiền tố `/static`).
- Thư mục `templates/` **không** tự phục vụ ra web (chỉ dùng với Thymeleaf + controller).

Cách đơn giản nhất: giữ HTML thuần, đặt vào `static/admin/` (ví dụ `/admin/area.html`), sửa thẻ trong HTML thành `/css/output.css` và `/js/fragments/AdminArea.js`, và sửa `href` trong `AdminNavigation.js` + đường dẫn login thành URL mới (đồng thời cập nhật `LOGIN_URL` trong `config.js`). Nếu dùng Thymeleaf thì mỗi trang cần một `@Controller` trả tên view.

### 2.6 Xác thực

- `HttpClient.js` gửi `credentials: 'same-origin'`: nó **giả định JWT nằm trong cookie HttpOnly** do `/api/auth/login` đặt (`Set-Cookie`), và code đăng nhập hiện tại cũng không đọc token trong body. Nếu backend của bạn chỉ trả token trong body, hãy lưu token và thêm header `Authorization: Bearer ...` trong `http()`.
- 401 → frontend chuyển về `LOGIN_URL`.
- `SecurityFilterChain` cần cho phép (permitAll) tài nguyên tĩnh và `/api/auth/**`, còn lại yêu cầu đăng nhập.
- Cookie nên `HttpOnly; Secure; SameSite=Lax`.

### 2.7 Khung code Spring Boot (tham khảo — chưa biên dịch, điều chỉnh theo project)

**Enum & lỗi nghiệp vụ**

```java
public enum TableStatus {
    EMPTY, SERVING, PAYING, RESERVED, MERGED;

    @JsonValue
    public String json() { return name().toLowerCase(); }
}

public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    public BusinessException(String message) { this(HttpStatus.BAD_REQUEST, message); }
    public BusinessException(HttpStatus status, String message) { super(message); this.status = status; }
    public HttpStatus getStatus() { return status; }
}

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<Map<String, String>> business(BusinessException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> other(Exception e) {
        return ResponseEntity.status(500).body(Map.of("message", "Có lỗi xảy ra, vui lòng thử lại"));
    }
}
```

**Entity (rút gọn)**

```java
@Entity
public class DiningTable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Area area;
    private String name;
    private int capacity;
    @Enumerated(EnumType.STRING) private TableStatus status = TableStatus.EMPTY;
    private int guests;
    private Long startedAt, reservedAt, reservedUntil;          // epoch ms
    @ElementCollection private List<OrderItem> orders = new ArrayList<>();
    @ManyToOne private DiningTable mergedInto;
    @OneToMany(mappedBy = "mergedInto") private List<DiningTable> mergedTables = new ArrayList<>();
    // getters/setters
}

@Embeddable
public record OrderItem(String name, long price, int qty) {}

@Entity
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Instant paidAt;
    private LocalDate day;                 // ngày theo múi giờ Asia/Ho_Chi_Minh
    private String tableName, areaName;
    @Enumerated(EnumType.STRING) private PayMethod method;   // CASH, TRANSFER
    private long total;
}
```

**DTO — id là String**

```java
public record TableDto(String id, String name, int capacity, TableStatus status, int guests,
                       Long startedAt, Long reservedAt, Long reservedUntil,
                       List<OrderItem> orders, String mergedInto, List<String> mergedIds) {
    static TableDto of(DiningTable t) {
        return new TableDto(String.valueOf(t.getId()), t.getName(), t.getCapacity(), t.getStatus(), t.getGuests(),
            t.getStartedAt(), t.getReservedAt(), t.getReservedUntil(), t.getOrders(),
            t.getMergedInto() == null ? null : String.valueOf(t.getMergedInto().getId()),
            t.getMergedTables().stream().map(m -> String.valueOf(m.getId())).toList());
    }
}
```

**Controller + service (mẫu cho thanh toán — các thao tác còn lại làm tương tự theo bảng 2.4)**

```java
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService service;

    record CheckoutRequest(String method, long received) {}
    record CheckoutResponse(long total, long change) {}

    @PostMapping("/{id}/checkout")
    CheckoutResponse checkout(@PathVariable Long id, @RequestBody CheckoutRequest req) {
        return service.checkout(id, req);
    }
    // open, orders, request-payment, cancel-payment, cancel-reservation, move, merge ...
}

@Service
@RequiredArgsConstructor
public class TableService {
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final DiningTableRepository tables;
    private final InvoiceRepository invoices;

    @Transactional   // hoá đơn + trả bàn cùng thành công hoặc cùng huỷ
    public TableController.CheckoutResponse checkout(Long id, TableController.CheckoutRequest req) {
        DiningTable t = tables.findById(id)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy bàn"));
        if (t.getStatus() != TableStatus.PAYING) throw new BusinessException("Bàn chưa yêu cầu thanh toán");

        long total = t.getOrders().stream().mapToLong(o -> o.price() * o.qty()).sum();
        boolean cash = "cash".equals(req.method());
        if (cash && req.received() < total) throw new BusinessException("Số tiền khách đưa chưa đủ");

        Invoice inv = new Invoice();
        Instant now = Instant.now();
        inv.setPaidAt(now);
        inv.setDay(now.atZone(ZONE).toLocalDate());
        inv.setTableName(t.getName());
        inv.setAreaName(t.getArea().getName());
        inv.setMethod(cash ? PayMethod.CASH : PayMethod.TRANSFER);
        inv.setTotal(total);
        invoices.save(inv);

        t.getMergedTables().forEach(this::reset);
        reset(t);
        return new TableController.CheckoutResponse(total, cash ? req.received() - total : 0);
    }

    private void reset(DiningTable t) { /* status=EMPTY, guests=0, mốc thời gian=null, orders.clear(), mergedInto=null */ }
}
```

**Doanh thu**

```java
public interface DailyRow { LocalDate getDay(); long getTotal(); long getCount(); }

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("select i.day as day, sum(i.total) as total, count(i) as count " +
           "from Invoice i where i.day between :from and :to group by i.day")
    List<DailyRow> daily(LocalDate from, LocalDate to);

    List<Invoice> findByDayOrderByPaidAtDesc(LocalDate day);
}
```

`/api/revenue/daily?days=N`: gọi `daily(today-(N-1), today)`, rồi điền 0 cho ngày thiếu và trả theo thứ tự tăng dần. `/api/revenue/today`: lấy `findByDayOrderByPaidAtDesc(today)`, tính `total`, `count`, `cash`, `transfer`, map mỗi hoá đơn sang `Tx` (`at = paidAt.toEpochMilli()`, `id` dạng chuỗi).

**Cấu hình**

```properties
spring.jackson.time-zone=Asia/Ho_Chi_Minh
spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Ho_Chi_Minh
```

### 2.8 Bí mật trong `application-dev.properties`

File hiện đang chứa mật khẩu ứng dụng Gmail và khoá JWT dạng chữ thường. Nên:

- **Đổi (thu hồi) mật khẩu ứng dụng Gmail và khoá JWT** vì chúng đã nằm trong file đã chia sẻ.
- Đọc từ biến môi trường: `spring.mail.password=${MAIL_PASSWORD}`, `jwt.secret-key=${JWT_SECRET}`.
- Không commit file chứa bí mật lên Git (thêm vào `.gitignore` hoặc dùng `application-local.properties` không commit).
- Ba khoá `jwt.secret-key`, `refresh-token.secret-key`, `access-token.secret-key` đang trùng giá trị; nên dùng khoá khác nhau.
