package nz.tzeentchful.tweet;

import java.util.List;

import nz.tzeentchful.tweet.objects.Status;


public class TweetMontor implements Runnable {
	
	private TweetAnnouncer plugin;


	public TweetMontor(TweetAnnouncer plugin) {
		this.plugin = plugin;
	}

	public void run() {
		for(String user : plugin.getUsers()){
				List<Status> statuses = TweetUtils.getUserTweets(user, plugin.ammount);
				if(statuses.get(0).getId() != plugin.entries.get(user).getId()){
					for (Status status : statuses) {
						if(status.getId() == plugin.entries.get(user).getId()) break;
						plugin.getServer().broadcastMessage(plugin.formatMessage(status.getUser(), status.getText()));
					}
					plugin.entries.put(user, statuses.get(0));
				}
		}
	}
}
