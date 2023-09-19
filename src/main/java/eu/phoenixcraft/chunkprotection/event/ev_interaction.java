package eu.phoenixcraft.chunkprotection.event;

import eu.phoenixcraft.chunkprotection.core.ClaimChunk;
import eu.phoenixcraft.chunkprotection.storage.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

import static eu.phoenixcraft.chunkprotection.core.ClaimChunk.getChunkID;
import static eu.phoenixcraft.chunkprotection.core.ClaimChunk.getChunkOwner;

public class ev_interaction implements Listener {

    private final HashMap<Player, Long> lastChunkID = new HashMap<>();


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Player owner = getChunkOwner(getChunkID(block.getLocation()), MySQL.connection);

        if (owner == null || !owner.getUniqueId().equals(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You don't have permissions here!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) { // Könnte null sein, wenn der Spieler die Luft klickt
            Player owner = getChunkOwner(getChunkID(block.getLocation()), MySQL.connection);

            if (owner == null || !owner.getUniqueId().equals(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You don't have permissions here!");
            }
        }
    }



    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        long currentChunkID = getChunkID(player.getLocation());

        if (!lastChunkID.containsKey(player) || lastChunkID.get(player) != currentChunkID) {
            Player owner = getChunkOwner(currentChunkID, MySQL.connection);
            ClaimChunk.ChunkInfo chunkinfo = ClaimChunk.getChunkData(currentChunkID, MySQL.connection);
            if (chunkinfo != null) {
                Player owner_p =  Bukkit.getPlayer(UUID.fromString(chunkinfo.playerUUID));
                if (owner_p != null) {
                    if(chunkinfo.isResell)
                        player.sendMessage("This property if for sale: $" + chunkinfo.price + ". You can buy with: " + ChatColor.GREEN + "/cp buy");
                    else
                        player.sendMessage("This is the property of " + owner_p.getName());
                }

            }
            else
                //player.sendMessage("no data in database");


            lastChunkID.put(player, currentChunkID);
        }
    }

}
