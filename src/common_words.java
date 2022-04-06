import java.io.File;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeMap;

import textprocessing.In;
import textprocessing.TST;


public class common_words {

	public static void common()
	{
		File[] allfiles=FileService.getFiles();
		System.out.println("----------------");
		System.out.println("find common words starts:");
		TST<Integer> tst = new TST<Integer>();
		int total_count=0;
		for(int i=0;i<allfiles.length;i++)
		{
			if(allfiles[i].isFile())
			{
				File oneFile= new File(Config.filePath+ allfiles[i].getName());

				String[] input_data=new In(oneFile).readAllLines();
				for(String temp: input_data)
				{
					StringTokenizer st = new StringTokenizer(temp, "0123456789 ,`*$|~(){}_@><=+[]\\?;/&#-.!:\"'\n\t\r");
					while(st.hasMoreElements())
					{  
						String current= st.nextToken().toLowerCase(Locale.ROOT);
						if(tst.get(current)== null)
						{
							tst.put(current, 1);
							total_count++;
						}
						else
						{
							int occurance= tst.get(current);
							tst.put(current, occurance+1);
						}
					} 
				}
			}
			else
			{
				System.out.println("not a file: "+i);
			}
		}
		System.out.println("----------------");
//         print results
		TreeMap<Integer, String> t= new TreeMap<Integer, String>();
        for (String key : tst.keys()) 
        {
           int fre=tst.get(key);
//           System.out.println("word: "+ key + " frequency: " + fre);
           t.put(fre, key);
        }
        for(int i=0;i<10;i++)
        {
        	int a=t.lastEntry().getKey();
        	String b=t.lastEntry().getValue();
        	System.out.println("word: "+ b + " ,frequency: "+ a);
        	t.remove(a);
        }
		System.out.println("----------------");
		
		
	}
	
//	public static void main(String args[])
//	{
//		common();
//	}
	
	
	
}
