package com.axelor.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.InvoiceService;
import com.axelor.apps.account.service.invoice.print.InvoicePrintService;
import com.axelor.db.JpaSupport;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GstInvoiceController extends JpaSupport {

  @Inject private InvoiceService invoiceService;

  @Inject private InvoiceLineService invoiceLineService;

  @Inject private InvoicePrintService invoicePrintService;

  @Inject private InvoiceRepository invoiceRepo;

  public void reCalculateInvoiceLine(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Map<String, Object> productInformation = new HashMap<>();
    if (invoice.getInvoiceLineList() != null && !invoice.getInvoiceLineList().isEmpty()) {
      for (InvoiceLine invoiceLine : invoice.getInvoiceLineList()) {
        productInformation = invoiceLineService.fillProductInformation(invoice, invoiceLine);
        invoiceLine.setiGst((BigDecimal) productInformation.get("iGst"));
        invoiceLine.setsGst((BigDecimal) productInformation.get("sGst"));
        invoiceLine.setcGst((BigDecimal) productInformation.get("cGst"));
      }

      invoice = invoiceService.compute(invoice);
      response.setValues(invoice);
    }
  }

  @SuppressWarnings("unchecked")
  public void setInvoicelineProducts(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Context ct = request.getContext();
    /* if (ct.get("productlist") != null) {
      Invoice invoice = ct.asType(Invoice.class);
      List<Integer> idList = (List<Integer>) ct.get("productlist");
      List<Product> productlist = invoiceService.getSelectedProducts(idList);
      List<InvoiceLine> invoiceLineList = new ArrayList<InvoiceLine>();

      for (Product product : productlist) {
        InvoiceLine invoiceLine = new InvoiceLine();
        invoiceLine.setProduct(product);
        invoiceLine.setQty(BigDecimal.ONE);
        Map<String, Object> productInformation = new HashMap<>();
        if (invoice.getCompany() != null && invoice.getPartner() != null) {
          productInformation = invoiceLineService.fillProductInformation(invoice, invoiceLine);
          productInformation.put("product", product);
          productInformation.put("qty", BigDecimal.ONE);
          invoiceLine =
              Mapper.toBean(InvoiceLine.class, productInformation); // convert map to object
          //  Mapper.toMap(abc); convert object to map
        }
        invoiceLineList.add(invoiceLine);
      }
      invoice.setInvoiceLineList(invoiceLineList);
      invoice.setAddressStr(
          Beans.get(AddressService.class).computeAddressStr(invoice.getAddress()));
      invoiceService.compute(invoice);
      response.setValues(invoice);
    }*/
  }

  public void showGstInvoiceReport(ActionRequest request, ActionResponse response) {
    Context context = request.getContext();
    String fileLink;
    String title;

    /* try {
      if (context.get("id") != null) {
        Invoice invoice = invoiceRepo.find(request.getContext().asType(Invoice.class).getId());
        fileLink = invoicePrintService.printInvoice(invoice, false);
        title = I18n.get("Invoice");
      } else {
        throw new AxelorException(
            TraceBackRepository.CATEGORY_MISSING_FIELD, I18n.get(IExceptionMessage.INVOICE_3));
      }
      response.setView(ActionView.define(title).add("html", fileLink).map());
      response.setReload(true);
    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }*/
  }
}
