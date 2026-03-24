import { LoginPage } from './login/LoginPage.js';
import { HomePage } from './home/homePage.js';
import { CheckOutPage } from './checkout/CheckOutPage.js';
import { PaymentPage } from './payment/PaymentPage.js'
import { OrderPage } from './order/OrderPage.js'; 
import { HistoryPage } from './history/HistoryPage.js';

export class PageManager {
  constructor(page) {
    this.page = page;
    this._loginPage = null;
    this._homePage = null;
    this._checkOutPage = null;
    this._paymentPage = null;
    this._orderPage = null;
    this._histroyPage = null;
  }

  loginPage() {
    if (!this._loginPage) {
      this._loginPage = new LoginPage(this.page);
    }
    return this._loginPage;
  }

  homePage() {
    if (!this._homePage) {
      this._homePage = new HomePage(this.page);
    }
    return this._homePage;
  }

  checkOutPage() {
    if (!this._checkOutPage) {
      this._checkOutPage = new CheckOutPage(this.page);
    }
    return this._checkOutPage;
  }
  paymentPage(){
    if(!this._paymentPage) {
      this._paymentPage = new PaymentPage(this.page);
    }
    return this._paymentPage;
  }
   orderPage(){
    if(!this._orderPage) {
      this._orderPage = new OrderPage(this.page);
    }
    return this._orderPage;
  }
   HistoryPage(){
    if(!this._histroyPage) {
      this._histroyPage = new HistoryPage(this.page);
    }
    return this._histroyPage;
  }
}
