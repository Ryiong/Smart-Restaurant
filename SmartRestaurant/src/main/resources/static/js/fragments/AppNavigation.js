import { ICONS, LOGO } from '../icon.js';

const NAV = [
    { href: '/src/main/resources/templates/admin/index.html', name: 'Tổng quan', icon: 'dashboard' },
    { href: '#', name: 'Bàn $ Khu vực', icon: 'table' },
    { href: '#', name: 'Thực đơn', icon: 'menu' },
    { href: '#', name: 'Kho nguyên liệu', icon: 'storage' },
    { href: '#', name: 'Cài đặt', icon: 'setting' },

];

const LINK_BASE = 'w-full flex items-center justify-center rounded-lg hover:bg-primary-50 transition-all mt-5 p-2 cursor-pointer';
const LINK_ACTIVE = 'bg-primary-100 text-accent-900';
const LINK_IDLE = 'text-support-900';

class AppNavigation extends HTMLElement {
    connectedCallback() {
        this.classList.add('block', 'fixed', 'top-0', 'left-0', 'h-screen', 'w-min', 'z-50', 'bg-white');
        const current = location.pathname;

        const items = NAV.map(n => {
            const active = current === n.href;
            return `
        <li class="${LINK_BASE} ${active ? LINK_ACTIVE : LINK_IDLE}">
          <a href="${n.href}" title="${n.name}" aria-label="${n.name}"
             ${active ? 'aria-current="page"' : ''}>
            ${ICONS[n.icon]}
          </a>
        </li>`;
        }).join('');

        this.innerHTML = `
            <nav id="app-navigation" class="h-full shadow-[0_4px_8px_var(--color-secondary-100)]">
                <div id="navigation__header" class="w-15 h-15 p-2 pt-4 m-auto text-primary">
                    <div class="mx-auto">${LOGO}</div>
                </div>
                <div id="navigation__content" class="h-full flex flex-col justify-around">
                    <div id="navigation__scrollable-container">
                        <ul id="navigation_list" class="flex flex-col items-center gap-2 overflow-y-auto px-2">
                            ${items}
                        </ul>
                    </div>
                    <div id="navigation__footer" class="flex items-center justify-center m-3">
                    <button id="navigation__footer-button" class="w-13 h-15 text-primary-600 bg-primary-200 rounded-lg flex items-center justify-center hover:bg-primary-400 hover:text-white hover:scale-90 transition-all cursor-pointer">
                        ${ICONS.logout}
                    </button>
                </div>
                </div>
                
            </nav>
        `;
    }
}

customElements.define('app-navigation', AppNavigation);