package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.businessproject.service.InvoiceLineProjectServiceImpl;
import com.axelor.apps.cash.management.service.InvoiceServiceManagementImpl;
import com.axelor.gst.service.GstInvoiceLineServiceImpl;
import com.axelor.gst.service.GstInvoiceServiceImpl;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {

    bind(InvoiceServiceManagementImpl.class).to(GstInvoiceServiceImpl.class);

    bind(InvoiceLineProjectServiceImpl.class).to(GstInvoiceLineServiceImpl.class);
  }
}
