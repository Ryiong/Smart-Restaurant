import { SPRING_BASE, LOGIN_URL } from '../config.js';

/**
 * Client gọi backend Spring Boot (chế độ 'spring').
 * - Gửi cookie phiên (JWT HttpOnly) cùng origin.
 * - Lỗi: backend trả JSON { "message": "..." } kèm HTTP status 4xx/5xx → ném Error(message).
 * - 401 → về trang đăng nhập.
 */
export async function http(method, url, body) {
    let res;
    try {
        res = await fetch(SPRING_BASE + url, {
            method,
            headers: body === undefined ? { Accept: 'application/json' } : { 'Content-Type': 'application/json', Accept: 'application/json' },
            credentials: 'same-origin',
            body: body === undefined ? undefined : JSON.stringify(body),
        });
    } catch {
        throw new Error('Không kết nối được máy chủ, vui lòng thử lại');
    }

    if (res.status === 401) {
        location.href = LOGIN_URL;
        throw new Error('Phiên đăng nhập đã hết hạn');
    }
    if (!res.ok) {
        let msg = 'Có lỗi xảy ra, vui lòng thử lại';
        try {
            msg = (await res.json()).message || msg;
        } catch { /* body không phải JSON */ }
        throw new Error(msg);
    }
    return res.status === 204 ? null : res.json();
}
