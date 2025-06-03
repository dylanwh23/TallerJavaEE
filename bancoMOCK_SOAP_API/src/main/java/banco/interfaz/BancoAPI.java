package banco.interfaz;

import banco.infrastructura.SOAP.IBancoSOAPEndpoint;

import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import java.net.URL;

public class BancoAPI {

    public String notificarDeposito(String comercio, int monto) {
        try {
            URL url = new URL("http://localhost:8080/bancoMOCK_SOAP_API-1.0-SNAPSHOT/BancoSOAPEndpointService?wsdl");
            QName qname = new QName("http://soap.infraestructura.banco.com/", "BancoSOAPEndpointService");

            Service service = Service.create(url, qname);
            IBancoSOAPEndpoint banco = service.getPort(IBancoSOAPEndpoint.class);

            return banco.notificarDeposito(comercio, monto);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR_CLIENTE";
        }
    }
}
