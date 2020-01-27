package com.bankzecure.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.bankzecure.webapp.repository.*;

@Controller
public class CreditCardController {
  private CreditCardRepository repository = new CreditCardRepository();
  
  @GetMapping("/customers/{identifier}/credit-cards")
  String getAll(Model model, @PathVariable String identifier) {
    model.addAttribute("creditCards", repository.findByCustomerIdentifier(identifier));
    return "customer_cards";
  }

}

/* -1-->
From its name, CreditCardController looks like a good candidate. Have a look at it: the getAll
method is preceded by a @GetMapping annotation, with the "/customers/{identifier}/credit-cards".
Bingo! It also takes in a @PathVariable, which makes it a potential target for SQL injections!
*/



/* -2-->
Inside getAll, we call the findByCustomerIdentifier method from a CreditCardRepository instance,
providing it with the customer's 6-digit identifier that we obtained from the URL, via the
@PathVariable annotation. Let's go deeper down that rabbit hole!
*/





