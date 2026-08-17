package com.example.proxymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyMod implements ClientModInitializer {
    public static String host = "";
    public static int port = 1080;
    public static String username = "";
    public static String password = "";
    public static boolean enabled = false;

    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.proxymod.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.proxymod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ProxyScreen(null));
                }
            }
        });
    }

    public static void applyProxy() {
        if (enabled && !host.isEmpty() && port > 0) {
            System.setProperty("socksProxyHost", host);
            System.setProperty("socksProxyPort", String.valueOf(port));

            if (!username.isEmpty() || !password.isEmpty()) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                });
            } else {
                Authenticator.setDefault(null);
            }
            System.out.println("[ProxyMod] Проксі активовано: " + host + ":" + port);
        } else {
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
            Authenticator.setDefault(null);
            System.out.println("[ProxyMod] Проксі вимкнено.");
        }
    }
}
