package banco.infrastructura.SOAP;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(
        serviceName = "BancoSOAPEndpointService",
        portName = "BancoSOAPEndpointPort",
        targetNamespace = "http://soap.infraestructura.banco.com/",
        endpointInterface = "banco.infrastructura.SOAP.IBancoSOAPEndpoint"
)
public class BancoSOAPEndpoint implements IBancoSOAPEndpoint {

    private final banco.aplicacion.BancoMockServiceImpl bancoService = new banco.aplicacion.BancoMockServiceImpl();

    @Override
    public String notificarDeposito(String comercio, int monto) {
        return bancoService.procesarDeposito(comercio, monto);
    }
}
