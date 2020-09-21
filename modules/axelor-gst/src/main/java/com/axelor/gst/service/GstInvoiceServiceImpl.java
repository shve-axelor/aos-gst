package com.axelor.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.account.service.config.AccountConfigService;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.factory.CancelFactory;
import com.axelor.apps.account.service.invoice.factory.ValidateFactory;
import com.axelor.apps.account.service.invoice.factory.VentilateFactory;
import com.axelor.apps.account.service.move.MoveToolService;
import com.axelor.apps.base.service.PartnerService;
import com.axelor.apps.base.service.alarm.AlarmEngineService;
import com.axelor.apps.cash.management.service.InvoiceServiceManagementImpl;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import java.math.BigDecimal;

public class GstInvoiceServiceImpl extends InvoiceServiceManagementImpl {

  @Inject
  public GstInvoiceServiceImpl(
      ValidateFactory validateFactory,
      VentilateFactory ventilateFactory,
      CancelFactory cancelFactory,
      AlarmEngineService<Invoice> alarmEngineService,
      InvoiceRepository invoiceRepo,
      AppAccountService appAccountService,
      PartnerService partnerService,
      InvoiceLineService invoiceLineService,
      AccountConfigService accountConfigService,
      MoveToolService moveToolService) {
    super(
        validateFactory,
        ventilateFactory,
        cancelFactory,
        alarmEngineService,
        invoiceRepo,
        appAccountService,
        partnerService,
        invoiceLineService,
        accountConfigService,
        moveToolService);
  }

  @Override
  public Invoice compute(Invoice invoice) throws AxelorException {
    invoice = super.compute(invoice);

    BigDecimal netamount = BigDecimal.ZERO,
        netigst = BigDecimal.ZERO,
        netcgst = BigDecimal.ZERO,
        netsgst = BigDecimal.ZERO;
    if (invoice.getInvoiceLineList() != null) {
      for (InvoiceLine invoiceline : invoice.getInvoiceLineList()) {
        netamount = invoiceline.getCompanyExTaxTotal().add(netamount);
        netigst = invoiceline.getiGst().add(netigst);
        netcgst = invoiceline.getcGst().add(netcgst);
        netsgst = invoiceline.getsGst().add(netsgst);
      }
    }
    invoice.setNetIgst(netigst);
    invoice.setNetSgst(netsgst);
    invoice.setNetCgst(netcgst);

    return invoice;
  }
}
