# ClickStream Analytics
ClickStream analytics/analysis is the process of collecting, analyzing and reporting aggregate data about which pages a website visitor visits -- and in what order. The path the visitor takes though a website is called the clickstream. This project aims for BigData context and processes through popular in-memory cluster computing frameworks called Apache Spark. 

# Getting Started

## Big Data Environment Setup

You have to make ready your big data tools (apache spark and hadoop initially) and cluster in your local machine or remote clusters. If you want to use virtual box you can follow this project https://github.com/mahedi-kaysar/bigdata-pipeline-vagrant-virtualbox. I have tested this project using that.

## Development Environment Setup:
+JDK-1.8
+Apache Spark 2.1.0
+Maven

## Build and Run Instruction
mvn clean install

## Examples:

	SparkSession spark = SparkSession.builder().appName("ClickStream").getOrCreate();
	// find the average clicks of all the users	
	ClickStream clickStream = new ClickStream(spark, file);
	double averageClicks = clickStream.getAverageClicks());
	// stop the spark session
	spark.stop();
	
# Conclusions
