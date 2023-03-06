package forite.free_move.mixin.client;

import forite.free_move.client.MovementInputSavingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T> extends Screen implements MovementInputSavingScreen {
    @Shadow public abstract boolean keyPressed(int keyCode, int scanCode, int modifiers);
    protected HandledScreenMixin(Text title) {
        super(title);
        throw new RuntimeException();
    }

    private boolean sprintState;
    private boolean sneakState;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void FreeMove_HandleMovement_KeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.handleMovement(keyCode, scanCode, true))
            cir.setReturnValue(true);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.handleMovement(keyCode, scanCode, false))
            return true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    /**
     * Handles movement key press and release
     * @param isPressed the press state to set the given key to
     * @return whether this function handled the key press/release
     */
    private boolean handleMovement(int keyCode, int scanCode, boolean isPressed) {
        assert MinecraftClient.getInstance() != null;
        GameOptions keys = MinecraftClient.getInstance().options;
        boolean keyHandled = false;
        if (keys.forwardKey.matchesKey(keyCode, scanCode)) {
            keys.forwardKey.setPressed(isPressed);
            keyHandled = true;
        }
        if (keys.backKey.matchesKey(keyCode, scanCode)) {
            keys.backKey.setPressed(isPressed);
            keyHandled = true;
        }
        if (keys.leftKey.matchesKey(keyCode, scanCode)) {
            keys.leftKey.setPressed(isPressed);
            keyHandled = true;
        }
        if (keys.rightKey.matchesKey(keyCode, scanCode)) {
            keys.rightKey.setPressed(isPressed);
            keyHandled = true;
        }
        if (keys.jumpKey.matchesKey(keyCode, scanCode)) {
            keys.jumpKey.setPressed(isPressed);
            keyHandled = true;
        }

        // Have to account for the Hold/Toggle Sneak/Sprint options, and in this case
        // we let them account for the Sneak/Sprint state instead if they are Toggle
        if (keys.sprintKey.matchesKey(keyCode, scanCode)) {
            if (!keys.getSneakToggled().getValue() && sneakState) keys.sneakKey.setPressed(true);
            keyHandled = true;
        }
        if (keys.sneakKey.matchesKey(keyCode, scanCode)) {
            if (!keys.getSprintToggled().getValue() && sprintState) keys.sprintKey.setPressed(true);
            keyHandled = true;
        }

        return keyHandled;
    }

    @Override
    public void saveInput() {
        assert MinecraftClient.getInstance() != null;
        GameOptions keys = MinecraftClient.getInstance().options;
        if (keys.sprintKey.isPressed()) this.sprintState = true;
        if (keys.sneakKey.isPressed()) this.sneakState = true;
    }
}
