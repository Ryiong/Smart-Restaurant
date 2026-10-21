import { ICONS } from '../js/icon.js';
import '../js/fragments/AppNavigation.js';
import '../js/fragments/AppHeader.js';

class DesignApp extends HTMLElement {
    connectedCallback() {
        if (this.dataset.ready) return;
        this.dataset.ready = '1';

        this.classList.add('block');
        this.innerHTML = `
        <div class="flex w-full flex-wrap items-center">
            <div class="m-auto w-[80%]">
                <h1>Đây là tiêu đề H1</h1>
                <h2>Đây là tiêu đề H2</h2>
                <h3>Đây là tiêu đề H3</h3>
                <h4>Đây là tiêu đề H4</h4>
                <h5>Đây là tiêu đề H5</h5>
                <p>Đây là đoạn văn p</p>
                <p>Đây là đoạn văn p</p>
            </div>

            <div class="ml-auto mr-auto mt-8 w-[80%]">
                <h3>Icon</h3>
                <div class="flex flex-wrap gap-6">
                    <div class="text-center">${ICONS.dashboard}<p>dashboard</p></div>
                    <div class="text-center">${ICONS.table}<p>table</p></div>
                    <div class="text-center">${ICONS.menu}<p>menu</p></div>
                    <div class="text-center">${ICONS.storage}<p>storage</p></div>
                    <div class="text-center">${ICONS.user_setting}<p>user_setting</p></div>
                    <div class="text-center">${ICONS.role}<p>role</p></div>
                    <div class="text-center">${ICONS.calendar}<p>calendar</p></div>
                    <div class="text-center">${ICONS.setting}<p>setting</p></div>
                    <div class="text-center">${ICONS.logout}<p>logout</p></div>
                    <div class="text-center">${ICONS.eye}<p>eye</p></div>
                    <div class="text-center">${ICONS.eyeOff}<p>eyeOff</p></div>
                    <div class="text-center">${ICONS.food}<p>food</p></div>
                    <div class="text-center">${ICONS.edit}<p>edit</p></div>
                    <div class="text-center">${ICONS.delete}<p>delete</p></div>
                    <div class="text-center">${ICONS.add}<p>add</p></div>
                </div>
            </div>

            <div class="ml-auto mr-auto mt-8 w-[80%]">
                <h3>Navigation</h3>
                    <div class="flex justify-center mb-3"><app-navigation style="height:min-content;" class="relative"></app-navigation></div>
                    <p>
                        Thay đổi nội dung biến <b>NAV</b> trong tập tin /fragments/AppNavigation.js để thay đổi điều hướng đồng thời thay đổi luôn cả tiêu đề của trang web
                    </p>
                    <p>
                        { <b>href:</b> 'location.path', <b>name:</b> 'Tiêu đề', <b>icon:</b> 'name icon phía trên' },
                    </p>
            </div>

            <div class="ml-auto mr-auto mt-8 w-[80%]">
                <h3>Header</h3>
                    <div class="flex justify-center mb-3"><app-header class="w-full bg-white" title="Tiêu đề" data-title="Tiêu đề nhỏ" username="Tên người dùng" user-role="Chức vụ người dùng"></app-header></div>
                    <h4>
                        Thuộc tính tuỳ chỉnh trong thẻ &lt;app-header&gt;&lt;/app-header&gt;
                    </h4>
                    <h5>title="Tiêu đề"</h5>
                    <app-header class="w-full bg-white" title="Tiêu đề"></app-header>
                    <h5>data-title="Tiêu đề phụ"</h5>
                    <app-header class="w-full bg-white" data-title="Tiêu đề phụ"></app-header>
                    <h5>username="Tên người dùng"</h5>
                    <app-header class="w-full bg-white" username="Tên người dùng"></app-header>
                    <h5>user-role="Chủ cửa hàng"</h5>
                    <app-header class="w-full bg-white" user-role="Chủ cửa hàng"></app-header>
                    <h5>user-image="link ảnh"</h5>
                    <p>Link ảnh nếu upload ảnh vui lòng sử dụng ảnh 1:1</p>
                    <app-header class="w-full bg-white" user-image="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpGwe9JmGKFv4gSR2RftkyhJUeWVy3izqmTI0Axh1BCpOTTvM7OPKVS_sw&s=10"></app-header>
            </div>
        </div>`;
    }
}

customElements.define('design-app', DesignApp);