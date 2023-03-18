package review.dal;

import review.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreditCardsDao {
	protected ConnectionManager connectionManager;

	private static CreditCardsDao instance = null;
	protected CreditCardsDao() {
		connectionManager = new ConnectionManager();
	}
	public static CreditCardsDao getInstance() {
		if(instance == null) {
			instance = new CreditCardsDao();
		}
		return instance;
	}

	public CreditCards create(CreditCards creditCard) throws SQLException {
		String insertCreditCard =
			"INSERT INTO CreditCards(CardNumber,Expiration,UserName) " +
			"VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
//		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			// BlogPosts has an auto-generated key. So we want to retrieve that key.
			insertStmt = connection.prepareStatement(insertCreditCard);
			insertStmt.setLong(1, creditCard.getCardNumber());
			// Note: for the sake of simplicity, just set Picture to null for now.
			insertStmt.setDate(2, (java.sql.Date) creditCard.getExpiration());
			insertStmt.setString(3, creditCard.getUser().getUserName());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			// For more details, see:
			// http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-last-insert-id.html
//			resultKey = insertStmt.getGeneratedKeys();
//			int postId = -1;
//			if(resultKey.next()) {
//				postId = resultKey.getInt(1);
//			} else {
//				throw new SQLException("Unable to retrieve auto-generated key.");
//			}
//			blogPost.setPostId(postId);
			return creditCard;
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
//			if(resultKey != null) {
//				resultKey.close();
//			}
		}
	}

	/**
	 * Update the content of the BlogPosts instance.
	 * This runs a UPDATE statement.
	 */
	public CreditCards updateExpiration(CreditCards creditCard, Date newExpiration) throws SQLException {
		String updateExpiration = "UPDATE CreditCards SET Expiration=? WHERE CardNumber=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateExpiration);
			updateStmt.setDate(1, (java.sql.Date) newExpiration);
			// Sets the Created timestamp to the current time.
			Date newCreatedTimestamp = new Date();
//			updateStmt.setTimestamp(2, new Timestamp(newCreatedTimestamp.getTime()));
			updateStmt.setLong(3, creditCard.getCardNumber());
			updateStmt.executeUpdate();

			// Update the blogPost param before returning to the caller.
			creditCard.setExpiration(newExpiration);;
//			blogPost.setCreated(newCreatedTimestamp);
			return creditCard;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}

	/**
	 * Delete the BlogPosts instance.
	 * This runs a DELETE statement.
	 */
	public CreditCards delete(CreditCards creditCard) throws SQLException {
		// Note: BlogComments has a fk constraint on BlogPosts with the reference option
		// ON DELETE CASCADE. So this delete operation will delete all the referencing
		// BlogComments.
		String deleteCreditCard = "DELETE FROM CreditCards WHERE CardNumber=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteCreditCard);
			deleteStmt.setLong(1, creditCard.getCardNumber());
			deleteStmt.executeUpdate();

			// Return null so the caller can no longer operate on the BlogPosts instance.
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
	 * Get the BlogPosts record by fetching it from your MySQL instance.
	 * This runs a SELECT statement and returns a single BlogPosts instance.
	 * Note that we use BlogUsersDao to retrieve the referenced BlogUsers instance.
	 * One alternative (possibly more efficient) is using a single SELECT statement
	 * to join the BlogPosts, BlogUsers tables and then build each object.
	 */
	public CreditCards getCreditCardByCardNumber(long cardNumber) throws SQLException {
		String selectCreditCard =
			"SELECT CardNumber,Expiration,UserName " +
			"FROM CreditCards " +
			"WHERE CardNumber=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectCreditCard);
			selectStmt.setLong(1, cardNumber);
			results = selectStmt.executeQuery();
			UsersDao usersDao = UsersDao.getInstance();
			if(results.next()) {
				long resultCardNumber = results.getLong("CardNumber");
				Date expiration = results.getDate("Expiration");
				String userName = results.getString("UserName");
				
				Users user = usersDao.getUserByUserName(userName);
				CreditCards creditCard = new CreditCards(resultCardNumber, expiration, user);
				return creditCard;
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
	 * Get the all the BlogPosts for a user.
	 */
	public List<CreditCards> getCreditCardsByUserName(String userName) throws SQLException {
		List<CreditCards> creditCards = new ArrayList<CreditCards>();
		String selectCreditCards =
			"SELECT CardNumber,Expiration,UserName " +
			"FROM CreditCards " +
			"WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectCreditCards);
			selectStmt.setString(1, userName);
			results = selectStmt.executeQuery();
			UsersDao usersDao = UsersDao.getInstance();
			while(results.next()) {
				long cardNumber = results.getLong("CardNumber");
				Date expiration = results.getDate("Expiration");
				Users user = usersDao.getUserByUserName(userName);
				CreditCards creditCard = new CreditCards(cardNumber, expiration, user);
				creditCards.add(creditCard);
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
		return creditCards;
	}
}
