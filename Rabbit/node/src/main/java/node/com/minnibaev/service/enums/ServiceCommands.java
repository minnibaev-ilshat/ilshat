package node.com.minnibaev.service.enums;

public enum ServiceCommands {

	HELP("/help"), START("/start"), CANCEL("/cancel"), REGISTRATION("/registration");

	private final String cmd;

	ServiceCommands(String cmd) {
		this.cmd = cmd;
	}

	public String toString() {
		return cmd;
	}

	public final boolean equals(String text) {
		return this.toString().equals(text);
	}

	public static ServiceCommands fromValue(String receivedMessage) {
		for (ServiceCommands c : ServiceCommands.values()) {
			if (receivedMessage.equals(c))
				return c;
		}
		return null;
	}
}
