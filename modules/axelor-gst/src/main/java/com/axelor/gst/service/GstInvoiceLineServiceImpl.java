package com.axelor.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.service.AccountManagementAccountService;
import com.axelor.apps.account.service.AnalyticMoveLineService;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.service.CurrencyService;
import com.axelor.apps.base.service.PriceListService;
import com.axelor.apps.base.service.ProductCompanyService;
import com.axelor.apps.businessproject.service.InvoiceLineProjectServiceImpl;
import com.axelor.apps.purchase.service.PurchaseProductService;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;

public class GstInvoiceLineServiceImpl extends InvoiceLineProjectServiceImpl {

  @Inject AccountManagementAccountService accountManagementService;

  @Inject AppAccountService appAccountService;

  @Inject
  public GstInvoiceLineServiceImpl(
      CurrencyService currencyService,
      PriceListService priceListService,
      AppAccountService appAccountService,
      AnalyticMoveLineService analyticMoveLineService,
      AccountManagementAccountService accountManagementAccountService,
      PurchaseProductService purchaseProductService,
      ProductCompanyService productCompanyService) {
    super(
        currencyService,
        priceListService,
        appAccountService,
        analyticMoveLineService,
        accountManagementAccountService,
        purchaseProductService,
        productCompanyService);
  }

  @Override
  public Map<String, Object> fillPriceAndAccount(
      Invoice invoice, InvoiceLine invoiceLine, boolean isPurchase) throws AxelorException {
    Map<String, Object> gstcalculation =
        super.fillPriceAndAccount(invoice, invoiceLine, isPurchase);

    Partner party = invoice.getPartner();
    Company company = invoice.getCompany();
    Address invoiceaddress = invoice.getAddress();
    Address companyaddress = company.getAddress();
    BigDecimal netamount = BigDecimal.ZERO,
        igst = BigDecimal.ZERO,
        cgst = BigDecimal.ZERO,
        sgst = BigDecimal.ZERO,
        gstrate = BigDecimal.ZERO;
    netamount = invoiceLine.getProduct().getSalePrice().multiply(invoiceLine.getQty());
    if (invoiceaddress != null && companyaddress != null) {
      if (party != null
          && company != null
          && invoiceLine.getTaxLine().getTax().getCode().equals("GST")
          && invoiceaddress.getCity().getState() != null
          && companyaddress.getCity().getState() != null) {
        gstrate = invoiceLine.getTaxLine().getValue();
        if (invoiceaddress.getCity().getState() != companyaddress.getCity().getState()) {
          igst = netamount.multiply(gstrate);
        } else {
          BigDecimal multi = netamount.multiply(gstrate);
          sgst = multi.divide(BigDecimal.valueOf(2));
          cgst = sgst;
        }
      }
    }

    gstcalculation.put("gstRate", gstrate);
    gstcalculation.put("sGst", sgst);
    gstcalculation.put("iGst", igst);
    gstcalculation.put("cGst", cgst);
    gstcalculation.put("exTaxTotal", netamount);

    return gstcalculation;
  }

  @Override
  public Map<String, Object> resetProductInformation(Invoice invoice) throws AxelorException {
    Map<String, Object> resetProductInformation = super.resetProductInformation(invoice);
    resetProductInformation.put("gstRate", null);
    resetProductInformation.put("cGst", null);
    resetProductInformation.put("iGst", null);
    resetProductInformation.put("sGst", null);
    return resetProductInformation;
  }
}
