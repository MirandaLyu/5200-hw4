package review.dal;

import review.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodCartRestaurantsDao extends RestaurantsDao{
	// Single pattern: instantiation is limited to one object.
	private static FoodCartRestaurantsDao instance = null;
	protected FoodCartRestaurantsDao() {
		super();
	}
	public static FoodCartRestaurantsDao getInstance() {
		if(instance == null) {
			instance = new FoodCartRestaurantsDao();
		}
		return instance;
	}
	
	public FoodCartRestaurants create(FoodCartRestaurants foodCartRestaurant) throws SQLException {
		// Insert into the superclass table first.
		Restaurants superF = create(new Restaurants(foodCartRestaurant.getName(), foodCartRestaurant.getDescription(), foodCartRestaurant.getMenu(),
				foodCartRestaurant.getHours(), foodCartRestaurant.isActive(), foodCartRestaurant.getCuisine(), foodCartRestaurant.getStreet1(),
				foodCartRestaurant.getStreet2(), foodCartRestaurant.getCity(), foodCartRestaurant.getState(), foodCartRestaurant.getZip(),
				foodCartRestaurant.getCompany()));

		String insertFoodCartRestaurant = "INSERT INTO FoodCartRestaurant(RestaurantId,Licensed) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertFoodCartRestaurant);
			insertStmt.setInt(1, superF.getRestaurantId());
			insertStmt.setBoolean(2, foodCartRestaurant.isLicensed());
			insertStmt.executeUpdate();
			return foodCartRestaurant;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
		}
	}
	
	public FoodCartRestaurants createFromRestaurants(Restaurants restaurant, boolean licensed) throws SQLException {

		String insertTakeOutRestaurant = "INSERT INTO FoodCartRestaurant(RestaurantId,Licensed) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertTakeOutRestaurant);
			insertStmt.setInt(1, restaurant.getRestaurantId());
			insertStmt.setBoolean(2, licensed);
			insertStmt.executeUpdate();
			return new FoodCartRestaurants(restaurant.getRestaurantId(), restaurant.getName(), restaurant.getDescription(),
					restaurant.getMenu(), restaurant.getHours(), restaurant.isActive(), restaurant.getCuisine(),
					restaurant.getStreet1(), restaurant.getStreet2(), restaurant.getCity(), restaurant.getState(),
					restaurant.getZip(), restaurant.getCompany(), licensed);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
		}
	}

	/**
	 * Delete the Administrators instance.
	 * This runs a DELETE statement.
	 */
	public FoodCartRestaurants delete(FoodCartRestaurants foodCartRestaurant) throws SQLException {
		String deleteFoodCartRestaurant = "DELETE FROM FoodCartRestaurant WHERE RestaurantId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteFoodCartRestaurant);
			deleteStmt.setInt(1, foodCartRestaurant.getRestaurantId());
			int affectedRows = deleteStmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("No records available to delete for RestaurantId=" + foodCartRestaurant.getRestaurantId());
			}

			// Then also delete from the superclass.
			// Notes:
			// 1. Due to the fk constraint (ON DELETE CASCADE), we could simply call
			//    super.delete() without even needing to delete from Administrators first.
			// 2. BlogPosts has a fk constraint on BlogUsers with the reference option
			//    ON DELETE SET NULL. If the BlogPosts fk reference option was instead
			//    ON DELETE RESTRICT, then the caller would need to delete the referencing
			//    BlogPosts before this BlogUser can be deleted.
			//    Example to delete the referencing BlogPosts:
			//    List<BlogPosts> posts = BlogPostsDao.getBlogPostsForUser(blogUser.getUserName());
			//    for(BlogPosts p : posts) BlogPostsDao.delete(p);
			super.delete(foodCartRestaurant);

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
	
	public FoodCartRestaurants getFoodCartRestaurantById(int foodCartRestaurantId) throws SQLException {
		// To build an BlogUser object, we need the Persons record, too.
		String selectFoodCartRestaurant =
			"SELECT FoodCartRestaurant.RestaurantId AS RestaurantId,Name,Description,Menu,Hours,Active,CuisineType,Street1,Street2,City,State,Zip,CompanyName,Licensed " +
			"FROM FoodCartRestaurant INNER JOIN Restaurants " +
			"  ON FoodCartRestaurant.RestaurantId = Restaurants.RestaurantId " +
			"WHERE FoodCartRestaurant.RestaurantId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectFoodCartRestaurant);
			selectStmt.setInt(1, foodCartRestaurantId);
			results = selectStmt.executeQuery();
			CompaniesDao companiesDao = CompaniesDao.getInstance();
			if(results.next()) {
				String name = results.getString("Name");
				String description = results.getString("Description");
				String menu = results.getString("Menu");
				String hours = results.getString("Hours");
				boolean active = results.getBoolean("Active");
				Restaurants.CuisineType cuisine = Restaurants.CuisineType.valueOf(
						results.getString("CuisineType"));
				String street1 = results.getString("Street1");
				String street2 = results.getString("Street2");
				String city = results.getString("City");
				String state = results.getString("State");
				int zip = results.getInt("Zip");
				String companyName = results.getString("CompanyName");
				
				Companies company = companiesDao.getCompanyByCompanyName(companyName);
				Boolean licensed = results.getBoolean("Licensed");
				FoodCartRestaurants foodCartRestaurant = new FoodCartRestaurants(foodCartRestaurantId, name, description, menu,
						hours, active, cuisine, street1, street2, city, state, zip, company, licensed);
				return foodCartRestaurant;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<FoodCartRestaurants> getFoodCartRestaurantsByCompanyName(String companyName)
			throws SQLException {
		List<FoodCartRestaurants> foodCartRestaurants = new ArrayList<FoodCartRestaurants>();
		String selectFoodCartRestaurants =
			"SELECT FoodCartRestaurant.RestaurantId AS RestaurantId,Name,Description,Menu,Hours,Active,CuisineType,Street1,Street2,City,State,Zip,CompanyName,Licensed " +
			"FROM FoodCartRestaurant INNER JOIN Restaurants " +
			"  ON FoodCartRestaurant.RestaurantId = Restaurants.RestaurantId " +
			"WHERE Restaurants.CompanyName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectFoodCartRestaurants);
			selectStmt.setString(1, companyName);
			results = selectStmt.executeQuery();
			CompaniesDao companiesDao = CompaniesDao.getInstance();
			while(results.next()) {
				int restaurantId = results.getInt("RestaurantId");
				String name = results.getString("Name");
				String description = results.getString("Description");
				String menu = results.getString("Menu");
				String hours = results.getString("Hours");
				boolean active = results.getBoolean("Active");
				Restaurants.CuisineType cuisine = Restaurants.CuisineType.valueOf(
						results.getString("CuisineType"));
				String street1 = results.getString("Street1");
				String street2 = results.getString("Street2");
				String city = results.getString("City");
				String state = results.getString("State");
				int zip = results.getInt("Zip");
				
				Companies company = companiesDao.getCompanyByCompanyName(companyName);
				Boolean licensed = results.getBoolean("Licensed");
				FoodCartRestaurants foodCartRestaurant = new FoodCartRestaurants(restaurantId, name, description, menu,
						hours, active, cuisine, street1, street2, city, state, zip, company, licensed);
				foodCartRestaurants.add(foodCartRestaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return foodCartRestaurants;
	}
}
