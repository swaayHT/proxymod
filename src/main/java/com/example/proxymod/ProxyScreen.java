package com.example.proxymod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ProxyScreen extends Screen {
    private final Screen parent;

    private TextFieldWidget ipField;
    private TextFieldWidget portField;
    private TextFieldWidget usernameField;
    private TextFieldWidget passwordField;

    public ProxyScreen(Screen parent) {
        super(Text.literal("Налаштування Проксі"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 35;
        int fieldWidth = 200;
        int fieldHeight = 20;

        this.ipField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 15, fieldWidth, fieldHeight, Text.literal("IP"));
        this.ipField.setText(ProxyMod.host);
        this.ipField.setMaxLength(255);
        this.addSelectableChild(this.ipField);

        this.portField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 55, fieldWidth, fieldHeight, Text.literal("Port"));
        this.portField.setText(String.valueOf(ProxyMod.port));
        this.portField.setMaxLength(10);
        this.addSelectableChild(this.portField);

        this.usernameField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 95, fieldWidth, fieldHeight, Text.literal("Username"));
        this.usernameField.setText(ProxyMod.username);
        this.usernameField.setMaxLength(255);
        this.addSelectableChild(this.usernameField);

        this.passwordField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 135, fieldWidth, fieldHeight, Text.literal("Password"));
        this.passwordField.setText(ProxyMod.password);
        this.passwordField.setMaxLength(255);
        this.addSelectableChild(this.passwordField);

        ButtonWidget toggleButton = ButtonWidget.builder(
                Text.literal("Проксі: " + (ProxyMod.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")),
                button -> {
                    ProxyMod.enabled = !ProxyMod.enabled;
                    button.setMessage(Text.literal("Проксі: " + (ProxyMod.enabled ? "УВІМКНЕНО" : "ВИМКНЕНО")));
                }
        ).dimensions(centerX - 100, startY + 168, fieldWidth, fieldHeight).build();
        this.addDrawableChild(toggleButton);

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Зберегти та Застосувати"),
                button -> {
                    saveAndApply();
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                }
        ).dimensions(centerX - 100, startY + 195, fieldWidth, fieldHeight).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Назад"),
                button -> {
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                }
        ).dimensions(centerX - 100, startY + 220, fieldWidth, fieldHeight).build());
    }

    private void saveAndApply() {
        ProxyMod.host = this.ipField.getText().trim();
        try {
            ProxyMod.port = Integer.parseInt(this.portField.getText().trim());
        } catch (NumberFormatException e) {
            ProxyMod.port = 1080;
        }
        ProxyMod.username = this.usernameField.getText().trim();
        ProxyMod.password = this.passwordField.getText().trim();

        ProxyMod.applyProxy();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int centerX = this.width / 2;
        int startY = 35;

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, 12, 0xFFFFFF);

        context.drawTextWithShadow(this.textRenderer, "IP Адреса:", centerX - 100, startY, 0xA0A0A0);
        this.ipField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "Порт:", centerX - 100, startY + 40, 0xA0A0A0);
        this.portField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "Користувач (Username):", centerX - 100, startY + 80, 0xA0A0A0);
        this.usernameField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, "Пароль (Password):", centerX - 100, startY + 120, 0xA0A0A0);
        this.passwordField.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }
}
