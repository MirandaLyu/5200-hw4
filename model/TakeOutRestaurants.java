package review.model;

//import java.util.Date;


public class TakeOutRestaurants extends Restaurants {
	protected int maxWaitTime;

	public TakeOutRestaurants(int restaurantId, String name, String description, String menu, String hours,
			boolean active, CuisineType cuisine, String street1, String street2, String city, String state, int zip,
			Companies company, int maxWaitTime) {
		super(restaurantId, name, description, menu, hours, active, cuisine, street1, street2, city, state, zip,
				company);
		this.maxWaitTime = maxWaitTime;
	}

	public TakeOutRestaurants(int restaurantId) {
		super(restaurantId);
	}

	public TakeOutRestaurants(String name, String description, String menu, String hours, boolean active,
			CuisineType cuisine, String street1, String street2, String city, String state, int zip, Companies company,
			int maxWaitTime) {
		super(name, description, menu, hours, active, cuisine, street1, street2, city, state, zip, company);
		this.maxWaitTime = maxWaitTime;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	
	
}
