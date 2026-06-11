package weebify.dptb2utils.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import weebify.dptb2utils.DPTB2Utils;

public class TrafficLightsManager {

    // 2m 50s
    private static final int GREEN_DURATION_TICKS = 170 * 20;

    // warning 3 seconds before red
    private static final int WARNING_TIME_TICKS = 60;

    private static int trafficTimer = -1;

    public static void setGreen() {
        trafficTimer = GREEN_DURATION_TICKS;
    }

    public static void setRed() {
        trafficTimer = -1;
    }

    public static void reset() {
        trafficTimer = -1;
    }

    private static void playWarningSound() {
        MinecraftClient.getInstance().getSoundManager().play(
                PositionedSoundInstance.master(
                        ModSounds.TRAFFIC_WARNING,
                        1.0F
                )
        );
    }

    public static void initialize() {
        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            DPTB2Utils mod = DPTB2Utils.getInstance();

            if (!mod.isInDPTB2) {
                return;
            }

            if (trafficTimer < 0) {
                return;
            }

            if (trafficTimer == WARNING_TIME_TICKS
                    && mod.getBoolConfig("others.trafficLightsWarning")) {
                playWarningSound();
            }

            trafficTimer--;

            if (trafficTimer <= 0) {
                reset();
            }
        });
    }
}