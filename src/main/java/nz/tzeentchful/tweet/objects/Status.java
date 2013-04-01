package nz.tzeentchful.tweet.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Status {

	
	@Getter @Setter
	private String user;
	@Getter @Setter
	private String text;
	@Getter @Setter
	private int id;
	
	public Status(String user, String text, int id) {
		super();
		this.user = user;
		this.text = text;
		this.id = id;
	}
}
