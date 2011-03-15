package net.gamerservices.iplock;

import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * <pluginname> block listener
 * @author <yourname>
 */
public class BListener extends BlockListener {
    private final iplock plugin;

    public BListener(final iplock plugin) {
        this.plugin = plugin;
    }

    //put all Block related code here
}