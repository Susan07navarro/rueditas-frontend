package pe.edu.cibertec.rueditas_frontend.viewmodel;

import java.math.BigDecimal;

public record RueditaModel(String marca, String modelo, Integer numeroAsiento,
                           BigDecimal precio,String color) {
}
