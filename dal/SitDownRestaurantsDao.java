package review.dal;

import review.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Data access object (DAO) class to interact with the underlying BlogUsers table in your
 * MySQL instance. This is used to store {@link SitDownRestaurants} into your MySQL instance and 
 * retrieve {@link SitDownRestaurants} from MySQL instance.
 */
public class SitDownRestaurantsDao extends RestaurantsDao {
	// Single pattern: instantiation is limited to one object.
	private static SitDownRestaurantsDao instance = null;
	protected SitDownRestaurantsDao() {
		super();
	}
	public static SitDownRestaurantsDao getInstance() {
		if(instance == null) {
			instance = new SitDownRestaurantsDao();
		}
		return instance;
	}

	public SitDownRestaurants create(SitDownRestaurants sitDownRestaurant) throws SQLException {
		// Insert into the superclass table first.
		create(new Restaurants(sitDownRestaurant.getName(), sitDownRestaurant.getDescription(), sitDownRestaurant.getMenu(),
				sitDownRestaurant.getHours(), sitDownRestaurant.isActive(), sitDownRestaurant.getCuisine(), sitDownRestaurant.getStreet1(),
				sitDownRestaurant.getStreet2(), sitDownRestaurant.getCity(), sitDownRestaurant.getState(), sitDownRestaurant.getZip(),
				sitDownRestaurant.getCompany()));

		String insertSitDownRestaurant = "INSERT INTO SitDownRestaurants(RestaurantId,Capacity) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertSitDownRestaurant);
			insertStmt.setInt(1, sitDownRestaurant.getRestaurantId());
			insertStmt.setInt(2, sitDownRestaurant.getCapacity());
			insertStmt.executeUpdate();
			return sitDownRestaurant;
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
	 * Update the LastName of the BlogUsers instance.
	 * This runs a UPDATE statement.
	 */
//	public SitDownRestaurants updateLastName(SitDownRestaurants blogUser, String newLastName) throws SQLException {
//		// The field to update only exists in the superclass table, so we can
//		// just call the superclass method.
//		super.updateLastName(blogUser, newLastName);
//		return blogUser;
//	}

	/**
	 * Delete the BlogUsers instance.
	 * This runs a DELETE statement.
	 */
	public SitDownRestaurants delete(SitDownRestaurants sitDownRestaurant) throws SQLException {
		String deleteSitDownRestaurant = "DELETE FROM SitDownRestaurants WHERE RestaurantId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteSitDownRestaurant);
			deleteStmt.setInt(1, sitDownRestaurant.getRestaurantId());
			int affectedRows = deleteStmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("No records available to delete for RestaurantId=" + sitDownRestaurant.getRestaurantId());
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
			super.delete(sitDownRestaurant);

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

	public SitDownRestaurants getSitDownRestaurantById(int sitDownRestaurantId) throws SQLException {
		// To build an BlogUser object, we need the Persons record, too.
		String selectSitDownRestaurant =
			"SELECT SitDownRestaurants.RestaurantId AS RestaurantId,Name,Description,Menu,Hours,Active,CuisineType,Street1,Street2,City,State,Zip,CompanyName, Capacity " +
			"FROM SitDownRestaurants INNER JOIN Restaurants " +
			"  ON SitDownRestaurants.RestaurantId = Restaurants.RestaurantId " +
			"WHERE SitDownRestaurants.RestaurantId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectSitDownRestaurant);
			selectStmt.setInt(1, sitDownRestaurantId);
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
				int capacity = results.getInt("Capacity");
				SitDownRestaurants sitDownRestaurant = new SitDownRestaurants(sitDownRestaurantId, name, description, menu,
						hours, active, cuisine, street1, street2, city, state, zip, company, capacity);
				return sitDownRestaurant;
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

	
	public List<SitDownRestaurants> getSitDownRestaurantsByCompanyName(String companyName)
			throws SQLException {
		List<SitDownRestaurants> sitDownRestaurants = new ArrayList<SitDownRestaurants>();
		String selectSitDownRestaurants =
			"SELECT SitDownRestaurants.RestaurantId AS RestaurantId,Name,Description,Menu,Hours,Active,CuisineType,Street1,Street2,City,State,Zip,CompanyName,Capacity " +
			"FROM SitDownRestaurants INNER JOIN Restaurants " +
			"  ON SitDownRestaurants.RestaurantId = Restaurants.RestaurantId " +
			"WHERE Restaurants.CompanyName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectSitDownRestaurants);
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
				int capacity = results.getInt("Capacity");
				SitDownRestaurants sitDownRestaurant = new SitDownRestaurants(restaurantId, name, description, menu,
						hours, active, cuisine, street1, street2, city, state, zip, company, capacity);
				sitDownRestaurants.add(sitDownRestaurant);
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
		return sitDownRestaurants;
	}
}
