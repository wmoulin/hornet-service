/**
 * ﻿Copyright Ministère des Affaires étrangères et du Développement international , 22 avril 2015
 * https://adullact.net/projects/hornet/
 *
 *
 * Ce logiciel est un programme informatique servant à faciliter la création
 *  d'applications Web accessibles conforémement au RGAA et performantes.
 *
 * Ce logiciel est régi par la licence CeCILL v2.1 soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package hornet.framework.export.fdf;

/**
 * Projet hornetserver.
 * 
 * @author EffiTIC - gpi131530
 * @date 30 déc. 2009
 */
public class FDFBean {

    /** The siret1. */
    private FDFBeanSiret siret1;

    /** The siret2. */
    private FDFBeanSiret siret2;

    /**
     * Gets the siret1.
     * 
     * @return Returns the siret1.
     */
    public FDFBeanSiret getSiret1() {

        return this.siret1;
    }

    /**
     * Sets the siret1.
     * 
     * @param siret1
     *            The siret1 to set.
     */
    public void setSiret1(final FDFBeanSiret siret1) {

        this.siret1 = siret1;
    }

    /**
     * Gets the siret2.
     * 
     * @return Returns the siret2.
     */
    public FDFBeanSiret getSiret2() {

        return this.siret2;
    }

    /**
     * Sets the siret2.
     * 
     * @param siret2
     *            The siret2 to set.
     */
    public void setSiret2(final FDFBeanSiret siret2) {

        this.siret2 = siret2;
    }

}
