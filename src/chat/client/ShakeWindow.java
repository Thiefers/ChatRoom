package chat.client;

public class ShakeWindow implements Runnable {

	ChatFrame chatFrame;

	public ShakeWindow() {
	}

	public ShakeWindow(ChatFrame chatFrame) {
		this.chatFrame = chatFrame;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 2; i++) {
				chatFrame.setLocation(chatFrame.getX() - 28, chatFrame.getY());
				Thread.sleep(88);
				chatFrame.setLocation(chatFrame.getX(), chatFrame.getY() - 28);
				Thread.sleep(88);
				chatFrame.setLocation(chatFrame.getX() + 28, chatFrame.getY());
				Thread.sleep(88);
				chatFrame.setLocation(chatFrame.getX(), chatFrame.getY() + 28);
				Thread.sleep(88);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
