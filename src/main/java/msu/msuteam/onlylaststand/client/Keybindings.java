package msu.msuteam.onlylaststand.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
    public static final KeyMapping OPEN_ACCESSORY_KEY = new KeyMapping(
            "key.onlylaststand.open_accessory_inventory",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.category.onlylaststand"
    );

    // --- ДОБАВЬТЕ ЭТОТ КОД ---
    public static final KeyMapping OPEN_SPELL_KEY = new KeyMapping(
            "key.onlylaststand.open_spell_inventory",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "key.category.onlylaststand"
    );
    // -------------------------

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ACCESSORY_KEY);
        event.register(OPEN_SPELL_KEY); // <-- ДОБАВЬТЕ ЭТУ СТРОКУ
    }
}