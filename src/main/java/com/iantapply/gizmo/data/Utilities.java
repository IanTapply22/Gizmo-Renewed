package com.iantapply.gizmo.data;

import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.logger.LoggingLevel;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {
    public static String chatTranslate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        try {
            return PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', message));
        } catch (NoClassDefFoundError e) {
            Logger.log(LoggingLevel.WARNING, "PlaceholderAPI is not currently installed! Please install PlaceholderAPI to use its features.");
            return message;
        }
    }
}
