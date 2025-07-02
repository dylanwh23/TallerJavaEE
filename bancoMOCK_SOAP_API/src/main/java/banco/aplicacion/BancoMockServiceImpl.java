package banco.aplicacion;


import jakarta.jws.WebService;
import java.util.Random;

@WebService
public class BancoMockServiceImpl {

    public String procesarDeposito(String comercio, int monto) {

        return "APROBADO";
    }
}