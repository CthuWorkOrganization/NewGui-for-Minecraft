package git.menu;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
