package fr.gouv.diplomatie.hornet.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Entité metier Secteur
 */
public class Secteur implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Date dateCreat;

    /**
     *
     */
    private String auteurCreat;

    /**
     *
     */
    private Date dateMajEnreg;

    /**
     *
     */
    private String auteurMajEnreg;

    /**
     *
     */
    private Date dateSupprEnreg;

    /**
     *
     */
    private String auteurSupprEnreg;

    /**
     *
     */
    private String nom;

    /**
     *
     */
    private String desc;

    /**
     *
     */
    public Secteur() {

    }

    /**
     *
     * @param nom
     *            String
     * @param description
     *            String
     * @param auteur
     *            Utilisateur
     */
    public Secteur(
                final String nom, final String description) {

        this.nom = nom;
        this.desc = description;
        this.dateCreat = new Date();
        this.dateMajEnreg = (Date) this.dateCreat.clone();
    }

    /**
     *
     * @param leNom
     *            String
     * @param laDescription
     *            String
     * @param util
     *            Utilisateur
     */
    public void modifier(final String leNom, final String laDescription) {

        this.nom = leNom;
        this.desc = laDescription;
        this.dateMajEnreg = new Date();
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Long id) {

        this.id = id;
    }

    /**
     * @return Returns the dateCreatEnreg.
     */
    public Date getDateCreat() {

        Date result = null;
        if (null != this.dateCreat) {
            result = (Date) this.dateCreat.clone();
        }
        return result;
    }

    /**
     * @param dateCreat
     *            The dateCreatEnreg to set.
     */
    public void setDateCreat(final Date dateCreat) {

        if (null == dateCreat) {
            this.dateCreat = null;
        } else {
            this.dateCreat = (Date) dateCreat.clone();
        }
    }

    /**
     * @return Returns the auteurCreatEnreg.
     */
    public String getAuteurCreat() {

        return this.auteurCreat;
    }

    /**
     * @param auteurCreat
     *            The auteurCreatEnreg to set.
     */
    public void setAuteurCreat(final String auteurCreat) {

        this.auteurCreat = auteurCreat;
    }

    /**
     * @return Returns the dateMajEnreg.
     */
    public Date getDateMajEnreg() {

        Date result = null;
        if (null != this.dateMajEnreg) {
            result = (Date) this.dateMajEnreg.clone();
        }
        return result;
    }

    /**
     * @param dateMajEnreg
     *            The dateMajEnreg to set.
     */
    public void setDateMajEnreg(final Date dateMajEnreg) {

        if (null == dateMajEnreg) {
            this.dateMajEnreg = null;
        } else {
            this.dateMajEnreg = (Date) dateMajEnreg.clone();
        }
    }

    /**
     * @return Returns the auteurMajEnreg.
     */
    public String getAuteurMajEnreg() {

        return this.auteurMajEnreg;
    }

    /**
     * @param auteurMajEnreg
     *            The auteurMajEnreg to set.
     */
    public void setAuteurMajEnreg(final String auteurMajEnreg) {

        this.auteurMajEnreg = auteurMajEnreg;
    }

    /**
     * @return Returns the dateSupprEnreg.
     */
    public Date getDateSupprEnreg() {

        Date result = null;
        if (null != this.dateSupprEnreg) {
            result = (Date) this.dateSupprEnreg.clone();
        }
        return result;
    }

    /**
     * @param dateSupprEnreg
     *            The dateSupprEnreg to set.
     */
    public void setDateSupprEnreg(final Date dateSupprEnreg) {

        if (null == dateSupprEnreg) {
            this.dateSupprEnreg = null;
        } else {
            this.dateSupprEnreg = (Date) dateSupprEnreg.clone();
        }
    }

    /**
     * @return Returns the auteurSupprEnreg.
     */
    public String getAuteurSupprEnreg() {

        return this.auteurSupprEnreg;
    }

    /**
     * @param auteurSupprEnreg
     *            The auteurSupprEnreg to set.
     */
    public void setAuteurSupprEnreg(final String auteurSupprEnreg) {

        this.auteurSupprEnreg = auteurSupprEnreg;
    }

    /**
     * @return Returns the nom.
     */
    public String getNom() {

        return this.nom;
    }

    /**
     * @param nom
     *            The nom to set.
     */
    public void setNom(final String nom) {

        this.nom = nom;
    }

    /**
     * @return Returns the desc.
     */
    public String getDesc() {

        return this.desc;
    }

    /**
     * @param desc
     *            The desc to set.
     */
    public void setDesc(final String desc) {

        this.desc = desc;
    }

}
