package fr.the__glacier.gcore.test;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.config.configObjects.PagedMessageConfig;
import fr.the__glacier.gcore.util.PagedMessage;
import java.util.List;
import java.util.UUID;

public class ConfigTest {
    public Boolean enable = true;
    public PagedMessageConfig pagedMessages;

    public ConfigTest(){
        pagedMessages = new PagedMessageConfig(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"),
                List.of("<gold>Test de messages"),
                "<blue>%previous%<- Previous%/previous%</blue><white> -- %page%/%max_page% -- </white><blue>%next%Next ->%/next%</blue>",
                PagedMessage.BottomType.previousAndNextButton,
                ImmutableMap.of(
                        PagedMessage.BottomTypePlaceholders.page, "<blue><b> %page% </b></blue>",
                        PagedMessage.BottomTypePlaceholders.previousPage, "<blue> %page% </blue>",
                        PagedMessage.BottomTypePlaceholders.nextPage, "<blue> %page% </blue>",
                        PagedMessage.BottomTypePlaceholders.firstPages, "<gold> %page% </gold>",
                        PagedMessage.BottomTypePlaceholders.lastPages, "<red> %page% </red>",
                        PagedMessage.BottomTypePlaceholders.separator, ":",
                        PagedMessage.BottomTypePlaceholders.bigSeparator, "..."),
                5);

    }
    public void load(){

        UUID uuid = UUID.randomUUID();
        GCore.getInstance().getPagedMessagesManager().addPagedMessage(GCore.getInstance(), uuid, pagedMessages.getPagedMessage(uuid, GCore.getInstance()));
    }
}
