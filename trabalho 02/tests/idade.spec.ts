import { test, expect } from '@playwright/test';

const casos = [
  { idade: '10', aceito: false, classe: 'abaixo do mínimo' },
  { idade: '18', aceito: true, classe: 'limite mínimo' },
  { idade: '25', aceito: true, classe: 'acima do mínimo' },
  { idade: '60', aceito: true, classe: 'abaixo do máximo' },
  { idade: '65', aceito: true, classe: 'limite máximo' },
  { idade: '70', aceito: false, classe: 'acima do máximo' },
  { idade: 'trinta', aceito: false, classe: 'tipo inválido' },
  { idade: '40.5', aceito: false, classe: 'decimal inválido' },
  { idade: '', aceito: false, classe: 'vazio' },
];

for (const caso of casos) {
  test(`idade ${caso.idade || '(vazia)'} — ${caso.classe}`, async ({ page }) => {
    await page.goto('/idade');
    await page.getByLabel('Idade').fill(caso.idade);
    await page.getByRole('button', { name: 'Validar cadastro' }).click();

    const resultado = page.locator('#resultado');
    await expect(resultado).toBeVisible();
    await expect(resultado).toHaveText(caso.aceito ? 'Cadastro permitido' : 'Idade inválida');
    await expect(resultado).toHaveAttribute('role', caso.aceito ? 'status' : 'alert');
  });
}