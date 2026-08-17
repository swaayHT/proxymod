package com.example.proxymod.mixin;

import com.example.proxymod.ProxyMod;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Shadow
    private Channel channel;

    @Inject(method = "connect", at = @At("HEAD"))
    private static void onConnect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir) {
        ProxyMod.applyProxy();
    }

    @Inject(method = "connect", at = @At("RETURN"))
    private static void onConnectReturn(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir) {
        if (ProxyMod.enabled && !ProxyMod.host.isEmpty()) {
            ClientConnection connection = cir.getReturnValue();
            if (connection != null) {
                // Використовуємо ін'єкцію в саму конкретну сутність підключення
                Channel nettyChannel = ((ClientConnectionMixin) (Object) connection).channel;
                if (nettyChannel != null) {
                    InetSocketAddress proxyAddress = new InetSocketAddress(ProxyMod.host, ProxyMod.port);
                    
                    Socks5ProxyHandler proxyHandler;
                    if (!ProxyMod.username.isEmpty()) {
                        proxyHandler = new Socks5ProxyHandler(proxyAddress, ProxyMod.username, ProxyMod.password);
                    } else {
                        proxyHandler = new Socks5ProxyHandler(proxyAddress);
                    }

                    nettyChannel.pipeline().addFirst("proxy", proxyHandler);
                }
            }
        }
    }
}
