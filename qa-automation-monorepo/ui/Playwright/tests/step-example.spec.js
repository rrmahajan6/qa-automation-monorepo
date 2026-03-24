import { test } from '@playwright/test';

// This sample test demonstrates the use of `test.step()`.
// Steps are helpful for grouping a set of actions and logging
// intermediate messages; they appear in traces and the debug UI.


test('checkout workflow with steps', async ({ page }) => {
  // set a simple page with a search input and button
  await test.step('prepare simple page', async () => {
    console.log('Setting page content');
    await page.setContent(`
      <input id="search" />
      <button type="submit" onclick="document.body.dataset.searched='true'">Go</button>
    `);
  });

  await test.step('search for product', async () => {
    console.log('Typing into search box');
    await page.fill('#search', 'laptop');
    await page.click('button[type=submit]');
    // verify the custom attribute is set
    await page.waitForFunction(() => document.body.dataset.searched === 'true');
  });

  await test.step('add first result to cart', async () => {
    console.log('Pretend to click an item');
    // nothing to click in this minimal example, just log
  });
});
