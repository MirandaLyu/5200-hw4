package review.tools;

import review.dal.*;
import review.model.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * main() runner, used for the app demo.
 * 
 * Instructions:
 * 1. Create a new MySQL schema and then run the CREATE TABLE statements from lecture:
 * http://goo.gl/86a11H.
 * 2. Update ConnectionManager with the correct user, password, and schema.
 */
public class Inserter {

	public static void main(String[] args) throws SQLException {
		// DAO instances.
		UsersDao usersDao = UsersDao.getInstance();
		TakeOutRestaurantsDao takeOutRestaurantsDao = TakeOutRestaurantsDao.getInstance();
		SitDownRestaurantsDao sitDownRestaurantsDao = SitDownRestaurantsDao.getInstance();
		FoodCartRestaurantsDao foodCartRestaurantsDao = FoodCartRestaurantsDao.getInstance();
		CreditCardsDao creditCardsDao = CreditCardsDao.getInstance();
		ReservationsDao reservationsDao = ReservationsDao.getInstance();
		ReviewsDao reviewsDao = ReviewsDao.getInstance();
		RecommendationsDao recommendationsDao = RecommendationsDao.getInstance();
		CompaniesDao companiesDao = CompaniesDao.getInstance();
		RestaurantsDao restaurantsDao = RestaurantsDao.getInstance();
		
		// test UsersDao
		Users user = new Users("mirandalyu", "123", "miranda", "lyu", "mirandalyu715@gmail.com", "2069457846");
//		user = usersDao.create(user);
		Users user1 = new Users("chloexu", "123", "chloe", "xu", "chloexu@gmail.com", "2061784988");
//		user1 = usersDao.create(user1);
		Users user2 = new Users("richardma", "456", "richard", "ma", "richardma@gmail.com", "2069983576");
//		user2 = usersDao.create(user2);
		Users u1 = usersDao.getUserByUserName("mirandalyu");
		System.out.format("Reading user: u:%s, p:%s, f:%s, l:%s, e:%s, p:%s \n", u1.getUserName(),
				u1.getPassword(), u1.getFirstName(), u1.getLastName(), u1.getEmail(), u1.getPhone());
//		usersDao.delete(user2);
		
		// test CreditCardsDao
		Calendar cal = Calendar.getInstance();
		cal.set(2023, 11, 1);
		Date date = cal.getTime();
		CreditCards card1 = new CreditCards(3499432187650987L, date, user);
//		card1 = creditCardsDao.create(card1);
		cal.set(2027, 7, 12);
		Date date1 = cal.getTime();
		CreditCards card2 = new CreditCards(3499432187654885L, date1, user);
//		card2 = creditCardsDao.create(card2);
		cal.set(2028, 3, 3);
		Date date2 = cal.getTime();
		CreditCards card3 = new CreditCards(3499432187656094L, date2, user2);
//		card3 = creditCardsDao.create(card3);
		CreditCards c1 = creditCardsDao.getCreditCardByCardNumber(3499432187656094L);
		System.out.format("Reading credit card: c:%d, e:%s, u:%s \n", c1.getCardNumber(), c1.getExpiration(), c1.getUser().getUserName());
		List<CreditCards> c2 = creditCardsDao.getCreditCardsByUserName("mirandalyu");
		for(CreditCards c: c2) {
			System.out.format("Looping credit cards: c:%d, e:%s, u:%s \n", c.getCardNumber(), c.getExpiration(), c.getUser().getUserName());
		}
		cal.set(2027, 11, 1);
		Date date3 = cal.getTime();
		creditCardsDao.updateExpiration(card1, date3);
//		creditCardsDao.delete(card1);
		
		// test CompaniesDao
		Companies company = new Companies("Heir", "western cuisines");
//		company = companiesDao.create(company);
		Companies company1 = new Companies("farEast", "chinese cuisines");
//		company1 = companiesDao.create(company1);
		Companies com1 = companiesDao.getCompanyByCompanyName("farEast");
		System.out.format("Reading company: c:%s, a:%s \n", com1.getCompanyName(), com1.getAbout());
		companiesDao.updateAbout(company1, "asian cuisines");
//		companiesDao.delete(company);
		
		// test RestaurantsDao
		Restaurants rest = new Restaurants("wasabi", "japanese", "menu", "11am-8pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98121, company1);
//		rest = restaurantsDao.create(rest);
		Restaurants rest1 = new Restaurants("bentoful", "korean lunch", "menu", "11am-2pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98109, company1);
//		rest1 = restaurantsDao.create(rest1);
		Restaurants rest2 = new Restaurants("zeeks", "pizza", "menu", "10am-10pm", true, Restaurants.CuisineType.AMERICAN,
				"street1", "street2", "Seattle", "WA", 98121, company);
//		rest2 = restaurantsDao.create(rest2);
		Restaurants rest3 = new Restaurants("chipotle", "mexican food", "menu", "10am-10pm", true, Restaurants.CuisineType.HISPANIC,
				"street1", "street2", "Seattle", "WA", 98109, company);
//		rest3 = restaurantsDao.create(rest3);
		Restaurants rest4 = new Restaurants("yumbit", "taiwan food", "menu", "11am-2pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98109, company1);
//		rest4 = restaurantsDao.create(rest4);
		Restaurants rest5 = new Restaurants("taqueria", "tacos", "menu", "11am-2pm", true, Restaurants.CuisineType.HISPANIC,
				"street1", "street2", "Seattle", "WA", 98109, company);
//		rest5 = restaurantsDao.create(rest5);
		Restaurants r1 = restaurantsDao.getRestaurantById(3);
		System.out.format("Reading restaurant: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
				+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s \n", r1.getRestaurantId(), r1.getName(), r1.getDescription(),
				r1.getMenu(), r1.getHours(), r1.isActive(), r1.getCuisine().toString(), r1.getStreet1(),
				r1.getStreet2(), r1.getCity(), r1.getState(), r1.getZip(), r1.getCompany().getCompanyName());
		List<Restaurants> rests = restaurantsDao.getRestaurantsByCuisine(Restaurants.CuisineType.ASIAN);
		for(Restaurants r: rests) {
			System.out.format("Looping restaurants: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
					+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s \n", r.getRestaurantId(), r.getName(), r.getDescription(),
					r.getMenu(), r.getHours(), r.isActive(), r.getCuisine().toString(), r.getStreet1(),
					r.getStreet2(), r.getCity(), r.getState(), r.getZip(), r.getCompany().getCompanyName());
		}
		List<Restaurants> rests1 = restaurantsDao.getRestaurantsByCompanyName("Heir");
		for(Restaurants r: rests1) {
			System.out.format("Looping restaurants: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
					+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s \n", r.getRestaurantId(), r.getName(), r.getDescription(),
					r.getMenu(), r.getHours(), r.isActive(), r.getCuisine().toString(), r.getStreet1(),
					r.getStreet2(), r.getCity(), r.getState(), r.getZip(), r.getCompany().getCompanyName());
		}
//		restaurantsDao.delete(restaurantsDao.getRestaurantById(15));
		
		// test SitDownRestaurantsDao
		SitDownRestaurants sr = new SitDownRestaurants("ooink", "japanese noodles", "menu", "11am-9pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98122, company1, 15);
//		sr = sitDownRestaurantsDao.create(sr);
		SitDownRestaurants sr1 = new SitDownRestaurants("tutubella", "italian", "menu", "11am-9pm", true, Restaurants.CuisineType.EUROPEAN,
				"street1", "street2", "Seattle", "WA", 98109, company, 50);
//		sr1 = sitDownRestaurantsDao.create(sr1);
//		sitDownRestaurantsDao.createFromRestaurants(restaurantsDao.getRestaurantById(2), 60);
		SitDownRestaurants sdr = sitDownRestaurantsDao.getSitDownRestaurantById(11);
		System.out.format("Reading sit down restaurant: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
				+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s, c:%d \n", sdr.getRestaurantId(), sdr.getName(), sdr.getDescription(),
				sdr.getMenu(), sdr.getHours(), sdr.isActive(), sdr.getCuisine().toString(), sdr.getStreet1(),
				sdr.getStreet2(), sdr.getCity(), sdr.getState(), sdr.getZip(), sdr.getCompany().getCompanyName(), sdr.getCapacity());
		List<SitDownRestaurants> sdrs = sitDownRestaurantsDao.getSitDownRestaurantsByCompanyName("farEast");
		for(SitDownRestaurants s: sdrs) {
			System.out.format("Looping sit down restaurants: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
					+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s, c:%d \n", s.getRestaurantId(), s.getName(), s.getDescription(),
					s.getMenu(), s.getHours(), s.isActive(), s.getCuisine().toString(), s.getStreet1(),
					s.getStreet2(), s.getCity(), s.getState(), s.getZip(), s.getCompany().getCompanyName(), s.getCapacity());
		}
//		sitDownRestaurantsDao.delete(sitDownRestaurantsDao.getSitDownRestaurantById(15));
		
		// test TakeOutRestaurantsDao
		TakeOutRestaurants tr = new TakeOutRestaurants("45thPoke", "poke", "menu", "10am-8pm", true, Restaurants.CuisineType.AMERICAN,
				"street1", "street2", "Seattle", "WA", 98109, company, 10);
//		tr = takeOutRestaurantsDao.create(tr);
//		takeOutRestaurantsDao.createFromRestaurants(restaurantsDao.getRestaurantById(3), 10);
//		takeOutRestaurantsDao.createFromRestaurants(restaurantsDao.getRestaurantById(5), 20);
//		takeOutRestaurantsDao.delete(takeOutRestaurantsDao.getTakeOutRestaurantById(17));
		List<TakeOutRestaurants> trs = takeOutRestaurantsDao.getTakeOutRestaurantsByCompanyName("Heir");
		for(TakeOutRestaurants t: trs) {
			System.out.format("Looping take out restaurants: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
					+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s, m:%d \n", t.getRestaurantId(), t.getName(), t.getDescription(),
					t.getMenu(), t.getHours(), t.isActive(), t.getCuisine().toString(), t.getStreet1(),
					t.getStreet2(), t.getCity(), t.getState(), t.getZip(), t.getCompany().getCompanyName(), t.getMaxWaitTime());
		}
		
		// test FoodCartRestaurantsDao
		FoodCartRestaurants fr = new FoodCartRestaurants("kaosamai", "thai food", "menu", "11am-2pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98109, company1, true);
//		fr = foodCartRestaurantsDao.create(fr);
//		foodCartRestaurantsDao.createFromRestaurants(restaurantsDao.getRestaurantById(6), true);
//		foodCartRestaurantsDao.createFromRestaurants(restaurantsDao.getRestaurantById(7), true);
		FoodCartRestaurants fr2 = new FoodCartRestaurants("kaosamai", "thai food", "menu", "11am-2pm", true, Restaurants.CuisineType.ASIAN,
				"street1", "street2", "Seattle", "WA", 98109, company1, true);
//		fr2 = foodCartRestaurantsDao.create(fr2);
//		foodCartRestaurantsDao.delete(foodCartRestaurantsDao.getFoodCartRestaurantById(19));
		List<FoodCartRestaurants> frs = foodCartRestaurantsDao.getFoodCartRestaurantsByCompanyName("farEast");
		for(FoodCartRestaurants f: frs) {
			System.out.format("Looping food cart restaurants: r:%d, n:%s, d:%s, m:%s, h:%s, a:%b, c:%s,"
					+ "s:%s, s:%s, c:%s, s:%s, z:%d, c:%s, l:%b \n", f.getRestaurantId(), f.getName(), f.getDescription(),
					f.getMenu(), f.getHours(), f.isActive(), f.getCuisine().toString(), f.getStreet1(),
					f.getStreet2(), f.getCity(), f.getState(), f.getZip(), f.getCompany().getCompanyName(), f.isLicensed());
		}
		
		// test ReviewsDao
		Date date4 = new Date();
		Reviews re = new Reviews(date4, "favorite go-to place", 4.8, user, restaurantsDao.getRestaurantById(3));
//		reviewsDao.create(re);
		Reviews re1 = new Reviews(date4, "best ramen place", 4.8, user, restaurantsDao.getRestaurantById(11));
//		reviewsDao.create(re1);
		Reviews re2 = new Reviews(date4, "most convenient food place", 4.5, user2, restaurantsDao.getRestaurantById(5));
//		reviewsDao.create(re2);
		Reviews re3 = new Reviews(date4, "healthy and clean", 4.7, user1, restaurantsDao.getRestaurantById(16));
//		reviewsDao.create(re3);
		Reviews re4 = new Reviews(date4, "the amount becomes less", 4.6, user, restaurantsDao.getRestaurantById(5));
//		reviewsDao.create(re4);
//		reviewsDao.delete(reviewsDao.getReviewById(2));
		List<Reviews> reviews = reviewsDao.getReviewsByUserName("mirandalyu");
		for(Reviews r: reviews) {
			System.out.format("Looping reviews: r:%d, c:%s, c:%s, r:%f, u:%s, r:%d \n", r.getReviewId(), r.getCreated(),
					r.getContent(), r.getRating(), r.getUser().getUserName(), r.getRestaurant().getRestaurantId());
		}
		List<Reviews> reviews1 = reviewsDao.getReviewsByRestaurantId(5);
		for(Reviews r: reviews1) {
			System.out.format("Looping reviews: r:%d, c:%s, c:%s, r:%f, u:%s, r:%d \n", r.getReviewId(), r.getCreated(),
					r.getContent(), r.getRating(), r.getUser().getUserName(), r.getRestaurant().getRestaurantId());
		}
		
		// test RecommendationDao
		Recommendations rm = new Recommendations(user, restaurantsDao.getRestaurantById(13));
//		recommendationsDao.create(rm);
		Recommendations rm1 = new Recommendations(user1, restaurantsDao.getRestaurantById(7));
//		recommendationsDao.create(rm1);
		Recommendations rm2 = new Recommendations(user2, restaurantsDao.getRestaurantById(2));
//		recommendationsDao.create(rm2);
		Recommendations rm3 = new Recommendations(user, restaurantsDao.getRestaurantById(11));
//		recommendationsDao.create(rm3);
		Recommendations rm4 = new Recommendations(user2, restaurantsDao.getRestaurantById(11));
//		recommendationsDao.create(rm4);
//		recommendationsDao.delete(recommendationsDao.getRecommendationById(3));
		List<Recommendations> recommendations = recommendationsDao.getRecommendationsByUserName("mirandalyu");
		for(Recommendations r: recommendations) {
			System.out.format("Looping recommendations: r:%d, u:%s, r:%d \n", r.getRecommendationId(), r.getUser().getUserName(),
					r.getRestaurant().getRestaurantId());
		}
		List<Recommendations> recommendations1 = recommendationsDao.getRecommendationsByRestaurantId(11);
		for(Recommendations r: recommendations1) {
			System.out.format("Looping recommendations: r:%d, u:%s, r:%d \n", r.getRecommendationId(), r.getUser().getUserName(),
					r.getRestaurant().getRestaurantId());
		}
		
		// test ReservationsDao
		Reservations reserve = new Reservations(date4, date4, 4, user1, sitDownRestaurantsDao.getSitDownRestaurantById(2));
//		reservationsDao.create(reserve);
		Reservations reserve1 = new Reservations(date4, date4, 6, user2, sitDownRestaurantsDao.getSitDownRestaurantById(2));
//		reservationsDao.create(reserve1);
		Reservations reserve2 = new Reservations(date4, date4, 3, user1, sitDownRestaurantsDao.getSitDownRestaurantById(13));
//		reservationsDao.create(reserve2);
		Reservations reserve4 = new Reservations(date4, date4, 2, user, sitDownRestaurantsDao.getSitDownRestaurantById(11));
//		reservationsDao.create(reserve4);
//		reservationsDao.delete(reservationsDao.getReservationById(4));
		List<Reservations> reservations = reservationsDao.getReservationsByUserName("chloexu");
		for(Reservations r: reservations) {
			System.out.format("Looping reservations: r:%d, s:%s, e:%s, s:%d, u:%s, r:%d \n", r.getReservationId(), r.getStart(),
					r.getEnd(), r.getSize(), r.getUser().getUserName(), r.getRestaurant().getRestaurantId());
		}
		List<Reservations> reservations1 = reservationsDao.getReservationsBySitDownRestaurantId(2);
		for(Reservations r: reservations1) {
			System.out.format("Looping reservations: r:%d, s:%s, e:%s, s:%d, u:%s, r:%d \n", r.getReservationId(), r.getStart(),
					r.getEnd(), r.getSize(), r.getUser().getUserName(), r.getRestaurant().getRestaurantId());
		}
		
	}
}
