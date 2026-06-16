package weebify.dptb2utils.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import weebify.dptb2utils.DPTB2Utils;
import weebify.dptb2utils.utils.GameState;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onSetOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        GameState gameState = DPTB2Utils.getInstance().getGameState();

        String text = message.getString();

        GameState.MapType newMap = null;

        if (text.contains("Map: City")) {
            newMap = GameState.MapType.CITY;
        } else if (text.contains("Map: Wild West")) {
            newMap = GameState.MapType.WILD_WEST;
        }

        if (newMap != null && newMap != gameState.getCurrentMap()) {
            gameState.setCurrentMap(newMap);
        }
    }
}