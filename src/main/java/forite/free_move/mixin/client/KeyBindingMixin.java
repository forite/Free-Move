package forite.free_move.mixin.client;

import forite.free_move.client.MovementInputSavingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;

/**
 * Compatibility Concerns: <br>
 * cancels unpressAll() if the current MinecraftClient's Screen is a MovementInputSavingScreen
 */
@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;
    @Shadow @Final public static String MOVEMENT_CATEGORY;

    /**
     * Changes unpress behavior for when MinecraftClient's current Screen is a MovementInputSavingScreen
     * (i.e. a HandledScreen), only cancelling unpressing KeyBindings that are not in the Movement Category.
     */
    @Inject(method = "unpressAll", at = @At("HEAD"), cancellable = true)
    private static void FreeMove_MovementInputSaving_UnpressAll(CallbackInfo ci) {
        assert MinecraftClient.getInstance().currentScreen != null;
        if (MinecraftClient.getInstance().currentScreen instanceof MovementInputSavingScreen) {
            for (KeyBinding key: KEYS_BY_ID.values()) {
                if (!key.getCategory().equals(MOVEMENT_CATEGORY)) {
                    key.reset(); // Access Widener used here
                }
            }
            ((MovementInputSavingScreen) (MinecraftClient.getInstance().currentScreen)).saveInput();
            ci.cancel();
        }
    }
}
