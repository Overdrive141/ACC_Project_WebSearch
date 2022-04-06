import java.io.File;
import java.util.Locale;
import java.util.StringTokenizer;

import searchtrees.SplayTree;
import textprocessing.In;


public class common_words {

	public static void common()
	{
		File[] allfiles=FileService.getFiles();
		System.out.println("----------------");
		System.out.println("find common words starts:");
		SplayTree<String> splay = new SplayTree<String>( );
		for(int i=0;i<allfiles.length;i++)
		{
			if(allfiles[i].isFile())
			{
				File oneFile= new File(Config.filePath+ allfiles[i].getName());

				String[] input_data=new In(oneFile).readAllLines();
//				String[] keys=null;
//				String delim= " ,.()\t\n\r";

				for(String temp: input_data)
				{
//					StringTokenizer st= new StringTokenizer(temp, delim , false);
					StringTokenizer st = new StringTokenizer(temp, "0123456789 ,`*$|~(){}_@><=+[]\\?;/&#-.!:\"'\n\t\r");
					while(st.hasMoreElements())
					{  
						String current= st.nextToken().toLowerCase(Locale.ROOT);
						if(splay.contains(current)==false)
						{
							splay.insert(current);
						}
						else
						{
							;
						}
//						System.out.println("Token:" + st.nextToken());  
					} 
				}
				

			}
			else
			{
				System.out.println("not a file: "+i);
			}
		}
		System.out.println("----------------");
		splay.printTree( );
		System.out.println("----------------");
		for(int i=0;i<10;i++)
		{
			String result=splay.root.accesselement();
			System.out.println("The most common word: "+ i +"is: "+ result);
			splay.remove(result);
		}
	}
	
//	public static void main(String args[])
//	{
//		common();
//	}
	
	
	
}
