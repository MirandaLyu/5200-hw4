package review.dal;

import review.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ReviewsDao {
	protected ConnectionManager connectionManager;

	private static ReviewsDao instance = null;
	protected ReviewsDao() {
		connectionManager = new ConnectionManager();
	}
	public static ReviewsDao getInstance() {
		if(instance == null) {
			instance = new ReviewsDao();
		}
		return instance;
	}

	public Reviews create(Reviews review) throws SQLException {
		String insertReview =
			"INSERT INTO Reviews(Content,Rating,UserName,RestaurantId) " +
			"VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertReview,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, review.getContent());
			insertStmt.setFloat(2, review.getRating());
			insertStmt.setString(3, review.getUser().getUserName());
			insertStmt.setInt(4, review.getRestaurant().getRestaurantId());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int reviewId = -1;
			if(resultKey.next()) {
				reviewId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			review.setReviewId(reviewId);
			return review;
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
			if(resultKey != null) {
				resultKey.close();
			}
		}
	}

	/**
	 * Delete the Reshares instance.
	 * This runs a DELETE statement.
	 */
	public Recommendations delete(Recommendations reshare) throws SQLException {
		String deleteReshare = "DELETE FROM Reshares WHERE ReshareId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteReshare);
			deleteStmt.setInt(1, reshare.getReshareId());
			deleteStmt.executeUpdate();

			// Return null so the caller can no longer operate on the Persons instance.
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

	/**
	 * Get the Reshares record by fetching it from your MySQL instance.
	 * This runs a SELECT statement and returns a single Reshares instance.
	 * Note that we use BlogPostsDao and BlogUsersDao to retrieve the referenced
	 * BlogPosts and BlogUsers instances.
	 * One alternative (possibly more efficient) is using a single SELECT statement
	 * to join the Reshares, BlogPosts, BlogUsers tables and then build each object.
	 */
	public Recommendations getReshareById(int reshareId) throws SQLException {
		String selectReshare =
			"SELECT ReshareId,UserName,PostId " +
			"FROM Reshares " +
			"WHERE ReshareId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectReshare);
			selectStmt.setInt(1, reshareId);
			results = selectStmt.executeQuery();
			SitDownRestaurantsDao blogUsersDao = SitDownRestaurantsDao.getInstance();
			CreditCardsDao blogPostsDao = CreditCardsDao.getInstance();
			if(results.next()) {
				int resultReshareId = results.getInt("ReshareId");
				String userName = results.getString("UserName");
				int postId = results.getInt("PostId");
				
				SitDownRestaurants blogUser = blogUsersDao.getBlogUserFromUserName(userName);
				Reservations blogPost = blogPostsDao.getBlogPostById(postId);
				Recommendations reshare = new Recommendations(resultReshareId, blogUser, blogPost);
				return reshare;
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

	/**
	 * Get the all the Reshares for a user.
	 */
	public List<Recommendations> getResharesForUser(SitDownRestaurants blogUser) throws SQLException {
		List<Recommendations> reshares = new ArrayList<Recommendations>();
		String selectReshares =
			"SELECT ReshareId,UserName,PostId " +
			"FROM Reshares " + 
			"WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectReshares);
			selectStmt.setString(1, blogUser.getUserName());
			results = selectStmt.executeQuery();
			CreditCardsDao blogPostsDao = CreditCardsDao.getInstance();
			while(results.next()) {
				int reshareId = results.getInt("ReshareId");
				int postId = results.getInt("PostId");
				Reservations blogPost = blogPostsDao.getBlogPostById(postId);
				Recommendations reshare = new Recommendations(reshareId, blogUser, blogPost);
				reshares.add(reshare);
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
		return reshares;
	}
}
