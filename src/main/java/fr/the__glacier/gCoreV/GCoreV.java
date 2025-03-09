package fr.the__glacier.gCoreV;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "g-corev", name = "G-CoreV", version = BuildConstants.VERSION, description = "A velocity core plugin for other G- plugins", authors = {"The__Glacier"})
public class GCoreV {

    @Inject
    @Getter
    private Logger logger;
    @Inject
    private ProxyServer server;
    @Inject
    private @DataDirectory Path dataDirectory;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(server.toString());
        logger.info(dataDirectory.toString());
    }

    public File getDataFolder(){
        return dataDirectory.toFile();
    }

}
