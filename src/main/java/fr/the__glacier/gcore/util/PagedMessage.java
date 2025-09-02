package fr.the__glacier.gcore.util;

import fr.the__glacier.gcore.GCore;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PagedMessage {
    public List<List<String>> message;
    private final List<String> rawMessages;
    private final List<String> top;
    private final String bottom;
    private final BottomType bottomType;
    private final Map<BottomTypePlaceholders, String> pagedNumberPlaceholders;
    private final int nbLines;
    @Setter
    private int page;
    private final int maxPage;
    private final UUID uuid;
    private final Plugin plugin;

    public PagedMessage(){
        this.rawMessages = List.of("");
        setMessage(rawMessages, 10);
        this.top = List.of("<gold>--- Help ---");
        this.bottom = "<aqua> <-- previous </aqua><white>...</white><aqua> next --></aqua>";
        this.bottomType = PagedMessage.BottomType.previousAndNextButton;
        this.pagedNumberPlaceholders = null;
        this.nbLines = 10;
        this.uuid = null;
        this.plugin = GCore.getInstance();
        this.page = 1;
        this.maxPage = this.message.size();
    }
    public PagedMessage(List<String> messages, List<String> top, String bottom, BottomType bottomType, Map<BottomTypePlaceholders, String> pagedNumberPlaceholders, int nbLines, UUID uuid, Plugin plugin){
        this.rawMessages = messages;
        this.top = top;
        this.bottom = bottom;
        this.bottomType = bottomType;
        this.pagedNumberPlaceholders = pagedNumberPlaceholders;
        this.nbLines = nbLines;
        this.uuid = uuid;
        this.plugin = plugin;
        this.page = 1;
        setMessage(messages, nbLines);
        this.maxPage = this.message.size();
    }
    public PagedMessage(List<List<String>> messages, List<String> top, String bottom, BottomType bottomType, Map<BottomTypePlaceholders, String> pageNumberPlaceholders, UUID uuid, Plugin plugin){
        this.rawMessages = null;
        this.message = messages;
        this.top = top;
        this.bottom = bottom;
        this.bottomType = bottomType;
        this.pagedNumberPlaceholders = pageNumberPlaceholders;
        this.nbLines = 0;
        this.uuid = uuid;
        this.plugin = plugin;
        this.page = 1;
        this.maxPage = this.message.size();
    }
    public void setMessage(List<String> list, int nbLines){
        List<List<String>> lists = new ArrayList<>();
        int i = 0;
        while (i < list.size()){
            lists.add(list.subList(i, Math.min(i + nbLines, list.size())));
            i += nbLines;
        }
        this.message = lists;
    }

    public void sendMessage(CommandSender p, int page){
        this.page = page;
        CompletableFuture.runAsync(() -> {
            List<String> list = message.get(page-1);
            for (String str : top){
                PlayerUtil.sendMiniMessage(p, str);
            }
            for (String str : list){
                PlayerUtil.sendMiniMessage(p, str);
            }
            PlayerUtil.sendMiniMessage(p, formatBottom());
        });
    }
    public String formatBottom(){
        if (bottomType == BottomType.previousAndNextButton){
            String command = "/gcoreutils " + plugin.getName() + " " + uuid + " ";

            String previousClickEvent = "<click:run_command:'" + command + (page-1) + "'><hover:show_text:'<white>< page " + (page-1) + "</white>'>";
            String previousClickEventEnd = "</hover></click>";
            if (page <= 1) {
                previousClickEvent = "";
                previousClickEventEnd = "";
            }

            String nextClickEvent = "<click:run_command:'" + command + (page+1) + "'><hover:show_text:'<white>page " + (page+1) + " ></white>'>";
            String nextClickEventEnd = "</hover></click>";
            if (page >= maxPage) {
                nextClickEvent = "";
                nextClickEventEnd = "";
            }

            String bottom = formatString(this.bottom);

            if (bottom.contains("%previous%") && bottom.contains("%/previous%")){
                bottom = bottom.replace("%previous%", previousClickEvent).replace("%/previous%", previousClickEventEnd);
            }

            if (bottom.contains("%next%") && bottom.contains("%/next%")){
                bottom = bottom.replace("%next%", nextClickEvent).replace("%/next%", nextClickEventEnd);
            }
            return bottom;
        } else if (bottomType == BottomType.pageNumber){
            String command = "/gcoreutils " + plugin.getName() + " " + uuid + " ";
            UUID uuid1 = UUID.randomUUID();
            String clickEvent = "<click:run_command:'" + command + uuid1 + "'><hover:show_text:'" + pagedNumberPlaceholders.get(BottomTypePlaceholders.hover).replace("%page%", uuid1.toString()) + "'>";
            String endEvent = "</hover></click>";

            StringBuilder bottom = new StringBuilder();
            bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.prefix, "").replace("%page%", String.valueOf(page)).replace("%maxPage%", String.valueOf(maxPage)));
            assert maxPage > 0;
            if (maxPage <= 10){
                int i = 1;
                while (i <= maxPage){
                    String clickEventReplaced = clickEvent.replace(uuid1.toString(), String.valueOf(i));
                    if (i == page){
                        bottom.append(clickEventReplaced).append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.page, "%page%").replace("%page%", String.valueOf(i))).append(endEvent);
                    } else if (i == page-1){
                        bottom.append(clickEventReplaced).append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.previousPage, "%page%").replace("%page%", String.valueOf(i))).append(endEvent);
                    } else if (i == page+1){
                        bottom.append(clickEventReplaced).append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.nextPage, "%page%").replace("%page%", String.valueOf(i))).append(endEvent);
                    } else if (i < page){
                        bottom.append(clickEventReplaced).append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.firstPages, "%page%").replace("%page%", String.valueOf(i))).append(endEvent);
                    } else {
                        bottom.append(clickEventReplaced).append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.lastPages, "%page%").replace("%page%", String.valueOf(i))).append(endEvent);
                    }
                    if (i < maxPage){
                        bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                    }
                    i ++;
                }
            } else {
                if (page >= 4){
                    bottom.append(clickEvent.replace(uuid1.toString(), "1"));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.firstPages, "%page%").replace("%page%", "1"));
                    bottom.append(endEvent);
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                }
                if (page >= 5){
                    bottom.append(clickEvent.replace(uuid1.toString(), "2"));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.firstPages, "%page%").replace("%page%", "2"));
                    bottom.append(endEvent);
                    if (page < 6){
                        bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                    }
                }
                if (page >= 6){
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.bigSeparator, "<gray> ... </gray>"));
                }
                if (page > 2){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(page-2)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.previousPage, "%page%").replace("%page%", String.valueOf(page-2)));
                    bottom.append(endEvent);
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                }
                if (page > 1){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(page-1)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.previousPage, "%page%").replace("%page%", String.valueOf(page-1)));
                    bottom.append(endEvent);
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                }

                bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.page, "%page%").replace("%page%", String.valueOf(page)));
                if (page != maxPage){
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                }

                if (page <= maxPage-1){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(page+1)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.nextPage, "%page%").replace("%page%", String.valueOf(page+1)));
                    bottom.append(endEvent);
                    if (page != maxPage -1){
                        bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                    }
                }
                if (page <= maxPage-2){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(page+2)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.nextPage, "%page%").replace("%page%", String.valueOf(page+2)));
                    bottom.append(endEvent);
                    if (page > maxPage-5 && page != maxPage -2){
                        bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                    }
                }
                if (page <= maxPage-5){
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.bigSeparator, "<gray> ... </gray>"));
                }
                if (page <= maxPage-4){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(maxPage-1)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.lastPages, "%page%").replace("%page%", String.valueOf(maxPage-1)));
                    bottom.append(endEvent);
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.separator, "/"));
                }
                if (page <= maxPage-3){
                    bottom.append(clickEvent.replace(uuid1.toString(), String.valueOf(maxPage)));
                    bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.lastPages, "%page%").replace("%page%", String.valueOf(maxPage)));
                    bottom.append(endEvent);
                }
            }
            bottom.append(pagedNumberPlaceholders.getOrDefault(BottomTypePlaceholders.suffix, "").replace("%page%", String.valueOf(page)).replace("%maxPage%", String.valueOf(maxPage)));
            return bottom.toString();
        }
        return null;
    }
    public String formatString(String str){
        return str.replace("%page%", String.valueOf(page)).replace("%max_page%", String.valueOf(maxPage));
    }

    public enum BottomType{
        previousAndNextButton,
        pageNumber
    }
    public enum BottomTypePlaceholders{
        page,
        firstPages,
        lastPages,
        previousPage,
        nextPage,
        separator,
        bigSeparator,
        hover,
        prefix,
        suffix
    }
}
