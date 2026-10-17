import { ICONS, LOGO } from '../icon.js';

const NAV = [
    { href: '/src/main/resources/templates/', name: 'Tổng quan', icon: 'dashboard' },
    { href: '#', name: 'Bàn $ Khu vực', icon: 'table' },
    { href: '#', name: 'Thực đơn', icon: 'menu' },
    { href: '#', name: 'Kho nguyên liệu', icon: 'storage' },
    { href: '#', name: 'Cài đặt', icon: 'setting' },

];

const LINK_BASE = 'flex size-10 items-center justify-center rounded-lg transition';
const LINK_ACTIVE = 'bg-yellow-400 text-[#064943]';
const LINK_IDLE = 'text-gray-700 hover:bg-yellow-300';

class AppNavigation extends HTMLElement {
    connectedCallback() {
        this.classList.add('block', 'h-full');
        const current = location.pathname;

        const items = NAV.map(n => {
            const active = current === n.href;
            return `
        <li>
          <a href="${n.href}" title="${n.name}" aria-label="${n.name}"
             ${active ? 'aria-current="page"' : ''}
             class="${LINK_BASE} ${active ? LINK_ACTIVE : LINK_IDLE}">
            ${ICONS[n.icon]}
          </a>
        </li>`;
        }).join('');

        this.innerHTML = `
            <nav id="app-navigation" class="h-full bg-neutral-100 flex flex-col justify-between">
                <div id="navigation__header" class="w-15 h-15 m-4 text-primary">
                    ${LOGO}
                </div>
                <div id="navigation__content">
                    <div id="navigation__scrollable-container">
                        <ul id="navigation_list" class="flex flex-col items-center gap-2 overflow-y-auto px-2">
                            ${items}
                        </ul>
                    </div>
                    <div id="navigation__footer" class="flex items-center justify-center m-3">
                    <button id="navigation__footer-button" class="w-13 h-15 text-primary-600 bg-primary-200 rounded-lg flex items-center justify-center">
                        ${ICONS.logout}
                    </button>
                </div>
                </div>
                
            </nav>
        `;
    }
}

customElements.define('app-navigation', AppNavigation);