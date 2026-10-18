import { ICONS } from '../icon.js';

class HiddenInput extends HTMLElement {
    static observedAttributes = ['placeholder'];

    connectedCallback() {
        if (this.dataset.ready) return;
        this.dataset.ready = '1';

        const name = this.getAttribute('name') ?? 'password';
        const placeholder = this.getAttribute('placeholder') ?? '';

        this.classList.add('block', 'relative');
        this.innerHTML = `
        <input id="login-form-pw" name="${name}" placeholder=${placeholder} type="password" autocomplete="current-password" required="" minlength="8" data-msg="Enter your password." aria-describedby="login-form-pw-err" class="block w-full min-w-12 pt-2.5 pb-2.5 pl-3.5 pr-3.5 border-solid border border-support-600 rounded-xl bg-white text-color transition-border duration-200 focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500">
              <button id="login-form__eye" type="button" aria-label="Show password" aria-pressed="false" aria-controls="login-form-pw" data-pw-toggle="" class="absolute top-1 right-1 grid place-items-center w-10 h-10 border-0 rounded-lg bg-transparent text-support-900 cursor-pointer">
                ${ICONS.eye}
              </button>
              `;
        this.input = this.querySelector('input');
        this.button = this.querySelector('button');
        this.button.addEventListener('click', () => this.toggle());
    }

    toggle(show = this.input.type === 'password') {
    this.input.type = show ? 'text' : 'password';
    this.button.innerHTML = show ? ICONS.eyeOff : ICONS.eye;
    this.button.setAttribute('aria-pressed', String(show));
    this.button.setAttribute('aria-label', show ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');

    this.dispatchEvent(new CustomEvent('visibility-change', {
      bubbles: true,
      detail: { visible: show },
    }));
  }

  get value() { return this.input.value; }
  set value(v) { this.input.value = v; }

  attributeChangedCallback(name, _old, val) {
    if (name === 'placeholder' && this.input) this.input.placeholder = val ?? '';
  }
}

customElements.define('hidden-input', HiddenInput);