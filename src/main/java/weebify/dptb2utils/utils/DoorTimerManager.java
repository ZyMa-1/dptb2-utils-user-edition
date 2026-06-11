package weebify.dptb2utils.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.sound.SoundEvents;
import weebify.dptb2utils.DPTB2Utils;
import weebify.dptb2utils.gui.screen.DoorTimerConfigScreen;

public class DoorTimerManager {
    public static final String prefix = "Door switch in: ";
    // 10 min
    public static final int defaultDoorTimerTicks = 10 * 60 * 20;
    private static int doorTimer = -1;


    public static void startDoorTimer() {
        doorTimer = defaultDoorTimerTicks;
    }

    public static void resetDoorTimer() {
        doorTimer = -1;
    }

    public static void playWarningSound() {
        MinecraftClient.getInstance().getSoundManager().play(
                PositionedSoundInstance.master(
                        ModSounds.DOOR_WARNING,
                        1.0F
                )
        );
    }

    public static Text tickToTime(int ticks) {
        if (ticks < 0) {
            return Text.of("N/A");
        }

        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;

        String timeString = String.format("%02d:%02d", minutes, seconds);

        if (ticks <= 200) { // 10 seconds
            return Text.literal(timeString).formatted(Formatting.RED);
        }
        if (ticks <= 600) { // 30 seconds
            return Text.literal(timeString).formatted(Formatting.GOLD);
        }

        if (ticks <= 1200) { // 1 minute
            return Text.literal(timeString).formatted(Formatting.YELLOW);
        }

        return Text.literal(timeString);
    }

    public static void initialize() {
        ClientTickEvents.START_CLIENT_TICK.register((mc) -> {
            DPTB2Utils mod = DPTB2Utils.getInstance();
            if (mod.isInDPTB2) {
                if (doorTimer >= 0) {
                    if (doorTimer == 200 && mod.getBoolConfig("doorTimer.playWarningSound")) {
                        DoorTimerManager.playWarningSound();
                    }
                    doorTimer--;
                }
            }
        });

        HudRenderCallback.EVENT.register(DoorTimerManager::renderDoorTimer);
    }

    private static void renderDoorTimer(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        DPTB2Utils mod = DPTB2Utils.getInstance();

        if (mod.isInDPTB2
                && mod.getBoolConfig("doorTimer.enabled")
                && !(mc.currentScreen instanceof DoorTimerConfigScreen)) {

            int width = mc.getWindow().getScaledWidth();
            int height = mc.getWindow().getScaledHeight();

            int posX = (int) (mod.getFloatConfig("doorTimer.posX") * width);
            int posY = (int) (mod.getFloatConfig("doorTimer.posY") * height);

            Text timerText = DoorTimerManager.tickToTime(doorTimer);

            int widgetWidth = Math.max(
                    mc.textRenderer.getWidth(timerText),
                    mc.textRenderer.getWidth(Text.of(prefix))
            );

            if (mod.getBoolConfig("doorTimer.renderBackground")) {
                drawContext.fill(
                        posX,
                        posY,
                        posX + widgetWidth + 8,
                        posY + 21 + mc.textRenderer.fontHeight,
                        0x63000000
                );
            }

            drawContext.drawText(
                    mc.textRenderer,
                    prefix,
                    posX + 4,
                    posY + 4,
                    Colors.WHITE,
                    mod.getBoolConfig("doorTimer.textShadow")
            );

            drawContext.drawText(
                    mc.textRenderer,
                    timerText,
                    posX + 4,
                    posY + 4 + mc.textRenderer.fontHeight + 3,
                    Colors.WHITE,
                    mod.getBoolConfig("doorTimer.textShadow")
            );
        }
    }
}

