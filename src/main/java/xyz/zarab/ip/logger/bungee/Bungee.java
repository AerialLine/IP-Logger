package xyz.zarab.ip.logger.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public final class Bungee extends Plugin implements Listener {
    Map<String, String> map = new HashMap<>();

    String path = getProxy().getPluginsFolder().getAbsolutePath() + "\\Log\\";

    @Override
    public void onEnable() {
        getLogger().info("");
        getLogger().info("| |   ");
        getLogger().info("| |__ v1.0.0 \"IP Logger\" Plugin");
        getLogger().info("");
        getLogger().info(ChatColor.AQUA + "initializing...");
        getProxy().getPluginManager().registerListener(this, this);
        try {
            map = load(path + "IPLogs.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info(ChatColor.GREEN + "Complete!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.AQUA + "Saving Now...");
        try {
            save(path + "IPLogs.log", map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info(ChatColor.GREEN + "good bye!");
    }

    @EventHandler
    public void PostLogin(PostLoginEvent e) throws UnknownHostException {
        String address = e.getPlayer().getAddress().getAddress().getHostAddress();
        map.put(e.getPlayer().getName(),address);
        getLogger().info("Logging now! (" + address + " / " + InetAddress.getByName(address).getCanonicalHostName() + ")");
    }

    private void save(String path, Map<String, String> map) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
        FileWriter fw = new FileWriter(file, false);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        for(Map.Entry<String, String> entry : map.entrySet()) {
            pw.append(entry.getKey() + "," + entry.getValue());
        }
        pw.flush();
        pw.close();
    }

    private Map<String, String> load(String path) throws IOException {
        Map<String, String> map = new HashMap<>();
        File f = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(f));
        try {
            String str;
            String[] split;
            while ((str = in.readLine()) != null) {
                split = str.split(",");
                map.put(split[0], split[1]);
            }
            in.close();
        } catch (Throwable throwable) {
            try {
                in.close();
            } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return map;
    }

}
