package it.unisa.beingdigital.control.gestionerisorse;

import it.unisa.beingdigital.control.gestionerisorse.form.MetaInfoForm;
import it.unisa.beingdigital.control.gestionerisorse.form.TeamForm;
import it.unisa.beingdigital.service.gestionerisorse.ModificaRisorsaService;
import it.unisa.beingdigital.service.presentazionerisorse.PrelievoMetaInfoService;
import it.unisa.beingdigital.service.presentazionerisorse.PrelievoTeamService;
import it.unisa.beingdigital.storage.entity.MetaInfo;
import it.unisa.beingdigital.storage.entity.Team;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * Questa classe rappresenta il controller per la modifica di un Team.
 */

@Controller
@RequestMapping("/modificaTeamUtente")
public class ModificaTeamUtenteController {

    @Autowired
    private ModificaRisorsaService modificaRisorsaService;

    @Autowired
    private PrelievoTeamService prelievoTeamService;

    /**
     * Implementa il get per la modifica di un Team.
     *
     * @param codice   codice del Team da modificare.
     * @param teamForm form da inserire nel model.
     * @return Stringa rappresentante il path della view da rappresentare.
     * @throws ResponseStatusException se il codice è nullo o non valido.
     */
    @GetMapping
    public String get(@RequestParam String codice, @ModelAttribute TeamForm teamForm) {
        Optional<Team> optional = prelievoTeamService.getTeam(codice);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Team team = optional.get();
        return "gestionerisorse/modificaTeam";
    }

    /**
     * Implementa il post per la modifica di un Team.
     *
     * @param codice        codice del team da modificare.
     * @param teamForm      form contenente i nuovi dati.
     * @param bindingResult risultato della validazione del form.
     * @param model         model da passare alla view.
     * @return Stringa rappresentante il path della view da rappresentare.
     * @throws ResponseStatusException se il codice risulta nullo, se il form non è ben formato.
     */
    @PostMapping
    public String post(@RequestParam String codice,
                       @ModelAttribute @Valid TeamForm teamForm,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!modificaRisorsaService.modificaTeam(codice, teamForm.getNome(), teamForm.getEmail())) {
            return "gestionerisorse/modificaTeam";
        }
        return "redirect:/auth/areaPersonale";
    }

    @PostMapping("/uscitaTeam")
    public String uscitaUtente(@RequestParam String codiceTeam, @RequestParam String idUtente) {

        Optional<Team> optionalTeam = prelievoTeamService.getTeam(codiceTeam);
        if (optionalTeam.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team non trovato");
        }

        Team team = optionalTeam.get();

        boolean espulsioneSuccesso = modificaRisorsaService.espelliUtententeDalTeam(team, Long.valueOf(idUtente));
        if (!espulsioneSuccesso) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Espulsione fallita");
        }

        return "redirect:/team";
    }
}