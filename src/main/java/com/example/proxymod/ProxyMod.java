package com.example.proxymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyMod implements ModInitializer {

    public static boolean proxyEnabled = false;
    public static String proxyHost = "";
    public static int proxyPort = 1080;
    public static String proxyUser = "";
    public static String proxyPassword = "";

    private static KeyBinding openGuiKey;

    @Override
    public void onInitialize() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Відкрити меню Проксі",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "Proxy Mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new ProxyConfigScreen(null));
            }
        });
    }

    public static void applyProxy() {
        if (proxyEnabled && !proxyHost.isEmpty()) {
            if (!proxyUser.isEmpty()) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                    }
                });
            } else {
                Authenticator.setDefault(null);
            }
        }
    }
}
