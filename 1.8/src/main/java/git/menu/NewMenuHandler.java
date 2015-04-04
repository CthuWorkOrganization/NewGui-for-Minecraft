package git.menu;



public class NewMenuHandler {
	public static NewMenuHandler instance = new NewMenuHandler();
	private ServerData field_146811_z;
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void openMainMenu(GuiOpenEvent event) throws IOException {
		if (event.gui instanceof GuiMainMenu) {
			event.gui = new NewMenu();
		}
	}
}
