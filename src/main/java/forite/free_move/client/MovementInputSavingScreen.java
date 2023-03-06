package forite.free_move.client;

/**
 * Describes a screen that remembers the inital movement state of sprinting and sneaking, as well
 * as indicates by type that Movement Category KeyBindings should not be unpressed when switching
 * to this screen
 */
public interface MovementInputSavingScreen {

    /**
     * Called by a Mixin when switching Screens
     */
    void saveInput();

}
