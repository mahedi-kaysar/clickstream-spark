/**
 * 
 */
package org.mahedi.clickstream;

import java.util.logging.Logger;

import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author mahedi
 *
 */
public class ClickStreamTest {
	private static final Logger logger = Logger.getLogger(ClickStreamTest.class.getName());


	/**
	 * Test for checking null <b>SparkSession</b>
	 * 
	 * Test method for {@link org.mahedi.clickstream.ClickStream#getAverageClicks()}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullSparkSession() {
		String file="src/main/resources/msnbc.txt";
		new ClickStream(null, file).getAverageClicks();
	}
	
	/**
	 * Test for 20 user's clicks
	 * 
	 * Test method for {@link org.mahedi.clickstream.ClickStream#getAverageClicks()}.
	 */
	@Test
	public void test20UserClicks(){
		String file="src/main/resources/msnbc";
		SparkSession spark = SparkSession.builder().appName("ClickStream").master("local").getOrCreate();
		double actualOutput = new ClickStream(spark, file).getAverageClicks();
		logger.info("Average clicks on the clickstream: "+actualOutput);
		assertTrue("Not equals", 3.0 -  actualOutput == 0);		
		spark.stop();

	}

}
