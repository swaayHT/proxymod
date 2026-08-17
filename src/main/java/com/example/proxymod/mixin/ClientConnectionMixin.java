package com.example.proxymod.mixin;

import com.example.proxymod.ProxyMod;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow private Channel channel;

    @Inject(method = "connect", at = @At("HEAD"))
    private static void onConnect(InetSocketAddress address, boolean useEpoll, ClientConnection connection, CallbackInfo ci) {
        if (ProxyMod.enabled && ProxyMod.host != null && !ProxyMod.host.isEmpty()) {
            connection.channel().pipeline().addFirst("proxy", 
                new Socks5ProxyHandler(new InetSocketAddress(ProxyMod.host, ProxyMod.port))
            );
            ProxyMod.LOGGER.info("[ProxyMod] Connecting through proxy: " + ProxyMod.host + ":" + ProxyMod.port);
        }
    }
}
