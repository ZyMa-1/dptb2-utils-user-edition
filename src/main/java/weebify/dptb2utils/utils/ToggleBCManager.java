package weebify.dptb2utils.utils;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import weebify.dptb2utils.DPTB2Utils;

public class ToggleBCManager {


    public static void initialize() {
        HudRenderCallback.EVENT.register(ToggleBCManager::renderToggleBC);
    }

    private static void renderToggleBC(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        DPTB2Utils mod = DPTB2Utils.getInstance();

        // Safety check to prevent the "Exit -1" crash
        if (mc.player == null || mc.world == null) return;

        // The logic: show if in DPTB2 mode AND (no screen open OR chat open)
        if (mod.isInDPTB2 && (mc.currentScreen == null || mc.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen)) {
            if (mod.getBoolConfig("toggleBC.enabled")) {
                int sw = mc.getWindow().getScaledWidth();
                int sh = mc.getWindow().getScaledHeight();

                // Get positions from config
                int x = (int) (sw * mod.getFloatConfig("toggleBC.posX"));
                int y = (int) (sh * mod.getFloatConfig("toggleBC.posY"));

                String status = mod.isToggleBc ? "§aON" : "§cOFF";
                drawContext.drawTextWithShadow(mc.textRenderer, "§7ToggleBC: " + status, x, y, 0xFFFFFF);
            }
        }
    }
}

