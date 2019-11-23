# TheFloow

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* UnderStanding of the Given Problem and The Solution Based on the Problem Statement

## General info
This project is simple Java Program to process the large File and Insert it into the Mongo Database after Processing the Data.
	
## Technologies
Project is created with:
* Java version: 7
* Mongo library version: mongo-java-driver-3.4.3.jar
	
## Setup
1: I have created the jar file with name FileReader.jar which contains all the dependencies.To run this jar file from the windowns cmd
   use the below command:

    java -jar [jarfileName] -source [filename] -mongo [hostname]:[port]
     
    Step1: jarFileName: Please pass the jar file in the Parameter e.g FileReader.jar
     
    Step2: -source [filename]: Inside the filename please pass the source fileName from which you need to read the content.Please make sure 
                               that jarfile and source file location should be in the same folder.If the location is different please
                               pass the full path of the source file in the above command.
                               
    Step3: -mongo [hostname:port]: Please pass the hostname and port to connect with the Mongo Database.
    
 ## UnderStanding of the Given Problem
 
 1: As per the problem statment we need to analyse a file with a large body of text and
    to count unique words so that the most common.
 
 2: I have used the file enwiki-latest-abstract.xml downloaded from the below location as a sample to develop my solution
    based on the problem statement and my undertanding.
 
     https://dumps.wikimedia.org/enwiki/latest/
  
  3: Since the above file was very big we first printed the few contents in the console to understand the format of the information.
 
 For Example: 
<doc>
<title>Wikipedia: An American in Paris</title>
<url>https://en.wikipedia.org/wiki/An_American_in_Paris</url>
<abstract>postopcaptionThemes from An American in Paris}}</abstract>
<links>
<sublink linktype="nav"><anchor>Background</anchor><link>https://en.wikipedia.org/wiki/An_American_in_Paris#Background</link></sublink>
<sublink linktype="nav"><anchor>Composition</anchor><link>https://en.wikipedia.org/wiki/An_American_in_Paris#Composition</link></sublink>
</links>
</doc>  

4: As per my understanding we need to read the above doc and extract the meaningful information from it and store the details 
   in Mongo Database.

5: I have developed the solution to fetch the details inside the tags like title, abstract and anchor from the file 
   and split them into words based on the space and removed all the special characters. 
   
6: After splitting them into words, we have counted them and inserted it into the Mongo database in the sorted order with their count.
   I have stored it in the table call 'floow' inside the schema 'theFloowDb' in Mongo DB.

7: My solution will count the words repeated in the document from the above three tags mentioned in the point 5 and insert it into 
   the Mongo database with their count.
   For example if the document contains the word 'Composition' 10 times it will store details in the MongoDB with 
   below format in the sorted order.
   
   _id: Auto Generated
   word:"Composition"
   count:10

 8: I have also printed the most repeatable word in the file at the console.
 
 9: Please do let me know if any modification required or if I misinterpreted the question.
    
            
                         
     
                         
