package me.lucasvanborkulo.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Commons {

    public static void sendMessage(CommandSender commandSender, String message) {
        String prefix = colorize("&e[&cKingdoms&e] &f");
        String messageToSend = prefix + colorize(message);
        commandSender.sendMessage(messageToSend);
    }

    public static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void logAction(String action) {
        String prefix = "Kingdoms Log: ";
        String messageToSend = prefix + action;
        System.out.println(messageToSend);
    }

}
