package baritonecommandhub;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

/**
 * Forge configuration holder for the Baritone Command Hub mod.
 *
 * <p>
 * Manages two persistent lists backed by {@link ForgeConfigSpec}:
 * <ul>
 * <li><b>Saved commands</b> — user-bookmarked Baritone commands, persisted
 * indefinitely until manually deleted.</li>
 * <li><b>History commands</b> — a Most-Recently-Used (MRU) ring buffer
 * of the last 100 executed commands, navigable via Page Up / Page Down
 * in {@link BaritoneCommandScreen}.</li>
 * </ul>
 *
 * <p>
 * Both lists are flushed to disk via {@code SPEC.save()} after every
 * mutation, ensuring survival across game restarts.
 *
 * @see BaritoneCommandScreen
 * @see CommandManagerScreen
 */
@Mod.EventBusSubscriber(modid = BaritoneCommandHub.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SAVED_COMMANDS = BUILDER
            .comment("A list of saved Baritone commands.")
            .defineListAllowEmpty("savedCommands", List.of(), o -> o instanceof String);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> HISTORY_COMMANDS = BUILDER
            .comment("History of executed commands (max 100).")
            .defineListAllowEmpty("historyCommands", List.of(), o -> o instanceof String);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    /** Mutable in-memory mirror of the saved-commands config list. */
    public static List<String> savedCommands;

    /** Mutable in-memory mirror of the command-history config list. */
    public static List<String> historyCommands;

    /**
     * Synchronises the in-memory lists with the on-disk config values.
     *
     * <p>
     * Invoked automatically by Forge whenever the config file is loaded
     * or reloaded. The raw immutable lists from the spec are copied into
     * mutable {@link java.util.ArrayList} instances so that runtime
     * mutations (add / remove) can be performed freely.
     *
     * @param event the config lifecycle event
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        savedCommands = new java.util.ArrayList<>(SAVED_COMMANDS.get());
        historyCommands = new java.util.ArrayList<>(HISTORY_COMMANDS.get());
    }

    /**
     * Persists a Baritone command to the saved-commands list.
     *
     * <p>
     * Duplicate entries are silently ignored to prevent clutter in the
     * {@link CommandManagerScreen} UI.
     *
     * @param cmd the command string to save; must not be {@code null}
     */
    public static void saveCommand(String cmd) {
        if (!savedCommands.contains(cmd)) {
            savedCommands.add(cmd);
            SAVED_COMMANDS.set(savedCommands);
            SPEC.save();
        }
    }

    /**
     * Removes all saved commands and flushes the empty list to disk.
     */
    public static void clearCommands() {
        savedCommands.clear();
        SAVED_COMMANDS.set(savedCommands);
        SPEC.save();
    }

    /**
     * Records an executed command into the MRU history ring.
     *
     * <p>
     * If the command already exists in the history it is first removed,
     * then re-appended at the tail so the most recent invocation always
     * occupies the latest position. When the history exceeds 100 entries,
     * the oldest entry (head) is evicted in FIFO order.
     *
     * @param cmd the command string to record; empty strings are ignored
     */
    public static void addHistoryCommand(String cmd) {
        if (!cmd.isEmpty()) {
            historyCommands.remove(cmd);
            historyCommands.add(cmd);
            while (historyCommands.size() > 100) {
                historyCommands.remove(0);
            }
            HISTORY_COMMANDS.set(historyCommands);
            SPEC.save();
        }
    }
}
