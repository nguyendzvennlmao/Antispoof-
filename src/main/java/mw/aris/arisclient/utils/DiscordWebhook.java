package mw.aris.arisclient.utils;

import mw.aris.arisclient.ArisAntiClient;
import org.json.simple.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordWebhook {

    private final ArisAntiClient plugin;

    public DiscordWebhook(ArisAntiClient plugin) {
        this.plugin = plugin;
    }

    public void sendAlert(String playerName, String clientName) {
        String webhookUrl = plugin.getConfig().getString("discord.webhook-url");
        if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.equals("YOUR_WEBHOOK_HERE")) return;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("content", "§x§f§f§0§8§1§2[ArisAntiClient] **" + playerName + "** bị kick vì dùng **" + clientName + "**");

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = json.toJSONString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                connection.getResponseCode();
            } catch (Exception ignored) {}
        });
    }
}
