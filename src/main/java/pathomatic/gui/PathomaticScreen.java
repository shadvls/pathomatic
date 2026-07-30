package pathomatic.gui;

import pathomatic.api.PathomaticAPI;
import pathomatic.api.IPathomatic;
import pathomatic.api.behavior.IPathingBehavior;
import pathomatic.api.pathing.goals.Goal;
import pathomatic.api.utils.BetterBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PathomaticScreen extends Screen {

    private static final int SIDEBAR_W = 100;
    private static final int TAB_H = 24;
    private static final int HEADER_H = 28;
    private static final int FOOTER_H = 24;

    private enum Tab {
        STATUS("Status"), GO("Go"), MINE("Mine"), BUILD("Build"),
        FARM("Farm"), FOLLOW("Follow"), EXPLORE("Explore"), SETTINGS("Settings");
        final String label;
        Tab(String label) { this.label = label; }
    }

    private Tab selectedTab = Tab.STATUS;
    private final List<Button> sidebarButtons = new ArrayList<>();

    private EditBox goX, goY, goZ, goBlock;
    private EditBox mineBlock;
    private EditBox farmRange;
    private EditBox followEntity;
    private EditBox exploreRange;
    private EditBox buildSchematic;

    private int contentX;
    private int contentY;
    private int contentW;
    private int inputW;

    public PathomaticScreen() {
        super(Component.literal("Pathomatic"));
    }

    @Override
    protected void init() {
        super.init();
        sidebarButtons.clear();
        clearWidgets();

        int sx = 4;
        int sy = HEADER_H + 4;
        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            Tab t = tabs[i];
            Button btn = Button.builder(Component.literal(t.label), b -> selectTab(t))
                    .bounds(sx, sy + i * (TAB_H + 2), SIDEBAR_W, TAB_H).build();
            sidebarButtons.add(btn);
            addRenderableWidget(btn);
        }

        contentX = sx + SIDEBAR_W + 8;
        contentY = HEADER_H + 4;
        contentW = width - contentX - 4;
        inputW = Math.min(contentW - 10, 160);

        goX = mkEdit("X", contentX, contentY);
        goY = mkEdit("Y", contentX, contentY + 24);
        goZ = mkEdit("Z", contentX, contentY + 48);
        goBlock = mkEdit("Block", contentX, contentY + 72);
        mineBlock = mkEdit("Block", contentX, contentY);
        farmRange = mkEdit("Range", contentX, contentY);
        followEntity = mkEdit("Entity", contentX, contentY);
        exploreRange = mkEdit("Range", contentX, contentY);
        buildSchematic = mkEdit("Schematic", contentX, contentY);

        EditBox[] all = {goX, goY, goZ, goBlock, mineBlock, farmRange, followEntity, exploreRange, buildSchematic};
        for (EditBox b : all) {
            b.setMaxLength(64);
            b.setResponder(s -> updateButton());
        }

        rebuildContent();
    }

    private EditBox mkEdit(String label, int x, int y) {
        return new EditBox(font, x + font.width(label) + 6, y, inputW, 18, Component.literal(label));
    }

    private void selectTab(Tab t) {
        selectedTab = t;
        rebuildContent();
    }

    private void rebuildContent() {
        clearWidgets();
        for (Button btn : sidebarButtons) addRenderableWidget(btn);

        switch (selectedTab) {
            case GO -> {
                addRenderableWidget(goX); addRenderableWidget(goY);
                addRenderableWidget(goZ); addRenderableWidget(goBlock);
                addRenderableWidget(Button.builder(Component.literal("Go"), b -> execGo())
                        .bounds(contentX, contentY + 100, 80, 20).build());
            }
            case MINE -> {
                addRenderableWidget(mineBlock);
                addRenderableWidget(Button.builder(Component.literal("Mine"), b -> execMine())
                        .bounds(contentX, contentY + 26, 80, 20).build());
            }
            case BUILD -> {
                addRenderableWidget(buildSchematic);
                addRenderableWidget(Button.builder(Component.literal("Build"), b -> execBuild())
                        .bounds(contentX, contentY + 26, 80, 20).build());
            }
            case FARM -> {
                addRenderableWidget(farmRange);
                addRenderableWidget(Button.builder(Component.literal("Farm"), b -> execFarm())
                        .bounds(contentX, contentY + 26, 80, 20).build());
            }
            case FOLLOW -> {
                addRenderableWidget(followEntity);
                addRenderableWidget(Button.builder(Component.literal("Follow"), b -> execFollow())
                        .bounds(contentX, contentY + 26, 80, 20).build());
            }
            case EXPLORE -> {
                addRenderableWidget(exploreRange);
                addRenderableWidget(Button.builder(Component.literal("Explore"), b -> execExplore())
                        .bounds(contentX, contentY + 26, 80, 20).build());
            }
            case SETTINGS -> buildSettings();
        }
    }

    private void buildSettings() {
        int x = contentX;
        int y = contentY;
        String[][] toggles = {
            {"Render Path", "renderPath"},
            {"Render Goal", "renderGoal"},
            {"Render Selection", "renderSelection"},
            {"Chat Control", "chatControl"},
            {"Prefix Control", "prefixControl"},
        };
        for (String[] t : toggles) {
            String name = t[1];
            addRenderableWidget(Button.builder(
                    Component.literal("Toggle " + t[0]),
                    b -> run("set toggle " + name)
            ).bounds(x, y, 140, 20).build());
            y += 24;
        }
        addRenderableWidget(Button.builder(
                Component.literal("Cancel Everything"),
                b -> cancelAll()
        ).bounds(x, y, 140, 20).build());
    }

    private void execGo() {
        String x = goX.getValue().trim();
        String y = goY.getValue().trim();
        String z = goZ.getValue().trim();
        String block = goBlock.getValue().trim();
        if (!block.isEmpty()) {
            run("go " + block);
        } else if (!x.isEmpty() && !y.isEmpty() && !z.isEmpty()) {
            run("go " + x + " " + y + " " + z);
        } else if (!x.isEmpty() && !z.isEmpty()) {
            run("go " + x + " " + z);
        } else if (!y.isEmpty()) {
            run("go " + y);
        }
    }

    private void execMine() {
        String b = mineBlock.getValue().trim();
        if (!b.isEmpty()) run("mine " + b);
    }

    private void execBuild() {
        String s = buildSchematic.getValue().trim();
        if (!s.isEmpty()) run("build " + s);
    }

    private void execFarm() {
        String r = farmRange.getValue().trim();
        run(r.isEmpty() ? "farm" : "farm " + r);
    }

    private void execFollow() {
        String e = followEntity.getValue().trim();
        run(e.isEmpty() ? "follow" : "follow " + e);
    }

    private void execExplore() {
        String r = exploreRange.getValue().trim();
        run(r.isEmpty() ? "explore" : "explore " + r);
    }

    private void cancelAll() {
        IPathomatic p = PathomaticAPI.getProvider().getPrimaryPathomatic();
        p.getPathingBehavior().cancelEverything();
    }

    private void run(String cmd) {
        IPathomatic p = PathomaticAPI.getProvider().getPrimaryPathomatic();
        p.getCommandManager().execute(cmd);
        Minecraft.getInstance().setScreen(null);
    }

    private void updateButton() {}

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        renderBackground(g, mx, my, delta);

        g.fill(0, 0, width, HEADER_H, 0xC0101010);
        g.fill(0, height - FOOTER_H, width, height, 0xC0101010);

        renderHeader(g);
        renderSidebar(g);
        renderContent(g);
        renderFooter(g);

        super.render(g, mx, my, delta);
    }

    private void renderHeader(GuiGraphics g) {
        g.drawString(font, Component.literal("Pathomatic"), 8, 8, 0xFFFFFF);
        IPathomatic p = PathomaticAPI.getProvider().getPrimaryPathomatic();
        boolean act = p.getPathingBehavior().isPathing();
        String st = act ? "Active" : "Idle";
        g.drawString(font, Component.literal("Status: " + st), width - font.width("Status: " + st) - 8, 8, act ? 0x55FF55 : 0xAAAAAA);
    }

    private void renderSidebar(GuiGraphics g) {
        int y = HEADER_H + 4;
        for (Tab t : Tab.values()) {
            int i = t.ordinal();
            int by = y + i * (TAB_H + 2);
            if (t == selectedTab) {
                g.fill(2, by, SIDEBAR_W + 2, by + TAB_H, 0x30FFFFFF);
            }
        }
        int sep = y + Tab.values().length * (TAB_H + 2);
        g.fill(0, sep, SIDEBAR_W + 4, sep + 1, 0xFF333333);
        IPathomatic p = PathomaticAPI.getProvider().getPrimaryPathomatic();
        boolean act = p.getPathingBehavior().isPathing();
        String dot = act ? "\u25CF" : "\u25CB";
        g.drawString(font, Component.literal(dot), 8, sep + 5, act ? 0x55FF55 : 0x555555);
        g.drawString(font, Component.literal(act ? "Active" : "Idle"), 22, sep + 5, 0x888888);
    }

    private void renderContent(GuiGraphics g) {
        if (selectedTab == Tab.STATUS) renderStatus(g);
        else if (selectedTab != Tab.SETTINGS) renderLabels(g);
    }

    private void renderLabels(GuiGraphics g) {
        int x = contentX;
        int y = contentY;
        switch (selectedTab) {
            case GO -> {
                g.drawString(font, Component.literal("X"), x, y + 5, 0xAAAAAA);
                g.drawString(font, Component.literal("Y"), x, y + 29, 0xAAAAAA);
                g.drawString(font, Component.literal("Z"), x, y + 53, 0xAAAAAA);
                g.drawString(font, Component.literal("or block"), x + goBlock.getX() + goBlock.getWidth() + 4, goBlock.getY() + 3, 0x555555);
            }
            case MINE -> g.drawString(font, Component.literal("Block"), x, y + 5, 0xAAAAAA);
            case BUILD -> g.drawString(font, Component.literal("Schematic"), x, y + 5, 0xAAAAAA);
            case FARM -> g.drawString(font, Component.literal("Range"), x, y + 5, 0xAAAAAA);
            case FOLLOW -> g.drawString(font, Component.literal("Entity"), x, y + 5, 0xAAAAAA);
            case EXPLORE -> g.drawString(font, Component.literal("Range"), x, y + 5, 0xAAAAAA);
        }
    }

    private void renderStatus(GuiGraphics g) {
        int cx = contentX;
        int cy = contentY + 4;
        IPathomatic p = PathomaticAPI.getProvider().getPrimaryPathomatic();
        IPathingBehavior b = p.getPathingBehavior();

        g.drawString(font, Component.literal("Status Overview"), cx, cy, 0xFFFFFF);
        cy += 16;

        boolean act = b.isPathing();
        g.drawString(font, Component.literal("Bot: " + (act ? "Pathing" : "Idle")), cx, cy, act ? 0x55FF55 : 0x888888);
        cy += 14;

        if (act) {
            Goal goal = b.getGoal();
            if (goal != null) {
                g.drawString(font, Component.literal("Goal: " + goal), cx, cy, 0xAAAAAA);
                cy += 14;
            }
        }

        BetterBlockPos pos = p.getPlayerContext().playerFeet();
        if (pos != null) {
            g.drawString(font, Component.literal("X: " + pos.x + " Y: " + pos.y + " Z: " + pos.z), cx, cy, 0xAAAAAA);
            cy += 18;
        }

        addRenderableWidget(Button.builder(Component.literal("Cancel All"), b2 -> cancelAll())
                .bounds(cx, cy, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Force Cancel"), b2 -> p.getPathingBehavior().forceCancel())
                .bounds(cx + 96, cy, 100, 20).build());
    }

    private void renderFooter(GuiGraphics g) {
        g.drawString(font, Component.literal("Press P to toggle GUI"), 8, height - FOOTER_H + 6, 0x555555);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
