package mw.aris.arisclient.manager;

import mw.aris.arisclient.ArisAntiClient;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import java.util.List;

public class BrandCheckManager {

    private final ArisAntiClient plugin;

    public BrandCheckManager(ArisAntiClient plugin) {
        this.plugin = plugin;
    }

    public void processBrand(Object playerObj, String brand) {
        if (!(playerObj instanceof Player)) return;
        Player player = (Player) playerObj;

        List<String> blacklisted = plugin.getConfig().getStringList("blacklisted-brands");
        for (String client : blacklisted) {
            if (brand.contains(client.toLowerCase())) {
                executeAction(player, brand);
                break;
            }
        }
    }

    private void executeAction(Player player, String brand) {
        plugin.getDiscordWebhook().sendAlert(player.getName(), brand.toUpperCase());
        
        player.getScheduler().execute(plugin, () -> {
            String kickMsg = plugin.getConfig().getString("settings.kick-message")
                    .replace("{brand}", brand.toUpperCase())
                    .replace("&", "§");
            player.kick(Component.text(kickMsg));
        }, null, 0);
    }
}
