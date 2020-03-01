package es.ucm.fdi.iw.matchandgo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.matchandgo.mensajes.Mensaje;
import es.ucm.fdi.iw.matchandgo.model.Test;


@Controller
public class RootController {

    private static Logger log = LogManager.getLogger(RootController.class);
    
    @Autowired
    private EntityManager entityManager;


    // Crea unos chats de ejemplo para la vista "mensajes"
    public Mensaje[] generarChatPorDefecto() {
        Mensaje[] res = new Mensaje[2];

        // Creamos el chat de ejemplo
        res[0] = new Mensaje("Buenas tardes!", "Rodolfo");
        res[1] = new Mensaje("Hombre, cuanto tiempo!", "Laura");

        return res;
    }

    @GetMapping("/mensajes")
    public String mostrarMensajes(Model model, HttpSession session) {

        Mensaje[] mensajes = generarChatPorDefecto();
        String[] contactos = new String[2];
        contactos[0] = "Laura";
        contactos[1] = "Samuel";

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("contactos", contactos);
        model.addAttribute("contacto", "Laura");
        session.setAttribute("usuario", "Rodolfo");

        return "mensajes";
    }
  
    @GetMapping("/busqueda")
    public String getMethodName(Model model, HttpSession session) {
        return "busqueda";
    }
    
    @Transactional
    @GetMapping("/revisar") 
	public String index(
			Model model 
			) { 
			model.addAttribute("titulo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."); // escribe en modelo
			model.addAttribute("descripcion", "Orci a scelerisque purus semper eget duis at. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus.");
			model.addAttribute("tags", new ArrayList<String>() {{
				add("tag");
				add("tag1");
				add("tag2");
				add("tag3");
				add("tag4");
				add("tag5");
			}});
			model.addAttribute("listnum", new ArrayList<Integer>() {{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
			}});
			
			Test t = new Test(1, "test");
			entityManager.persist(t);
			entityManager.flush();
			List<Test> us = entityManager.createNativeQuery("select * from test").getResultList();
			System.out.println(us);
			
			return "matchAndGoVistaModerador"; 
	}
    
    @GetMapping("/evento") 
	public String showEvent(
			Model model // comunicación con vist
			) { // viene del formulario
			return "matchAndGoEvento"; // vista resultante
	}
    
    @GetMapping("/profile") 
   	public String profile(
			   Model model // comunicación con vist
			   , HttpSession session
			   ) { // viene del formulario
				
        	model.addAttribute("session.user", "patata");
   			return "profile"; // vista resultante
   	}
    
    @GetMapping("/admin") 
	public String admin(
			Model model // comunicación con vist
			) { // viene del formulario
			return "admin_view"; // vista resultante
	}
    
    
}