import { test, expect } from '@playwright/test';

test.describe('login funcional', () => {
  test('permite login com credenciais válidas', async ({ page }) => {
    await page.goto('/login');

    await page.getByLabel('E-mail').fill('carlos@exemplo.com');
    await page.getByLabel('Senha').fill('MinhaSenh@456');
    await page.getByRole('button', { name: 'Entrar' }).click();

    await expect(page).toHaveURL(/\/conta$/);
    await expect(page.getByRole('heading', { name: 'Minha conta' })).toBeVisible();
    await expect(page.getByTestId('usuario')).toHaveText('Usuário: Carlos');
    await expect
      .poll(() => page.evaluate(() => sessionStorage.getItem('usuarioAutenticado')))
      .toBe('Carlos');
  });

  test('nega login com senha inválida', async ({ page }) => {
    await page.goto('/login');

    await page.getByLabel('E-mail').fill('carlos@exemplo.com');
    await page.getByLabel('Senha').fill('senha-errada');
    await page.getByRole('button', { name: 'Entrar' }).click();

    await expect(page.getByRole('alert')).toHaveText('E-mail ou senha inválidos');
    await expect(page).toHaveURL(/\/login$/);
    await expect
      .poll(() => page.evaluate(() => sessionStorage.getItem('usuarioAutenticado')))
      .toBeNull();
  });
});import { test, expect } from '@playwright/test';

test.describe('login funcional', () => {
  test('permite login com credenciais válidas', async ({ page }) => {
    await page.goto('/login');

    await page.getByLabel('E-mail').fill('carlos@exemplo.com');
    await page.getByLabel('Senha').fill('MinhaSenh@456');
    await page.getByRole('button', { name: 'Entrar' }).click();

    await expect(page).toHaveURL(/\/conta$/);
    await expect(page.getByRole('heading', { name: 'Minha conta' })).toBeVisible();
    await expect(page.getByTestId('usuario')).toHaveText('Usuário: Carlos');
    await expect
      .poll(() => page.evaluate(() => sessionStorage.getItem('usuarioAutenticado')))
      .toBe('Carlos');
  });

  test('nega login com senha inválida', async ({ page }) => {
    await page.goto('/login');

    await page.getByLabel('E-mail').fill('carlos@exemplo.com');
    await page.getByLabel('Senha').fill('senha-errada');
    await page.getByRole('button', { name: 'Entrar' }).click();

    await expect(page.getByRole('alert')).toHaveText('E-mail ou senha inválidos');
    await expect(page).toHaveURL(/\/login$/);
    await expect
      .poll(() => page.evaluate(() => sessionStorage.getItem('usuarioAutenticado')))
      .toBeNull();
  });
});