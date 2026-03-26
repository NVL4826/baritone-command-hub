package com.example.examplemod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import baritone.api.BaritoneAPI;
import java.util.List;

/**
 * Paginated management screen for viewing, executing, and deleting
 * previously saved Baritone commands.
 *
 * <p>Displays up to {@value #ITEMS_PER_PAGE} commands per page, each
 * rendered as two buttons:
 * <ul>
 *   <li>A <b>command button</b> (truncated to 30 characters) that immediately
 *       dispatches the command to Baritone and records it in history.</li>
 *   <li>A <b>delete button</b> that removes the entry from
 *       {@link Config#savedCommands} and refreshes the page.</li>
 * </ul>
 *
 * <p>Navigation buttons ({@code <} / {@code >}) allow paging through the
 * full list, and a "Back" button returns to the parent
 * {@link BaritoneCommandScreen}.
 *
 * @see Config#saveCommand(String)
 * @see BaritoneCommandScreen
 */
public class CommandManagerScreen extends Screen {
    private final Screen parentScreen;
    private int currentPage = 0;

    /** Maximum number of command entries displayed on a single page. */
    private static final int ITEMS_PER_PAGE = 5;

    public CommandManagerScreen(Screen parentScreen) {
        super(Component.literal("Saved Commands Manager"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        buildUI();
    }

    /**
     * Rebuilds all widgets for the current page.
     *
     * <p>Called both during {@link #init()} and after any mutation
     * (deletion, page change) to synchronise the displayed buttons with
     * the underlying {@link Config#savedCommands} list. All existing
     * widgets are cleared before reconstruction via {@code clearWidgets()}.
     *
     * <p>Page bounds are clamped so that deleting the last item on the
     * final page automatically falls back to the previous page rather
     * than rendering an empty view.
     */
    private void buildUI() {
        this.clearWidgets();
        
        List<String> saved = Config.savedCommands;
        int totalItems = saved.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;
        if (currentPage >= totalPages) currentPage = totalPages - 1;

        int startY = 40;
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            final String cmd = saved.get(i);
            int yOffset = startY + (i - startIndex) * 25;
            
            String displayCmd = cmd.length() > 30 ? cmd.substring(0, 27) + "..." : cmd;

            this.addRenderableWidget(Button.builder(Component.literal(displayCmd), button -> {
                if (BaritoneAPI.getProvider().getPrimaryBaritone() != null) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(cmd);
                    Config.addHistoryCommand(cmd);
                }
                this.minecraft.setScreen(null);
            }).bounds(this.width / 2 - 150, yOffset, 200, 20).build());

            this.addRenderableWidget(Button.builder(Component.literal("Delete"), button -> {
                Config.savedCommands.remove(cmd);
                Config.SPEC.save();
                buildUI();
            }).bounds(this.width / 2 + 60, yOffset, 90, 20).build());
        }

        int navY = this.height - 40;
        
        Button prevBtn = Button.builder(Component.literal("<"), button -> {
            currentPage--;
            buildUI();
        }).bounds(this.width / 2 - 150, navY, 40, 20).build();
        prevBtn.active = currentPage > 0;
        this.addRenderableWidget(prevBtn);

        Button nextBtn = Button.builder(Component.literal(">"), button -> {
            currentPage++;
            buildUI();
        }).bounds(this.width / 2 + 110, navY, 40, 20).build();
        nextBtn.active = currentPage < totalPages - 1;
        this.addRenderableWidget(nextBtn);
        
        this.addRenderableWidget(Button.builder(Component.literal("Back"), button -> {
            this.minecraft.setScreen(parentScreen);
        }).bounds(this.width / 2 - 50, navY, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        
        List<String> saved = Config.savedCommands;
        int totalItems = saved.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE));
        
        guiGraphics.drawCenteredString(this.font, "Page " + (currentPage + 1) + " / " + totalPages, this.width / 2, this.height - 35, 0xAAAAAA);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
