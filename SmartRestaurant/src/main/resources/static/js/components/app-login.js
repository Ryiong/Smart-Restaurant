class AppLogin extends HTMLElement {
    connectedCallback() {
        this.classList.add("w-screen", "flex", "justify-center", "items-center", "h-screen");
        this.innerHTML = `
        <form id="login-form" class="min-w-md">
            <h1 class="mb-2 ">Welcome</h1>
            <div id="login-form-username-group" class="mb-4.5" data-field="username">
                <label id="login-form-username__label" for="login-form-username" class="block mb-1.5 font-semibold">Username</label>
                <input id="login-form-username" name="username" type="username" autocomplete="username" required="" placeholder="Abc" data-msg="Enter the username you signed up with." aria-describedby="login-username-err" class="block w-full min-w-12 pt-2.5 pb-2.5 pl-3.5 pr-3.5 border-solid border border-support-600 rounded-xl bg-white text-text transition-border duration-200 focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500">
                <p id="login-form-username__error" data-error="" class="flex items-center gap-1.5 mt-1.5 mb-1.5 text-bug font-medium empty:hidden"></p>
            </div>
            <div id="login-form__field" class="mb-4.5" data-field="">
            <div id="login-form__label-row" class="flex items-baseline justify-between gap-3">
              <label id="login-form-pw__label" for="login-form-pw" class="block mb-1.5 font-semibold">Password</label>
              <a id="login-form-pw__link" href="#" class="block mb-1.5 font-medium text-primary-600 rounded-sm no-underline hover:underline hover:underline-offset-3">Forgot password?</a>
            </div>
            <div id="login-form__pw" class="relative">
              <input id="login-form-pw" name="password" type="password" autocomplete="current-password" required="" minlength="8" data-msg="Enter your password." aria-describedby="login-form-pw-err" class="block w-full min-w-12 pt-2.5 pb-2.5 pl-3.5 pr-3.5 border-solid border border-support-600 rounded-xl bg-white text-color transition-border duration-200 focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500">
              <button id="login-form__eye" type="button" aria-label="Show password" aria-pressed="false" aria-controls="login-form-pw" data-pw-toggle="" class="absolute top-1 right-1 grid place-items-center w-10 h-10 border-0 rounded-lg bg-transparent text-support-900 cursor-pointer">
                <svg id="login-form__eye-on" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                <svg id="login-form__eye-off" class="hidden" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 3l18 18M10.6 5.1A10.8 10.8 0 0 1 12 5c6.4 0 10 7 10 7a17.6 17.6 0 0 1-3.2 4.1M6.6 6.6C3.9 8.3 2 12 2 12s3.6 7 10 7a9.8 9.8 0 0 0 5.4-1.6M9.9 9.9a3 3 0 0 0 4.2 4.2"></path></svg>
              </button>
            </div>
            <p id="login-form-pw__error" data-error="" class="flex items-center gap-1.5 mt-1.5 mb-1.5 text-bug font-medium empty:hidden"></p>
            </div>
          <button id="login-form__submit" type="submit" class="w-full min-h-12.5 border rounded-xl bg-primary-700 text-white font-bold cursor-pointer shadow-[0_8px_20px_-8px_rgba(20,83,45,.6)] hover:bg-primary-800">Đăng nhập</button>
          <div id="login-form__live" role="status" aria-live="polite" data-live=""></div>
          <p id="login-form__foot" class="mt-6 text-text text-center">Chưa có tài khoản? <a id="login-form__link" href="#" class="text-primary-600 font-medium no-underline rounded-sm">Tạo tài khoản</a></p>
        </form>
        `;
    }
}

customElements.define('app-login', AppLogin);