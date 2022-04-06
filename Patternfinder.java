import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import textprocessing.In;

public class Patternfinder {
	
	public static void find_phonenumber()
	{
		String phonepattern= "(\\+1)?[(]?(\\d){3}[)]?[- ]?[(]?(\\d){3}[)]?[- ]?(\\d){4}";
		Pattern r=Pattern.compile(phonepattern);
		File[] allfiles=FileService.getFiles();
		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for(int i=0;i<allfiles.length;i++)
		{
			if(allfiles[i].isFile())
			{
				File oneFile= new File("new/"+ allfiles[i].getName());

				String[] input_data=new In(oneFile).readAllLines();
				for(String temp: input_data)
				{
					Matcher m1=r.matcher(temp);
					while(m1.find())
					{
						System.out.println("Found phonenumber from file "+i+": " + m1.group() );
					}
				}
			}
			else
			{
				System.out.println("not a file: "+i);
			}
		}
		System.out.println("----------------");
	}
	
	public static void find_emailaddress()
	{
		String emailpattern="[\\w-]+@([\\w-]+\\.)+[\\w-]+";
		Pattern r=Pattern.compile(emailpattern);
		File[] allfiles=FileService.getFiles();
		
		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for(int i=0;i<allfiles.length;i++)
		{
			if(allfiles[i].isFile())
			{
				File oneFile= new File("new/"+ allfiles[i].getName());

				String[] input_data=new In(oneFile).readAllLines();
				for(String temp: input_data)
				{
					Matcher m=r.matcher(temp);
					while(m.find())
					{
						System.out.println("Found emailaddress from file "+i+": " + m.group() );
					}
				}
			}
			else
			{
				System.out.println("not a file: "+i);
			}
		}
		System.out.println("----------------");
	}
	
	public static void find_url()
	{
		String Urlpattern= "http(s)?:\\/\\/[\\w-]+(.){1}(?:[\\w\\-]+)+(.){1}[\\w-._~:/?\\[\\]@!&'\\(\\)\\*\\+,;=]+";
		Pattern r=Pattern.compile(Urlpattern);
		File[] allfiles=FileService.getFiles();
		
		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for(int i=0;i<allfiles.length;i++)
		{
			if(allfiles[i].isFile())
			{
				File oneFile= new File("new/"+ allfiles[i].getName());

				String[] input_data=new In(oneFile).readAllLines();
				for(String temp: input_data)
				{
					Matcher m=r.matcher(temp);
					while(m.find())
					{
						System.out.println("Found Url from file "+i+": " + m.group() );
					}
				}
			}
			else
			{
				System.out.println("not a file: "+i);
			}
		}
		System.out.println("----------------");
	}
}
