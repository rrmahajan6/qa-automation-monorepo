import {test, expect} from '../../../fixtures/testFixtures.js';
import { LoginPage } from '../../../pages/login/LoginPage.js';
import logger from '../../../utils/logger.js';

//test case without using login fixture
test("Login to application with correct credentials",async({page})=>{
    const login = new LoginPage(page);
    await login.goto();
    await login.loginWithDefaultCredentials();
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
