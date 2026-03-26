package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Primary Forge mod entrypoint for the Baritone Command Hub mod.
 *
 * <p>Acts as a lightweight overlay that provides a GUI-based command interface
 * for the Baritone pathfinding API. During construction, this class wires up
 * three critical registrations:
 * <ul>
 *   <li>The {@link FMLCommonSetupEvent} lifecycle listener</li>
 *   <li>The Forge {@code EVENT_BUS} for runtime game events (e.g., key bindings)</li>
 *   <li>The {@link Config} specification so Forge manages config file I/O</li>
 * </ul>
 *
 * @see Config
 * @see ClientEvents
 * @see BaritoneCommandScreen
 */
@Mod(BaritoneCommandHub.MODID)
public class BaritoneCommandHub
{
    /** Mod identifier used across Forge registration annotations. */
    public static final String MODID = "baritonecommandhub";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BaritoneCommandHub(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /**
     * Handles the common-setup lifecycle phase.
     *
     * <p>Currently used only for a startup confirmation log. Future
     * cross-side initialization (e.g., network channel registration)
     * should be placed here.
     *
     * @param event the common-setup event fired by Forge mod loader
     */
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Baritone Command Hub initialized.");
    }
}
