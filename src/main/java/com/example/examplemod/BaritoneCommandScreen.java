package com.example.examplemod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import baritone.api.BaritoneAPI;

/**
 * Primary GUI screen for composing and dispatching Baritone commands.
 *
 * <p>Presents a {@link MultiLineEditBox} that supports multi-line input with
 * text wrapping. Before execution, newlines are collapsed into spaces so that
 * Baritone receives a single flat command string.
 *
 * <p><b>History navigation:</b> Page Up / Page Down keys cycle through the
 * MRU command history maintained by {@link Config#historyCommands}. The
 * cursor position ({@code historyPos}) is initialised to one past the end
 * of the list, representing the "blank new command" state.
 *
 * <p><b>Button layout</b> (two rows + a third management row):
 * <ol>
 *   <li>Execute | Cancel</li>
 *   <li>Save Command | Clear</li>
 *   <li>Manage Commands (opens {@link CommandManagerScreen})</li>
 * </ol>
 *
 * @see Config#addHistoryCommand(String)
 * @see CommandManagerScreen
 */
public class BaritoneCommandScreen extends Screen {
    private MultiLineEditBox commandField;

    /** Index into {@link Config#historyCommands}; equal to list size means "new command". */
    private int historyPos = 0;
    

    public BaritoneCommandScreen() {
        super(Component.literal("Baritone Command Hub"));
    }

    /**
     * Initialises the GUI widgets each time the screen is opened or resized.
     *
     * <p>Resets {@code historyPos} to the end of the history list so the
     * player starts with an empty input field, then constructs the edit box
     * and the five action buttons.
     */
    @Override
    protected void init() {
        super.init();
        
        if (Config.historyCommands != null) {
            historyPos = Config.historyCommands.size();
        }

        int boxWidth = 300;
        int boxHeight = 100;
        this.commandField = new MultiLineEditBox(this.font, this.width / 2 - boxWidth / 2, this.height / 2 - 70, boxWidth, boxHeight, Component.literal("Baritone Command"), Component.literal(""));
        
        this.commandField.setCharacterLimit(32767); 
        this.addRenderableWidget(this.commandField);

        this.addRenderableWidget(Button.builder(Component.literal("Execute"), button -> {
            this.executeCommand();
        }).bounds(this.width / 2 - 105, this.height / 2 + 40, 100, 20).build());
        
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 + 5, this.height / 2 + 40, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Save Command"), button -> {
            String cmd = this.commandField.getValue().trim();
            if (!cmd.isEmpty()) {
                Config.saveCommand(cmd);
            }
        }).bounds(this.width / 2 - 105, this.height / 2 + 65, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Clear"), button -> {
            this.commandField.setValue("");
        }).bounds(this.width / 2 + 5, this.height / 2 + 65, 100, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Manage Commands"), button -> {
            this.minecraft.setScreen(new CommandManagerScreen(this));
        }).bounds(this.width / 2 - 50, this.height / 2 + 90, 100, 20).build());
    }

    /**
     * Intercepts Page Up / Page Down for command history navigation.
     *
     * <p>Page Up decrements {@code historyPos} (showing older commands);
     * Page Down increments it. When {@code historyPos} moves past the last
     * history entry, the edit field is cleared to let the player type a
     * fresh command. All other key events are delegated to the superclass
     * so that the {@link MultiLineEditBox} receives normal text input.
     *
     * @return {@code true} if the key event was consumed by history navigation
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP) {
            if (Config.historyCommands != null && !Config.historyCommands.isEmpty()) {
                if (historyPos > 0) historyPos--;
                this.commandField.setValue(Config.historyCommands.get(historyPos));
            }
            return true;
        } else if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN) {
            if (Config.historyCommands != null && !Config.historyCommands.isEmpty()) {
                if (historyPos < Config.historyCommands.size() - 1) {
                    historyPos++;
                    this.commandField.setValue(Config.historyCommands.get(historyPos));
                } else if (historyPos == Config.historyCommands.size() - 1) {
                    historyPos++;
                    this.commandField.setValue("");
                }
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Flattens the multi-line input into a single-line command, records it
     * in the history, and dispatches it to Baritone's command manager.
     *
     * <p>Newline characters ({@code \n}) are replaced with spaces before
     * execution because Baritone's command parser expects a single-line
     * string. The screen is closed immediately after dispatch regardless
     * of whether the command succeeded.
     */
    private void executeCommand() {
        String cmd = this.commandField.getValue().trim();
        if (!cmd.isEmpty()) {
            String flatCmd = cmd.replace("\n", " ");
            Config.addHistoryCommand(flatCmd);
            if (BaritoneAPI.getProvider().getPrimaryBaritone() != null) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(flatCmd);
            }
        }
        this.minecraft.setScreen(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics); 
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 85, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}