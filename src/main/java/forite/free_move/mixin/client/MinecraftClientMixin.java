package forite.free_move.mixin.client;

import forite.free_move.client.MovementInputSavingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Compatibility Concerns: <br>
 * None
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin  {

    @Inject(method = "setScreen", at = @At(value = "INVOKE",
            target = "net/minecraft/client/option/KeyBinding.unpressAll ()V"))
    public void FreeMove_SaveMovementInput_SetScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof MovementInputSavingScreen) {
            ((MovementInputSavingScreen) screen).saveInput();
        }
    }
}
