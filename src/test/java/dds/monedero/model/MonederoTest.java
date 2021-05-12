package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class,
            () -> cuenta.poner(-1500),
            "-1500 : el monto a ingresar debe ser un valor positivo");
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(), 1500+456+1900);
  }

  @Test
  void MasDeTresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertThrows(MaximaCantidadDepositosException.class,
            () -> cuenta.poner(245),
      "Ya excedio los 3 depositos diarios");
  }

  @Test
  void ExtraerMasQueElSaldo() {
    cuenta.poner(90);
    assertThrows(SaldoMenorException.class,
            () -> cuenta.sacar(1001),
            "No puede sacar mas de " + cuenta.getSaldo() + " $");
  }

  @Test
  public void ExtraerMasDe1000() {
    cuenta.poner(5000);
    assertThrows(MaximoExtraccionDiarioException.class,
            () -> cuenta.sacar(1001),
            "No puede extraer mas de $ " + 1000
                    + " diarios, límite: " + (1000 - cuenta.getMontoExtraidoA(LocalDate.now())));
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class,
            () -> cuenta.sacar(-500),
            -500 + ": el monto a ingresar debe ser un valor positivo");
  }

}