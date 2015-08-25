# LittleSearchEngine

Using Hashtable to implement a "little" search engine. This assignment was completed for CS 112 -- Data Structures at Rutgers University (Spring 2015). The official assignment can be found here: http://www.cs.rutgers.edu/courses/112/classes/spring_2015_venugopal/progs/prog4/prog4.html

The little search engine could complete two tasks:
  1. Gather and index keywords that appear in a set of plain text document
  2. Search for user-input keywords against the index and return a list of matching documents in which thes keywords occur.
  
  
The following API was implemented


     public void getKeyword(String word): Given a list of keywords, checks if this word is a keyword. The word must only contain alphabetic characters with no leading or trailing punction (String processing required)
     public void mergeKeywords(): Load keywords from a specific document to the master Hashtable that contains all keywords from all documents
     public ArrayList<Integer> insertLastOccurrence(): 
     public ArrayList<String> top5search(): Returns a list of documents in which 2 keywords appear most frequenly, arranged in descending order of frequencies.


This project was completed on April 13, 2015
