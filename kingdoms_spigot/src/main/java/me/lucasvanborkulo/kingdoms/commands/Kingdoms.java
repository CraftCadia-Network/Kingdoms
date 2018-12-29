package me.lucasvanborkulo.kingdoms.commands;

import me.lucasvanborkulo.kingdoms.Commons;
import me.lucasvanborkulo.kingdoms.Main;
import me.lucasvanborkulo.kingdoms.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kingdoms implements CommandExecutor {

    private Main main;

    private String permission;

    public Kingdoms(Main main) {
        this.main = main;
        this.permission = "kingdoms.admin";
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (sender instanceof Player) {
            Player bukkitPlayer = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (!this.main.multicraftDriver.hasServer(bukkitPlayer)){
                        if (args.length > 1) {
                            String kingdomName = args[1];
                            if (kingdomName.length() < 3 || kingdomName.length() > 15) {
                                Commons.sendMessage(bukkitPlayer, "Your kingdom name must be 3 to 15 character in length, and only be made of letters and numbers");
                                return false;
                            }

                            Integer kingdomID = this.main.multicraftDriver.createServer(bukkitPlayer, kingdomName);
                            this.main.mySQLDriver.setValue(bukkitPlayer, "server_name", kingdomName);
                            this.main.mySQLDriver.setValue(bukkitPlayer, "server_id", kingdomID.toString());

                            this.main.multicraftDriver.createUser(bukkitPlayer, true);

                            Commons.sendMessage(bukkitPlayer, "Kingdom " + kingdomName + " has been created. Join it using /" + cmd.getName() + " join " + kingdomName);
                            Commons.sendMessage(bukkitPlayer, "Setup plugins with /" + cmd.getName() + " plugins");
                            Commons.sendMessage(bukkitPlayer, "Stop your Kingdom from running with /" + cmd.getName() + " stop");
                        } else {
                            Commons.sendMessage(bukkitPlayer, "Correct usage: /" + cmd.getName() + " create [kingdom_name]");
                            return false;
                        }
                    } else {
                        Commons.sendMessage(bukkitPlayer, "You already have a kingdom.");
                    }
                } else if (args[0].equalsIgnoreCase("plugins")) {

                } else if (args[0].equalsIgnoreCase("start")) {
                    if (this.main.multicraftDriver.hasServer(bukkitPlayer)) {
                        Integer server_id = this.main.multicraftDriver.getServerID(bukkitPlayer);
                        this.main.multicraftDriver.startServer(server_id);
                    } else {
                        Commons.sendMessage(bukkitPlayer, "You do not have a server, use /" + cmd.getName() + " create [kingdom_name] to create a server");
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (args.length > 1) {
                        String kingdomName = args[1];
                        Commons.sendMessage(bukkitPlayer, "Kingdom " + kingdomName + " has been told to shutdown.");
                    } else {
                        Commons.sendMessage(bukkitPlayer, "Correct usage: /" + cmd.getName() + " stop [kingdom_name]");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    this.sendHelp(bukkitPlayer, cmd.getName());
                } else if (args[0].equalsIgnoreCase("settier")) {
                    if (bukkitPlayer.hasPermission(this.permission)) {
                        if (args.length > 3) {
                            OfflinePlayer target = null;
                            Tier tier = null;
                            try {
                                target = Bukkit.getOfflinePlayer(args[2]);
                            } catch (Exception e) {
                                Commons.sendMessage(bukkitPlayer, "Player not found!");
                            }

                            try {
                                tier = Tier.valueOf(args[3]);
                            } catch (Exception e) {
                                Commons.sendMessage(bukkitPlayer, "Tier not found!");
                            }
                            if (target != null && tier != null) {
                                this.main.mySQLDriver.setValue(target, "tier", tier.toString().toUpperCase());
                                Commons.sendMessage(bukkitPlayer, "The tier has been updated!");
                            }
                        } else {
                            Commons.sendMessage(bukkitPlayer, "Correct usage: /" + cmd.getName() + " admin tier [username] [tier]");
                            return false;
                        }
                    }
                }
            } else {
                this.sendHelp(bukkitPlayer, cmd.getName());
            }
        } else {
            Commons.sendMessage(sender, "Only in-game players can execute this command!");
            return false;
        }
        return false;
    }

    private void sendHelp(Player bukkitPlayer, String cmd) {
        Commons.sendMessage(bukkitPlayer, "Member commands:");
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " create"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " join"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " plugins"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " start"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " stop"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " setmotd"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " setfavicon"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " createftp"));
        bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " deleteftp"));
        bukkitPlayer.sendMessage("");
        if (bukkitPlayer.hasPermission(this.permission)) {
            Commons.sendMessage(bukkitPlayer, "Admin commands:");
            bukkitPlayer.sendMessage(Commons.colorize("&e/" + cmd + " admin tier"));
        }
    }
}
