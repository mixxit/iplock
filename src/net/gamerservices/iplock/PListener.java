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
    	
    	if (!name.matches("[A-Za-z0-9]+"))
    	{
            System.out.println("iplock - kicked, special characters in name");
    		event.disallow(Result.KICK_OTHER,"You cannot connect with special characters");
    		return;
    	}
    	
    	if (name.length() > 15)
    	{
            System.out.println("iplock - kicked, name not less than 15 characters");
    		event.disallow(Result.KICK_OTHER,"Player names must be less than 15 characters.");

    		
    	}
    	} catch (Exception e) 
    	{
    		System.out.println("iplock - exception running name checks");
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
		          System.out.println("iplock - kicked, ip not in same subnet");
		          event.getPlayer().kickPlayer("You are trying to login to an account from the wrong IP");
		          return;
	    	  
	    	}
	    	
    	} catch (Exception e)
    	{
    		System.out.println("iplock - exception removing player: ");
    		e.printStackTrace();
            
    	}
    
    }

    
}