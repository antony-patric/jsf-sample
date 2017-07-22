package ws.client;

/**
 * Created by dev on 15/05/17.
 */
import javax.net.ssl.*;
import javax.xml.soap.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;

import static java.security.Security.*;

public class SOAPClientSAAJ {

    static {
        setProperty("jdk.tls.disabledAlgorithms", "");
    }


    public static void main(String args[]) throws Exception {

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket soc = (SSLSocket) factory.createSocket();

        // Returns the names of the protocol versions which are
        // currently enabled for use on this connection.
        String[] protocols = soc.getEnabledProtocols();

        System.out.println("Enabled protocols:");
        for (String s : protocols) {
            System.out.println(s);
        }

        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
//        String url = "http://ws.cdyne.com/emailverify/Emailvernotestemail.asmx";
        String url = "https://www-devbankval.allianz.co.uk";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

        // print SOAP Response
        System.out.print("Response SOAP Message:");
        soapResponse.writeTo(System.out);

        soapConnection.close();
    }

    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "https://www-devbankval.allianz.co.uk";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("example", serverURI);

        /*
        Constructed SOAP Request Message:
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
                <example:VerifyEmail>
                    <example:email>mutantninja@gmail.com</example:email>
                    <example:LicenseKey>123</example:LicenseKey>
                </example:VerifyEmail>
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
         */

        String wsRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsb=\"http://www-schema/WSBankDetailsLookup/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <wsb:GETBANKDETAILSREQUEST>\n" +
                "         <ACAH>\n" +
                "            <COMPLETIONCODE>0</COMPLETIONCODE>\n" +
                "            <REASONCODE></REASONCODE>\n" +
                "            <REASONTEXT></REASONTEXT>\n" +
                "            <SERVICENAME>paris.CBCVS.1.verifybankdetailsrequest.anyadditionalinfo</SERVICENAME>\n" +
                "         </ACAH>\n" +
                "         <ACSH>\n" +
                "            <USERID>d19107</USERID>\n" +
                "            <ROLEID>tester</ROLEID>\n" +
                "         </ACSH>\n" +
                "         <ACXMLBODY>\n" +
                "            <BANKDETAILSREQUEST>\n" +
                "               <BANKDETAILSREQUESTRECORD>\n" +
                "                  <SORTCODE>090128</SORTCODE>\n" +
                "                  <ACCOUNTNUMBER>05564234</ACCOUNTNUMBER>\n" +
                "               </BANKDETAILSREQUESTRECORD>\n" +
                "            </BANKDETAILSREQUEST>\n" +
                "         </ACXMLBODY>\n" +
                "      </wsb:GETBANKDETAILSREQUEST>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.setValue(wsRequest);
        /*SOAPElement soapBodyElem = soapBody.addChildElement("VerifyEmail", "example");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("email", "example");
        soapBodyElem1.addTextNode("mutantninja@gmail.com");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("LicenseKey", "example");
        soapBodyElem2.addTextNode("123");
*/
        /*MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "VerifyEmail");*/

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
