package net.fabricmc.fabric.utils.crystal;

import java.util.List;
import net.fabricmc.fabric.ClientMain;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public final class CrystalUtils {

    private CrystalUtils() {}

    public static boolean canPlaceCrystalServer(BlockPos pos) {
        if (ClientMain.mc.world == null) return false;

        BlockState state = ClientMain.mc.world.getBlockState(pos);
        if (state.getBlock() != Blocks.OBSIDIAN && state.getBlock() != Blocks.BEDROCK) return false;

        BlockPos crystalPos = pos.up();
        if (!ClientMain.mc.world.isAir(crystalPos)) return false;

        Box box = new Box(crystalPos).offset(0.5, 0.0, 0.5).stretch(0.0, 2.0, 0.0);
        List<Entity> entities = ClientMain.mc.world.getEntitiesByClass(Entity.class, box, e -> !(e instanceof ClientPlayerEntity));
        for (Entity entity : entities) {
            if (entity instanceof EndCrystalEntity) return false;
        }

        return true;
    }

    public static boolean canPlaceCrystalClient(BlockPos pos) {
        if (ClientMain.mc.world == null) return false;

        BlockState state = ClientMain.mc.world.getBlockState(pos);
        if (state.getBlock() != Blocks.OBSIDIAN && state.getBlock() != Blocks.BEDROCK) return false;

        return canPlaceCrystalClientAssumeObsidian(pos);
    }

    public static boolean canPlaceCrystalClientAssumeObsidian(BlockPos block) {
        BlockPos crystalPos = block.up();
        if (!ClientMain.mc.world.isAir(crystalPos)) return false;

        Box box = new Box(crystalPos).offset(0.5, 0.0, 0.5).stretch(0.0, 2.0, 0.0);
        List<Entity> entities = ClientMain.mc.world.getEntitiesByClass(Entity.class, box, e -> !(e instanceof ClientPlayerEntity));
        for (Entity entity : entities) {
            if (entity instanceof EndCrystalEntity) return false;
        }

        return true;
    }
}
