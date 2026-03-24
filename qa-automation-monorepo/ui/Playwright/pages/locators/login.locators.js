// This is a normal JS module (function-based locator factory), not a class.
export function getLoginLocators(page) {
  return {
    usernameInput: page.locator('input[type="email"], input[name="email"], input[name="username"], #email, #username').first(),
    passwordInput: page.locator('input[type="password"], input[name="password"], #password').first(),
    loginButton: page.getByRole('button', { name: /login|sign in/i }),
  };
}