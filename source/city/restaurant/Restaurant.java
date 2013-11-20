package city.restaurant;

import city.Place;

public abstract class Restaurant extends Place {
	
	// ------------------------------------ TYPE ------------------------------------------
	
	public enum Cuisine { BREAKFAST, NORMAL }
	public enum Upscaleness { UPSCALE, NORMAL, CHEAP }
	
	protected Cuisine _cuisine;
	protected Upscaleness _upscaleness;

	public Cuisine cuisine() { return _cuisine; }
	public Upscaleness upscaleness() { return _upscaleness; }
	
	
	
	// --------------------------------- CORRESPONDENCE ----------------------------------
	
	//Do we need this?
	//private Host _host;
	//protected void setHost(Host host) { _host = host; }
	//public host() { return _host; }
}
