package com.example.proxymod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ProxyConfigScreen extends Screen {

    private final Screen parent;
    private TextFieldWidget hostField;
    private TextFieldWidget portField;
    private TextFieldWidget userField;
    private TextFieldWidget passField;

    public ProxyConfigScreen(Screen parent) {
        super(Text.literal("Proxy Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;

        this.hostField = new TextFieldWidget(this.textRenderer, centerX - 100, startY, 200, 20, Text.literal("IP Address"));
        this.hostField.setText(ProxyMod.host);
        this.addSelectableChild(this.hostField);

        this.portField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 30, 200, 20, Text.literal("Port"));
        this.portField.setText(String.valueOf(ProxyMod.port));
        this.addSelectableChild(this.portField);

        this.userField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 60, 200, 20, Text.literal("Username"));
        this.userField.setText(ProxyMod.username);
        this.addSelectableChild(this.userField);

        this.passField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 90, 200, 20, Text.literal("Password"));
        this.passField.setText(ProxyMod.password);
        this.addSelectableChild(this.passField);

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Проксі: " + (ProxyMod.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")),
                button -> {
                    ProxyMod.enabled = !ProxyMod.enabled;
                    button.setMessage(Text.literal("Проксі: " + (ProxyMod.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")));
                }
        ).dimensions(centerX - 100, startY + 125, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Зберегти"), button -> {
            ProxyMod.host = this.hostField.getText().trim();
            try {
                ProxyMod.port = Integer.parseInt(this.portField.getText().trim());
            } catch (NumberFormatException ignored) {
                ProxyMod.port = 1080;
            }
            ProxyMod.username = this.userField.getText().trim();
            ProxyMod.password = this.passField.getText().trim();
            ProxyMod.applyProxy();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).dimensions(centerX - 100, startY + 155, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        context.drawTextWithShadow(this.textRenderer, "IP Address:", this.width / 2 - 100, this.height / 4 - 12, 0xA0A0A0);
        this.hostField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "Port:", this.width / 2 - 100, this.height / 4 + 18, 0xA0A0A0);
        this.portField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "User (optional):", this.width / 2 - 100, this.height / 4 + 48, 0xA0A0A0);
        this.userField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "Password (optional):", this.width / 2 - 100, this.height / 4 + 78, 0xA0A0A0);
        this.passField.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
