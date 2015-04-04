package git.menu;



@SideOnly(Side.CLIENT)
public class NewServerPinger
{
    private static final Splitter field_147230_a = Splitter.on('\u0000').limit(6);
    private static final Logger logger = LogManager.getLogger();
    private final List field_147229_c = Collections.synchronizedList(new ArrayList());
    private static final String __OBFID = "CL_00000892";

    public void func_147224_a(final ServerData p_147224_1_) throws UnknownHostException
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(p_147224_1_.serverIP);
        final NetworkManager networkmanager = NetworkManager.provideLanClient(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort());
        this.field_147229_c.add(networkmanager);
        p_147224_1_.serverMOTD = "§e§l加载中...";
        p_147224_1_.pingToServer = -1L;
        p_147224_1_.field_147412_i = null;
        networkmanager.setNetHandler(new INetHandlerStatusClient()
        {
            private boolean field_147403_d = false;
            private static final String __OBFID = "CL_00000893";
            public void handleServerInfo(S00PacketServerInfo p_147397_1_)
            {
                ServerStatusResponse serverstatusresponse = p_147397_1_.func_149294_c();

                if (serverstatusresponse.func_151317_a() != null)
                {
                    //p_147224_1_.serverMOTD = serverstatusresponse.func_151317_a().getFormattedText();
                	p_147224_1_.serverMOTD = "§a§l在线";
                }
                else
                {
                    p_147224_1_.serverMOTD = "§4§l关闭";
                }

                if (serverstatusresponse.func_151322_c() != null)
                {
                    p_147224_1_.gameVersion = serverstatusresponse.func_151322_c().func_151303_a();
                    p_147224_1_.field_82821_f = serverstatusresponse.func_151322_c().func_151304_b();
                }
                else
                {
                    p_147224_1_.gameVersion = "Old";
                    p_147224_1_.field_82821_f = 0;
                }

                if (serverstatusresponse.func_151318_b() != null)
                {
                    p_147224_1_.populationInfo = EnumChatFormatting.GRAY + "" + serverstatusresponse.func_151318_b().func_151333_b() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + serverstatusresponse.func_151318_b().func_151332_a();
                    NewMenu.maxplayer = "§a§l" + serverstatusresponse.func_151318_b().func_151332_a();
                	NewMenu.onlineplayer = "§a§l" + serverstatusresponse.func_151318_b().func_151333_b();

                    if (ArrayUtils.isNotEmpty(serverstatusresponse.func_151318_b().func_151331_c()))
                    {
                        StringBuilder stringbuilder = new StringBuilder();
                        GameProfile[] agameprofile = serverstatusresponse.func_151318_b().func_151331_c();
                        int i = agameprofile.length;

                        for (int j = 0; j < i; ++j)
                        {
                            GameProfile gameprofile = agameprofile[j];

                            if (stringbuilder.length() > 0)
                            {
                                stringbuilder.append("\n");
                            }

                            stringbuilder.append(gameprofile.getName());
                        }

                        if (serverstatusresponse.func_151318_b().func_151331_c().length < serverstatusresponse.func_151318_b().func_151333_b())
                        {
                            if (stringbuilder.length() > 0)
                            {
                                stringbuilder.append("\n");
                            }

                            stringbuilder.append("... and ").append(serverstatusresponse.func_151318_b().func_151333_b() - serverstatusresponse.func_151318_b().func_151331_c().length).append(" more ...");
                        }

                        p_147224_1_.field_147412_i = stringbuilder.toString();
                    }
                }
                else
                {
                    p_147224_1_.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
                    NewMenu.maxplayer = "§4§l0";
                    NewMenu.onlineplayer = "§4§l0";
                }

                if (serverstatusresponse.func_151316_d() != null)
                {
                    String s = serverstatusresponse.func_151316_d();

                    if (s.startsWith("data:image/png;base64,"))
                    {
                        p_147224_1_.func_147407_a(s.substring("data:image/png;base64,".length()));
                    }
                    else
                    {
                        NewServerPinger.logger.error("Invalid server icon (unknown format)");
                    }
                }
                else
                {
                    p_147224_1_.func_147407_a((String)null);
                }

                FMLClientHandler.instance().bindServerListData(p_147224_1_, serverstatusresponse);
                networkmanager.scheduleOutboundPacket(new C01PacketPing(Minecraft.getSystemTime()), new GenericFutureListener[0]);
                this.field_147403_d = true;
            }
            public void handlePong(S01PacketPong p_147398_1_)
            {
                long i = p_147398_1_.func_149292_c();
                long j = Minecraft.getSystemTime();
                p_147224_1_.pingToServer = j - i;
                networkmanager.closeChannel(new ChatComponentText("Finished"));
            }
            /**
             * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
             */
            public void onDisconnect(IChatComponent p_147231_1_)
            {
                if (!this.field_147403_d)
                {
                    NewServerPinger.logger.error("Can\'t ping " + p_147224_1_.serverIP + ": " + p_147231_1_.getUnformattedText());
                    //p_147224_1_.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    p_147224_1_.serverMOTD = "§4§l关闭";
                    p_147224_1_.populationInfo = "";
                    NewMenu.onlineplayer = "§4§l0";
                    NewMenu.maxplayer = "§4§l0";
                    NewServerPinger.this.func_147225_b(p_147224_1_);
                }
            }
            /**
             * Allows validation of the connection state transition. Parameters: from, to (connection state). Typically
             * throws IllegalStateException or UnsupportedOperationException if validation fails
             */
            public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_)
            {
                if (p_147232_2_ != EnumConnectionState.STATUS)
                {
                    throw new UnsupportedOperationException("Unexpected change in protocol to " + p_147232_2_);
                }
            }
            /**
             * For scheduled network tasks. Used in NetHandlerPlayServer to send keep-alive packets and in
             * NetHandlerLoginServer for a login-timeout
             */
            public void onNetworkTick() {}
        });

        try
        {
            networkmanager.scheduleOutboundPacket(new C00Handshake(5, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS), new GenericFutureListener[0]);
            networkmanager.scheduleOutboundPacket(new C00PacketServerQuery(), new GenericFutureListener[0]);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable);
        }
    }

    private void func_147225_b(final ServerData p_147225_1_)
    {
        final ServerAddress serveraddress = ServerAddress.func_78860_a(p_147225_1_.serverIP);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(NetworkManager.eventLoops)).handler(new ChannelInitializer()
        {
            private static final String __OBFID = "CL_00000894";
            protected void initChannel(Channel p_initChannel_1_)
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                }
                catch (ChannelException channelexception1)
                {
                    ;
                }

                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                }
                catch (ChannelException channelexception)
                {
                    ;
                }

                p_initChannel_1_.pipeline().addLast(new ChannelHandler[] {new SimpleChannelInboundHandler()
                {
                    private static final String __OBFID = "CL_00000895";
                    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
                    {
                        super.channelActive(p_channelActive_1_);
                        ByteBuf bytebuf = Unpooled.buffer();

                        try
                        {
                            bytebuf.writeByte(254);
                            bytebuf.writeByte(1);
                            bytebuf.writeByte(250);
                            char[] achar = "MC|PingHost".toCharArray();
                            bytebuf.writeShort(achar.length);
                            char[] achar1 = achar;
                            int i = achar.length;
                            int j;
                            char c0;

                            for (j = 0; j < i; ++j)
                            {
                                c0 = achar1[j];
                                bytebuf.writeChar(c0);
                            }

                            bytebuf.writeShort(7 + 2 * serveraddress.getIP().length());
                            bytebuf.writeByte(127);
                            achar = serveraddress.getIP().toCharArray();
                            bytebuf.writeShort(achar.length);
                            achar1 = achar;
                            i = achar.length;

                            for (j = 0; j < i; ++j)
                            {
                                c0 = achar1[j];
                                bytebuf.writeChar(c0);
                            }

                            bytebuf.writeInt(serveraddress.getPort());
                            p_channelActive_1_.channel().writeAndFlush(bytebuf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        }
                        finally
                        {
                            bytebuf.release();
                        }
                    }
                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_)
                    {
                        short short1 = p_channelRead0_2_.readUnsignedByte();

                        if (short1 == 255)
                        {
                            String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
                            String[] astring = (String[])Iterables.toArray(NewServerPinger.field_147230_a.split(s), String.class);

                            if ("\u00a71".equals(astring[0]))
                            {
                                int i = MathHelper.parseIntWithDefault(astring[1], 0);
                                String s1 = astring[2];
                                String s2 = astring[3];
                                int j = MathHelper.parseIntWithDefault(astring[4], -1);
                                int k = MathHelper.parseIntWithDefault(astring[5], -1);
                                p_147225_1_.field_82821_f = -1;
                                p_147225_1_.gameVersion = s1;
                                p_147225_1_.serverMOTD = s2;
                                p_147225_1_.populationInfo = EnumChatFormatting.GRAY + "" + j + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + k;
                            }
                        }

                        p_channelRead0_1_.close();
                    }
                    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_)
                    {
                        p_exceptionCaught_1_.close();
                    }
                    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_)
                    {
                        this.channelRead0(p_channelRead0_1_, (ByteBuf)p_channelRead0_2_);
                    }
                }
                                                                         });
            }
        })).channel(NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public void func_147223_a()
    {
        List list = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator iterator = this.field_147229_c.iterator();

            while (iterator.hasNext())
            {
                NetworkManager networkmanager = (NetworkManager)iterator.next();

                if (networkmanager.isChannelOpen())
                {
                    networkmanager.processReceivedPackets();
                }
                else
                {
                    iterator.remove();

                    if (networkmanager.getExitMessage() != null)
                    {
                        networkmanager.getNetHandler().onDisconnect(networkmanager.getExitMessage());
                    }
                }
            }
        }
    }

    public void func_147226_b()
    {
        List list = this.field_147229_c;

        synchronized (this.field_147229_c)
        {
            Iterator iterator = this.field_147229_c.iterator();

            while (iterator.hasNext())
            {
                NetworkManager networkmanager = (NetworkManager)iterator.next();

                if (networkmanager.isChannelOpen())
                {
                    iterator.remove();
                    networkmanager.closeChannel(new ChatComponentText("Cancelled"));
                }
            }
        }
    }
}