package com.example.proxymod.mixin;

import com.example.proxymod.ProxyMod;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "connect", at = @At("HEAD"))
    private static void onConnect(InetSocketAddress address, boolean useEpoll, ClientConnection connection, CallbackInfo ci) {
        if (ProxyMod.enabled && ProxyMod.host != null && !ProxyMod.host.isEmpty()) {
            System.setProperty("socksProxyHost", ProxyMod.host);
            System.setProperty("socksProxyPort", String.valueOf(ProxyMod.port));
            ProxyMod.LOGGER.info("[ProxyMod] SOCKS5 proxy set to " + ProxyMod.host + ":" + ProxyMod.port);
        }
    }
}
