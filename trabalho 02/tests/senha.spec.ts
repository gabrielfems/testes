import { test, expect } from "@playwright/test";

//Casos com resultado positivo

test("senha válida", async ({ page }) => {
  await page.goto("/senha");
  await page.getByLabel("Nova senha").fill("Jo98765#");
  await page.getByLabel("Confirmar senha").fill("Jo98765#");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Senha cadastrada");
});

//Casos com resultado negativo

test("mensagem de erro ao inserir Nova Senha como vazio", async ({ page }) => {
  await page.goto("/senha");

  await page.getByLabel("Confirmar senha").fill("Jo98765#");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Senha fora do padrão");
});

test("mensagem de erro ao inserir senha válida em Nova Senha e deixar Confirmar Senha como vazio", async ({
  page,
}) => {
  await page.goto("/senha");

  await page.getByLabel("Nova senha").fill("Jo98765#");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("As senhas não coincidem");
});

test("mensagem de erro ao inserir senha inválida em Nova Senha e deixar Confirmar Senha como vazio", async ({
  page,
}) => {
  await page.goto("/senha");

  await page.getByLabel("Nova senha").fill("Jo987");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Senha fora do padrão");
});

test("mensagem de erro ao inserir senha com mais de 20 caracteres", async ({
  page,
}) => {
  await page.goto("/senha");

  await page.getByLabel("Nova senha").fill("Jo98765432109876543##");
  await page.getByLabel("Confirmar senha").fill("Jo98765432109876543##");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Senha fora do padrão");
});

test("mensagem de erro ao inserir senha válida com espaços em branco", async ({
  page,
}) => {
  await page.goto("/senha");

  await page.getByLabel("Nova senha").fill("Jo98765 ");
  await page.getByLabel("Confirmar senha").fill("Jo98765 ");

  await page.getByRole("button", { name: "Cadastrar senha" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Senha fora do padrão");
});