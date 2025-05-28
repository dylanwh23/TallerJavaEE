package banco.infrastructura.SOAP;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://soap.infraestructura.banco.com/")
public interface IBancoSOAPEndpoint {
    @WebMethod
    String notificarDeposito(
            @WebParam(name = "comercio") String comercio,
            @WebParam(name = "monto") int monto
    );
}
