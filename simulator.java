import java.util.ArrayList;
import java.lang.Math;

public class simulator
{
	private int [][] sensor;
	private int [][] launcher;
	private int [][] interceptor;
	
	private int [][] foe;
	private int [][] missile;
	
	String log;
	const int maxSizeOfEachItem=256;
	
	protected int foeHits;
	protected int foeIntercepted;
	
	//tallyer
	protected int tFoe, tMissle, tSensor, tLauncher, tInterceptor;
	
	public simulator()
	{
		foeHits=0;
		foeIntercepted=0;
		
		//total ____ that are active
		tFoe=0;
		tMissle=0;
		tSensor=0;
		tLauncher=0;
		tInterceptor=0;
		
		foe = new int[maxSizeOfEachItem][5];
		/*
		0. Live
		1. this.X
		2. this.Y
		3. cooldown=3
		4. currentCooldown=0
		5. Launch at T
		6. Ammount to launch
		*/
		
		missile = new int[maxSizeOfEachItem][6];
		/*
		0. Live
		1. this.X
		2. this.Y
		3. target.x
		4. target.y
		5. isAssignedInterceptor
		*/
		
		sensor = new int[maxSizeOfEachItem][7]; 
		/*
		0. Live
		1. this.X
		2. this.Y
		3. range X min
		4. range Y min
		5. range X max
		6. range Y max
		*/
		
		launcher = new int[maxSizeOfEachItem][5];
		//Live, X, Y, cooldown, currentCooldown
		/*
		0. Live
		1. this.X
		2. this.Y
		3. currentCooldown=0
		4. cooldown=5
		*/
		
		interceptor = new int[maxSizeOfEachItem][3];
		/*
		0. Live
		1. distanceleft
		2. chanceItWorks
		3. Which missle
		*/
	}
	
	/*
	Describe: Our runner function
	Input: None
	Output: None
	*/
	public static void simRunner(int duration)
	{
		simpleGameSetup();
		
		toLog(-1, "Game setup");
		
		for(int t=0; t<duration-1; t++)//what makes the simulation run
		{
			//----------Missle Launchers----------
			for(int i=0; i<maxSizeOfEachItem; i++)
			{
				if(foe[i][0]==1)//if live
				{
					if(foe[i][3] < foe[i][4]) //if cooldown not replenished
					{
						foe[i][3]++; //1 replenish
					}
					if(t>=foe[i][5] && foe[i][4]<foe[i][3]) //if launchtime and cooldown is permissible
					{
						createMissle(foe[i][1], foe[i][2], foe[i][1], 10); //goto (currentx,10)
						foe[i][3]=0; //Reset current cooldown
					}
				}
				//if live and time is right and cooldown
				
			}
			
			//----------Move Missles----------
			for(int ia=0; i<maxSizeOfEachItem; i++)
			{
				if(missile[ia][0]=1) //if we have a live missle
				{
					//Do we move x or y
					if(missile[ia][2] > missile[ia][4]) //this.y<target.y
					{
						missile[ia][1]--; // down south to target
					}else
					{
						//left or right
						if(missile[ia][2] > missile[ia][3]) //this.x > target.x
						{
							missile[ia][]--;
						}else{
							missile[ia][]++;
						}
					}
					
					//Did we hit: x=x y=y
					if((missile[ia][1]==missile[ia][3])&&(missile[ia][2]==missile[ia][4]))
					{
						foeHits++;
						missile[i][0]=0;
					}
				}
			}
			
			//----------Detect Missles----------
			
			/*
			For each live sensors
			Search its box
				If something is found
				make sure it is not on the detected Missle list
				Add it to the list
			*/
			ArrayList<int> detectedMisslesIndex = new ArrayList<int>();
			
			for(int ir=0; ir<maxSizeOfEachItem; ir++) //for all the sensors
			{
				if(sensor[ir][0]==1) //if the sensor is live
				{
					for(int jr=0; jr<maxSizeOfEachItem; jr++) //check all missles
					{
						if
						(
							sensor[ir][3] <= missile[jr][1] && //s. min x <= m.x
							sensor[ir][5] >= missile[jr][1] && //s. max x >= m.x
							sensor[ir][4] <= missile[jr][2] && //s. min y <= m.y
							sensor[ir][6] >= missile[jr][2] //s. max y >= m.y
						) //if a given missle is in the box
						{
							//make sure it is not in the detectedMissle List
							if(!detectedMisslesIndex.contains(jr))
							{
								//add it to the list
								detectedMisslesIndex.add(jr);
							}
						}
					}
				}
			}
			
			//----------Launch Interceptors----------
			
			/*
			For each Launcher of interceptors
			If it is live and a valid cooldown
			Create an interceptor for the first item on the list.
			*/
			if(detectedMisslesIndex.size()>0)//if their are missles
			{
				for(int iw=0; iw<maxSizeOfEachItem; iw++)
				{
					if(launcher[iw][0]==1 && launcher[iw][3]==launcher[iw][4]) //if live and cooled down
					{
						int currentMissleIndex=detectedMisslesIndex.get(0);
						if(missle[currentMissleIndex][5]==0)
						{
							int distanceToMissle= Math.abs(launcher[iw][1]-missile[currentMissleIndex][1]) 
								+ Math.abs(launcher[iw][2]-missile[currentMissleIndex][2]);
							
							createInterceptor(distanceToMissle, 67, currentMissleIndex); //create missle
							
							missle[currentMissleIndex][5]=1; //isAssignedInterceptor
							launcher[iw][3]=0; //reset cooldown
							
							detectedMisslesIndex.remove(0);
						}
					}
				}
			}
			
			
			//----------Run Interceptors----------
			for(int iz=0; iz<maxSizeOfEachItem; iz++)
			{
				//Reduce distance by 2 (1 for us, one for their movement)
				if(interceptor[iz][0]==1)
				{
					interceptor[iz][1]-=2;
					if(interceptor[iz][1]<=0)
					{
						//upon hit
						if(chance/100 < getRandomNumber(0,100) )
						{
							//Eliminate the missle
							missle[interceptor[iz][3]][0]=0; //missle no longer live
							toLog(t, "Interceptor Worked");
						}else
						{
							toLog(t, "Interceptor Failed");
						}
					}
				}
			}
		}
		toPrint("Game completed");
		printr(log); //Print the log
	}
	
