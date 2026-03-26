package baritonecommandhub;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side event wiring for the Baritone Command Hub mod.
 *
 * <p>
 * Encapsulates key-binding registration and runtime key-press handling
 * in two nested static classes, each subscribing to a different Forge event
 * bus:
 * <ul>
 * <li>{@link ClientModBusEvents} — {@code MOD} bus, registers the key mapping
 * during the client setup phase.</li>
 * <li>{@link ClientForgeBusEvents} — {@code FORGE} bus, listens for per-tick
 * key-input events at runtime.</li>
 * </ul>
 *
 * @see BaritoneCommandScreen
 */
public class ClientEvents {

    /**
     * Default key binding that opens the Baritone command GUI.
     *
     * <p>
     * Bound to {@code GLFW_KEY_V} by default; players can rebind it
     * through the standard Minecraft controls menu under the
     * {@code "category.yourmod.general"} category.
     */
    public static final KeyMapping OPEN_GUI_KEY = new KeyMapping(
            "key.yourmod.open_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.yourmod.general");

    /**
     * MOD-bus subscriber responsible for registering key mappings during
     * the client mod-loading phase.
     *
     * <p>
     * Forge requires key mappings to be registered via
     * {@link RegisterKeyMappingsEvent} on the MOD bus so they appear in
     * the controls settings screen.
     */
    @Mod.EventBusSubscriber(modid = BaritoneCommandHub.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(OPEN_GUI_KEY);
        }
    }

    /**
     * FORGE-bus subscriber that handles runtime key-input events.
     *
     * <p>
     * When the bound key is pressed (default {@code V}), opens a new
     * {@link BaritoneCommandScreen} instance. Uses
     * {@link KeyMapping#consumeClick()} to prevent the event from being
     * processed multiple times in the same tick.
     */
    @Mod.EventBusSubscriber(modid = BaritoneCommandHub.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeBusEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (OPEN_GUI_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new BaritoneCommandScreen());
            }
        }
    }
}