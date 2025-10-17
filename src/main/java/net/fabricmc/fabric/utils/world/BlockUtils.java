package net.fabricmc.fabric.utils.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.utils.player.FindItemResult;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SignBlock;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class BlockUtils {
    private static final List<Block> storage = Arrays.asList(Blocks.FURNACE, Blocks.BLAST_FURNACE, Blocks.SMOKER, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.DISPENSER, Blocks.DROPPER, Blocks.HOPPER, Blocks.SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ENDER_CHEST, Blocks.SPAWNER, Blocks.BEACON, Blocks.JUKEBOX, Blocks.ENCHANTING_TABLE, Blocks.BREWING_STAND, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, Blocks.BELL, Blocks.CONDUIT, Blocks.COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.DAYLIGHT_DETECTOR, Blocks.SCULK_SENSOR, Blocks.SCULK_CATALYST, Blocks.SCULK_SHRIEKER, Blocks.LECTERN, Blocks.PISTON, Blocks.MOVING_PISTON, Blocks.STRUCTURE_BLOCK, Blocks.JIGSAW, Blocks.BAMBOO_SIGN, Blocks.BAMBOO_HANGING_SIGN);
    private static final List<Block> ore = Arrays.asList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS);
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static ActionResult interact(Vec3d vec) {
        return BlockUtils.interact(vec, Direction.UP);
    }

    public static void interact(BlockPos pos, Direction dir) {
        Vec3d vec = new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        BlockUtils.interact(vec, dir);
    }

    public static int getAnchorChargeLevel(BlockPos block) {
        if (ClientMain.mc.world != null && ClientMain.mc.world.getBlockState(block).getBlock().equals(Blocks.RESPAWN_ANCHOR)) {
            BlockState pos = ClientMain.mc.world.getBlockState(block);
            return (Integer)pos.get((Property)RespawnAnchorBlock.CHARGES);
        }
        return -1;
    }

    public static ActionResult interact(Vec3d vec3d, Direction dir) {
        Vec3i vec3i = new Vec3i((int)vec3d.x, (int)vec3d.y, (int)vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        BlockHitResult result = new BlockHitResult(vec3d, dir, pos, false);
        assert (ClientMain.mc.interactionManager != null);
        assert (ClientMain.mc.player != null);
        return ClientMain.mc.interactionManager.interactBlock(ClientMain.mc.player, ClientMain.mc.player.getActiveHand(), result);
    }

    public static boolean isBlock(Block block, BlockPos pos) {
        return BlockUtils.getBlockState(pos).getBlock() == block;
    }

    public static BlockState getBlockState(BlockPos pos) {
        return ClientMain.mc.world.getBlockState(pos);
    }

    public static boolean hasBlock(BlockPos pos) {
        return !ClientMain.mc.world.getBlockState(pos).isAir();
    }

    public static ActionResult interact(BlockHitResult result) {
        return ClientMain.mc.interactionManager.interactBlock(ClientMain.mc.player, ClientMain.mc.player.getActiveHand(), result);
    }

    public static boolean isAnchorCharged(BlockPos anchor) {
        if (!BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, anchor)) {
            return false;
        }
        try {
            return (Integer)BlockUtils.getBlockState(anchor).get((Property)RespawnAnchorBlock.CHARGES) != 0;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isAnchorUncharged(BlockPos anchor) {
        try {
            if (!BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, anchor)) {
                return false;
            }
            return (Integer)BlockUtils.getBlockState(anchor).get((Property)RespawnAnchorBlock.CHARGES) == 0;
        }
        catch (IllegalArgumentException var2) {
            return false;
        }
    }

    public static boolean place(BlockPos blockPos, FindItemResult findItemResult, boolean swingHand, boolean checkEntities) {
        if (findItemResult.isOffhand()) {
            return BlockUtils.place(blockPos, Hand.OFF_HAND, ClientMain.mc.player.getInventory().selectedSlot, swingHand, checkEntities);
        }
        if (findItemResult.isHotbar()) {
            return BlockUtils.place(blockPos, Hand.MAIN_HAND, findItemResult.getSlot(), swingHand, checkEntities);
        }
        return false;
    }

    public static boolean place(BlockPos blockPos, Hand hand, int slot, boolean swingHand, boolean checkEntities) {
        BlockPos neighbour;
        if (slot < 0 || slot > 8) {
            return false;
        }
        Block toPlace = Blocks.OBSIDIAN;
        ItemStack i = hand == Hand.MAIN_HAND ? ClientMain.mc.player.getInventory().getStack(slot) : ClientMain.mc.player.getInventory().getStack(Hand.OFF_HAND.ordinal());
        Item item = i.getItem();
        if (item instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)item;
            toPlace = blockItem.getBlock();
        }
        if (!BlockUtils.canPlaceBlock(blockPos, checkEntities, toPlace)) {
            return false;
        }
        Vec3d hitPos = Vec3d.ofCenter((Vec3i)blockPos);
        Direction side = BlockUtils.getPlaceSide(blockPos);
        if (side == null) {
            side = Direction.UP;
            neighbour = blockPos;
        } else {
            neighbour = blockPos.offset(side);
            hitPos = hitPos.add((double)side.getOffsetX() * 0.5, (double)side.getOffsetY() * 0.5, (double)side.getOffsetZ() * 0.5);
        }
        BlockHitResult bhr = new BlockHitResult(hitPos, side.getOpposite(), neighbour, false);
        BlockUtils.interact(bhr, hand, swingHand);
        return true;
    }

    public static void interact(BlockHitResult blockHitResult, Hand hand, boolean swing) {
        boolean wasSneaking = ClientMain.mc.player.input.sneaking;
        ClientMain.mc.player.input.sneaking = false;
        ActionResult result = ClientMain.mc.interactionManager.interactBlock(ClientMain.mc.player, hand, blockHitResult);
        if (result.shouldSwingHand()) {
            if (swing) {
                ClientMain.mc.player.swingHand(hand);
            } else {
                ClientMain.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(hand));
            }
        }
        ClientMain.mc.player.input.sneaking = wasSneaking;
    }

    public static boolean canPlaceBlock(BlockPos blockPos, boolean checkEntities, Block block) {
        if (blockPos == null) {
            return false;
        }
        if (!World.isValid((BlockPos)blockPos)) {
            return false;
        }
        if (!ClientMain.mc.world.getBlockState(blockPos).isReplaceable()) {
            return false;
        }
        return !checkEntities || ClientMain.mc.world.canPlace(block.getDefaultState(), blockPos, ShapeContext.absent());
    }

    public static Direction getPlaceSide(BlockPos blockPos) {
        Vec3d lookVec = blockPos.toCenterPos().subtract(ClientMain.mc.player.getEyePos());
        double bestRelevancy = -1.7976931348623157E308;
        Direction bestSide = null;
        for (Direction side : Direction.values()) {
            double relevancy;
            BlockPos neighbor = blockPos.offset(side);
            BlockState state = ClientMain.mc.world.getBlockState(neighbor);
            if (state.isAir() || BlockUtils.isClickable(state.getBlock()) || !state.getFluidState().isEmpty() || !((relevancy = side.getAxis().choose(lookVec.getX(), lookVec.getY(), lookVec.getZ()) * (double)side.getDirection().offset()) > bestRelevancy)) continue;
            bestRelevancy = relevancy;
            bestSide = side;
        }
        return bestSide;
    }

    public static boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock || block instanceof AnvilBlock || block instanceof LoomBlock || block instanceof CartographyTableBlock || block instanceof GrindstoneBlock || block instanceof StonecutterBlock || block instanceof ButtonBlock || block instanceof AbstractPressurePlateBlock || block instanceof BlockWithEntity || block instanceof BedBlock || block instanceof FenceGateBlock || block instanceof DoorBlock || block instanceof NoteBlock || block instanceof TrapdoorBlock || block instanceof ChestBlock || block instanceof EnderChestBlock || block instanceof BarrelBlock || block instanceof SignBlock;
    }

    public static CompletableFuture<List<Pair<BlockPos, Block>>> getStorageBlocksAsync() {
        return CompletableFuture.supplyAsync(() -> BlockUtils.getEntityBlocksMatching(storage::contains), executor);
    }

    public static CompletableFuture<List<Pair<BlockPos, Block>>> getOreBlocksAsync() {
        return CompletableFuture.supplyAsync(() -> BlockUtils.getBlocksMatching(ore::contains), executor);
    }

    public static CompletableFuture<List<Pair<BlockPos, Block>>> getExposedOreAsync() {
        return CompletableFuture.supplyAsync(() -> BlockUtils.getBlocksMatching(pos -> {
            BlockState state = ClientMain.mc.world.getBlockState(pos);
            return ore.contains(state.getBlock()) && BlockUtils.isExposedToAir(pos);
        }), executor);
    }

    private static List<Pair<BlockPos, Block>> getBlocksMatching(Predicate<BlockPos> condition) {
        ArrayList<Pair<BlockPos, Block>> foundBlocks = new ArrayList<Pair<BlockPos, Block>>();
        WorldUtils.getLoadedChunks().forEach(chunk -> {
            ChunkPos chunkPos = chunk.getPos();
            int startX = chunkPos.getStartX();
            int startZ = chunkPos.getStartZ();
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    for (int y = 0; y < ClientMain.mc.world.getHeight(); ++y) {
                        BlockPos pos = new BlockPos(startX + x, y, startZ + z);
                        BlockState state = chunk.getBlockState(pos);
                        if (!condition.test(pos)) continue;
                        foundBlocks.add(new Pair((Object)pos, (Object)state.getBlock()));
                    }
                }
            }
        });
        return foundBlocks;
    }

    private static List<Pair<BlockPos, Block>> getEntityBlocksMatching(Predicate<Block> condition) {
        ArrayList<Pair<BlockPos, Block>> foundBlocks = new ArrayList<Pair<BlockPos, Block>>();
        WorldUtils.getLoadedChunks().forEach(chunk -> chunk.getBlockEntities().values().forEach(blockEntity -> {
            BlockPos pos = blockEntity.getPos();
            BlockState state = chunk.getBlockState(pos);
            if (condition.test(state.getBlock())) {
                foundBlocks.add(new Pair((Object)pos, (Object)state.getBlock()));
            }
        }));
        return foundBlocks;
    }

    private static boolean isExposedToAir(BlockPos pos) {
        return ClientMain.mc.world.getBlockState(pos.up()).isAir() || ClientMain.mc.world.getBlockState(pos.down()).isAir() || ClientMain.mc.world.getBlockState(pos.north()).isAir() || ClientMain.mc.world.getBlockState(pos.south()).isAir() || ClientMain.mc.world.getBlockState(pos.east()).isAir() || ClientMain.mc.world.getBlockState(pos.west()).isAir();
    }
}
