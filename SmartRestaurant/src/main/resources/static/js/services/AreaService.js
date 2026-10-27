import { API_MODE } from '../config.js';
import { http } from './HttpClient.js';
import { mockAreaService } from './AreaService.mock.js';
import { mockAPIAreaService } from './AreaService.mockapi.js';

export const USE_MOCK = API_MODE !== 'spring';

const springAreaService = {
    listAreas: () => http('GET', '/areas'),
    listMenu: () => http('GET', '/menu-items'),
    saveArea: a => (a.id ? http('PUT', `/areas/${a.id}`, a) : http('POST', '/areas', a)),
    deleteArea: id => http('DELETE', `/areas/${id}`),
    setAreaActive: (id, active) => http('PATCH', `/areas/${id}/active`, { active }),
    openTable: (id, guests) => http('POST', `/tables/${id}/open`, { guests }),
    addOrder: (id, items) => http('POST', `/tables/${id}/orders`, { items }),
    requestPayment: id => http('POST', `/tables/${id}/request-payment`),
    cancelPayment: id => http('POST', `/tables/${id}/cancel-payment`),
    checkoutTable: (id, payment) => http('POST', `/tables/${id}/checkout`, payment),
    cancelReservation: id => http('POST', `/tables/${id}/cancel-reservation`),
    moveTable: (fromId, toId) => http('POST', `/tables/${fromId}/move`, { toId }),
    mergeTables: (fromId, primaryId) => http('POST', `/tables/${fromId}/merge`, { primaryId }),
};

const SERVICES = {
    local: mockAreaService,
    mockapi: mockAPIAreaService,
    spring: springAreaService
};

if (!(API_MODE in SERVICES)) {
    throw new Error(`API_MODE "${API_MODE}" không hợp lệ - chọn 'local', 'mockapi' hoặc 'spring' trong config.js`);
}

export const areaService = SERVICES[API_MODE];