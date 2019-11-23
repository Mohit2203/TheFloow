package com.test.floow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @author Mohit
 *
 */
public class FileMainProcessing {

	public static void main(String[] args) {
		try {
			if (!(args[0].isEmpty() && args[1].isEmpty() && args[2].isEmpty() && args[3].isEmpty())) {

				fileProcessing(args[1], args[3]);
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					"Please pass the arguments in the correct format to start processing the file. for an e.g. \"-source filename -mongo localhost:port\"");
		}

	}

	/***
	 * This Method will perform the sorting of the value
	 * 
	 * @param unsortedMap
	 * @return
	 */
	public static SortedSet<Map.Entry<String, Double>> sortMapByValue(Map<String, Double> unsortedMap) {

		SortedSet<Map.Entry<String, Double>> sortedEntries = new TreeSet<Map.Entry<String, Double>>(
				new Comparator<Map.Entry<String, Double>>() {
					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						// TODO Auto-generated method stub
						int res = o1.getValue().compareTo(o2.getValue());
						return res != 0 ? res : 1;
					}

				});

		sortedEntries.addAll(unsortedMap.entrySet());
		return sortedEntries;

	}

	/**
	 * This is the method will Process the Input File
	 * 
	 * @param filePath
	 * @param dbDetails
	 */

	public static void fileProcessing(String filePath, String dbDetails) {
		// String
		// filePath="C:\\Users\\Mohit\\Downloads\\enwiki-latest-abstract.xml\\enwiki-latest-abstract.xml";
		// String filePath="C:\\Users\\Mohit\\Downloads\\enwiki-latest-abstract.xml\\";
		String hostname = dbDetails.split(":")[0];
		int port = Integer.parseInt(dbDetails.split(":")[1]);

		double count = 0;
		Scanner sc =null;
		Map<String, Double> treemap = new TreeMap<>();
		try {
			FileInputStream fs = new FileInputStream(filePath);
			 sc = new Scanner(fs);
			System.out.println("Started processing file " + filePath + " at " + new Date());
			System.out.println("processing the record ");
			// while(sc.hasNextLine() && count < 100 ){
			while (sc.hasNextLine()) {
				if (count % 1000000 == 0) {
					System.out.print(".");
				}

				String newline = "";
				boolean found = false;
				StringBuffer line = new StringBuffer(sc.nextLine());
				// System.out.println(line);
				if (line.toString().contains("<title>")) {
					newline = "";
					newline = line.toString().replace("<title>", "").replace("</title>", "")
							.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().trim();
					// System.out.println(newline);
					found = true;
				}
				if (line.toString().contains("<abstract>")) {
					newline = "";
					newline = line.toString().replace("<abstract>", "").replace("</abstract>", "")
							.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().trim();
					// System.out.println(newline);
					found = true;
				}
				if (line.toString().contains("<sublink")) {
					newline = "";
					newline = line.toString().replace("<sublink linktype=\"nav\"><anchor>", "").split("</anchor>")[0]
							.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().trim();
					// System.out.println(newline);
					found = true;
				}

				if (found) {
					String[] words = newline.toString().split(" ");
					// System.out.println(newline);
					for (String word : words) {
						if (!(word.isEmpty())) {
							// System.out.println(word);
							if (treemap.containsKey(word)) {
								double wordsCount = treemap.get(word);
								treemap.put(word, ++wordsCount);
							} else {
								treemap.put(word, new Double(1));
							}
						}
					}
				}

				count++;
			}
			System.out.println("");
			SortedSet<Entry<String, Double>> sortedMap = sortMapByValue(treemap);
			// Inserting data in database
			dbInsert(sortedMap, hostname, port);
			printDesiredResult(sortedMap);

			System.out.println("Finished processing at " + new Date());
			System.out.println("Number of records procesed are " + count);
		} catch (FileNotFoundException e) {
			// Output expected FileNotFoundExceptions.
			System.out.print(e.getStackTrace());
		} catch (Exception exception) {
			// Output unexpected Exceptions.
			System.out.println(exception.getStackTrace());
		}
		finally {
		    if (sc != null)
		        sc.close();
		}
	}

	/***
	 * This Method will responsible for insertion of the Values and there count in
	 * the mongo DB.
	 * 
	 * @param sortedMap
	 * @param hostname
	 * @param port
	 */
	public static void dbInsert(SortedSet<Entry<String, Double>> sortedMap, String hostname, int port) {
		System.out.println("Database insertation Started at " + new Date());
		BasicDBObject document = new BasicDBObject();
		DBCollection table;
		DB mongoDb = DBConnection.connectDB(hostname, port);
		// Creating the Table Name floow in the DB
		table = mongoDb.getCollection("floow");
		double i = 0;
		if (table.count() > 0) {
			table.drop();
		}
		table = mongoDb.getCollection("floow");
		for (Map.Entry<String, Double> entry : sortedMap) {
			document.put("_id", ++i);
			document.put("word", entry.getKey());
			document.put("count", entry.getValue());
			table.insert(document);
			// System.out.println("Key: " + entry.getKey() + ". Value: " +
			// entry.getValue());
		}
	}

	/***
	 * This method will display the most repeatable word in the Console.
	 * 
	 * @param sortedMap
	 */
	public static void printDesiredResult(SortedSet<Entry<String, Double>> sortedMap) {
		Map.Entry<String, Double> firstEntry = sortedMap.first();
		Map.Entry<String, Double> lastEntry = sortedMap.last();
		// System.out.println("The least repeatable word is : "+firstEntry.getKey()+"
		// and the count is : "+firstEntry.getValue());
		System.out.println("The most repeatable word is : \"" + lastEntry.getKey() + "\" \nand the count is : "
				+ lastEntry.getValue());
	}

}