	public static void createFoe(int x, int y, int cooldown, int launchAtT, int ammoQuanitityToLaunch)
	{
		tFoe++;
		int index=tFoe;
		
		foe[index][0]=1;
		foe[index][1]=x;
		foe[index][2]=y;
		foe[index][3]=cooldown;
		foe[index][4]=cooldown;
		foe[index][5]=launchAtT;
		foe[index][6]=ammoQuanitityToLaunch;
	}
	
	public static void createMissle(int x, int y, int targetX, targetY)
	{
		tMissle++;
		int index=tMissle;
		
		missle[index][0]=1;
		missle[index][1]=x;
		missle[index][2]=y;
		missle[index][3]=targetX;
		missle[index][4]=targetY;
		missle[index][5]=0;
	}
	
	/*
	Describe: Make the sesnor entry
	Input:
	Output: None
	*/
	public static void createSensor(int x, int y, int sizeX, int sizeY)
	{
		if(sizeX%2!=0)
		{
			sizeX++;
		}
		if(sizeY%2!=0)
		{
			sizeY++;
		}
		
		tSensor++;
		int index=tSensor;
		
		sensor[index][0]=1;
		sensor[index][1]=x;
		sensor[index][2]=y;
		sensor[index][3]=x-(sizeX/2);
		sensor[index][4]=y-(sizeY/2);
		sensor[index][5]=x+(sizeX/2);
		sensor[index][6]=y+(sizeY/2);
	}
	
	/*
	Describe: Make the launcher entry
	Input:
	Output: None
	*/
	public static void createLauncher(int x, int y, int cooldown)
	{
		tLauncher++;
		int index=tLauncher;
		
		launcher[index][0]=1;
		launcher[index][1]=x;
		launcher[index][2]=y;
		launcher[index][3]=cooldown;
		launcher[index][4]=cooldown;
	}
	
	/*
	Describe: Make the interceptor entry
	Input:
	Output: None
	*/
	public static void createInterceptor(int distanceLeft, int chanceItWorks, int missleIndex)
	{
		tInterceptor++;
		int index = tInterceptor;
		interceptor[index][0]=1;
		interceptor[index][1]=distanceLeft;
		interceptor[index][2]=chanceItWorks;
		interceptor[index][3]=missleIndex;
	}
	
	public static void simpleGameSetup()
	{
		createFoe(45, 10, 5, 1, 7);
		createFoe(55, 25, 12, 30, 3);
		
		createSensor(14, 30, 100, 100);
		createSensor(14, 0, 20, 10);
		createSensor(14, 20, 10, 100);
		
		createLauncher(2, 5, 4);
		createLauncher(12, 12, 20);
	}
	
	/*
	Describe: Prepare the log
	Input: Time, Message
	Output: None
	*/
	public static void toLog(int t, messageOut)
	{
		log+="Time: " + t + messageOut + "\n";
	}
	
	/*
	Describe: Write a message to the console
	Input: Message
	Output: None
	*/
	public static void printr(String toPrint)
	{
		System.out.println(toPrint);
	}
	
	//Guideance: https://www.baeldung.com/java-generating-random-numbers-in-range
	public int getRandomNumber(int min, int max)
	{
		return (int) ((Math.random() * (max - min)) + min);
	}
}