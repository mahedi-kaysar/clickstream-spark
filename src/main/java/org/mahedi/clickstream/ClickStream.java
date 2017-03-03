package org.mahedi.clickstream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

/**
 * 
 * @author mahedi
 *
 */
public class ClickStream implements Serializable{

	private static final Logger logger = Logger.getLogger(ClickStream.class.getName());
	private static final long serialVersionUID = 1L;
	private static final Pattern SPACE = Pattern.compile(" ");

	private SparkSession spark;
	private String file;

	public ClickStream(SparkSession spark, String file) {
		this.spark = spark;
		this.file = file;
	}

	/**
	 * 
	 * @return average number of clicks or visits of the users
	 */
	public double getAverageClicks() {
		if (spark == null)
			throw new IllegalArgumentException("SparkSession is null!");

		JavaRDD<String> lines = spark.read().textFile(file).javaRDD();

		/*
		 * Here users RDD has been created and each user has a list of clicks on
		 * different types of pages.
		 */
		JavaRDD<User> users = lines.map(new Function<String, User>() {

			@Override
			public User call(String s) throws Exception {
				// TODO Auto-generated method stub

				ArrayList<Integer> clicks = new ArrayList<>();
				for (String sp : SPACE.split(s))
					clicks.add(Integer.parseInt(sp));
				return new User(clicks);
			}
		}).cache();

		// calculate the average by counting individuals clicks

		JavaRDD<Integer> clickCounts = users.map(new Function<ClickStream.User, Integer>() {

			@Override
			public Integer call(User v1) throws Exception {
				// TODO Auto-generated method stub
				return v1.getClicks().size();
			}
		});

		return clickCounts.reduce((a, b) -> a + b) / users.count();
	}

	/**
	 * 
	 * @author mahedi
	 *
	 */
	public static enum PageType {
		frontpage, news, tech, local, opinion, on_air, misc, weather, health, living, business, sports, summary, bbs, travel, msn_news, msn_sports

	}

	/**
	 * 
	 * @author mahedi
	 *
	 */
	private static class User implements Serializable {

		private static final long serialVersionUID = 1L;
		ArrayList<Integer> clicks;

		public User(ArrayList<Integer> clicks) {
			super();
			this.clicks = clicks;
		}

		public ArrayList<Integer> getClicks() {
			return clicks;
		}

		@Override
		public String toString() {
			return "User [clicks=" + clicks.toString() + "]";
		}

	}

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: ClickStream <file>");
			System.exit(1);
		}
		String file = args[0];
		//String file = "C:/Users/mahedi/Google Drive/packt-publications/Mastering Machine Learning with Spark/chapter 6 (Extracting Patterns from Clickstream Data)/MSNBC-Dataset/msnbc990928.seq";
		
		// initialize spark session for the SQL context
		SparkSession spark = SparkSession.builder().appName("ClickStream").getOrCreate();
		// find the average clicks of all the users
		ClickStream clickStream = new ClickStream(spark, file);
		logger.info("Average clicks on hte clickstream"+clickStream.getAverageClicks());

		// stop the spark session
		spark.stop();

	}

}
