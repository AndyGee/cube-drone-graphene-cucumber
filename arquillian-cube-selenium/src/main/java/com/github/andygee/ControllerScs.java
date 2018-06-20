package com.github.andygee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;



@Controller
public class ControllerScs {
	
	@RequestMapping(ConfigurationScs.BASE_PATH)
	public ModelAndView serveHome(final Map<String, Object> model) {

		model.put("basePath", ConfigurationScs.BASE_PATH);

		return new ModelAndView("home", model);
	}
	
	
	/**
	 * This is a dev helper. All requests should start with the {@value ConfigurationScs#BASE_PATH}.
	 *
	 * @param model the model
	 * @return the model and view
	 */
	@RequestMapping("/")
	public ModelAndView serveRoot(final Map<String, Object> model) {
		
		model.put("basePath", ConfigurationScs.BASE_PATH);
		
		return new ModelAndView("root", model);
	}

	@RequestMapping("/scs/login")
	public String serveLogin() {
		return "login";
	}
}
