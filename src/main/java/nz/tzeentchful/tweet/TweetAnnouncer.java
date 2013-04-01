package nz.tzeentchful.tweet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import nz.tzeentchful.tweet.objects.Status;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


/**
 * @author Tzeentchful
 *
 */
public class TweetAnnouncer extends JavaPlugin {

	@Getter
	private BukkitTask task;
	@Getter
	private List<String> users = new ArrayList<String>();
	@Getter
	private List<String> tags = new ArrayList<String>();//TODO
	@Getter
	private String format;
	@Getter
	private long interval;

	protected Map<String, Status> entries = new HashMap<String, Status>();
	protected final int ammount = 15;

	@Override
	public void onEnable() {
		if(loadConfig()){
			for(String user : users){
					List<Status> statuses = TweetUtils.getUserTweets(user, ammount);
					entries.put(user, statuses.get(0));
			}

			task = getServer().getScheduler().runTaskTimerAsynchronously(this, new TweetMontor(this), interval, interval);
		}
		else this.setEnabled(false);

	}

	@Override
	public void onDisable() {
		if(task != null) task.cancel();
	}

	private boolean loadConfig(){
		FileConfiguration config = getConfig();

		if (config.getBoolean("ResetConfig", true)) {
			FileOutputStream writer;
			try {
				File configFile = new File(getDataFolder() + File.separator + "config.yml");
				getDataFolder().mkdirs();
				configFile.createNewFile();
				writer = new FileOutputStream(configFile);
				InputStream out = this.getResource("config.yml");
				byte[] linebuffer = new byte[4096];
				int lineLength = 0;
				while((lineLength = out.read(linebuffer)) > 0) {
					writer.write(linebuffer, 0, lineLength);
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			getLogger().info("Created a configuration file, make sure to edit it and restart!");
			return false;
		}

		format = config.getString("AnnounceFormat");
		interval = config.getLong("AnnounceInterval") * 20;
		users = config.getStringList("Users");

		return true;
	}
	

	public String formatMessage(String user, String text){
		return ChatColor.translateAlternateColorCodes('&', format.replace("$%user", user).replace("$%text", text));
	}
}
