import './AdminNavigation.js';
import './AdminHeader.js';
import '../components/StatRing.js';
import '../components/RevenueChart.js';
import { revenueService } from '../services/RevenueService.js';

const $ = sel => document.querySelector(sel);
const ring = $('#stat-revenue-today');
const chartDays = $('#revenue-chart-days');
const chartMonth = $('#revenue-chart-month');
const table = $('#revenue-table');

const now = new Date();
const ym = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
chartMonth.setAttribute('month', ym);
chartMonth.setAttribute('title', `Doanh thu tháng ${now.getMonth() + 1}`);

const toMillion = vnd => Math.round(vnd / 1e4) / 100; 

let loading = false, again = false;
async function refresh() {
    if (loading) { again = true; return; }
    loading = true;
    try {
        const [today, daily] = await Promise.all([revenueService.getToday(), revenueService.getDaily(45)]);
        ring.setAttribute('value', today.total);
        ring.setAttribute('note', today.count ? `${today.count} hoá đơn hôm nay` : 'Chưa có hoá đơn hôm nay');
        const points = daily.map(d => ({ date: d.date, value: toMillion(d.total) }));
        chartDays.data = points;
        chartMonth.data = points;
        if (table) table.data = { today, daily };
    } catch (err) {
        console.error(`Bug: ${err}`);
    } finally {
        loading = false;
        if (again) { again = false; refresh(); }
    }
}

revenueService.subscribe(refresh);
refresh();
