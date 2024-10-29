package fr.the__glacier.gcore.color;

import org.bukkit.Bukkit;

import java.awt.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gradient implements ColorPatterns {
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("&#<([0-9A-Fa-f]{6})>(.*?)&#/<([0-9A-Fa-f]{6})>");
    Pattern pattern2 = Pattern.compile("<gradient:([0-9A-Fa-f]{6})>(.*?)</gradient:([0-9A-Fa-f]{6})>");

    public Gradient() {
    }

    public String process(String string) {
        Matcher matcher = pattern2.matcher(string);
        while (matcher.find()){
            String str = string.substring(matcher.start(), matcher.end());
            String str1 = str.replace("<gradient:", "&#<").replace("</gradient:", "&#/<");
            string = string.replace(str, str1);
        }
        String start;
        String end;
        String content;
        for(matcher = this.pattern.matcher(string); matcher.find(); string = string.replace(matcher.group(), ColorsUtil.color(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))))) {
            start = matcher.group(1);
            end = matcher.group(3);
            content = matcher.group(2);
            if (content.length() < 2){
                Logger logger = Bukkit.getLogger();
                logger.severe("Error : Cannot send message ! Error in " + matcher.group() + ", \"" + content + "\" must have at least 2 chars in gradient :");
                logger.severe(string);
                break;
            }
        }

        return string;
    }
}
