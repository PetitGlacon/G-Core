package fr.the__glacier.gcore.test;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.util.PageMessages;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConfigTest {
    public Boolean enable = true;
    public PageMessageConfigTest pageMessages;

    public ConfigTest(){
        pageMessages = new PageMessageConfigTest(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"),
                List.of("<gold>Test de messages"),
                "<blue>%previous%<- Previous%/previous%</blue><white> -- %page%/%max_page% -- </white><blue>%next%Next ->%/next%</blue>",
                PageMessages.BottomType.previousAndNextButton,
                ImmutableMap.of(
                        PageMessages.BottomTypePlaceholders.page, "<blue><b> %page% </b></blue>",
                        PageMessages.BottomTypePlaceholders.previousPage, "<blue> %page% </blue>",
                        PageMessages.BottomTypePlaceholders.nextPage, "<blue> %page% </blue>",
                        PageMessages.BottomTypePlaceholders.firstPages, "<gold> %page% </gold>",
                        PageMessages.BottomTypePlaceholders.lastPages, "<red> %page% </red>",
                        PageMessages.BottomTypePlaceholders.separator, ":",
                        PageMessages.BottomTypePlaceholders.bigSeparator, "..."),
                5);

    }
    public void load(){

        UUID uuid = UUID.randomUUID();
        GCore.getInstance().pageMessagesMap.put(uuid, pageMessages.getPageMessage(uuid, GCore.getInstance()));
    }
}
