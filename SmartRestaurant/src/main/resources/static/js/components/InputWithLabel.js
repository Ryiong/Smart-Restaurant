class InputAndLabel extends HTMLElement {
    static observedAttributes = ['placeholder'];

    connectedCallback() {

        const label = this.getAttribute('label') ?? 'Label Input';
        const name = this.getAttribute('name') ?? 'password';
        const placeholder = this.getAttribute('placeholder') ?? '';

        this.classList.add('block', 'mb-4.5');
        this.innerHTML = `
          <label id="login-form-username__label" for="login-form-username" class="block mb-1.5 font-semibold">${label}</label>
          <input id="login-form-username" name="${name}" type="text" autocomplete="username" required="" placeholder="${placeholder}" data-msg="Enter the username you signed up with." aria-describedby="login-username-err" class="block w-full min-w-12 pt-2.5 pb-2.5 pl-3.5 pr-3.5 border-solid border border-support-600 rounded-xl bg-white text-text transition-border duration-200 focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500">

              `;
        
    }
  }

customElements.define('form-field', InputAndLabel);