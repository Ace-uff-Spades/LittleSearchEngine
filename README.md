# LittleSearchEngine

Using Hashtable to implement a "little" search engine. This assignment was completed for CS 112 -- Data Structures at Rutgers University (Spring 2015). 

The little search engine could complete two tasks:
  1. Gather and index keywords that appear in a set of plain text document
  2. Search for user-input keywords against the index and return a list of matching documents in which thes keywords occur.
  
  
The following API was implemented


     public class LittleSearchEngine {
     public LittleSearchEngine                                 //Creates new Hashmaps of keywords and noise words
     public void makeIndex(String docsFile, String noiseFile)  //indexes all keywords found in all documents
     public HashMap loadKeyWords(String docFile)               //Loads all keywords from into hashmap
     public void mergeKeyWords(Hashmap kws)                    //Merges keywords from single document into master keywordsIndex
     public void getKeyword(String word)                       //Returns the keyword (without punctuation) if it passes the keyword test
     public ArrayList insertLastOccurrence(ArrayList occs)     //Insert word and occurence into correct location in list using binary search
     public ArrayList top5search(Strign kw1, String kw2)       //Returns top 5 documents containing either kw1 or kw2 in order
     

Please check the assignment specification for full details.

This project was completed on April 13, 2015
