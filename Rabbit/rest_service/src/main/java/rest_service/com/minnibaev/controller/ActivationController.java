package rest_service.com.minnibaev.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest_service.com.minnibaev.service.UserActivationService;

@RequestMapping("/user")
@RestController
public class ActivationController {
	private final UserActivationService activationService;

	public ActivationController(UserActivationService activationService) {
		this.activationService = activationService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "activation")
	public ResponseEntity<?> activation(@RequestParam("id") String id) {
		boolean res = activationService.activation(id);
		if (res) {
			return ResponseEntity.ok().body("User has registrated");
		}
		return ResponseEntity.internalServerError().build();
	}
}
