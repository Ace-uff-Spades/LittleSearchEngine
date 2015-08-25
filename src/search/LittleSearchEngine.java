package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> map=new HashMap<String, Occurrence>(100,2f);
		Scanner sc= new Scanner(new File(docFile));
		while(sc.hasNext()){
			String word=sc.next();
			word=this.getKeyWord(word);
			if (word==null)
				continue;
			//if it is not a noise word
			if(!noiseWords.containsKey(word)){
				if(!map.containsKey(word)){
					Occurrence occur=new Occurrence(docFile,1);
					map.put(word, occur);
				}
				else if (map.containsKey(word)){
					Occurrence temp=map.get(word);
					temp.frequency++;
					map.put(word, temp);
				}
			}
		}
		
		return map;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		Set<String> set=kws.keySet();
		Iterator<String> iterator=set.iterator();
		while(iterator.hasNext()){
			String key=iterator.next();
			Occurrence occur=kws.get(key);
			ArrayList<Occurrence> list=keywordsIndex.get(key);
			if(list==null){
				list=new ArrayList<Occurrence>();
				keywordsIndex.put(key, list);
			}
			
			list.add(occur);
			this.insertLastOccurrence(list);
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		
		// Strips all trailing punctuation
		word=this.stripString(word);
		if (word==null)
			return null;
		if((noiseWords.containsKey(word)==false) && (this.isAlpha(word)==true)){
			word=word.toLowerCase();
			return word;
		}
		return null;
	}
	
	private String stripString(String word){
		ArrayList<String>array=new ArrayList<String>();
		array.add(".");
		array.add(",");
		array.add("?");
		array.add(":");
		array.add(";");
		array.add("!");
		if(this.isAlpha(word.substring(0,1))==false)
			return null;
		while(array.contains(word.substring(word.length()-1)))
			word=word.substring(0,word.length()-1);
		return word;
	}
	
	private boolean isAlpha(String name) {
	    return name.matches("[a-zA-Z]+");
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		ArrayList<Integer> integerList=new ArrayList<Integer>();
		
		if(occs.size()==1){
			integerList.add(0);
			return integerList;
		}
		
		
		//Takes frequency of occurrences and places into array, descending order
		int [] indices= new int[occs.size()-1];
		for(int i=0;i<occs.size()-1;i++){
			Occurrence occur=occs.get(i);
			indices[i]=occur.frequency;
		}
		// Target, last element in occs
		int target=occs.get(occs.size()-1).frequency;	
		
		
		
		//Binary Search
		int low=0;
		int high=indices.length-1;
		
		while(low<=high){
			int mid=low+(high-low)/2;
			if(indices[mid]==target){
				integerList.add(mid);
				break;
			}
			else if(indices[mid]<target)
				high=mid-1;
			else if(indices[mid]>target)
				low=mid+1;
			if((low<=high)==false)
				break;
			integerList.add(mid);
		}
		int secondLast=occs.get(occs.size()-2).frequency;
		if(integerList.size()==0)
			integerList.add(0);
		
		if(!(target<secondLast)){
			Occurrence o=occs.remove(occs.size()-1);
			int location=integerList.get(integerList.size()-1);
			occs.add(location, o);
			
		}
		
		
		
		return integerList;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		//Creates ArrayList of documents to return
		ArrayList<String> documents=new ArrayList<String>();
		
		//Gets ArrayList
		ArrayList<Occurrence> l1=keywordsIndex.get(kw1);
		ArrayList<Occurrence>l2=keywordsIndex.get(kw2);
		
		
		//Gets Iterators
		ListIterator<Occurrence>iter1=l1.listIterator();
		ListIterator<Occurrence>iter2=l2.listIterator();
		
		// Gets first element in each list
		Occurrence firstOccur=iter1.next();
		Occurrence secondOccur=iter2.next();
		
		
		while((firstOccur!=null)&&(secondOccur!=null)){
			if(documents.size()==5)
				break;
			if(firstOccur.frequency>=secondOccur.frequency){
				if(!documents.contains(firstOccur.document))
					documents.add(firstOccur.document);
				try{
				firstOccur=iter1.next();
				}
				catch(NoSuchElementException e){
					firstOccur=null;
				}
			}
			
			else if(secondOccur.frequency>firstOccur.frequency){
				if(!documents.contains(secondOccur.document))
					documents.add(secondOccur.document);
				try{
				secondOccur=iter2.next();
				}
				catch(NoSuchElementException e){
					secondOccur=null;
				}
			}
				
		
		}
		
		if(firstOccur==null && secondOccur!=null){
			while(secondOccur!=null && documents.size()!=5){
				if(!documents.contains(secondOccur.document))
					documents.add(secondOccur.document);
				try{
				secondOccur=iter2.next();
				}
				catch(NoSuchElementException e){
					secondOccur=null;
				}
			}
		}
		
		
		else if(secondOccur==null && firstOccur!=null){
			while(firstOccur!=null && documents.size()!=5){
				if(!documents.contains(firstOccur.document))
					documents.add(firstOccur.document);
				try{
				firstOccur=iter2.next();
				}
				catch(NoSuchElementException e){
					firstOccur=null;
				}
			}
		}
		
		
		return documents;
	}
}
