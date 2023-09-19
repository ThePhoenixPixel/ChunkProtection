package eu.phoenixcraft.chunkprotection;

import eu.phoenixcraft.chunkprotection.command.Cp;
import eu.phoenixcraft.chunkprotection.command.CpTabCompleter;
import eu.phoenixcraft.chunkprotection.event.ev_interaction;
import eu.phoenixcraft.chunkprotection.core.tabCompleter;
import eu.phoenixcraft.chunkprotection.storage.MySQL;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public final class ChunkProtection extends JavaPlugin {

    FileConfiguration config = getConfig();
    private MySQL mysql;
    public static String version = "0.01";
    public static String lang;
    public static String db_type;
    public static String db_hostname;
    public static int db_port;
    public static String db_name;
    public static String db_user;
    public static String db_passwd;
    //public static Economy econ = null;




    @Override
    public void onEnable() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";


        /*if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/




        // generate config file
        config.options().copyDefaults(true);
        config.addDefault("id", getAlphaNumericString(15));
        saveConfig();

        this.lang = config.getString("lang");
        this.db_type = config.getString("db_type");
        this.db_name = config.getString("db_name");
        this.db_hostname = config.getString("db_hostname");
        this.db_port = config.getInt("db_port");
        this.db_user = config.getString("db_user");
        this.db_passwd = config.getString("db_passwd");

        this.mysql = new MySQL(db_hostname, db_port, db_name, db_user, db_passwd);

        if (mysql.connect()){
            getLogger().info("Database Connect");
        } else {
            getLogger().info("Database Disconnect");
        }

        // Commands registrieren
        getCommand("cp").setExecutor(new Cp(this));
        getCommand("cp").setTabCompleter(new CpTabCompleter());
        getServer().getPluginManager().registerEvents(new ev_interaction(), this);


        getLogger().info(ANSI_PURPLE + "   ____ _                 _    ____            _            _   _             " + ANSI_RESET);
        getLogger().info(ANSI_PURPLE + "  / ___| |__  _   _ _ __ | | _|  _ \\ _ __ ___ | |_ ___  ___| |_(_) ___  _ __  " + ANSI_RESET);
        getLogger().info(ANSI_PURPLE + " | |   | '_ \\| | | | '_ \\| |/ / |_) | '__/ _ \\| __/ _ \\/ __| __| |/ _ \\| '_ \\ " + ANSI_RESET);
        getLogger().info(ANSI_PURPLE + " | |___| | | | |_| | | | |   <|  __/| | | (_) | ||  __/ (__| |_| | (_) | | | |" + ANSI_RESET);
        getLogger().info(ANSI_PURPLE + "  \\____|_| |_|\\__,_|_| |_|_|\\_\\_|   |_|  \\___/ \\__\\___|\\___|\\__|_|\\___/|_| |_|" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "  You are using the version: v" + version + " of ChunkProtection." + ANSI_RESET);




    }

    /*private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }*/

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (mysql != null) {
            mysql.disconnect();
        }

    }

    public MySQL getMysql(){
        return this.mysql;
    }



    // function to generate a random string of length n
    static String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
