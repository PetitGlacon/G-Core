package fr.the__glacier.gcore.color;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hexa implements ColorPatterns{
    Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}|<color:[a-fA-F0-9]{6}>");
    @Override
    public String process(String string){
        String color;
        Matcher match = pattern.matcher(string);
        while (match.find()){
            color = string.substring(match.start(), match.end());
            string = string.replace(color, ChatColor.of(color.replace("&","").replace("<color:", "#").replace(">", "")) + "");
            match = pattern.matcher(string);
        }

        return string;
    }
}
