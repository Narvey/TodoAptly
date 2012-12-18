/*
 * Chris card
 * Nathan Harvey
 * 11/15/12
 * This class provides code to sync with other files
 * and will be started by a intent service and will be using a singleton pattern
 * also stores the user preferences for sync
 */

package csci422.CandN.to_dolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.ContentValues;

public class FileSync {

	//Singleton pattern stores its own instance
	private static FileSync instance = null;

	//this flag will be set true if the user updated preferences to
	//sync with google cal
	private boolean toSyncCal;

	//to be used later but for sync the data base
	//with the todo.txt file format or just a file
	private boolean toSyncFile;

	//this tells other methods that sync is running so that they will not
	//with the state of this class
	private static boolean isRunning;

	/**
	 * Private constructor so that this class controls
	 * and ensures only one instance of the class
	 */
	private FileSync()
	{
		toSyncCal = false;
		toSyncFile = false;
		isRunning = false;
	}

	/**
	 * This class returns an instance of its self if instance exists
	 * or creates a new instance (enforces the singleton pattern)
	 * @return: new instance if one doesn't exist or a single instance
	 * 			of the class
	 */
	public static FileSync getInstance()
	{
		if(instance == null)
		{
			instance = new FileSync();
		}

		return instance;
	}

	/**
	 * this method loads the past state of this class from synchelper
	 * @param h data base from which it will get the previous state
	 */
	public void load(SyncHelper h)
	{
		Cursor c = h.getAll();
		if(c.moveToFirst())
		{
			if(h.getSync(c).equals("true"))
			{
				toSyncCal = true;
			}

			if(h.getSave(c).equals("true"))
			{
				toSyncFile = true;
			}
		}
		c.close();
	}

	/**
	 * This saves the current state of the flags to SyncHelper data base
	 * @param h place to save to
	 */
	public void save(SyncHelper h)
	{
		Cursor c = h.getAll();
		if(c.moveToFirst())
		{//if the data base has at least one only needs one entry
			h.update(c.getString(0), toSyncCal, toSyncFile);
		}
		else
		{//other wise insert into the data base
			h.insert(toSyncCal, toSyncFile);
		}
		c.close();
	}


	/**
	 * Toggles toSyncCal to the opposite value of what it is at the moment
	 */
	public void toggleCalSync()
	{
		//toggles toSync to the opposite of what it is 
		toSyncCal = (toSyncCal ? false : true);
	}

	/**
	 * This method returns if it is to sync with google calender
	 * @return: true if it is to sync with calendar
	 */
	public boolean isSyncCal()
	{
		return toSyncCal;
	}

	/**
	 * This method returns if it is to sync with a todo.txt file
	 * @return: true if it is to sync with file
	 */
	public boolean isSyncFile()
	{
		return toSyncFile;
	}

	/**
	 * Toggles toSaveFile to the opposite value of what it is at the moment
	 * This alows us to set the value from another file with out having to know its value
	 */
	public void toggleSaveFile()
	{
		toSyncFile = (toSyncFile ? false : true);
	}

	/**
	 * WARNING: ONLY CALL THIS FUNCTION FROM AN INTENTSERVICE OR AYSNCTASK OR RISK SLOWING DOWN
	 *			THE UI THREAD OR HAVE IT STOP BECAUSE USER CLOSES APP
	 *
	 * This function saves the entire data base stored in ToDoHelper to a
	 * file the might be able to be moved to another app (to be implemented latter mabe)
	 * 
	 * @param: ToDoHelper that has the data base since this class can't get its
	 *			calling context.
	 */
	public void saveToFile(ToDoHelper help)
	{
		if (toSyncFile) 
		{
			Cursor c = help.getAll("title");
			c.moveToFirst();
			String datum = "";
			do{
				if(help.getPriority(c)>-1)datum += "("+(char)('C'-help.getPriority(c))+")";	
				//TODO: Save directly to file from this string each time.
			}while(c.moveToNext());

			c.close();
		}//else do nothing
	}


}
