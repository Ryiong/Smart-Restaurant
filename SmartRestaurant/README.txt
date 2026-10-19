Thẻ được đặt trong mỗi thẻ head của các file html
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
được sử dụng cho mục đích thiết kế
Khi tiến vào dev production vui lòng xoá bỏ và chạy lệnh 
pnpm dev run
và build khi chạy real production

Chú thích của RevenueChart
<revenue-chart title="Doanh thu 14 ngày gần nhất" unit="triệu VNĐ" days="14"></revenue-chart>

title: Tiêu đề đồ thị
unit: Đơn vị
days: là số lượng cột được hiển thị với cột cuối cùng là ngày hôm nay
Nếu thay giá trị days thành giá trị month thì sẽ hiển thị đủ ngày của tháng đó

Quy tắc viết
month: yyyy-mm

Demo data cho chart
chart.data = [
    { date: '2026-10-07', value: 7 },
    { date: '2026-10-08', value: 2.2 },
]
Quy tắc viết giá trị ngày
date: yyyy-mm-dd
