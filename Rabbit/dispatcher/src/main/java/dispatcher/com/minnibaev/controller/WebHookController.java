package dispatcher.com.minnibaev.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class WebHookController {

	private final UpdateProcessor updateProcessor;


	@RequestMapping(value = "/callback/update", method = RequestMethod.POST)
	public ResponseEntity<?> inUpdateReceived(@RequestBody Update update) {
		updateProcessor.processUpdate(update);
		return ResponseEntity.ok().build();
	}
}
