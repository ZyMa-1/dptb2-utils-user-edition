package weebify.dptb2utils.utils;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import weebify.dptb2utils.DPTB2Utils;

public class ModSounds {

    public static final SoundEvent TRAFFIC_WARNING =
            SoundEvent.of(
                    Identifier.of(DPTB2Utils.MOD_ID, "traffic_warning")
            );
    public static final SoundEvent MICRO_WARNING =
            SoundEvent.of(
                    Identifier.of(DPTB2Utils.MOD_ID, "micro_warning")
            );
}