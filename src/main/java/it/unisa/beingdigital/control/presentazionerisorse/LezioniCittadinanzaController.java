package it.unisa.beingdigital.control.presentazionerisorse;

import it.unisa.beingdigital.service.presentazionerisorse.PrelievoArgomentoService;
import it.unisa.beingdigital.storage.entity.util.Livello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lezioniCittadinanza")
public class LezioniCittadinanzaController {

  @Autowired
  private PrelievoArgomentoService prelievoArgomentoService;

  @GetMapping
  public String get(Model model) {
    model.addAttribute("lezioniPerMetaInfo",
        prelievoArgomentoService.getLezioniPerMetaInfoSortedByLivelloKeywordTitolo(
            Livello.CITTADINANZA_DIGITALE));
    model.addAttribute("tipo", "lezioniCittadinanza");
    return "presentazionerisorse/listaLezioni";
  }
}