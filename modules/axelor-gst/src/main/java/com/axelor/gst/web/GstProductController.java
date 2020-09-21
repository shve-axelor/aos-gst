package com.axelor.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.supplychain.service.app.AppSupplychainService;
import com.axelor.db.JpaSupport;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class GstProductController extends JpaSupport {

  public void createInvoiceFromProductGrid(ActionRequest request, ActionResponse response) {

    response.setView(
        ActionView.define("Invoice")
            .model(Invoice.class.getName())
            .add("form", "invoice-form")
            .param("forceEdit", "true")
            .context("company", request.getContext().get("company"))
            .context("productlist", request.getContext().get("productlists"))
            .context("partner", request.getContext().get("partner"))
            .context("_operationTypeSelect", String.valueOf(3))
            .context("todayDate", Beans.get(AppSupplychainService.class).getTodayDate())
            .map());
  }
}
