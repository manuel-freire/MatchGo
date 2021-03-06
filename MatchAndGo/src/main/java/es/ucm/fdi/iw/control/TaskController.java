package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Evaluation;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Tags;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("tag")
public class TaskController {
	
	private static final Logger log = LogManager.getLogger(TaskController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@PostMapping(path = "/listTags/{id}", produces = "application/json")
	@Transactional
	@ResponseBody
	public String listTagUser (final HttpSession session, @PathVariable long id) {

		log.warn("Entra en listagUser");
		User u = entityManager.find(User.class, id);
		final List<Tags> user_tags = new ArrayList<>(u.getTags()); 
		List<String> alltagsUser = new ArrayList<>();
	/*	for(Tags t: user_tags) {
			alltagsUser.add(t.toString());
		}*/
	
		
		
		log.warn("Manda la lista de tags del user");
		return user_tags.get(0).toString();
	}


	
}
