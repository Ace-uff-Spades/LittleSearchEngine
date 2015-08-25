package search;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
public class LSEDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		LittleSearchEngine engine=new LittleSearchEngine();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter document file name => ");
		String docsFile=br.readLine();
		System.out.print("Enter noise words file name => ");
		String noiseWordsFile=br.readLine();
		engine.makeIndex(docsFile, noiseWordsFile);
		System.out.println(engine.top5search("jumped", "moon"));
	}

}
