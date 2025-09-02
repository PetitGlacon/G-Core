package fr.the__glacier.gcore.color;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

@Deprecated
public class ColorsUtil {
    private static final List<ColorPatterns> PATTERNS;
    private static final List<String> SPECIAL_COLORS;
    private static final List<String> COLORS;

    static {
        SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m", "&r", "§r", "\n");
        PATTERNS = Arrays.asList(new Gradient(), new Hexa());
        COLORS =  Arrays.asList("&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f");
    }
    public static List<String> color(List<String> list){
        List<String> list1 = new ArrayList<>();
        for (String str : list){
            list1.add(color(str));
        }
        return list1;
    }
    public static String color(String str){
        ColorPatterns patterns;
        for (Iterator<ColorPatterns> var1 = PATTERNS.iterator(); var1.hasNext(); str = patterns.process(str)) {
            patterns = var1.next();
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static String color(String str, @Nonnull Color start, @Nonnull Color end){
        ChatColor[] colors = createGradient(start, end, withoutSpecialChar(str).length());
        return apply(str, colors);
    }


    public static ChatColor[] createGradient(Color start, Color end, int nb){
        ChatColor[] colors = new ChatColor[nb];
        int R = Math.abs(start.getRed() - end.getRed()) / (nb -1);
        int G = Math.abs(start.getGreen() - end.getGreen()) / (nb -1);
        int B = Math.abs(start.getBlue() - end.getBlue()) / (nb -1);
        int[] direction = new int[]{start.getRed() < end.getRed() ? 1 : -1, start.getGreen() < end.getGreen() ? 1 : -1, start.getBlue() < end.getBlue() ? 1 : -1};

        for (int i = 0; i < nb; ++i){
            Color color = new Color(start.getRed() + R*i*direction[0], start.getGreen() + G*i*direction[1], start.getBlue() + B*i*direction[2]);
            colors[i] = ChatColor.of(color);
        }
        return colors;
    }


    public static String apply(String str, ChatColor[] colors){
        StringBuilder specialColors = new StringBuilder();
        StringBuilder messageBuilder = new StringBuilder();
        int outIndex = 0;
        int regulateur = 0;

        for (int i = 0; i < str.length(); i++){
            char currentChar = str.charAt(i);
            if ('&' == currentChar && i+1 < str.length()){
                char nextChar = str.charAt(i+1);
                if ('r' != nextChar && 'R' != nextChar) {
                    specialColors.append(currentChar).append(nextChar);
                } else {
                    specialColors.setLength(0);
                }
            } else if (!(i>0 && ((str.charAt(i-1) == '&' &&  COLORS.contains("&" + currentChar)) || (str.charAt(i-1) == '&' && SPECIAL_COLORS.contains("&" + currentChar))))) {
                messageBuilder.append(colors[outIndex++ - regulateur]).append(specialColors).append(currentChar);
            }
        } return messageBuilder.toString();
    }


    private static String withoutSpecialChar(@Nonnull String str) {
        String workingString = str;

        for (String color : SPECIAL_COLORS) {
            if (workingString.contains(color)) {
                workingString = workingString.replaceAll(color, "");
            }
        }
        for (String color : COLORS){
            if (workingString.contains(color)){
                workingString = workingString.replaceAll(color, "");
            }
        }

        return workingString;
    }

    public static String apply(String str, ChatColor[] colors, Matcher matcher){
        StringBuilder specialColors = new StringBuilder();
        StringBuilder messageBuilder = new StringBuilder();
        int outIndex = 0;
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();

        while (matcher.find()){
            starts.add(matcher.start());
            ends.add(matcher.end());
        }

        boolean isMatcher = false;

        for (int i = 0; i < str.length(); i++){
            char currentChar = str.charAt(i);
            if (isMatcher && ends.contains(i)){
                messageBuilder.append(currentChar);
                isMatcher = false;
                continue;
            }
            if (isMatcher || starts.contains(i)){
                messageBuilder.append(currentChar);
                isMatcher = true;
                continue;
            }
            if ('&' == currentChar && i+1 < str.length()){
                char nextChar = str.charAt(i+1);
                if ('r' != nextChar && 'R' != nextChar) {
                    specialColors.append(currentChar).append(nextChar);
                } else {
                    specialColors.setLength(0);
                }
            } else if (!(i>0 && ((str.charAt(i-1) == '&' &&  COLORS.contains("&" + currentChar)) || (str.charAt(i-1) == '&' && SPECIAL_COLORS.contains("&" + currentChar))))) {
                messageBuilder.append("<color:").append(colors[outIndex++].toString().replace("§", "").replace("x", "")).append(">").append(specialColors).append(currentChar);
            }
        }
        return messageBuilder.toString();
    }
}
