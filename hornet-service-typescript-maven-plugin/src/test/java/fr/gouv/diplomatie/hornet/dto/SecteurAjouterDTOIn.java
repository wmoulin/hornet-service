package fr.gouv.diplomatie.hornet.dto;


/**
 * @author Hornet
 * @since 1.0 - 4 févr. 2015
 */
public class SecteurAjouterDTOIn {

    private String nom;

    private String description;

    /**
     * @return nom
     */
    public String getNom() {

        return nom;
    }

    /**
     * @param nom
     *            nom
     */
    public void setNom(final String nom) {

        this.nom = nom;
    }

    /**
     * @return description
     */
    public String getDescription() {

        return description;
    }

    /**
     * @param description
     *            description
     */
    public void setDescription(final String description) {

        this.description = description;
    }

}
