package com.axelor.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import com.axelor.db.JpaSupport;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;

public class GstInvoiceLineController extends JpaSupport {

  public void comuteGstFields(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    Address invoiceAddress = invoice.getAddress();
    Address companyAddress = invoice.getCompany().getAddress();
    BigDecimal gstRate = invoiceLine.getProduct()!= null ? invoiceLine.getProduct().getGstRate() : BigDecimal.ZERO;
    response.setValue("gstRate", gstRate);
    if (invoiceAddress != null && companyAddress != null) {
      if (gstRate != null
          && invoiceAddress.getCity().getState() != null
          && companyAddress.getCity().getState() != null) {
        BigDecimal netamount = invoiceLine.getExTaxTotal();
        if (invoice.getAddress().getCity().getState()
            != invoice.getCompany().getAddress().getCity().getState()) {
          response.setValue("iGst", netamount.multiply(gstRate));
        } else {
          BigDecimal multi = netamount.multiply(gstRate);
          response.setValue("cGst", multi.divide(BigDecimal.valueOf(2)));
          response.setValue("sGst", multi.divide(BigDecimal.valueOf(2)));
        }
      } else {
        response.setFlash("Please Set a Set In the Address First");
      }
    } else {
      response.setFlash("Please Set a Address For the Partner Or Company");
    }
  }
}
