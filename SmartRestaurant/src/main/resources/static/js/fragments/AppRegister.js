import '../components/InputWithLabel.js';
import '../components/HiddenInput.js';
import '../components/HiddenInputWithLabel.js';

class AppRegister extends HTMLElement {
  connectedCallback() {
    if (this.dataset.ready) return;
    this.dataset.ready = '1';

    this.classList.add("w-screen", "flex", "justify-center", "items-center", "h-screen");
    this.innerHTML = `
        <form id="login-form" class="min-w-md">
            <h1 class="mb-2">Tạo tài khoản</h1>
            <p>Demo tự thêm các trường cần thiết vào và xoá thẻ p này đi</p>
            <form-field label="Email" name="email" placeholder="example@email.com"></form-field>
            <hidden-field label="Password">
              <hidden-input name="password" placeholder="Nhập mật khẩu"></hidden-input>
            </hidden-field>
            
            <p id="login-form__error" data-error="" class="flex items-center gap-1.5 mt-1.5 mb-.5 text-bug font-medium empty:hidden"></p>
          <button id="login-form__submit" type="submit" class="w-full min-h-12.5 border rounded-xl bg-primary-700 text-white font-bold cursor-pointer shadow-[0_8px_20px_-8px_rgba(20,83,45,.6)] hover:bg-primary-800 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-primary-700"">Đăng ký</button>
          <div id="login-form__live" role="status" aria-live="polite" data-live=""></div>
          <p id="login-form__foot" class="mt-6 text-text text-center">Đã có tài khoản <a id="login-form__link" href="/src/main/resources/templates/auth/login.html" class="text-primary-600 font-medium no-underline hover:underline hover:underline-offset- rounded-sm">ĐĂNG NHẬP</a></p>
        </form>
      `;
    this.form = this.querySelector('#login-form');
    this.error = this.querySelector('[data-error]');
    this.submitBtn = this.querySelector('#login-form__submit');

    this.form.addEventListener('submit', e => this.onSubmit(e));
  }

  async onSubmit(e) {
    e.preventDefault();
    this.setError('');
    this.submitBtn.disabled = true;

    const data = Object.fromEntries(new FormData(this.form));
    console.log(data);
  }

  setError(msg) {
    this.error.textContent = msg;
    this.error.classList.toggle('hidden', !msg);
  }
}

customElements.define('app-register', AppRegister);