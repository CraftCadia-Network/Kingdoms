package me.lucasvanborkulo.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Commons {

    /*
     * This is used for sending messages to an user.
     */

    public static void sendMessage(CommandSender commandSender, String message) {
        String prefix = colorize("&e[&cKingdoms&e] &f");
        String messageToSend = prefix + colorize(message);
        commandSender.sendMessage(messageToSend);
    }

    /*
     * This is used for translating the '&' character to a ChatColor.
     */

    public static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /*
     * This is used for logging actions to the console.
     */

    public static void logAction(String action) {
        String prefix = "INFO: ";
        String messageToSend = prefix + action;
        System.out.println(messageToSend);
    }
}
