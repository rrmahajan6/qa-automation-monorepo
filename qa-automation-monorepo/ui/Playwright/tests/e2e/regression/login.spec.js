import {test, expect} from '../../../fixtures/testFixtures.js';
import { PageManager } from '../../../pages/PageManager.js';
import logger from '../../../utils/logger.js';

//test case without using login fixture
test("Login to application with correct credentials",async({page})=>{
    const pageManager = new PageManager(page);
    const loginPage = pageManager.loginPage();
    await loginPage.goto();
    await loginPage.loginWithDefaultCredentials();
    await page.waitForTimeout(5000);
});

//login using login page fixture
test("login with fixture",{tag:'@smoke'},async({loginPage})=>{
    await loginPage.goto();
    await loginPage.loginWithDefaultCredentials();
});

//login using authentication fixture
test("login using authentication fixture",{tag:'@run'},async({authenticatedPage})=>{
    logger.info("login successful");
});
