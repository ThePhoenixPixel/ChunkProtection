package eu.phoenixcraft.chunkprotection.event;

import eu.phoenixcraft.chunkprotection.ChunkProtection;
import eu.phoenixcraft.chunkprotection.core.ClaimChunk;
import eu.phoenixcraft.chunkprotection.storage.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.Connection;

public class BlockPlace implements Listener {
    private ChunkProtection plugin;
    private MySQL mysql;
    private Connection connection;
    public BlockPlace(ChunkProtection plugin){
        this.plugin = plugin;
        this.mysql = plugin.getMysql();
        this.connection = plugin.getMysql().getConnection();
    }

    public void onEvent(BlockPlaceEvent event){
        Player player = event.getPlayer();

        switch (ClaimChunk.checkChunkOwnership(player, connection)){
            case 1:
                //gehört dem spieler
                break;

            case 2:
                //gehört einem anderen spieler

                if (!(ClaimChunk.checkPermission("player.can.other.block.place", player, connection))){
                    player.sendMessage("Du hast nicht die Rechte dafür");
                    event.isCancelled();
                }
                break;

            case 3:
                //gehört keinem spieler
                break;
            default:
                player.sendMessage("Error beim BlockBreakEvent");
                player.sendMessage("Bitte kontaktiere das support Team");
                break;
        }
    }
}

