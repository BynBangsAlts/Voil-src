package net.fabricmc.fabric.gui.screens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.scripting.ScriptWrapper;
import net.fabricmc.fabric.gui.clickgui.components.Component;
import net.fabricmc.fabric.gui.screens.ConfigEditorScreen;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EditScriptScreen
extends Component {
    private final ConfigEditorScreen parent;
    private final ScriptWrapper script;
    private StringBuilder content;
    private int cursorPosition = 0;
    private int scrollOffset = 0;
    private final int visibleLines = 20;

    public EditScriptScreen(ConfigEditorScreen parent, ScriptWrapper script) {
        this.parent = parent;
        this.script = script;
        this.content = new StringBuilder(script.getScript());
    }

    @Override
    public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int newScrollOffset;
        int x = this.parent.parent.x;
        int y = this.parent.parent.y;
        int textWidth = (int)ClientMain.fontRenderer.getWidth("Currently editing " + this.script.getName());
        int centerX = this.parent.parent.x + (this.parent.parent.width - textWidth) / 2 + 60;
        ClientMain.fontRenderer.draw(matrices, "Currently editing " + this.script.getName(), centerX, y, 0xFFFFFF);
        Render2DEngine.drawTexturedRectangle(RenderHelper.getContext(), x + 170, y + 10, 25, 25, Identifier.of((String)"tulip", (String)"icons/back.png"));
        String[] lines = this.content.toString().split("\n");
        int lineHeight = (int)(ClientMain.fontRenderer.getFont().getSize2D() + 2.0f);
        int linesPerPage = 12;
        int cursorLine = 0;
        int cursorColumn = 0;
        int charCount = 0;
        for (int i = 0; i < lines.length; ++i) {
            if (charCount + lines[i].length() >= this.cursorPosition) {
                cursorLine = i;
                cursorColumn = this.cursorPosition - charCount;
                break;
            }
            charCount += lines[i].length() + 1;
        }
        if ((newScrollOffset = cursorLine / linesPerPage * linesPerPage) != this.scrollOffset) {
            this.scrollOffset = newScrollOffset;
        }
        for (int i = this.scrollOffset = Math.max(0, Math.min(this.scrollOffset, lines.length - 20)); i < Math.min(lines.length, this.scrollOffset + 20); ++i) {
            int drawY = y + (i - this.scrollOffset) * lineHeight + 30;
            String lineNumber = String.valueOf(i + 1);
            ClientMain.fontRenderer.draw(matrices, lineNumber, x + 170, drawY, 0xAAAAAA);
            String line = lines[i];
            int cursorX = x + 190;
            boolean cursorDrawn = false;
            Matcher matcher = Pattern.compile("\\d+|\\D+").matcher(line);
            while (matcher.find()) {
                String word = matcher.group();
                int color = EditScriptScreen.getColor(word);
                if (i == cursorLine && !cursorDrawn && matcher.start() >= cursorColumn) {
                    ClientMain.fontRenderer.draw(matrices, "|", cursorX, drawY, 0xFFFFFF);
                    cursorDrawn = true;
                }
                ClientMain.fontRenderer.draw(matrices, word, cursorX, drawY, color);
                cursorX += (int)(ClientMain.fontRenderer.getWidth(word) + 6.0f);
            }
            if (i != cursorLine) continue;
            int cursorOffsetX = cursorColumn == 0 ? 0 : (int)ClientMain.fontRenderer.getWidth(lines[i].substring(0, cursorColumn));
            ClientMain.fontRenderer.draw(matrices, "|", x + 190 + cursorOffsetX, drawY, 0xFFFFFF);
        }
    }

    private String formatScript(String rawScript) {
        StringBuilder formatted = new StringBuilder();
        String[] lines = rawScript.split("\n");
        int indentLevel = 0;
        for (String line : lines) {
            Object trimmed = line.trim();
            if (((String)trimmed).isEmpty()) continue;
            if (((String)trimmed).endsWith("{")) {
                trimmed = ((String)trimmed).substring(0, ((String)trimmed).length() - 1).trim() + " {";
            }
            if (((String)trimmed).equals("}")) {
                indentLevel = Math.max(0, indentLevel - 1);
            }
            formatted.append("    ".repeat(indentLevel)).append(((String)trimmed).replaceAll("(?<=[a-zA-Z]) (?=\\d)", "")).append("\n");
            if (!((String)trimmed).endsWith("{")) continue;
            ++indentLevel;
        }
        return formatted.toString();
    }

    private static int getColor(String word) {
        if (word.matches("^#[0-9A-Fa-f]{6}$")) {
            return Integer.parseInt(word.substring(1), 16) | 0xFF000000;
        }
        if (word.startsWith("def") || word.startsWith("on")) {
            return 65280;
        }
        if (word.matches("\\d+(\\.\\d+)?")) {
            return 0xFFAA00;
        }
        if (word.matches(".*\\(.*\\)")) {
            return 8900331;
        }
        if (word.matches("\".*\"|'.*'")) {
            return 16753920;
        }
        if (word.equals("true") || word.equals("false")) {
            return 0xFF00FF;
        }
        return 0xFFFFFF;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int x = this.parent.parent.x;
            int y = this.parent.parent.y;
            int width = 25;
            int height = 25;
            if (mouseX >= (double)(x + 170) && mouseX <= (double)(x + 170 + width) && mouseY >= (double)(y + 10) && mouseY <= (double)(y + 10 + height)) {
                this.parent.editScriptScreen = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public void charTyped(char chr, int keyCode) {
        boolean alt;
        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (keyCode & 8) != 0 : (keyCode & 2) != 0;
        boolean shift = (keyCode & 1) != 0;
        boolean bl = alt = (keyCode & 4) != 0;
        if (!control) {
            if (shift) {
                chr = Character.toUpperCase(chr);
            }
            if (alt || Character.isDefined(chr)) {
                this.content.insert(this.cursorPosition, chr);
                ++this.cursorPosition;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean control = MinecraftClient.IS_SYSTEM_MAC ? (modifiers & 8) != 0 : (modifiers & 2) != 0;
        String[] lines = this.content.toString().split("\n");
        int currentLine = 0;
        int currentColumn = 0;
        int charCount = 0;
        for (String line : lines) {
            if (charCount + line.length() >= this.cursorPosition) {
                currentColumn = this.cursorPosition - charCount;
                break;
            }
            charCount += line.length() + 1;
            ++currentLine;
        }
        if (control && keyCode == 65) {
            this.cursorPosition = this.content.length();
            return true;
        }
        if (control && keyCode == 67) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.content.toString());
            return true;
        }
        if (control && keyCode == 88) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.content.toString());
            this.content.setLength(0);
            this.cursorPosition = 0;
            return true;
        }
        switch (keyCode) {
            case 259: {
                if (this.cursorPosition <= 0) break;
                this.content.deleteCharAt(this.cursorPosition - 1);
                --this.cursorPosition;
                break;
            }
            case 257: 
            case 335: {
                this.content.insert(this.cursorPosition, '\n');
                ++this.cursorPosition;
                break;
            }
            case 263: {
                this.cursorPosition = Math.max(0, this.cursorPosition - 1);
                break;
            }
            case 262: {
                this.cursorPosition = Math.min(this.content.length(), this.cursorPosition + 1);
                break;
            }
            case 265: {
                int i;
                if (currentLine <= 0) break;
                int prevLineStart = 0;
                for (i = 0; i < currentLine - 1; ++i) {
                    prevLineStart += lines[i].length() + 1;
                }
                this.cursorPosition = Math.min(prevLineStart + currentColumn, prevLineStart + lines[currentLine - 1].length());
                break;
            }
            case 264: {
                int i;
                if (currentLine >= lines.length - 1) break;
                int nextLineStart = 0;
                for (i = 0; i <= currentLine; ++i) {
                    nextLineStart += lines[i].length() + 1;
                }
                this.cursorPosition = Math.min(nextLineStart + currentColumn, nextLineStart + lines[currentLine + 1].length());
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
        if (verticalScroll > 0.0) {
            this.scrollOffset = Math.max(0, this.scrollOffset - 1);
            System.out.println("scroll1 " + this.scrollOffset);
        } else if (verticalScroll < 0.0) {
            ++this.scrollOffset;
            System.out.println("scroll 2" + this.scrollOffset);
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }
}
