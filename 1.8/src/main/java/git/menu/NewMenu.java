package git.menu;



@SideOnly(Side.CLIENT)
public class NewMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random field_175374_h = new Random();
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v = true;
    private final Object threadLock = new Object();
    private String openGLWarning1;
    private String openGLWarning2;
    private String field_104024_v;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton realmsButton;
    private static final String __OBFID = "CL_00001154";
    
    public static ServerData server = new ServerData("Server1", ConfigVar.ServerAddress);
    public static ServerData server1 = new ServerData("Server2", ConfigVar.ServerAddress1);
    private static String updatemessage = getJsonString("update.html");
	private static String announcement = getJsonString("announcement.html");
	private final NewServerPinger oldServerPinger = new NewServerPinger();
    
    private GuiButton btns1;
    private GuiButton btns2;

    public NewMenu()
    {
        this.openGLWarning2 = field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            ArrayList arraylist = Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    arraylist.add(s);
                }
            }

            if (!arraylist.isEmpty())
            {
                do
                {
                    this.splashText = (String)arraylist.get(field_175374_h.nextInt(arraylist.size()));
                }
                while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ioexception1)
        {
            ;
        }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException ioexception)
                {
                    ;
                }
            }
        }

        this.updateCounter = field_175374_h.nextFloat();
        this.openGLWarning1 = "";
        //this.newServerPinger.pingPendingNetworks();
        /*if(!this.server.field_78841_f)
        {
        	this.server.field_78841_f = true;
        	this.server.pingToServer = -2L;
        	this.server.serverMOTD = "";
        	this.server.populationInfo = "";*/
        	field_148302_b.submit(new Runnable()
        	{
        		private static final String __OBFID = "CL_00000818";
        		public void run()
        		{
        			try
        			{
        				NewMenu.this.getOldServerPinger().ping(NewMenu.this.server);
        			}
        			catch (UnknownHostException unknownhostexception)
        			{
        				NewMenu.this.server.pingToServer = -1L;
        				NewMenu.this.server.serverMOTD = "§4§l网络错误";
        			}
        			catch (Exception exception)
        			{
        				NewMenu.this.server.pingToServer = -1L;
        				NewMenu.this.server.serverMOTD = "§4§l网络错误";
        			}
        		}
        	});
        //}
        FMLClientHandler.instance().setupServerList();
    	//FMLClientHandler.instance().connectToServer(this, new ServerData("Server", "skyfleet.cc:20024"));
    }

    public void updateScreen()
    {
        ++this.panoramaTimer;
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {}

    public void initGui()
    {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
        {
            this.splashText = "Notch生日快乐~";
        }
        else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "圣诞快乐~";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "新年快乐！";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "Yoooooooooooo！万圣节！";
        }

        boolean flag = true;
        int i = this.height / 4 + 48;

        if (this.mc.isDemo())
        {
            this.addDemoButtons(i, 24);
        }
        else
        {
            this.addSingleplayerMultiplayerButtons(i, 24);
        }

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(20, this.width / 2 - 124, i + 72 + 12));
        Object object = this.threadLock;

        synchronized (this.threadLock)
        {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int j = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - j) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + j;
            this.field_92019_w = this.field_92021_u + 24;
        }
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {
    	if (ConfigVar.IsTwoIP)
    	{
    		this.buttonList.add(this.btns1 = new GuiButton(21, this.width / 2 + 2, p_73969_1_, 98, 20,I18n.format("服务器入口I", new Object[0])));
    		this.buttonList.add(this.btns2 = new GuiButton(22, this.width / 2 + 2, p_73969_1_ + p_73969_2_* 1, 98, 20, I18n.format("服务器入口II", new Object[0])));
    		//this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
    		//this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
    		this.buttonList.add(new GuiButton(1, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("单人游戏", new Object[0])));
    		if (isOutdate() && ConfigVar.onlinecheck)
    		{
    			this.openGLWarning1 = I18n.format("§c§l您使用的客户端版本已过期...", new Object[0]);
    			this.openGLWarning2 = I18n.format("§a§l检测到新版本 §e§l请点击此更新", new Object[0]);
    			this.field_104024_v = ConfigVar.downloads;
    			this.btns1.enabled = false;
    			this.btns2.enabled = false;
    		}
    		//this.buttonList.add(new GuiButton(6, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
    	}
    	else
    	{
    		this.buttonList.add(this.btns1 = new GuiButton(21, this.width / 2 + 2, p_73969_1_, 98, 20,I18n.format("进入服务器", new Object[0])));
    		//this.buttonList.add(this.btns2 = new GuiButton(22, this.width / 2 + 2, p_73969_1_ + p_73969_2_* 1, 98, 20, I18n.format("\u5907\u7528\u5165\u53E3", new Object[0])));
    		//this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
    		//this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
    		this.buttonList.add(new GuiButton(1, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 1, 98, 20, I18n.format("单人游戏", new Object[0])));
    		if (isOutdate() && ConfigVar.onlinecheck)
    		{
    			this.openGLWarning1 = I18n.format("§c§l您使用的客户端版本已过期...", new Object[0]);
    			this.openGLWarning2 = I18n.format("§a§l检测到新版本 §e§l请点击此更新", new Object[0]);
    			this.field_104024_v = ConfigVar.downloads;
    			this.btns1.enabled = false;
    			//this.btns2.enabled = false;
    		}
    		this.buttonList.add(new GuiButton(6, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
    		//GuiButton fmlModButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("fml.menu.mods"));
    		//fmlModButton.xPosition = this.width / 2 + 2;
    		//realmsButton.width = 98;
    		//fmlModButton.width = 98;
    		//this.buttonList.add(fmlModButton);
    	}
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_)
    {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 14 && this.realmsButton.visible)
        {
            this.switchToRealms();
        }

        if (button.id == 4)
        {
            this.mc.shutdown();
        }

        if (button.id == 6)
        {
            this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
        }

        if (button.id == 11)
        {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
        
        if (button.id == 20)
        {
        	this.getOldServerPinger().clearPendingNetworks();
        	field_148302_b.submit(new Runnable()
            {
                private static final String __OBFID = "CL_00000818";
                public void run()
                {
                    try
                    {
                    	NewMenu.this.getOldServerPinger().ping(NewMenu.this.server);
                    }
                    catch (UnknownHostException var2)
                    {
                    	NewMenu.this.server.pingToServer = -1L;
                    	NewMenu.this.server.serverMOTD = "§4§l网络错误";
                    }
                    catch (Exception var3)
                    {
                    	NewMenu.this.server.pingToServer = -1L;
                    	NewMenu.this.server.serverMOTD = "§4§l网络错误";
                    }
                }
            });
        	FMLClientHandler.instance().setupServerList();
        }
        
        if (button.id == 21)
        {
        	this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
        }
        
        if (button.id == 22)
        {
        	this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server1));
        }
    }

    private void switchToRealms()
    {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result && id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13)
        {
            if (result)
            {
                try
                {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(this.field_104024_v)});
                }
                catch (Throwable throwable)
                {
                    logger.error("Couldn\'t open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; ++k)
        {
            GlStateManager.pushMatrix();
            float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64.0F;
            float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GlStateManager.translate(f1, f2, f3);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l)
            {
                GlStateManager.pushMatrix();

                if (l == 1)
                {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3)
                {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4)
                {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5)
                {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                worldrenderer.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f4), (double)(0.0F + f4));
                worldrenderer.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f4), (double)(0.0F + f4));
                worldrenderer.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f4), (double)(1.0F - f4));
                worldrenderer.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f4), (double)(1.0F - f4));
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        GlStateManager.disableAlpha();
        byte b0 = 3;

        for (int i = 0; i < b0; ++i)
        {
            worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float)(i - b0 / 2) / 256.0F;
            worldrenderer.addVertexWithUV((double)j, (double)k, (double)this.zLevel, (double)(0.0F + f1), 1.0D);
            worldrenderer.addVertexWithUV((double)j, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 1.0D);
            worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 0.0D);
            worldrenderer.addVertexWithUV(0.0D, (double)k, (double)this.zLevel, (double)(0.0F + f1), 0.0D);
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f2 = (float)this.height * f1 / 256.0F;
        float f3 = (float)this.width * f1 / 256.0F;
        worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        worldrenderer.addVertexWithUV(0.0D, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F + f3));
        worldrenderer.addVertexWithUV((double)k, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F - f3));
        worldrenderer.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F - f3));
        worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F + f3));
        tessellator.draw();
    }
    
    public String online, ping;
	public static String maxplayer;
	public static String onlineplayer;
    
    private void drawAnnouncement() {
		if (field_148302_b != null) {
			online = server.serverMOTD;
			ping = "§a§l" + server.pingToServer;
			//maxplayer = server.maxplayer;
			//onlineplayer = server.onlineplayer;
		} else {
			ping = "§4§l0";
		}
	}

    public static int Alocation = 0;
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {	
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        byte b0 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double)this.updateCounter < 1.0E-4D)
        {
            this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
            this.drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
            this.drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
            this.drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
            this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
        }
        else
        {
            this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
            this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
        }

        worldrenderer.setColorOpaque_I(-1);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f1, f1, f1);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.8";

        if (this.mc.isDemo())
        {
            s = s + " Demo";
        }

        java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
        for (int i = 0; i < brandings.size(); i++)
        {
            String brd = brandings.get(i);
            if (!com.google.common.base.Strings.isNullOrEmpty(brd))
            {
                this.drawString(this.fontRendererObj, brd, 2, this.height - ( 10 + i * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215);
            }
        }
        //net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, this.fontRendererObj, this.width, this.height);
        //String s1 = "Copyright Mojang AB. Do not distribute!";
        //this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);

        String var11 = "§bMojang AB.版权所有";
        this.drawString(this.fontRendererObj, var11, this.width - this.fontRendererObj.getStringWidth(var11) - 2, this.height - 10, -1);
        this.drawString(this.fontRendererObj, "§eUI制作BakaSea(SPE_HaiKuo)?", this.width - this.fontRendererObj.getStringWidth("§eUI制作BakaSea(SPE_HaiKuo)?") - 2, this.height - 20, -1);

        drawRect(0,0,800,12,0x80000000);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestring = "§e"+time.format(new Date());
		this.drawString(this.fontRendererObj, timestring, 1, 2, -1);

		if (ConfigVar.announcementcheck == true)
		{
			ConfigVar.announcement = this.Getannouncement();
		}
		if (ConfigVar.announcementmove == true)
		{
			AMove am = new AMove(this.width);
			am.start();
			this.drawString(this.fontRendererObj, ConfigVar.announcement, this.Alocation, 2, -1);
		} else {this.drawString(this.fontRendererObj, ConfigVar.announcement, (this.width / 2 - ConfigVar.announcement.length() * 3), 2, -1);}
		//this.getOldServerPinger().clearPendingNetworks();
		
        int varinfow = this.width / 2 - 119;
        int varinfoh = this.height / 4 + 48;
        drawAnnouncement();
        this.drawGradientRect(varinfow - 4, varinfoh, varinfow + 117, varinfoh + 70, 1610612736, 1610612736);
        this.drawString(this.fontRendererObj, "§e============公告============", varinfow, varinfoh,
                        16777215);
        this.drawString(this.fontRendererObj, "§e服务器状态: " + online, varinfow,
                        varinfoh + 12, 16777215);
        this.drawString(this.fontRendererObj, "§e在线人数: " + onlineplayer, varinfow,
                        varinfoh + 24, 16777215);
        this.drawString(this.fontRendererObj, "§e最大人数: " + maxplayer, varinfow,
                        varinfoh + 36, 16777215);
        this.drawString(this.fontRendererObj, "§e与服务器延迟: " + ping, varinfow,
                        varinfoh + 48, 16777215);
        this.drawString(this.fontRendererObj, "§e祝您游戏愉快'_>'", varinfow,
                        varinfoh + 60, 16777215);
        
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object object = this.threadLock;

        synchronized (this.threadLock)
        {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }
    
    public NewServerPinger getOldServerPinger()
    {
        return this.oldServerPinger;
    }
    
    public static String getJsonString(String file) {
		if (ConfigVar.onlinecheck)
		{
			URL url;
			String temp;
			StringBuffer sb = new StringBuffer();
			try 
			{
				url = new URL(ConfigVar.url + file);
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
				while ((temp = in.readLine()) != null) {
					sb.append(temp);
				}
				in.close();
			} 
			catch (final MalformedURLException me) 
			{
				me.getMessage();
				return "offline";
			} 
			catch (final IOException e) 
			{
				e.printStackTrace();
				return "offline";
			}
			return sb.toString();
		}
		return "offline";
	}
    
    public static boolean isOutdate() {
		if (!updatemessage.equalsIgnoreCase("offline")) {
			JSONObject jsonobj = new JSONObject(updatemessage);
			double newversion = jsonobj.getDouble("Version");
			if (newversion > ConfigVar.version) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
    
    public static String Getannouncement() {
    	if (!announcement.equalsIgnoreCase("offline")){
			JSONObject jsonobj1 = new JSONObject(announcement);
			return jsonobj1.getString("Announcement");
		} else {
			return "§4网络出错";
		}
    }
}

class AMove extends Thread
{
	public AMove(int width) 
	{
		try 
		{
	        Thread.sleep(25);
	        if (NewMenu.Alocation >= width)
	        {
	        	NewMenu.Alocation = 0;
	        }
	        NewMenu.Alocation++;
		} catch (InterruptedException e){
	        e.printStackTrace();
		}
	}
}