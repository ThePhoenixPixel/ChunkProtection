package eu.phoenixcraft.chunkprotection.command;

import eu.phoenixcraft.chunkprotection.ChunkProtection;
import eu.phoenixcraft.chunkprotection.storage.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static eu.phoenixcraft.chunkprotection.core.ClaimChunk.*;

public class Cp implements CommandExecutor {

    private ChunkProtection plugin;
    private MySQL mySQL;
    private Connection connection;

    public Cp(ChunkProtection plugin){
        this.plugin = plugin;
        this.mySQL = plugin.getMysql();
        this.connection = mySQL.getConnection();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Your are not a player");
            return true;
        }

        Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage("you need to use arguments: /cp <claim | unclaim | info>");
            return true;
        }

        // COMMAND CLAIM ----------------------------------------------------
        if(args[0].equalsIgnoreCase("claim")) {
            if (checkChunkOwnership(player, connection) == 1){
                player.sendMessage("Dieser Chunk Gehört schon dir");
                return true;

            } else if (checkChunkOwnership(player, connection) == 2) {
                player.sendMessage("Dieser Chunk gehört jemand anderem");
                return true;

            } else if (checkChunkOwnership(player, connection) == 3) {
                player.sendMessage("dieser chunk ist frei");

            } else {
                player.sendMessage("Error Bitte kontaktire das Team");
                return true;
            }

            if (addClaimChunk(player, connection)){
                player.sendMessage("Claim added successfully");
            } else {
                player.sendMessage("Error bitte das Team kontaktieren");
                return true;
            }
        } else if(args[0].equalsIgnoreCase("unclaim")) {
            // COMMAND UNCLAIM ----------------------------------------------------

            if (removeClaimedChunk(player, connection)){
                player.sendMessage("Succsesfuly remove the Chunk");

            } else {
                player.sendMessage("se");
            }
        }

        // COMMAND INFO ----------------------------------------------------
        else if(args[0].equalsIgnoreCase("info")) {
            Player owner = getChunkOwner( getChunkID( player.getLocation()), connection );
            if(owner != null)
                player.sendMessage("The owner is: " + owner.getName());
            else
                player.sendMessage("No owner: claim with /cp claim");

        }


        // COMMAND RESELL ----------------------------------------------------
        /*else if(args[0].equalsIgnoreCase("resell")) {
            if(args.length < 2) {
                player.sendMessage("Syntax: /cp resell <amount>");
            }
            else {
                long price = Long.parseLong(args[1]);
                if(resellChunk(player, price, connection))
                    player.sendMessage("Plot is now on resale: $" + price);
                else {
                    player.sendMessage("That's not your plot!");
                }
            }
        }


        // COMMAND BUY ----------------------------------------------------
        else if(args[0].equalsIgnoreCase("buy")) {
            Boolean status = buyChunk(player, connection);
            if(status)
                player.sendMessage("Plot transfered successfully!");
            else
                player.sendMessage("Plot not bought. To less money?");
        }*/



        else {
            player.sendMessage("Not implemented!");
        }

        return false;
    }
}
