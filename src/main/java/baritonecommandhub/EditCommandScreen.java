package baritonecommandhub;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Dedicated screen for editing an existing saved Baritone command.
 *
 * <p>
 * Provides a {@link MultiLineEditBox} pre-populated with the command's
 * current text. Users can modify the command over multiple lines;
 * upon saving, the text is flattened (newlines replaced with spaces)
 * and persisted via {@link Config#updateCommand(int, String)}.
 */
public class EditCommandScreen extends Screen {
    private final Screen parentScreen;
    private final int index;
    private final String initialCommand;
    private MultiLineEditBox commandField;

    /**
     * Constructs a new screen to edit an existing command.
     *
     * @param parentScreen   the {@link CommandManagerScreen} to return to upon
     *                       completion
     * @param index          the index of the command within
     *                       {@link Config#savedCommands}
     * @param initialCommand the current text of the command being edited
     */
    public EditCommandScreen(Screen parentScreen, int index, String initialCommand) {
        super(Component.literal("Edit Command"));
        this.parentScreen = parentScreen;
        this.index = index;
        this.initialCommand = initialCommand;
    }

    @Override
    protected void init() {
        super.init();

        int boxWidth = 300;
        int boxHeight = 100;
        this.commandField = new MultiLineEditBox(this.font, this.width / 2 - boxWidth / 2, this.height / 2 - 70,
                boxWidth, boxHeight, Component.literal("Edit Command"), Component.literal(""));

        this.commandField.setCharacterLimit(32767);
        this.commandField.setValue(initialCommand);
        this.addRenderableWidget(this.commandField);

        // Save Button: Flattens newlines and updates the config
        this.addRenderableWidget(Button.builder(Component.literal("Save"), button -> {
            String newCmd = this.commandField.getValue().trim().replace("\n", " ");
            if (!newCmd.isEmpty()) {
                Config.updateCommand(index, newCmd);
            }
            this.minecraft.setScreen(parentScreen);
        }).bounds(this.width / 2 - 105, this.height / 2 + 40, 100, 20).build());

        // Cancel Button: Discards changes
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            this.minecraft.setScreen(parentScreen);
        }).bounds(this.width / 2 + 5, this.height / 2 + 40, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 85, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
