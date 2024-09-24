package pe.edu.cibertec.rueditas_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pe.edu.cibertec.rueditas_frontend.dto.RueditaRequestDTO;
import pe.edu.cibertec.rueditas_frontend.viewmodel.RueditaModel;

@Controller
@RequestMapping("/formulario")
public class RueditaController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/inicio")
    public ModelAndView inicioVehiculo () {

        return new ModelAndView("inicio", "error", "");
    }
    @PostMapping("/inicio")
    public ModelAndView inicioVehiculo (@RequestParam("placa") String placa){
        ModelAndView modelview = new ModelAndView("vehiculo");
        if(!StringUtils.hasText(placa) || placa.length() != 8){
            modelview.setViewName("inicio");
            modelview.addObject("error", "La placa ingresado es invalida");
            return modelview;
        }

        try {
            String URL = "http://localhost:8081/inicio";
            RueditaRequestDTO request = new RueditaRequestDTO(placa);
            ResponseEntity<RueditaModel> response = restTemplate.postForEntity(URL,request, RueditaModel.class);
            modelview.setViewName("vehiculo");
            modelview.addObject("vehiculo", response.getBody());
            modelview.addObject("error", "");
        } catch (HttpClientErrorException ex){
            if(HttpStatus.NOT_FOUND.equals(ex.getStatusCode())){
                modelview.setViewName("inicio");
                modelview.addObject("error", "Vehiculo con placa: " + placa + " no existe" );
            }
        } catch (ResourceAccessException rex){
            modelview.setViewName("inicio");
            modelview.addObject("error", "El servicio no esta operativo");
        }
        return modelview;
    }

}
