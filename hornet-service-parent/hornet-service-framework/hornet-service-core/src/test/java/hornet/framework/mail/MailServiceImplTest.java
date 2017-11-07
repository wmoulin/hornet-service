/**
 * Copyright ou © ou Copr. Ministère de l'Europe et des Affaires étrangères (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * Ce logiciel est un programme informatique servant à faciliter la création
 * d'applications Web conformément aux référentiels généraux français : RGI, RGS et RGAA
 * <p/>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 * <p/>
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 * <p/>
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
 * <p/>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 * <p/>
 * <p/>
 * Copyright or © or Copr. Ministry for Europe and Foreign Affairs (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to facilitate creation of
 * web application in accordance with french general repositories : RGI, RGS and RGAA.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 */
package hornet.framework.mail;

import hornet.framework.exception.BusinessException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Classe de test de la classe de l'implémentation du service d'envoi de mail
 *
 * @author GOP - O. ROUSSEIL
 *
 */
public class MailServiceImplTest {

    /**
     * Test de conversion HTML -> Text
     */
    @Test
    public void conversionPlainText() {

        final String chaine =
                    "<p><i>titre</i></p><p>paragraphe1</p><p><b>paragraphe2<br/>saut de ligne</b></p>";
        final String chaineAttendu = "titre\n\nparagraphe1\n\nparagraphe2\nsaut de ligne";

        final MailServiceImpl instance = new MailServiceImpl(null, null, null);
        final String result = instance.preparerMessageTexte(chaine);

        Assert.assertEquals(chaineAttendu, result);
    }

    /**
     * Test adresse SMTP invalide
     */
    @Test
    public void adresseSMTPInvalide() {

        final JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost("fake.smtp.fr");

        final MailServiceImpl instance = new MailServiceImpl(jms, "hornetserver-core", null);
        try {
            instance.envoyer("aa@test.com", "sujet", "message", null, "o.rousseil@groupeonepoint.com");
        } catch (final BusinessException e) {
            Assert.assertTrue(e.getCause() instanceof MailSendException);
        } catch (final Exception e) {
            Assert.fail();
        }
    }

    /**
     * Test de la méthode 'preparerMessageTexte'
     */
    @Test
    public void preparerMessageTexteTest() {

        final MailServiceImpl instance = new MailServiceImpl(null, null, null);
        final String message = "test\nligne1<br/>ligne2<a>lien</a>";
        final String attendu = "test\nligne1\nligne2lien";
        final String resultat = instance.preparerMessageTexte(message);
        Assert.assertNotNull(resultat);
        Assert.assertEquals(attendu, resultat);

        // autres tests
        Assert.assertEquals("test",
            instance.preparerMessageTexte("<br  ><br/><br>test<br    /><br  \r\n  />"));
        Assert.assertEquals(
            "un paragraphe\n\nun autre §",
            instance.preparerMessageTexte("<p style='toto' class=\"hehe\"  >un paragraphe</p><p>un autre §</p>"));
        Assert.assertEquals("un paragraphe",
            instance.preparerMessageTexte("<p \r\n style='toto' class=\"hehe\"  >un paragraphe</p>"));
        Assert.assertEquals(
            "test\ntest2",
            instance.preparerMessageTexte("<br  ><br/><hr ><br><hr style='' />test<br    />test2<br  \r\n  />"));
        Assert.assertEquals("mon lien (http://www.google.fr)",
            instance.preparerMessageTexte("<a href=\"http://www.google.fr\" class='link' >mon lien</a >"));
        Assert.assertEquals("mon lien (http://www.google.fr)",
            instance.preparerMessageTexte("<a href='http://www.google.fr' class='link' >mon lien</a >"));
    }

}
