package banco.infrastructura.SOAP;

import banco.interfaz.BancoAPI;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/notificarDeposito")
public class NotificarDepositoServlet extends HttpServlet {

    private BancoAPI bancoAPI;

    @Override
    public void init() throws ServletException {
        super.init();
        bancoAPI = new BancoAPI(); // instancia tu cliente SOAP

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener parámetros del request ( formulario)
        String comercio = req.getParameter("comercio");
        String montoStr = req.getParameter("monto");

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (comercio == null || montoStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Faltan parámetros comercio o monto\"}");
            return;
        }

        try {
            int monto = Integer.parseInt(montoStr);
            String resultado = bancoAPI.notificarDeposito(comercio, monto);
            out.print( resultado );
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Monto debe ser un número entero\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}