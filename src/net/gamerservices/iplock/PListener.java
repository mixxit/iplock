package net.gamerservices.iplock;

import java.net.InetSocketAddress;
import org.bukkit.Location;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.entity.Player;


/**
 * Handle events for all Player related events
 * @author <yourname>
 */
public class PListener extends PlayerListener {
    private final iplock plugin;

    public PListener(iplock instance) {
        plugin = instance;
        
    }

    //Insert Player related code here
    @Override
    public void onPlayerLogin(PlayerLoginEvent event)
    {
    	System.out.println("debug: " + event.getPlayer().getName());
    	try
    	{
	    	Player player = event.getPlayer();
	    	String name = player.getName();
	    	//System.out.println("debug: " + event.getPlayer().getAddress().getHostName().toString());
	    	
	    	// Special Characters Addon
	    	
	    	if (plugin.spechar.equals("true"))
	    	{
	    	
		    	if (!name.matches("[A-Za-z0-9]+"))
		    	{
		            System.out.println("iplock - kicked (" + player + "), special characters in name");
		    		event.disallow(Result.KICK_OTHER,"DENIED: Special characters in your name");
		    		return;
		    	}
	    	}
	    	if (Integer.parseInt(plugin.maxchar) > 0)
	    	{
		    	if (name.length() > Integer.parseInt(plugin.maxchar))
		    	{
		            System.out.println("iplock - kicked (" + player + "), name not less than " + (Integer.parseInt(plugin.maxchar) + 1) + " characters");
		    		event.disallow(Result.KICK_OTHER,"DENIED: Player names must be less than " + (Integer.parseInt(plugin.maxchar) + 1) + " characters on this server");
		
		    		
		    	}
		    	
	    	}
    	} catch (Exception e) 
    	{
    		System.out.println("iplock - exception running name checks for " + event.getPlayer().getName());
    	}
    }
    
    
    public void onPlayerJoin(PlayerEvent event) {
    	
    	
    	
    	
    	try
    	{
	    	Player player = event.getPlayer();
	    	String name = player.getName();
	    	//System.out.println("debug: " + event.getPlayer().getAddress().getHostName().toString());
	    	
	    	String user = event.getPlayer().getAddress().getHostName().toString() + "=" + event.getPlayer().getName();
	    
	    	
	    	if (!plugin.isValid(user))
	    	{
		          System.out.println("iplock - kicked (" + name + "), ip (" + event.getPlayer().getAddress().getHostName().toString() + ") not in same subnet");
		          event.getPlayer().kickPlayer("DENIED: Are you trying to login from somewhere different than normal?");
		          return;
	    	  
	    	}
	    	
    	} catch (Exception e)
    	{
    		System.out.println("iplock - exception removing player: " + event.getPlayer().getName());
    		e.printStackTrace();
            
    	}
    
    }

    
}