package com.example.proxymod.mixin;

import com.example.proxymod.ProxyMod;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "connect", at = @At("HEAD"))
    private static void onConnect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir) {
        ProxyMod.applyProxy();
    }

    @Inject(method = "connect", at = @At("RETURN"))
    private static void onConnectReturn(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir) {
        if (ProxyMod.proxyEnabled && !ProxyMod.proxyHost.isEmpty()) {
            ClientConnection connection = cir.getReturnValue();
            if (connection != null) {
                Channel channel = connection.getChannel();
                if (channel != null) {
                    InetSocketAddress proxyAddress = new InetSocketAddress(ProxyMod.proxyHost, ProxyMod.proxyPort);
                    
                    Socks5ProxyHandler proxyHandler;
                    if (!ProxyMod.proxyUser.isEmpty()) {
                        proxyHandler = new Socks5ProxyHandler(proxyAddress, ProxyMod.proxyUser, ProxyMod.proxyPassword);
                    } else {
                        proxyHandler = new Socks5ProxyHandler(proxyAddress);
                    }

                    // Вставляємо SOCKS5 проксі-хендлер на початок Netty пайплайну
                    channel.pipeline().addFirst("proxy", proxyHandler);
                }
            }
        }
    }
}
