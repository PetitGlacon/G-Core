package fr.the__glacier.gcore.color;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessages {
    @Getter
    private final String rawText;
    @Getter
    private final Component component;
    public MiniMessages(String message){
        rawText = message;
        var mm = MiniMessage.miniMessage();
        component = mm.deserialize(message);
    }
}

    /* private final String regex = "<hover:(.*?):'(.*?)'>|</hover>|<click:(.*?):'(.*?)'>|</click>|<key:'(.*?)'>|<insert:'(.*?)'>|</insert>|<reset>|<color:(.*?)>|</color>|<bold>|</bold>|<italic>|</italic>|<underline>|</underline>|<strikethrough>|</strikethrough>|<magic>|</magic>";
    private final Pattern pattern = Pattern.compile(regex);
    private final Pattern patternNewline = Pattern.compile(regex + "|<newline>|<nl>");
    private final String regexGradiant = "<gradient:([0-9A-Fa-f]{6})>(.*?)</gradient:([0-9A-Fa-f]{6})>";
    private final Pattern gradiant = Pattern.compile(regexGradiant);
    private final String hoverRegex = "<key:'(.*?)'>|<reset>|<color:(.*?)>|</color>|<bold>|</bold>|<italic>|</italic>|<underline>|</underline>|<strikethrough>|</strikethrough>|<magic>|</magic>|<newline>|<nl>";
    private final Pattern hoverPattern = Pattern.compile(hoverRegex);
    private final List<String> colorsName = List.of("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");
    private final List<String> colorsCode = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "a", "b", "c", "d", "e", "f");
    private String baseMessage;
    private final String baseMessageBackup;
    private Logger logger = Bukkit.getLogger();
    public MiniMessages(String str){
        baseMessage = str;
        baseMessageBackup = str;
    }
    public BaseComponent[] getBasesComponents(){

        List<BaseComponent> baseComponents = new ArrayList<>();

        HoverEvent hoverEvent = null;
        boolean isHover = false;

        ClickEvent clickEvent = null;
        boolean isClick = false;

        String insertion = null;
        boolean isInsert = false;

        ChatColor chatColor = ChatColor.WHITE;
        boolean isColored = false;

        boolean isBold = false;
        boolean isUnderline = false;
        boolean isMagic = false;
        boolean isItalic = false;
        boolean isStrikethrough = false;


        Matcher gradiantMatcher = this.gradiant.matcher(baseMessage);
        while (gradiantMatcher.find()){
            String message = gradiantMatcher.group(2);
            Matcher matcher1 = patternNewline.matcher(message);
            String replacedMessage = matcher1.replaceAll("");
            ChatColor[] colors = ColorsUtil.createGradient(new Color(Integer.parseInt(gradiantMatcher.group(1), 16)), new Color(Integer.parseInt(gradiantMatcher.group(3), 16)), replacedMessage.length());
            String str = ColorsUtil.apply(message, colors, patternNewline.matcher(message)) + "</color>";
            baseMessage = baseMessage.replace(gradiantMatcher.group(), str);
        }

        Matcher matcher = this.pattern.matcher(baseMessage);
        while (matcher.find()) {

            BaseComponent bc = new TextComponent(baseMessage.substring(0, baseMessage.indexOf(matcher.group())));
            baseMessage = baseMessage.substring(baseMessage.indexOf(matcher.group()) + matcher.group().length());

            if (matcher.group().startsWith("<key:")){
                String var1 = matcher.group(5);
                try {
                    bc.addExtra(new KeybindComponent(var1.toLowerCase()));
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() + ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }

            if (isHover){
                bc.setHoverEvent(hoverEvent);
                if (matcher.group().equals("</hover>")){
                    isHover = false;
                }
            }

            if (matcher.group().startsWith("<hover:")){
                String var1 = matcher.group(1);
                HoverEvent.Action action = null;
                try {
                    action = HoverEvent.Action.valueOf(var1.toUpperCase());
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
                String var2 = matcher.group(2);
                if (Objects.equals(action, HoverEvent.Action.SHOW_TEXT)){
                    ArrayList<TextComponent> list = parseHoverText(var2);
                    TextComponent[] txtComponent = new TextComponent[list.size()];
                    txtComponent = list.toArray(txtComponent);
                    hoverEvent = new HoverEvent(action, new ArrayList<>(Collections.singletonList(new Text(txtComponent))));
                    isHover = true;
                } else if (action != null){
                    logger.severe("Error : not yet developed ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    /* try {
                        hoverEvent = new HoverEvent(HoverEvent.Action.valueOf(var1.toUpperCase()), new Text(var2));
                        isHover = true;
                    } catch (IllegalArgumentException e){
                        logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                        logger.severe(baseMessageBackup);
                        logger.severe(e.getMessage());
                    }
                }
            }

            if (isClick){
                bc.setClickEvent(clickEvent);
                if (matcher.group().equals("</click>")){
                    isClick = false;
                }
            }

            if (matcher.group().startsWith("<click:")){
                String var1 = matcher.group(3);
                String var2 = matcher.group(4);
                try {
                    clickEvent = new ClickEvent(ClickEvent.Action.valueOf(var1.toUpperCase()), var2);
                    isClick = true;
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }


            if (isInsert){
                bc.setInsertion(insertion);
                if (matcher.group().equals("</insert>")){
                    isClick = false;
                }
            }

            if (matcher.group().startsWith("<insert:")){
                String var1 = matcher.group(6);
                try {
                    insertion = var1;
                    isInsert = true;
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }


            if (isColored){
                bc.setColor(chatColor);
                if (matcher.group().equals("</color>") || matcher.group().equals("<reset>")){
                    chatColor = ChatColor.WHITE;
                    isColored = false;
                }
            }
            if (matcher.group().startsWith("<color:")){
                String var1 = matcher.group(7);
                try {
                    if (colorsName.contains(var1.toUpperCase())){
                        chatColor = ChatColor.of(var1);
                    } else if (colorsCode.contains(var1.toLowerCase())){
                        chatColor = ChatColor.getByChar(var1.charAt(0));
                    } else {
                        chatColor = ChatColor.of(new Color(Integer.parseInt(var1, 16)));
                    }
                    isColored = true;
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }

            bc.setBold(isBold);
            bc.setItalic(isItalic);
            bc.setObfuscated(isMagic);
            bc.setUnderlined(isUnderline);
            bc.setStrikethrough(isStrikethrough);

            if (matcher.group().equals("<bold>")){
                isBold = true;
            } else if (matcher.group().equals("</bold>")){
                isBold = false;
            }

            if (matcher.group().equals("<italic>")){
                isItalic = true;
            } else if (matcher.group().equals("</italic>")){
                isItalic = false;
            }

            if (matcher.group().equals("<underline>")){
                isUnderline = true;
            } else if (matcher.group().equals("</underline>")){
                isUnderline = false;
            }

            if (matcher.group().equals("<strikethrough>")){
                isStrikethrough = true;
            } else if (matcher.group().equals("</strikethrough>")){
                isStrikethrough = false;
            }

            if (matcher.group().equals("<magic>")){
                isMagic = true;
            } else if (matcher.group().equals("</magic>")){
                isMagic = false;
            }


            baseComponents.add(bc);
        }
        if (baseMessage != null){
            BaseComponent bc = new TextComponent(baseMessage);
            if (isHover) bc.setHoverEvent(hoverEvent);
            if (isClick) bc.setClickEvent(clickEvent);
            if (isColored) bc.setColor(chatColor);
            bc.setBold(isBold);
            bc.setItalic(isItalic);
            bc.setObfuscated(isMagic);
            bc.setUnderlined(isUnderline);
            bc.setStrikethrough(isStrikethrough);
            baseComponents.add(bc);

        }
        return baseComponents.toArray(new BaseComponent[0]);
    }


    public ArrayList<TextComponent> parseHoverText(String str){
        ArrayList<TextComponent> contents = new ArrayList<>();
        TextComponent txt = new TextComponent(" ");
        ChatColor chatColor = ChatColor.WHITE;
        boolean isColored = false;

        boolean isBold = false;
        boolean isUnderline = false;
        boolean isMagic = false;
        boolean isItalic = false;
        boolean isStrikethrough = false;


        Matcher gradiantMatcher = this.gradiant.matcher(str);
        while (gradiantMatcher.find()){
            String message = gradiantMatcher.group(2);
            Matcher matcher1 = hoverPattern.matcher(message);
            String replacedMessage = matcher1.replaceAll("");
            ChatColor[] colors = ColorsUtil.createGradient(new Color(Integer.parseInt(gradiantMatcher.group(1), 16)), new Color(Integer.parseInt(gradiantMatcher.group(3), 16)), replacedMessage.length());
            String str1 = ColorsUtil.apply(message, colors, hoverPattern.matcher(message));
            str = str.replace(gradiantMatcher.group(), str1);
        }

        Matcher matcher = this.hoverPattern.matcher(str);
        while (matcher.find()) {

            TextComponent bc = new TextComponent(str.substring(0, str.indexOf(matcher.group())));
            str = str.substring(str.indexOf(matcher.group()) + matcher.group().length());

            if (matcher.group().equals("<newline>")){
                bc.addExtra("\n");
            }
            if (matcher.group().equals("<nl>")){
                bc.addExtra("\n");
            }

            if (matcher.group().startsWith("<key:")){
                String var1 = matcher.group(1);
                try {
                    bc.addExtra(new KeybindComponent(var1.toLowerCase()));
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }

            if (isColored){
                bc.setColor(chatColor);
                if (matcher.group().equals("</color>") || matcher.group().equals("<reset>")){
                    chatColor = ChatColor.WHITE;
                    isColored = false;
                }
            }
            if (matcher.group().startsWith("<color:")){
                String var1 = matcher.group(2);
                try {
                    if (colorsName.contains(var1.toUpperCase())){
                        chatColor = ChatColor.of(var1);
                    } else if (colorsCode.contains(var1.toLowerCase())){
                        chatColor = ChatColor.getByChar(var1.charAt(0));
                    } else {
                        chatColor = ChatColor.of(new Color(Integer.parseInt(var1, 16)));
                    }
                    isColored = true;
                } catch (IllegalArgumentException e){
                    logger.severe("Error : Cannot send message ! Error in " + matcher.group() +  ", \"" + var1 + "\" is not valid in minimessage :");
                    logger.severe(baseMessageBackup);
                    logger.severe(e.getMessage());
                }
            }

            bc.setBold(isBold);
            bc.setItalic(isItalic);
            bc.setObfuscated(isMagic);
            bc.setUnderlined(isUnderline);
            bc.setStrikethrough(isStrikethrough);

            if (matcher.group().equals("<bold>")){
                isBold = true;
            } else if (matcher.group().equals("</bold>")){
                isBold = false;
            }

            if (matcher.group().equals("<italic>")){
                isItalic = true;
            } else if (matcher.group().equals("</italic>")){
                isItalic = false;
            }

            if (matcher.group().equals("<underline>")){
                isUnderline = true;
            } else if (matcher.group().equals("</underline>")){
                isUnderline = false;
            }

            if (matcher.group().equals("<strikethrough>")){
                isStrikethrough = true;
            } else if (matcher.group().equals("</strikethrough>")){
                isStrikethrough = false;
            }

            if (matcher.group().equals("<magic>")){
                isMagic = true;
            } else if (matcher.group().equals("</magic>")){
                isMagic = false;
            }


            contents.add(bc);
        }
        if (!str.isEmpty()){
            TextComponent bc = new TextComponent(str);
            if (isColored) bc.setColor(chatColor);
            bc.setBold(isBold);
            bc.setItalic(isItalic);
            bc.setObfuscated(isMagic);
            bc.setUnderlined(isUnderline);
            bc.setStrikethrough(isStrikethrough);
            contents.add(bc);
        }
        str = ColorsUtil.color(str.replace("<newline>", "\n").replace("<nl>", "\n"));
        txt.addExtra(str.replace("<newline>", "\n").replace("<nl>", "\n"));
        return contents;
    }


}*/
