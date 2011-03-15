package net.gamerservices.iplock;

import java.util.Timer;

import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import java.io.File;
import java.net.UnknownHostException;

/**
 * Sample plugin for Bukkit
 *
 * @author Mixxit
 */
public class iplock extends JavaPlugin {
    private final PListener playerListener = new PListener(this);
    private final BListener blockListener = new BListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
   
    // NOTE: There should be no need to define a constructor any more for more info on moving from
    // the old constructor see:
    // http://forums.bukkit.org/threads/too-long-constructor.5032/

    private final String FILE_IPLOCKUSERS = "iplock.users";
    private ArrayList<String> iplockUsersList = new ArrayList<String>();
    private File configFolder;
    
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("iplock ended");
    }

    
    
    
    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Normal, this);

        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        
        Timer timer = new Timer();
        System.out.print("iplock save timer has begun");
        timer.schedule(new savetask(this), 60000);
        
        configFolder = getDataFolder();
        // Create configuration file
        if (!configFolder.exists())
        {
        	System.out.print("Creating iplock config folder");
        	configFolder.mkdir();
        }
        File iplockUsers = new File(configFolder.getAbsolutePath() + File.separator + FILE_IPLOCKUSERS);
        if (!iplockUsers.exists())
        {
        	System.out.print("Creating iplock user file");
        	try 
        	{
        		iplockUsers.createNewFile();
        	} catch (IOException e)
        	{
        		System.out.println("Error creating users file");
        	}
        }
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        loadIPLockUsers();
        
    }
    
    public boolean isUser(String name)
    {
    	
    	for (String player : iplockUsersList)
        {
    		if (player.compareToIgnoreCase(name) == 0)
    		{
    			return true;
    		}
    		
    		
        }
    
    	return false;
    	
    }
    
    public boolean isIPOnList(String ip)
    {
    	for (String player : iplockUsersList)
        {

    		if (player.split("=")[0].compareToIgnoreCase(ip) == 0)
    		{


    			return true;
    		}
    		
    		
        }
    	
    	return false;
    }
    
    public boolean isNameOnList(String name)
    {
    	for (String player : iplockUsersList)
        {

    		if (player.split("=")[1].compareToIgnoreCase(name) == 0)
    		{
    	    	
    			return true;
    		}
    		
    		
        }
    	
    	return false;
    }
    
    public boolean isValid(String name)
    {
    	if (!isUser(name))
    	{
    		String ip = name.split("=")[0];
    		String pname = name.split("=")[1];
    		if (!isIPOnList(ip) && !isNameOnList(pname))
    		{
	    		// create user
				System.out.print("iplock : allowing " + name.split("=")[1] + " because they are new!");

	    		createUser(name);
	    		
	    		// let them play    		
	    		return true;
    		}
    	}
    	
    	// does ip match?
    	String pip = name.split("=")[0];
    	String pname = name.split("=")[1];
    	
    	for (String player : iplockUsersList)
        {
    		if (player.split("=")[0].equalsIgnoreCase(pip))
    		{
    			if (player.split("=")[1].equalsIgnoreCase(pname))
    			{
    				System.out.print("iplock : allowing " + name.split("=")[1] + " because their ip matches");
    				return true;
    			}
    		}
        }
    	
    	// Look at the list and see if we can find a player on the same subnet
    	java.net.InetAddress inetAddthem;
    	try {
			inetAddthem = java.net.InetAddress.getByName(name.split("=")[0]);
			String theirip = inetAddthem.getHostAddress();
			System.out.println ("IP Address is : " + theirip);
	    	String ippart1 = theirip.split("\\.")[0];
	    	String ippart2 = theirip.split("\\.")[1];
		
    	
    	
	    	for (String player : iplockUsersList)
	        {
	    		if (player.split("=")[1].equalsIgnoreCase(pname))
	    		{
	    			java.net.InetAddress inetAdd;
					try {
						inetAdd = java.net.InetAddress.getByName(player.split("=")[0]);
						System.out.println ("IP Address is : " + inetAdd.getHostAddress());
	
		    			String currentip = inetAdd.getHostAddress();
		    			if (currentip.split("\\.")[0].equalsIgnoreCase(ippart1) && currentip.split("\\.")[1].equalsIgnoreCase(ippart2))
		    			{
		    				 System.out.print("iplock : ALLOWED " + name.split("=")[1] + " because they are on same subnet");
		    				 return true;
		    			}
						
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
	        }
    	
    	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		 System.out.print("iplock : DENIED " + name.split("=")[1] + " because they are on the list but not on the same subnet");    	
    	return false;
    	
    }
    
    public void createUser(String name)
    {
    	System.out.println("IPLOCK: Creating new user: " + name);
    	iplockUsersList.add(name);
    	
    	
    }
       
    
    public boolean loadIPLockUsers()
    {
    	try 
    	{
    		iplockUsersList.clear();
    		BufferedReader reader = new BufferedReader(new FileReader((configFolder.getAbsolutePath() + File.separator + FILE_IPLOCKUSERS)));
    		String line = reader.readLine();
    		
    		while (line != null)
    		{
    			iplockUsersList.add(line);
    			line = reader.readLine();
    			
    		}
    		reader.close();
    		
    	} catch (Exception e)
    	{
    		
    	}
    	return true;
    	
    }
    
    public boolean saveIPLockUsers()
    {
    	Timer timer = new Timer();
        timer.schedule(new savetask(this), 60000);

    	try
        {
          BufferedWriter writer = new BufferedWriter(new FileWriter((configFolder.getAbsolutePath() + File.separator + FILE_IPLOCKUSERS)));
          for (String player : iplockUsersList)
          {
            writer.write(player);
            writer.newLine();
          }
          writer.close();
        } catch (Exception ex)
        {
          System.out.println(ex);
          return false;
        }
        return true;
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}