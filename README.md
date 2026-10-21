# Hướng dẫn sử dụng thiết kế Frontend
Bộ giao diện được xây dựng bằng **Web Components thuần (Custom Elements)** kết hợp **Tailwind CSS (V4)**, không cần framework. Mỗi thành phần là một thẻ HTML tuỳ chỉnh, chỉ cần `import` file JS tương ứng rồi dùng như một thẻ HTML bình thường.
## Mục lục
1. [Cấu trúc thư mục](#1-cấu-trúc-thư-mục)
2. [Quy ước chung](#2-quy-ước-chung)
3. [Màu sắc & kiểu chữ](#3-màu-sắc--kiểu-chữ)
4. [Icon (`icon.js`)](#4-icon-iconjs)
5. [Điều hướng `<app-navigation>`](#5-điều-hướng-app-navigation)
6. [Tiêu đề trang `<app-header>`](#6-tiêu-đề-trang-app-header)
7. [Form: `<form-field>`, `<hidden-input>`, `<hidden-field>`](#7-form)
8. [Nút bấm: `<p-button>`, `<l-button>`](#8-nút-bấm)
9. [Biểu đồ doanh thu `<revenue-chart>`](#9-biểu-đồ-doanh-thu-revenue-chart)
10. [Thẻ thống kê `<stat-ring>`](#10-thẻ-thống-kê-stat-ring)
11. [Trang xác thực: login / register / reset](#11-trang-xác-thực)
12. [File gom import (`AppArea`, `AppIndex`)](#12-file-gom-import)
##1. Cấu trúc thư mục
```
js/
├── icon.js                     # Bộ icon SVG + logo
├── fragments/                  # Thành phần lớn (khung trang)
│   ├── AdminArea.js            # Gom import cho trang "Bàn & Khu vực"
│   ├── AdminHeader.js          # <app-header>
│   ├── AdminIndex.js           # Gom import cho trang "Tổng quan"
│   ├── AdminLogin.js           # <app-login>
│   ├── AdminNavigation.js        # <app-navigation>
│   ├── AdminRegister.js          # <app-register>
│   └── AdminReset.js             # <app-reset>
├── components/                 # Thành phần nhỏ tái sử dụng
│   ├── InputWithLabel.js       # <form-field>
│   ├── HiddenInput.js          # <hidden-input>
│   ├── HiddenInputWithLabel.js # <hidden-field>
│   ├── PrimaryButton.js        # <p-button>
│   ├── LabelButton.js          # <l-button>
│   ├── RevenueChart.js         # <revenue-chart>
│   └── StatRing.js             # <stat-ring>
```
> Đường dẫn import trong code hiện tại chỉ sử dụng cho mục đích thiết kế và mở với `Live Server`. Khi chuyển sang dev production và build production thì có thể phải đổi lại đường dẫn.

## 2. Quy ước chung
- **Cách dùng:** `import` file JS (dùng `<script type="module">`), sau đó đặt thẻ vào HTML.
```html
  <script type="module">
    import './js/components/PrimaryButton.js';
  </script>
  <p-button label="Lưu" variant="solid"></p-button>
```
- **Thuộc tính** truyền vào qua attribute của thẻ (ví dụ `label="..."`).
- **Chống render lặp:** hầu hết thành phần dùng `data-ready` để chỉ khởi tạo một lần.
- **Thành phần nhận attribute động** (`p-button`, `l-button`, `revenue-chart`, `stat-ring`) sẽ tự render lại khi attribute thay đổi (`setAttribute`).
- **Chống XSS:** các thành phần hiển thị dữ liệu (nút, biểu đồ, thẻ thống kê) dùng hàm `esc()` để escape ký tự HTML. Khi tự viết thành phần mới, hãy escape mọi dữ liệu người dùng trước khi chèn vào `innerHTML`.
- **Tên thẻ** bắt buộc có dấu gạch ngang theo chuẩn Custom Elements.
- **Lưu ý:** Đường dẫn import sẽ thay đổi theo vị trí của từng file.

## 3. Màu sắc & kiểu chữ
 
Dự án dùng các token màu của Tailwind (khai báo ở file CSS theme của bạn qua biến `--color-*`):
 
| Token | Dùng cho |
|---|---|
| `primary-50 … 800` | Màu chủ đạo (nút chính, link, thanh điều hướng đang chọn, cột biểu đồ) |
| `secondary-100 … 800` | Màu phụ (icon `food`, vòng tròn thống kê, bóng đổ của thanh điều hướng) |
| `support-600 / 800 / 900` | Viền input, chữ phụ, icon chưa được chọn |
| `accent-900` | Chữ/icon của mục điều hướng đang chọn |
| `text` | Màu chữ chính |
| `bug` | Màu thông báo lỗi |
 
Các tiêu đề `h1 … h5` và đoạn văn `p` được định kiểu toàn cục; xem mẫu trực tiếp trong `design.js`.
 
## 4. Icon (`icon.js`)
 
File xuất ra hai thứ: `ICONS` (object chứa các icon dạng chuỗi SVG) và `LOGO`.

### Danh sách icon
 
`dashboard`, `table`, `menu`, `storage`, `user_setting`, `role`, `calendar`, `setting`, `logout`, `eye`, `eyeOff`, `food`, `edit`, `delete`, `add`

### Cách dùng
 
```js
import { ICONS, LOGO } from './icon.js';
 
element.innerHTML = `<button>${ICONS.edit} Sửa</button>`;
```
- Icon dùng `currentColor`, nên **đổi màu bằng class chữ** của thẻ cha, ví dụ `text-primary-600`.
- Kích thước mặc định: `size-6`. Riêng `edit`, `delete`, `add` là `size-5`; `logout` là `w-5 h-10`.
- Icon `food` dùng biến màu `--color-secondary-500/600/800` nên cần theme có các biến này.
- `LOGO` là SVG logo (màu `#064943`), dùng trong `<app-navigation>`.
### Thêm icon mới
 
Thêm một mục vào `ICONS` bằng hàm `icon(viewBox, fillColor, body, cls)`:
 
```js
heart: icon('0 0 24 24', 'none',
    `<g stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="..."/>
    </g>`,
    'size-5'   // tuỳ chọn, mặc định 'size-6'
),
```
 
Sau đó dùng được ngay qua `ICONS.heart`, `<p-button icon="heart">`, `<stat-ring icon="heart">`, hoặc trong mảng `NAV`.
 
## 5. Điều hướng `<app-navigation>`
 
Thanh điều hướng cố định bên trái (`fixed`, cao toàn màn hình), gồm logo, danh sách icon và nút đăng xuất.
 
```html
<script type="module" src="./js/fragments/AppNavigation.js"></script>
<app-navigation></app-navigation>
```
 
### Chỉnh danh sách mục
 
Sửa mảng `NAV` ở đầu file `AppNavigation.js`:
 
```js
const NAV = [
    { href: '/src/main/resources/templates/admin/index.html', name: 'Tổng quan', icon: 'dashboard' },
    { href: '#', name: 'Thực đơn', icon: 'menu' },
];
```
 
| Trường | Ý nghĩa |
|---|---|
| `href` | Đường dẫn trang. Được so sánh **chính xác** với `location.pathname` để xác định mục đang chọn |
| `name` | Tên hiển thị khi rê chuột (`title`) và **tự gán vào `document.title`** khi trang đang chọn |
| `icon` | Tên icon trong `ICONS` |
 
> Đổi `NAV` cũng là cách đổi tiêu đề tab của trang.
 
Mục đang chọn dùng class `LINK_ACTIVE`, các mục còn lại dùng `LINK_IDLE` (chỉnh ở đầu file).
 
**Hiển thị trong trang mẫu/demo:** vì thẻ mặc định là `fixed`, để nhúng vào giữa trang hãy ghi đè class, ví dụ `<app-navigation class="relative" style="height:min-content;">`.
 
Nút đăng xuất (`#navigation__footer-button`) hiện **chưa gắn sự kiện**; hãy tự thêm xử lý đăng xuất.
 
## 6. Tiêu đề trang `<app-header>`
 
Thanh đầu trang: tiêu đề bên trái, thông tin người dùng bên phải.
 
```html
<app-header
    class="w-full bg-white"
    title="Tổng quan"
    data-title="Doanh thu hôm nay"
    username="Nguyễn Văn A"
    user-role="Chủ cửa hàng"
    user-image="https://example.com/avatar.jpg">
</app-header>
```
 
| Attribute | Mặc định | Mô tả |
|---|---|---|
| `title` | `''` | Tiêu đề chính (thẻ `h4`) |
| `data-title` | `''` | Tiêu đề phụ, chữ nhạt |
| `username` | `Admin` | Tên người dùng |
| `user-role` | `Chức vụ` | Chức vụ người dùng |
| `user-image` | `https://placehold.co/400` | Ảnh đại diện, **nên dùng ảnh tỉ lệ 1:1** (hiển thị bo tròn 44px) |
 
Các attribute chỉ được đọc **một lần** lúc khởi tạo, không tự cập nhật khi đổi sau đó.
 
## 7. Form
> **Quy tắc `id`:** mọi ô nhập đều có attribute `id-input` để bạn tự đặt `id` cho `<input>` (dùng cho `label`, CSS, JS, test...). **Bỏ trống thì component tự sinh id duy nhất** (dạng `field-1a2b3c4d`, `pw-5e6f7a8b`), nên đặt nhiều ô trên cùng một trang vẫn không bị trùng id. Hãy tự đặt `id-input` khi cần tham chiếu id đó từ bên ngoài, và đảm bảo nó duy nhất trong trang.
 
### 7.1 `<form-field>` — ô nhập văn bản có nhãn
 
```html
<form-field label="Email" name="email" placeholder="example@email.com"></form-field>
```
 
| Attribute | Mặc định | Mô tả |
|---|---|---|
| `label` | `Label Input` | Nhãn phía trên |
| `name` | `password` | Giá trị `name` của `<input>` (nhớ đặt đúng, nếu không sẽ trùng mặc định `password`) |
| `placeholder` | `''` | Gợi ý trong ô |
 
Ô nhập có `required` và `autocomplete="username"`.
 
### 7.2 `<hidden-input>` — ô mật khẩu có nút hiện/ẩn
 
```html
<hidden-input name="password" placeholder="Nhập mật khẩu"></hidden-input>
```
 
- Attribute: `name` (mặc định `password`), `placeholder` (tự cập nhật khi đổi).
- Yêu cầu `required` và `minlength="8"`.
- API: `el.value` (đọc/ghi), `el.toggle(show?)` (hiện/ẩn mật khẩu).
- Sự kiện: `visibility-change` (bubbles), `event.detail.visible` là `true/false`.
```js
document.querySelector('hidden-input')
    .addEventListener('visibility-change', e => console.log(e.detail.visible));
```
 
### 7.3 `<hidden-field>` — nhãn bao quanh ô mật khẩu
 
Bọc các phần tử con (thường là `<hidden-input>`) và thêm nhãn, tự liên kết `label for` với `<input>` bên trong.
 
```html
<hidden-field label="Password">
    <hidden-input name="password" placeholder="Nhập mật khẩu"></hidden-input>
</hidden-field>
```
 
> Với `<form-field>`/`<hidden-input>`, hãy nhúng chúng **bên trong thẻ `<form>`** để `FormData` thu được giá trị khi gửi.

## 8. Nút bấm
 
### 8.1 `<p-button>` — nút chính (có icon)
 
```html
<p-button label="Thêm bàn" icon="add" variant="solid"></p-button>
<p-button label="Xoá" icon="delete" variant="danger"></p-button>
<p-button label="Sửa" icon="edit"></p-button>
<p-button label="Lưu" type="submit" disabled></p-button>
```
 
| Attribute | Giá trị | Mô tả |
|---|---|---|
| `label` | chuỗi | Chữ trên nút (mặc định `Button`) |
| `icon` | tên trong `ICONS` | Tuỳ chọn, hiển thị bên trái chữ |
| `variant` | `primary` (mặc định), `danger`, `solid` | `primary`: viền nhạt, nền trắng; `danger`: đỏ; `solid`: nền đặc |
| `type` | `button` (mặc định), `submit`, `reset` | Kiểu nút |
| `disabled` | có/không | Vô hiệu hoá, chặn cả sự kiện click |
 
Có thể đổi bằng JS: `btn.disabled = true;` hoặc `btn.setAttribute('label', 'Mới')`.
 
### 8.2 `<l-button>` — nút nhỏ dạng viên thuốc
 
```html
<l-button label="Đang trống"></l-button>
<l-button label="Có khách" variant="solid"></l-button>
<l-button label="Đã đặt" disabled></l-button>
```
 
| Attribute | Giá trị | Mô tả |
|---|---|---|
| `label` | chuỗi | Chữ trên nút (mặc định `Label`) |
| `type` | `button` (mặc định)… | Kiểu nút |
| `variant` | `primary` (mặc định), `solid` | `primary`: nền xanh chữ trắng; `solid`: nền trắng viền nhạt |
| `disabled` | có/không | Hiển thị gạch chéo trên nút |
 
Phù hợp làm nhãn trạng thái hoặc bộ lọc nhỏ.
 
## 9. Biểu đồ doanh thu `<revenue-chart>`
 
Biểu đồ cột thuần HTML/CSS, không cần thư viện.
 
```html
<revenue-chart id="chart" month="2026-10" title="Doanh thu tháng 10" unit="nghìn đồng"
               href="/admin/revenue.html"></revenue-chart>
 
<script type="module">
  import './js/components/RevenueChart.js';
  document.getElementById('chart').data = [
    { date: '2026-10-01', value: 120 },
    { date: '2026-10-02', value: 95 },
  ];
</script>
```
 
| Attribute | Mô tả |
|---|---|
| `month` | Dạng `YYYY-MM`. Hiển thị mọi ngày trong tháng đó |
| `days` | Nếu **không** có `month`: hiển thị `n` ngày gần nhất tính đến hôm nay (mặc định 14) |
| `title` | Tiêu đề biểu đồ |
| `unit` | Đơn vị, hiện ở chân biểu đồ và trong tooltip |
| `href` | Liên kết của nút "Xem chi tiết >>" (mặc định `#`) |
 
**Dữ liệu:** gán qua thuộc tính JS `chart.data = [{ date: 'YYYY-MM-DD', value: number }]`. Ngày thiếu dữ liệu sẽ hiển thị cột xám giá trị 0. Mỗi cột có tooltip `ngày: giá trị đơn vị`.
 
Biểu đồ tự tính trục Y (tối đa ~5 vạch), thu nhỏ cột và thưa nhãn khi có nhiều hơn 20 ngày.
 
## 10. Thẻ thống kê `<stat-ring>`
 
Thẻ hiển thị một chỉ số kèm vòng tròn tiến độ.
 
```html
<stat-ring label="Bàn đang dùng" value="8" total="12" note="Cập nhật 15:00" icon="table"></stat-ring>
<stat-ring label="Đơn hôm nay" value="35" note="So với hôm qua +5" icon="menu"></stat-ring>
```
 
| Attribute | Mô tả |
|---|---|
| `label` | Tên chỉ số |
| `value` | Giá trị hiện tại |
| `total` | Tổng (tuỳ chọn). Có `total` → hiển thị `value/total` và vòng tròn theo tỉ lệ; không có → hiện `value` và vòng đầy |
| `note` | Ghi chú nhỏ bên dưới |
| `icon` | Icon ở giữa vòng tròn (tên trong `ICONS`) |
 
> `htotal` được khai báo trong `observedAttributes` nhưng chưa được dùng.
 
## 11. Trang xác thực
 
Ba thành phần toàn trang (chiếm `w-screen h-screen`, căn giữa), đặt trực tiếp trong `<body>`:
 
| Thẻ | File | Nội dung |
|---|---|---|
| `<app-login>` | `AppLogin.js` | Tên đăng nhập, mật khẩu, link quên mật khẩu và tạo tài khoản |
| `<app-register>` | `AppRegister.js` | Email, mật khẩu (có đoạn `<p>` demo cần xoá) |
| `<app-reset>` | `AppReset.js` | Nhập email lấy lại mật khẩu |
 
```html
<body>
  <script type="module" src="./js/fragments/AppLogin.js"></script>
  <app-login></app-login>
</body>
```
 
Hành vi gửi form hiện tại:
 
- **Login:** `POST /api/auth/login` (JSON), lỗi hiển thị `Sai tên đăng nhập hoặc mật khẩu`; thành công thì `location.href = ''`.
- **Reset:** `POST /api/auth/reset`, lỗi hiển thị `Sai email`.
- **Register:** mới chỉ `console.log(data)`, chưa gọi API.
Các đoạn `fetch` đang ghi chú `demo for design only`; hãy đổi endpoint và đường dẫn chuyển trang thành giá trị thật. Các đường dẫn `href` liên kết giữa các trang (`/src/main/resources/templates/auth/...`) cũng cần chỉnh theo dự án.
 
Thông báo lỗi nằm ở thẻ `[data-error]`; lỗi tự ẩn khi rỗng (`empty:hidden`).
 
## 12. File gom import
 
`AppArea.js` và `AppIndex.js` chỉ có `import`, dùng để mỗi trang nạp một lần đủ các thành phần cần thiết:
 
| File | Nạp |
|---|---|
| `AppIndex.js` | Navigation, Header, `RevenueChart`, `StatRing` |
| `AppArea.js` | Navigation, Header, `PrimaryButton`, `LabelButton` |
 
Khi tạo trang mới, tạo file gom tương tự rồi chỉ cần một thẻ `<script type="module">`:
 
```js
// fragments/AppMenu.js
import './AppNavigation.js';
import './AppHeader.js';
import '../components/PrimaryButton.js';
```
 
Bố cục trang quản trị gợi ý (thanh điều hướng `fixed` nên chừa lề trái):
 
```html
<body>
  <app-navigation></app-navigation>
  <main class="ml-16">
    <app-header title="Tổng quan" data-title="Hôm nay"></app-header>
    <section class="grid gap-4 p-5 md:grid-cols-3">
      <stat-ring label="Bàn" value="8" total="12" icon="table"></stat-ring>
    </section>
    <revenue-chart month="2026-10" title="Doanh thu" unit="nghìn đồng"></revenue-chart>
  </main>
</body>
```
 
