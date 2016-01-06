package com.myjavapapers.timer;


import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerExampleOnce {
	public static void main(String args[]){
		//Getting current date and adding 5 seconds to it
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, 30);
		
		//Displaying current time
		System.out.println("Time now is -> " + new Date());
		
		//Creating timer which executes once after five seconds
		Timer timer = new Timer();
		timer.schedule(new TaskExampleOnce(), c.getTime());
	}
}

class TaskExampleOnce extends TimerTask{
	//This task will execute just once after seven seconds of starting the program
	public void run(){
		System.out.println("I will be displayed after five seconds" + " ->" + new Date());
	}
}