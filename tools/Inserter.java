package review.tools;

import review.dal.*;
import review.model.*;

import java.sql.SQLException;
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
		UsersDao personsDao = UsersDao.getInstance();
		TakeOutRestaurantsDao administratorsDao = TakeOutRestaurantsDao.getInstance();
		SitDownRestaurantsDao blogUsersDao = SitDownRestaurantsDao.getInstance();
		CreditCardsDao blogPostsDao = CreditCardsDao.getInstance();
		BlogCommentsDao blogCommentsDao = BlogCommentsDao.getInstance();
		ReviewsDao resharesDao = ReviewsDao.getInstance();
		
		// INSERT objects from our model.
		Persons person = new Persons("b", "bruce", "chhay");
		person = personsDao.create(person);
		Persons person1 = new Persons("b1", "bruce", "chhay");
		person1 = personsDao.create(person1);
		Persons person2 = new Persons("b2", "bruce", "chhay");
		person2 = personsDao.create(person2);
		Date date = new Date();
		TakeOutRestaurants administrator = new TakeOutRestaurants("a", "bruce", "chhay_a", date);
		administrator = administratorsDao.create(administrator);
		TakeOutRestaurants administrator1 = new TakeOutRestaurants("a1", "bruce", "chhay_a1", date);
		administrator1 = administratorsDao.create(administrator1);
		TakeOutRestaurants administrator2 = new TakeOutRestaurants("a2", "bruce", "chhay_a2", date);
		administrator2 = administratorsDao.create(administrator2);
		SitDownRestaurants blogUser = new SitDownRestaurants("bu", "bruce", "chhay", date, SitDownRestaurant.SitDownRestaurants.novice);
		blogUser = blogUsersDao.create(blogUser);
		SitDownRestaurants blogUser1 = new SitDownRestaurants("bu1", "bruce", "chhay_bu1", date, SitDownRestaurant.SitDownRestaurants.intermediate);
		blogUser1 = blogUsersDao.create(blogUser1);
		SitDownRestaurants blogUser2 = new SitDownRestaurants("bu2", "bruce", "chhay_bu2", date, SitDownRestaurant.SitDownRestaurants.advanced);
		blogUser2 = blogUsersDao.create(blogUser2);
		Reservations blogPost = new Reservations("Laser Cats", "pic", "content", true, date, blogUser);
		blogPost = blogPostsDao.create(blogPost);
		Reservations blogPost1 = new Reservations("Dancing Cats", "pic1", "content1", true, date, blogUser);
		blogPost1 = blogPostsDao.create(blogPost1);
		Reservations blogPost2 = new Reservations("Sleeping Cats", "pic2", "content2", true, date, blogUser);
		blogPost2 = blogPostsDao.create(blogPost2);
		Reviews blogComment = new Reviews("Comment", date, blogPost, blogUser1);
		blogComment = blogCommentsDao.create(blogComment);
		Reviews blogComment1 = new Reviews("Comment1", date, blogPost, blogUser1);
		blogComment1 = blogCommentsDao.create(blogComment1);
		Reviews blogComment2 = new Reviews("Comment2", date, blogPost, blogUser1);
		blogComment2 = blogCommentsDao.create(blogComment2);
		Reviews blogComment3 = new Reviews("Comment3", date, blogPost, blogUser);
		blogComment = blogCommentsDao.create(blogComment3);
		Reviews blogComment4 = new Reviews("Comment4", date, blogPost, blogUser);
		blogComment1 = blogCommentsDao.create(blogComment4);
		Reviews blogComment5 = new Reviews("Comment5", date, blogPost, blogUser);
		blogComment2 = blogCommentsDao.create(blogComment5);
		Recommendations reshare = new Recommendations(blogUser2, blogPost);
		reshare = resharesDao.create(reshare);
		Recommendations reshare1 = new Recommendations(blogUser2, blogPost1);
		reshare1 = resharesDao.create(reshare1);
		Recommendations reshare2 = new Recommendations(blogUser2, blogPost2);
		reshare2 = resharesDao.create(reshare2);
		
		// READ.
		Persons p1 = personsDao.getPersonFromUserName("b");
		List<Persons> pList1 = personsDao.getPersonsFromFirstName("bruce");
		System.out.format("Reading person: u:%s f:%s l:%s \n",
			p1.getUserName(), p1.getFirstName(), p1.getLastName());
		for(Persons p : pList1) {
			System.out.format("Looping persons: u:%s f:%s l:%s \n",
				p.getUserName(), p.getFirstName(), p.getLastName());
		}
		TakeOutRestaurants a1 = administratorsDao.getAdministratorFromUserName("a");
		List<TakeOutRestaurants> aList1 = administratorsDao.getAdministratorsFromFirstName("bruce");
		System.out.format("Reading administrator: u:%s f:%s l:%s d:%s \n",
			a1.getUserName(), a1.getFirstName(), a1.getLastName(), a1.getLastLogin());
		for(TakeOutRestaurants a : aList1) {
			System.out.format("Looping administrators: u:%s f:%s l:%s \n",
				a.getUserName(), a.getFirstName(), a.getLastName(), a.getLastLogin());
		}
		SitDownRestaurants bu1 = blogUsersDao.getBlogUserFromUserName("bu");
		List<SitDownRestaurants> buList1 = blogUsersDao.getBlogUsersFromFirstName("bruce");
		System.out.format("Reading blog user: u:%s f:%s l:%s d:%s s:%s \n",
			bu1.getUserName(), bu1.getFirstName(), bu1.getLastName(), bu1.getDob(), bu1.getStatusLevel().name());
		for(SitDownRestaurants bu : buList1) {
			System.out.format("Looping blog users: u:%s f:%s l:%s d:%s s:%s \n",
				bu.getUserName(), bu.getFirstName(), bu.getLastName(), bu.getDob(), bu.getStatusLevel().name());
		}
		List<Reservations> bpList1 = blogPostsDao.getBlogPostsForUser(bu1);
		for(Reservations bp : bpList1) {
			System.out.format("Looping blog posts: t:%s c:%s u:%s \n",
				bp.getTitle(), bp.getContent(), bu1.getUserName());
		}
		List<Reviews> bcList1 = blogCommentsDao.getBlogCommentsForUser(blogUser1);
		for(Reviews bc : bcList1) {
			System.out.format("Looping blog comments: t:%s u:%s \n",
				bc.getContent(), blogUser1.getUserName());
		}
		bcList1 = blogCommentsDao.getBlogCommentsForUser(blogUser);
		for(Reviews bc : bcList1) {
			System.out.format("Looping blog comments: t:%s u:%s \n",
				bc.getContent(), blogUser.getUserName());
		}
		List<Recommendations> rList1 = resharesDao.getResharesForUser(blogUser2);
		for(Recommendations r : rList1) {
			System.out.format("Looping reshare: i:%s u:%s t:%s \n",
				r.getReshareId(), r.getBlogUser().getUserName(),
				r.getBlogPost().getTitle());
		}
	}
}
