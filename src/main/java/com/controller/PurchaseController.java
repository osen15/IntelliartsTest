package com.controller;

import com.models.Purchase;
import com.service.PurchaseService;
import com.exceptions.BadRequestEXception;
import com.exceptions.InternalServerError;
import com.utils.JsonToModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final JsonToModel jsonToModel;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, JsonToModel jsonToModel) {
        this.purchaseService = purchaseService;
        this.jsonToModel = jsonToModel;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/savePurchase")
    @ResponseBody
    public Map<LocalDate, List<String>> purchase(HttpServletRequest request) {
        Purchase purchase = jsonToModel.jsonToEntity(request);
        if (purchase.getDateOfPurchase() == null)
            purchase.setDateOfPurchase(LocalDate.now());
        return purchaseService.addPurchase(purchase);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/removeByDate")
    @ResponseBody
    Map<LocalDate, List<String>> doClear(@RequestParam("date") String stDate) throws BadRequestEXception, InternalServerError {
      return  purchaseService.clear(stDate);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/allPurchases")
    @ResponseBody
    Map<LocalDate, List<String>> doAll() throws BadRequestEXception, InternalServerError {
        return purchaseService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/report")
    @ResponseBody
    public String report(@RequestParam(defaultValue = "2019") String year,
                         @RequestParam(defaultValue = "UAH") String currency) {
        return purchaseService.getReport(year, currency);
    }
}

