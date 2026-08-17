package com.example.proxymod.mixin;

import com.example.proxymod.ProxyMod;
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
}
