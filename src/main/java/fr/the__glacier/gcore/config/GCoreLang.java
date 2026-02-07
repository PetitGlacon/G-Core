package fr.the__glacier.gcore.config;

import fr.the__glacier.gcore.config.configObjects.BaseLang;

public class GCoreLang extends BaseLang {
    public String gcore_test;
    public GCoreLang() {}
    public static GCoreLang en_US(){
        GCoreLang lang = new GCoreLang();
        lang.gcore_test = "traduction US";
        return lang;
    }
    public static GCoreLang fr_FR(){
        GCoreLang lang = new GCoreLang();
        lang.gcore_test = "traduction FR";
        return lang;
    }
}
