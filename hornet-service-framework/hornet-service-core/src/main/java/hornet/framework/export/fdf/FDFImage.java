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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

/**
 * Image pour formulaire FDF Projet hornetserver.
 * 
 * @date 30 déc. 2009
 * 
 */
public class FDFImage {

    /**
     * Binaries data for image file
     */
    private final transient byte[] data;

    /**
     * true if the image should be resized<br />
     * 
     * <br />
     * indique si l'image doit être adaptée au cadre du champ (true) ou si les dimensions de l'images doivent
     * être conservées. true par défaut
     */
    private boolean fit = true;

    /**
     * Constructor of FDFimage with image in byte array
     * 
     * @param data
     *            byte
     */
    public FDFImage(
                final byte[] data) {

        if (data == null) {
            this.data = new byte[0];
        } else {
            this.data = Arrays.copyOf(data, data.length);
        }
    }

    /**
     * Constructor of FDFimage with URL for image
     * 
     * @param url
     *            URL
     * @throws IOException
     *             IOException from openStream()
     */
    public FDFImage(
                final URL url) throws IOException {

        this(url.openStream());
    }

    /**
     * Constructor of FDFimage with InputStream for image
     * 
     * @param inStream
     *            InputStream
     * @throws IOException
     *             IOException from read()
     */
    public FDFImage(
                final InputStream inStream) throws IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int aByte;
        while ((aByte = inStream.read()) != -1) {
            baos.write(aByte);
        }

        this.data = baos.toByteArray();
    }

    /**
     * Getter of binaries data for image file
     * 
     * @return Returns the data.
     */
    public byte[] getData() {

        return this.data.clone();
    }

    /**
     * Getter of image should be resized
     * 
     * @param fit
     *            The fit to set.
     */
    public void setFit(final boolean fit) {

        this.fit = fit;
    }

    /**
     * True if the image should be resized
     * 
     * @return Returns the fit.
     */
    public boolean isFit() {

        return this.fit;
    }

}
