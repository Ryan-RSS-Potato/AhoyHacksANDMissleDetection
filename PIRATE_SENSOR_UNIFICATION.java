	/*
	Describe:
	Input:
	Output:
	*/

import java.io.File; //Use files
import java.io.IOException; //Avoid errors

public class PIRATE_SENSOR_UNIFICATION
{
	/*
	Describe: Our runner function
	Input: None
	Output: None
	*/
	public static void main(String[] args)
	{
		simulator s = new simulator();
		s.simRunner(523);
	}
	
	/*
	Describe: Create a file
	Input: None
	Output: Did it work?
	*/
	/*
	public boolean makeFile()
	{
		try
		{
			File scenarioDefine = new File("decleartion.txt");
			if (scenarioDefine.createNewFile())
			{
				printr("File created: " + scenarioDefine.getName());
			}else
			{
				printr("File exists already");
			}
		}catch(IOException e)
		{
			printr("An error occurred.");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}*/
	
	/*
	Describe:
	Input:
	Output: None
	*/
	public void printr(String toPrint)
	{
		System.out.println(toPrint);
	}
}
