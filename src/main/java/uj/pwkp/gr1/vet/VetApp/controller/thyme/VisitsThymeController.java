package uj.pwkp.gr1.vet.VetApp.controller.thyme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

@Controller
@RequestMapping("/ui")
public class VisitsThymeController {

  @Autowired
  private VisitService visitsService;

  @GetMapping("/visits")
  public String showAllVisits(Model model) {
    model.addAttribute("visits", visitsService.getAllVisits());
    return "visits";
  }

  @GetMapping("/visits2")
  public String showAllVisits2(Model model) {
    model.addAttribute("visit", visitsService.getAllVisits());
    return "visit";
  }

}