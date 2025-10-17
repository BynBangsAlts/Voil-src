package net.fabricmc.fabric.systems.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ColorPickerSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.render.ColorUtil;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.Render3DEngine;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.WorldChunk;

public class BlockESP
extends Module {
    ModeSetting mode = new ModeSetting("m".concat("^").concat("o").concat("@").concat("d").concat("$").concat("e"), "O".concat("#").concat("u").concat("(").concat("t").concat("#").concat("l").concat("@").concat("i").concat("@").concat("n").concat("&").concat("e"), "R".concat("#").concat("e").concat("^").concat("n").concat(")").concat("d").concat("&").concat("e").concat("#").concat("r").concat("@").concat(" ").concat("&").concat("m").concat("&").concat("o").concat("&").concat("d").concat("-").concat("e"), "F".concat("-").concat("i").concat("-").concat("l").concat("#").concat("l").concat("!").concat("e").concat("(").concat("d").concat(")").concat(" ").concat("+").concat("elementCodec").concat("$").concat("o").concat("$").concat("x"), "O".concat("(").concat("u").concat("#").concat("t").concat(")").concat("l").concat("_").concat("i").concat("*").concat("n").concat("!").concat("e"));
    public BooleanSetting showNames = new BooleanSetting("s".concat(")").concat("h").concat("+").concat("o").concat(")").concat("w").concat("-").concat("N").concat("^").concat("keyCodec").concat("!").concat("m").concat("_").concat("e").concat("_").concat("s"), true, "S".concat("&").concat("h").concat("!").concat("o").concat("_").concat("w").concat("!").concat("s").concat("&").concat(" ").concat("#").concat("elementCodec").concat("@").concat("l").concat("+").concat("o").concat("#").concat("c").concat("-").concat("k").concat("&").concat(" ").concat("!").concat("n").concat("-").concat("keyCodec").concat("_").concat("m").concat("@").concat("e").concat("#").concat("s"));
    public ColorPickerSetting blockColor = new ColorPickerSetting("elementCodec".concat("*").concat("l").concat("*").concat("o").concat("_").concat("c").concat("+").concat("k").concat("$").concat("C").concat("+").concat("o").concat("$").concat("l").concat("_").concat("o").concat("^").concat("r"), new Color(255, 0, 0, 255), "C".concat("#").concat("o").concat("*").concat("l").concat("+").concat("o").concat(")").concat("r").concat("(").concat(" ").concat(")").concat("o").concat("$").concat("f").concat("&").concat(" ").concat("(").concat("t").concat("#").concat("h").concat("@").concat("e").concat("-").concat(" ").concat("!").concat("elementCodec").concat(")").concat("l").concat("!").concat("o").concat("$").concat("c").concat("(").concat("k").concat("^").concat("s"));
    public ModeSetting blocks = new ModeSetting("B".concat(")").concat("l").concat("^").concat("o").concat(")").concat("c").concat("&").concat("k").concat(")").concat("s"), "S".concat(")").concat("t").concat("&").concat("o").concat("&").concat("r").concat("&").concat("keyCodec").concat("(").concat("g").concat("^").concat("e"), "B".concat("#").concat("l").concat("^").concat("o").concat("#").concat("c").concat("(").concat("k").concat("$").concat("s").concat("&").concat(" ").concat(")").concat("t").concat("$").concat("o").concat("(").concat(" ").concat(")").concat("h").concat("-").concat("i").concat("-").concat("g").concat("*").concat("h").concat("&").concat("l").concat("-").concat("i").concat("!").concat("g").concat("-").concat("h").concat("@").concat("t"), "O".concat("_").concat("r").concat("-").concat("e").concat("!").concat("s"), "S".concat(")").concat("t").concat("(").concat("o").concat("^").concat("r").concat("*").concat("keyCodec").concat(")").concat("g").concat("!").concat("e"), "B".concat("+").concat("o").concat("#").concat("t").concat("*").concat("h"));
    public BooleanSetting onlyVisible = new BooleanSetting("O".concat("&").concat("n").concat("(").concat("l").concat("*").concat("y").concat("_").concat(" ").concat("^").concat("V").concat("&").concat("i").concat("$").concat("s").concat("!").concat("i").concat("^").concat("elementCodec").concat("#").concat("l").concat("_").concat("e"), true, "O".concat("(").concat("n").concat("$").concat("l").concat("!").concat("y").concat("+").concat(" ").concat("+").concat("h").concat(")").concat("i").concat("$").concat("g").concat(")").concat("h").concat("(").concat("l").concat("!").concat("i").concat("$").concat("g").concat("_").concat("h").concat("^").concat("t").concat("(").concat("s").concat("#").concat(" ").concat("_").concat("v").concat(")").concat("i").concat("(").concat("s").concat(")").concat("i").concat("(").concat("elementCodec").concat("$").concat("l").concat("@").concat("e").concat("*").concat(" ").concat("@").concat("o").concat("!").concat("r").concat(")").concat("e").concat("!").concat("s"));
    private List<Block> blocksToHighlightOres = Arrays.asList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.ANCIENT_DEBRIS);
    private List<Block> blocksToHighlight = Arrays.asList(Blocks.FURNACE, Blocks.BLAST_FURNACE, Blocks.SMOKER, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.DISPENSER, Blocks.DROPPER, Blocks.HOPPER, Blocks.SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ENDER_CHEST, Blocks.SPAWNER, Blocks.BEACON, Blocks.JUKEBOX, Blocks.ENCHANTING_TABLE, Blocks.BREWING_STAND, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, Blocks.BELL, Blocks.CONDUIT, Blocks.COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.DAYLIGHT_DETECTOR, Blocks.SCULK_SENSOR, Blocks.SCULK_CATALYST, Blocks.SCULK_SHRIEKER, Blocks.LECTERN, Blocks.PISTON, Blocks.MOVING_PISTON, Blocks.STRUCTURE_BLOCK, Blocks.JIGSAW, Blocks.BAMBOO_SIGN, Blocks.BAMBOO_HANGING_SIGN);

    public BlockESP() {
        super("B".concat("(").concat("l").concat("^").concat("o").concat("(").concat("c").concat("+").concat("k").concat("#").concat("E").concat("(").concat("S").concat("-").concat("P"), "H".concat("-").concat("i").concat("!").concat("g").concat("-").concat("h").concat(")").concat("l").concat(")").concat("i").concat("^").concat("g").concat("&").concat("h").concat("-").concat("t").concat("&").concat("s").concat("#").concat(" ").concat("$").concat("elementCodec").concat("_").concat("l").concat("(").concat("o").concat("^").concat("c").concat("^").concat("k").concat("&").concat("s"), Category.Render);
        this.addSettings(this.mode, this.showNames, this.onlyVisible, this.blocks, this.blockColor);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (!this.isEnabled()) {
            return;
        }
        CompletableFuture<List<Pair<BlockPos, Block>>> futureOreBlocks = BlockUtils.getOreBlocksAsync();
        BlockESP.getLoadedChunks().forEach(chunk -> chunk.getBlockEntities().values().forEach(blockEntity -> {
            Block block = blockEntity.getCachedState().getBlock();
            BlockPos pos = blockEntity.getPos();
            if ((this.blocks.isMode("Storage") || this.blocks.isMode("Both")) && this.blocksToHighlight.contains(block)) {
                if (this.mode.isMode("Outline")) {
                    this.drawHighlightOutline(matrices, pos);
                } else if (this.mode.isMode("Filled box")) {
                    Color newBlockColor = ColorUtil.addAlpha(this.blockColor.getColor(), 128);
                    Render3DEngine.drawBoxWithParams(Vec3d.ofCenter((Vec3i)pos).add(0.0, -0.5, 0.0), newBlockColor, matrices, 0.5f, 1.0f);
                }
            }
        }));
        if (this.blocks.isMode("Ores") || this.blocks.isMode("Both")) {
            futureOreBlocks.thenAccept(oreBlocks -> {
                for (Pair pair : oreBlocks) {
                    if (!this.blocksToHighlightOres.contains(pair.getRight())) continue;
                    BlockPos pos = (BlockPos)pair.getLeft();
                    if (this.mode.isMode("Outline")) {
                        this.drawHighlightOutline(matrices, pos);
                        continue;
                    }
                    if (!this.mode.isMode("Filled box")) continue;
                    Color newBlockColor = ColorUtil.addAlpha(this.blockColor.getColor(), 128);
                    Render3DEngine.drawBoxWithParams(Vec3d.ofCenter((Vec3i)pos).add(0.0, -0.5, 0.0), newBlockColor, matrices, 0.5f, 1.0f);
                }
            });
        }
        super.onWorldRender(matrices);
    }

    @Override
    public void draw(MatrixStack matrices) {
        BlockESP.getLoadedChunks().forEach(chunk -> chunk.getBlockEntities().values().forEach(blockEntity -> {
            if (this.blocksToHighlight.contains(blockEntity.getCachedState().getBlock())) {
                BlockPos pos = blockEntity.getPos();
                if (this.showNames.isEnabled()) {
                    this.drawNameTag(matrices, blockEntity.getCachedState().getBlock(), pos);
                }
            }
        }));
    }

    private void drawNameTag(MatrixStack matrices, Block block, BlockPos blockPos) {
        Vec3d screenPos = WorldUtils.worldToScreen(new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.5, (double)blockPos.getZ() + 0.5));
        if (screenPos.z < 0.0 || screenPos.z > 1.0) {
            return;
        }
        int boxWidth = 20;
        int boxHeight = 20;
        Render2DEngine.drawRoundedBlur(matrices, (int)screenPos.x - boxWidth / 2, (int)screenPos.y - boxHeight / 2, boxWidth, boxHeight, 4.0f, 14.0f, 0.0f, true);
        ItemStack blockItemStack = new ItemStack((ItemConvertible)block.asItem());
        Render2DEngine.renderItem(matrices, blockItemStack, (float)(screenPos.x - 8.0), (float)(screenPos.y - 8.0), 1.0f, true);
    }

    private void drawHighlightOutline(MatrixStack matrices, BlockPos blockPos) {
        Camera camera = BlockESP.mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        double x = (double)blockPos.getX() - camPos.x;
        double y = (double)blockPos.getY() - camPos.y;
        double z = (double)blockPos.getZ() - camPos.z;
        Vec3d position = new Vec3d(x, y, z);
        Color color = new Color(255, 0, 0, 89);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Render3DEngine.drawRectangleOutline(matrices, buffer, x, y, z, this.blockColor.getColor());
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void drawFilledBox(MatrixStack matrices, BlockPos blockPos) {
        Camera camera = BlockESP.mc.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        double x = (double)blockPos.getX() - camPos.x;
        double y = (double)blockPos.getY() - camPos.y;
        double z = (double)blockPos.getZ() - camPos.z;
        Vec3d position = new Vec3d(x, y, z);
        Color color = this.blockColor.getColor();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Render3DEngine.drawFilledRectangle(matrices, buffer, (float)x, (float)y, (float)z, 1.0f, 1.0f, color);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)buffer.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static Stream<WorldChunk> getLoadedChunks() {
        int radius = Math.max(2, BlockESP.mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;
        ChunkPos center = BlockESP.mc.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);
        return Stream.iterate(min, pos -> {
            int x = pos.x;
            int z = pos.z;
            if (++x > max.x) {
                x = min.x;
                ++z;
            }
            if (z > max.z) {
                throw new IllegalStateException("Stream limit didn't work.");
            }
            return new ChunkPos(x, z);
        }).limit(diameter * diameter).filter(c -> BlockESP.mc.world.isChunkLoaded(c.x, c.z)).map(c -> BlockESP.mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
    }
}
