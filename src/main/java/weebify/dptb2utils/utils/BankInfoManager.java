package weebify.dptb2utils.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import weebify.dptb2utils.DPTB2Utils;
import weebify.dptb2utils.gui.screen.BankInfoConfigScreen;

public class BankInfoManager {

    private static int tntPlayerCount = 0;
    private static int scanTicks = 0;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            DPTB2Utils mod = DPTB2Utils.getInstance();

            if (mod.isInDPTB2 && mc.player != null) {
                GameState gameState = mod.getGameState();

                if (gameState.isWildWest()) {
                    if (++scanTicks >= 40) {
                        scanTicks = 0;
                        updateTntPlayerCount(mc);
                    }
                }
            }
        });

        HudRenderCallback.EVENT.register(BankInfoManager::render);
    }

    private static void updateTntPlayerCount(MinecraftClient mc) {
        int count = 0;

        if (mc.world == null) {
            return;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            ItemStack helmet =
                    player.getEquippedStack(EquipmentSlot.HEAD);

            if (helmet.isOf(Items.TNT)) {
                count++;
            }
        }

        tntPlayerCount = count;
    }

    private static void render(
            DrawContext drawContext,
            RenderTickCounter renderTickCounter
    ) {
        MinecraftClient mc = MinecraftClient.getInstance();
        DPTB2Utils mod = DPTB2Utils.getInstance();
        GameState gameState = mod.getGameState();

        if (mod.isInDPTB2
                && mod.getBoolConfig("bankInfo.enabled")
                && gameState.isWildWest()
                && !(mc.currentScreen instanceof BankInfoConfigScreen)) {
            int width = mc.getWindow().getScaledWidth();
            int height = mc.getWindow().getScaledHeight();

            int posX = (int) (mod.getFloatConfig("bankInfo.posX") * width);
            int posY = (int) (mod.getFloatConfig("bankInfo.posY") * height);

            Text jackpotText;

            if (gameState.getCurrentJackpotValue() < 0) {
                jackpotText = Text.literal("Jackpot: N/A");
            } else {
                jackpotText = Text.empty()
                        .append(Text.literal("Jackpot: "))
                        .append(
                                Text.literal(
                                        String.format(
                                                "%,d",
                                                gameState.getCurrentJackpotValue()
                                        )
                                ).formatted(Formatting.GOLD)
                        )
                        .append(
                                Text.literal(" ⛂")
                                        .formatted(Formatting.GOLD)
                        );
            }

            Text tntText = Text.literal(
                    "Players with TNT: " + tntPlayerCount
            );

            int widgetWidth = Math.max(
                    mc.textRenderer.getWidth(jackpotText),
                    mc.textRenderer.getWidth(tntText)
            );

            int widgetHeight =
                    mc.textRenderer.fontHeight * 2 + 11;

            if (mod.getBoolConfig("bankInfo.renderBackground")) {
                drawContext.fill(
                        posX,
                        posY,
                        posX + widgetWidth + 8,
                        posY + widgetHeight,
                        0x63000000
                );
            }

            boolean shadow =
                    mod.getBoolConfig("bankInfo.textShadow");

            drawContext.drawText(
                    mc.textRenderer,
                    jackpotText,
                    posX + 4,
                    posY + 4,
                    Colors.WHITE,
                    shadow
            );

            drawContext.drawText(
                    mc.textRenderer,
                    tntText,
                    posX + 4,
                    posY + 7 + mc.textRenderer.fontHeight,
                    Colors.WHITE,
                    shadow
            );
        }
    }
}