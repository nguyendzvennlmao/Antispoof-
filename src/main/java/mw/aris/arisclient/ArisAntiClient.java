package mw.aris.arisclient;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import mw.aris.arisclient.manager.BrandCheckManager;
import mw.aris.arisclient.utils.DiscordWebhook;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.charset.StandardCharsets;

public class ArisAntiClient extends JavaPlugin implements PacketListener {

    private static ArisAntiClient instance;
    private BrandCheckManager brandCheckManager;
    private DiscordWebhook discordWebhook;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        this.brandCheckManager = new BrandCheckManager(this);
        this.discordWebhook = new DiscordWebhook(this);
        
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage wrapper = new WrapperPlayClientPluginMessage(event);
            String channel = wrapper.getChannelName();

            if (channel.equals("minecraft:brand") || channel.equals("MC|Brand")) {
                byte[] data = wrapper.getData();
                if (data != null && data.length > 0) {
                    String brand = new String(data, StandardCharsets.UTF_8).substring(1).toLowerCase();
                    brandCheckManager.processBrand(event.getPlayer(), brand);
                }
            }
        }
    }

    public DiscordWebhook getDiscordWebhook() {
        return discordWebhook;
    }

    public static ArisAntiClient getInstance() {
        return instance;
    }
          }
