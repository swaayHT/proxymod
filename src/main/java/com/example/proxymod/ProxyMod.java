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

    public static boolean enabled = false;
    public static String host = "";
    public static int port = 1080;
    public static String username = "";
    public static String password = "";

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
                client.setScreen(new ProxyScreen(null));
            }
        });
    }

    public static void applyProxy() {
        if (enabled && !host.isEmpty()) {
            if (!username.isEmpty()) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                });
            } else {
                Authenticator.setDefault(null);
            }
        }
    }
}
