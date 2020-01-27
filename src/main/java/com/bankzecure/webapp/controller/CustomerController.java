package com.bankzecure.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.bankzecure.webapp.repository.*;
import com.bankzecure.webapp.entity.Customer;

@Controller
public class CustomerController {
  private CustomerRepository repository = new CustomerRepository();
  
  @PostMapping("/customers/update")
  String getAll(Model model, @RequestParam String identifier, @RequestParam String email, @RequestParam String password) {
    Customer customer = repository.update(identifier, email, password);
    model.addAttribute("customer", customer);
    model.addAttribute("updated", true);
    return "profile";
  }

  @PostMapping("/customers/authenticate")
  String login(Model model, @RequestParam String identifier, @RequestParam String password) {
    Customer customer = repository.findByIdentifierAndPassword(identifier, password);
    model.addAttribute("customer", customer);
    return "profile";
  }

}

/* - 6 -->  (Coming from CreditCardRepository)
Check out the signin_form template: the <form> posts its data to the /login endpoint of your server.
This request is handled by CustomerController. This controller has two methods, which accept incoming
data via their parameters (annotated with @RequestParam). Obviously, login is the relevant one here.
*/





