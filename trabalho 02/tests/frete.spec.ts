import { test, expect } from "@playwright/test";

//Casos com resultado positivo

test("frete de 15 reais com CEP começando com 8", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("85000000");
  await page.getByLabel("Valor do pedido").fill("150");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Frete: R$ 15,00");
});

test("frete de 25 reais com CEP comum", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("60000000");
  await page.getByLabel("Valor do pedido").fill("150");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Frete: R$ 25,00");
});

test("frete grátis para produtos a partir de 200", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("60000000");
  await page.getByLabel("Valor do pedido").fill("250");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Frete grátis");
});

//Casos com resultado negativo

test("mensagem de erro ao inserir o campo Valor do Pedido vazio", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("60000000");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir o campo CEP vazio", async ({ page }) => {
  await page.goto("/frete");

  await page.getByLabel("Valor do pedido").fill("250");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir CEP com menos de 8 dígitos", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("6000000");

  await page.getByLabel("Valor do pedido").fill("250");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir CEP com mais de 8 dígitos", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("600000000");

  await page.getByLabel("Valor do pedido").fill("250");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir CEP com 8 dígitos, porém negativo", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("-60000000");

  await page.getByLabel("Valor do pedido").fill("250");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir Valor do Pedido como 0", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("60000000");

  await page.getByLabel("Valor do pedido").fill("0");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});

test("mensagem de erro ao inserir Valor do Pedido com número negativo", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("60000000");

  await page.getByLabel("Valor do pedido").fill("-10");

  await page.getByRole("button", { name: "Calcular frete" }).click();

  const resultado = page.locator("#resultado");

  await expect(resultado).toBeVisible();
  await expect(resultado).toHaveText("Dados inválidos");
});