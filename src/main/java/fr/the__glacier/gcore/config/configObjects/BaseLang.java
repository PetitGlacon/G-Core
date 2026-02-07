package fr.the__glacier.gcore.config.configObjects;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class BaseLang {
    Map<String, String> customs;
    public BaseLang(){}
    public void register(TranslationStore.StringBased<MessageFormat> store, Locale locale){

        Field[] fields = this.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(f -> f.getType().equals(String.class))
                .forEach(f -> {
                    String key = f.getName().replaceAll("_", ".");
                    try {
                        String value = (String) f.get(this);
                        store.register(key, locale, new MessageFormat(value));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        if (this.customs != null){
            this.customs.forEach((key, value) ->
                    store.register(key, locale, new MessageFormat(value)));
        }
        GlobalTranslator.translator().addSource(store);
    }
}
