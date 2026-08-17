package com.example.proxymod.mixin;

import com.example.proxymod.ProxyConfigScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addProxyButton(CallbackInfo ci) {
        // Кнопка у лівому верхньому кутку головного меню
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Proxy"), button -> {
            if (this.client != null) {
                this.client.setScreen(new ProxyConfigScreen(this));
            }
        }).dimensions(10, 10, 60, 20).build());
    }
}
