class HiddenInputWithLabel extends HTMLElement {
    connectedCallback() {
        const label = this.getAttribute('label') ?? '';
        const children = [...this.childNodes];

        this.classList.add('block', 'mb-4.5');
        this.innerHTML = `
            <label id="login-form-pw__label" for="login-form-pw" class="block mb-1.5 font-semibold">Password</label>
            <div id="login-form__pw" class="relative">
                <div data-slot></div>
            </div>
        `;
        this.querySelector('label').textContent = label;
        this.querySelector('[data-slot]').append(...children);

        const input = this.querySelector('input');
        if (input) {
            input.id ||= `f-${crypto.randomUUID().slice(0, 8)}`;
            this.querySelector('label').htmlFor = input.id;
        }
    }
}

customElements.define('hidden-field', HiddenInputWithLabel);