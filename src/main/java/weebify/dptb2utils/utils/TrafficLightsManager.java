package weebify.dptb2utils.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
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

    public static void playWarningSound() {
        MinecraftClient.getInstance().getSoundManager().play(
                PositionedSoundInstance.master(
                        ModSounds.TRAFFIC_WARNING,
                        1.0F
                )
        );
    }

    private static double distanceToRegion(
            double px, double py, double pz,
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ
    ) {
        double dx = Math.max(Math.max(minX - px, 0), px - maxX);
        double dy = Math.max(Math.max(minY - py, 0), py - maxY);
        double dz = Math.max(Math.max(minZ - pz, 0), pz - maxZ);

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static double getTrafficDistance(PlayerEntity player) {
        return Math.min(
                distanceToRegion(player.getX(), player.getY(), player.getZ(),
                        56, 20, -11,
                        67, 25, -10),
                distanceToRegion(player.getX(), player.getY(), player.getZ(),
                        56, 20, 37,
                        67, 25, 38)
        );
    }

    public static void initialize() {
        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            DPTB2Utils mod = DPTB2Utils.getInstance();
            GameState gameState = mod.getGameState();

            if (!mod.isInDPTB2 || mc.player == null || trafficTimer < 0) {
                return;
            }

            if (trafficTimer == WARNING_TIME_TICKS
                    && mod.getBoolConfig("others.trafficLightsWarning")
                    && gameState.isCity()
                    && getTrafficDistance(mc.player) <= 40.0) {

                playWarningSound();
            }

            trafficTimer--;

        });
    }
}