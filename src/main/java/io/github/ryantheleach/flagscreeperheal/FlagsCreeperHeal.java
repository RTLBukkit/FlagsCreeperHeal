/* Copyright 2013 Ryan Leach
 Based upon the original work of Kevin Seiden, And thus the following license applies.

 This works is licensed under the Creative Commons Attribution-NonCommercial 3.0

 You are Free to:
 to Share: to copy, distribute and transmit the work
 to Remix: to adapt the work

 Under the following conditions:
 Attribution: You must attribute the work in the manner specified by the author (but not in any way that suggests that they endorse you or your use of the work).
 Non-commercial: You may not use this work for commercial purposes.

 With the understanding that:
 Waiver: Any of the above conditions can be waived if you get permission from the copyright holder.
 Public Domain: Where the work or any of its elements is in the public domain under applicable law, that status is in no way affected by the license.
 Other Rights: In no way are any of the following rights affected by the license:
 Your fair dealing or fair use rights, or other applicable copyright exceptions and limitations;
 The author's moral rights;
 Rights other persons may have either in the work itself or in how the work is used, such as publicity or privacy rights.

 Notice: For any reuse or distribution, you must make clear to others the license terms of this work. The best way to do this is with a link to this web page.
 http://creativecommons.org/licenses/by-nc/3.0/

 */
package io.github.ryantheleach.FlagsCreeperHeal;

import com.nitnelave.CreeperHeal.events.CHExplosionRecordEvent;
import io.github.alshain01.Flags.*;
import io.github.alshain01.Flags.System;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Flags - Block Module that adds block flags to the plug-in Flags.
 *
 * @author Alshain01
 */
public class FlagsCreeperHeal extends JavaPlugin {

    /**
     * Called when this module is enabled
     */
    @Override
    public void onEnable() {
        final PluginManager pm = Bukkit.getServer().getPluginManager();

        
        Flags flags = (Flags) Bukkit.getServer().getPluginManager().getPlugin("Flags");
        
        if (!pm.isPluginEnabled("Flags") || flags == null) {
            getLogger().severe("Flags was not found. Shutting down.");
            pm.disablePlugin(this);
        }
        
        // Connect to the data file and register the flags
        flags.getRegistrar().register(new ModuleYML(this, "flags.yml"), "CreeperHeal");
        
        // Load plug-in events and data
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    /*
     * The event handlers for the flags we created earlier
     */
    private class BlockListener implements Listener {
        /*
         * Snow and Ice melt event handler
         */

        @EventHandler()
        private void onCreeperHealExplosion(CHExplosionRecordEvent e) {
            Flag flag = Flags.getRegistrar().getFlag("BlocksHeal");
            if (flag != null) {
                List<Block> healBlocks = e.getBlocks();
                System sys = io.github.alshain01.Flags.System.getActive();
                for (Iterator<Block> it = healBlocks.iterator(); it.hasNext();) {
                    Location loc = it.next().getLocation();
                    boolean heal = sys.getAreaAt(loc).getValue(flag, false);
                    if (!heal) {
                        it.remove();
                    }
                }
            }
        }

    }
}
