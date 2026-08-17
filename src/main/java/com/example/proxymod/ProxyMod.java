package com.example.proxymod;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("proxymod");

    // Твої налаштування SOCKS5 проксі
    public static boolean enabled = true;
    public static String host = "127.0.0.1";
    public static int port = 1080;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[ProxyMod] Mod initialized!");
    }
}
