package fr.gouv.diplomatie.hornet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.gouv.diplomatie.hornet.dto.Secteur;
import fr.gouv.diplomatie.hornet.dto.SecteurAjouterDTOIn;
import fr.gouv.diplomatie.hornet.dto.SecteurAjouterDTOOut;

/**
 * @author Hornet
 * @since 1.0 - 6 févr. 2015
 */
@RestController
@RequestMapping("/rest")
public class RestService {

    @RequestMapping(value = "/ajout/{id}", method = RequestMethod.GET)
    public @ResponseBody SecteurAjouterDTOOut lire(@RequestParam final String id,
                @RequestBody final SecteurAjouterDTOIn dtoIn) {

        final SecteurAjouterDTOOut dtoOut = new SecteurAjouterDTOOut();
        final Secteur secteur = new Secteur();
        secteur.setId(5L);
        dtoOut.setSecteur(secteur);

        return dtoOut;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody Secteur ajouterSecteur(@RequestBody final SecteurAjouterDTOIn dtoIn) {

        final Secteur secteur = null;
        return secteur;
    }

}
