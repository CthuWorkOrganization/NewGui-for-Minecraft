package git.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.config.*;

@Mod(modid = "New Gui")
public class Menu {
	@Mod.Instance("Menu")
	public static int ALocation;
	public static Menu instance;
	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void preInit(FMLPreInitializationEvent e) {
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
		//读取配置文件
		ConfigVar.onlinecheck = config.get("Online", "VersionCheck", false).getBoolean();
		ConfigVar.version = config.get("Online", "Version", 1.0).getDouble();
		ConfigVar.announcementcheck = config.get("Online", "zAnnouncementCheck", false).getBoolean();
		ConfigVar.url = config.get("Online", "Url", "http://").getString();
		ConfigVar.downloads = config.get("Online", "Downloads", "http://").getString();
		
		ConfigVar.IsTwoIP = config.get("Server", "1sTwoAddress", false).getBoolean();
		ConfigVar.ServerAddress = config.get("Server", "Address1", "127.0.0.1").getString();
		ConfigVar.ServerAddress1 = config.get("Server", "Address2", "127.0.0.1").getString();
		ConfigVar.Captain = config.get("Server", "Captain", "Minecraft 1.8").getString();
		ConfigVar.announcementmove = config.get("Server", "AnnouncementMove", false).getBoolean();
		ConfigVar.announcement = config.get("Server", "Announcement", "").getString();
		//结束读取
		config.save();
		String title = ConfigVar.Captain;
		Display.setTitle(title);
		MinecraftForge.EVENT_BUS.register(NewMenuHandler.instance);
	}
}
