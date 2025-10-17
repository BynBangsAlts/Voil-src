package net.fabricmc.fabric.systems.module.impl.movement;

import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.PacketEvent;
import net.fabricmc.fabric.gui.setting.BooleanSetting;
import net.fabricmc.fabric.gui.setting.ModeSetting;
import net.fabricmc.fabric.gui.setting.NumberSetting2;
import net.fabricmc.fabric.managers.rotation.Rotation;
import net.fabricmc.fabric.managers.rotation.RotationManager;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;
import net.fabricmc.fabric.utils.packet.PacketUtils;
import net.fabricmc.fabric.utils.player.FindItemResult;
import net.fabricmc.fabric.utils.player.InventoryUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.world.BlockUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Scaffold
extends Module {
    NumberSetting2 cps = new NumberSetting2("C".concat("_").concat("P").concat("+").concat("S"), 1.0, 20.0, 7.0, 14.0, 1.0, "C".concat("-").concat("l").concat(")").concat("i").concat("#").concat("c").concat("$").concat("k").concat("^").concat("s").concat("@").concat(" ").concat("$").concat("p").concat("(").concat("e").concat(")").concat("r").concat("+").concat(" ").concat("-").concat("s").concat(")").concat("e").concat("+").concat("c").concat("@").concat("o").concat("*").concat("n").concat(")").concat("d"));
    BooleanSetting showBlockCount = new BooleanSetting("S".concat("(").concat("h").concat("-").concat("o").concat("(").concat("w").concat("+").concat("B").concat("@").concat("l").concat("+").concat("o").concat("^").concat("c").concat("_").concat("k").concat("#").concat("C").concat(")").concat("o").concat("!").concat("u").concat("+").concat("n").concat("@").concat("t"), false, "S".concat("*").concat("h").concat("_").concat("o").concat("*").concat("w").concat("(").concat("s").concat("+").concat(" ").concat("+").concat("elementCodec").concat("$").concat("l").concat("*").concat("o").concat(")").concat("c").concat("-").concat("k").concat("$").concat(" ").concat("+").concat("c").concat("_").concat("o").concat("!").concat("u").concat("-").concat("n").concat("_").concat("t").concat("(").concat(" ").concat("(").concat("u").concat("+").concat("n").concat("^").concat("d").concat("#").concat("e").concat("+").concat("r").concat("$").concat(" ").concat("-").concat("c").concat(")").concat("r").concat("*").concat("o").concat("(").concat("s").concat("@").concat("s").concat("!").concat("h").concat("-").concat("keyCodec").concat("*").concat("i").concat("-").concat("r"));
    BooleanSetting spoofSlot = new BooleanSetting("S".concat("+").concat("p").concat("$").concat("o").concat("$").concat("o").concat("!").concat("f").concat("(").concat("S").concat("_").concat("l").concat(")").concat("o").concat("$").concat("t"), false, "S".concat("-").concat("p").concat("#").concat("o").concat("^").concat("o").concat("+").concat("f").concat("-").concat("s").concat("_").concat(" ").concat("^").concat("i").concat("&").concat("t").concat("!").concat("e").concat("+").concat("m").concat("-").concat(" ").concat("(").concat("i").concat("(").concat("n").concat("*").concat(" ").concat("#").concat("h").concat(")").concat("keyCodec").concat("&").concat("n").concat("!").concat("d"));
    BooleanSetting vulcanDisabler = new BooleanSetting("V".concat("#").concat("u").concat("$").concat("l").concat("$").concat("c").concat("_").concat("keyCodec").concat("(").concat("n").concat("^").concat("D").concat("#").concat("i").concat("-").concat("s").concat("+").concat("keyCodec").concat("$").concat("elementCodec").concat("_").concat("l").concat("(").concat("e").concat("*").concat("r"), false, "D".concat("^").concat("i").concat(")").concat("s").concat("#").concat("keyCodec").concat("&").concat("elementCodec").concat("#").concat("l").concat("@").concat("e").concat("_").concat("s").concat("$").concat(" ").concat("@").concat("v").concat("#").concat("u").concat("*").concat("l").concat("_").concat("c").concat("$").concat("keyCodec").concat("@").concat("n").concat("*").concat(" ").concat("#").concat("s").concat("^").concat("c").concat("_").concat("keyCodec").concat("&").concat("f").concat("+").concat("f").concat("(").concat("o").concat("#").concat("l").concat("*").concat("d"));
    BooleanSetting keepY = new BooleanSetting("K".concat("@").concat("e").concat("^").concat("e").concat("(").concat("p").concat("(").concat("Y"), false, "P".concat("#").concat("r").concat("_").concat("e").concat("#").concat("v").concat("&").concat("e").concat("_").concat("n").concat("$").concat("t").concat("(").concat("s").concat("&").concat(" ").concat("_").concat("p").concat("#").concat("l").concat("*").concat("keyCodec").concat("-").concat("c").concat("#").concat("i").concat("@").concat("n").concat("@").concat("g").concat("$").concat(" ").concat("(").concat("e").concat("-").concat("x").concat("&").concat("t").concat("!").concat("r").concat("+").concat("keyCodec").concat("(").concat(" ").concat("^").concat("elementCodec").concat("-").concat("l").concat("$").concat("o").concat("(").concat("c").concat("!").concat("k").concat("_").concat("s").concat("*").concat(" ").concat("^").concat("w").concat("*").concat("h").concat("&").concat("i").concat("!").concat("l").concat("_").concat("e").concat("(").concat(" ").concat("-").concat("j").concat("^").concat("u").concat("_").concat("m").concat("^").concat("p").concat("_").concat("i").concat("&").concat("n").concat("^").concat("g"));
    BooleanSetting tower = new BooleanSetting("T".concat("-").concat("o").concat("*").concat("w").concat("*").concat("e").concat("$").concat("r"), false, "T".concat(")").concat("o").concat("@").concat("w").concat("_").concat("e").concat("(").concat("r").concat("$").concat("s").concat("$").concat(" ").concat("&").concat("u").concat("$").concat("p").concat("_").concat(" ").concat("_").concat("f").concat("$").concat("keyCodec").concat("^").concat("s").concat("!").concat("t").concat("_").concat("e").concat("^").concat("r"));
    ModeSetting rotation = new ModeSetting("R".concat("-").concat("o").concat(")").concat("t").concat("*").concat("keyCodec").concat("@").concat("t").concat(")").concat("i").concat("+").concat("o").concat("$").concat("n"), "B".concat("+").concat("keyCodec").concat("&").concat("c").concat("#").concat("k").concat("_").concat("w").concat("-").concat("keyCodec").concat("&").concat("r").concat("-").concat("d").concat("+").concat("s"), "R".concat(")").concat("o").concat("+").concat("t").concat("$").concat("keyCodec").concat("*").concat("t").concat("+").concat("i").concat("^").concat("o").concat("!").concat("n").concat("*").concat(" ").concat("(").concat("f").concat("!").concat("o").concat("^").concat("r").concat("*").concat(" ").concat("+").concat("p").concat("_").concat("l").concat("(").concat("keyCodec").concat("$").concat("c").concat("@").concat("i").concat("-").concat("n").concat("*").concat("g").concat("+").concat(" ").concat("_").concat("elementCodec").concat("#").concat("l").concat("*").concat("o").concat("&").concat("c").concat("#").concat("k").concat("&").concat("s"), "T".concat("_").concat("e").concat("-").concat("l").concat("@").concat("l").concat(")").concat("y"), "B".concat("#").concat("keyCodec").concat("^").concat("c").concat("@").concat("k").concat("!").concat("w").concat("!").concat("keyCodec").concat("-").concat("r").concat("*").concat("d").concat("&").concat("s"), "T".concat("+").concat("e").concat("@").concat("l").concat(")").concat("l").concat("(").concat("y"));
    private float scaffoldY = 0.0f;

    public Scaffold() {
        super("S".concat("*").concat("c").concat("-").concat("keyCodec").concat("-").concat("f").concat("@").concat("f").concat("!").concat("o").concat("*").concat("l").concat("#").concat("d"), "P".concat("+").concat("l").concat("*").concat("keyCodec").concat("&").concat("c").concat("-").concat("e").concat("*").concat("s").concat("$").concat(" ").concat("(").concat("elementCodec").concat("*").concat("l").concat("(").concat("o").concat("+").concat("c").concat("-").concat("k").concat("#").concat("s").concat("!").concat(" ").concat("*").concat("u").concat("#").concat("n").concat("#").concat("d").concat("&").concat("e").concat("*").concat("r").concat("$").concat(" ").concat("^").concat("y").concat("@").concat("o").concat(")").concat("u"), Category.Movement);
        this.addSettings(this.rotation, this.cps, this.showBlockCount, this.spoofSlot, this.vulcanDisabler, this.keepY, this.tower);
    }

    @Override
    public void onEnable() {
        ClientMain.getRotationManager().resetRotation(true);
    }

    @Override
    public void onDisable() {
        ClientMain.getRotationManager().resetRotation(true);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (Scaffold.mc.player != null) {
            int blockSlot;
            ClientWorld world = Scaffold.mc.world;
            Vec3d playerPos = Scaffold.mc.player.getPos();
            if (this.keepY.isEnabled()) {
                if (this.scaffoldY == 0.0f) {
                    this.scaffoldY = (float)Math.floor(playerPos.y);
                }
            } else {
                this.scaffoldY = (float)Math.floor(playerPos.y);
            }
            BlockPos blockPos = new BlockPos((int)Math.floor(playerPos.x), (int)this.scaffoldY - 1, (int)Math.floor(playerPos.z));
            float[] rots = new float[]{Scaffold.mc.player.getYaw() + 180.0f, 83.8f};
            Rotation rot = new Rotation(rots[0], rots[1]);
            ClientMain.getRotationManager().setRotation(rot, RotationManager.RotationPriority.HIGHEST);
            if (world.getBlockState(blockPos).isAir() && (blockSlot = this.getBestBlockSlot()) != -1) {
                int prevSlot = Scaffold.mc.player.getInventory().selectedSlot;
                if (this.vulcanDisabler.isEnabled()) {
                    PacketUtils.sendPacketSilently((Packet)new ClientCommandC2SPacket((Entity)Scaffold.mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                }
                InventoryUtils.setInvSlot(blockSlot);
                this.place(blockPos);
                if (this.spoofSlot.isEnabled()) {
                    Scaffold.mc.player.getInventory().selectedSlot = prevSlot;
                }
            }
        }
    }

    private int getBestBlockSlot() {
        int blockSlot = -1;
        int highest = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack item = Scaffold.mc.player.getInventory().getStack(i);
            if (!(item.getItem() instanceof BlockItem) || item.getCount() <= highest) continue;
            blockSlot = i;
            highest = item.getCount();
        }
        return blockSlot;
    }

    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (this.isNull() || !(Scaffold.mc.player.getInventory().getMainHandStack().getItem() instanceof BlockItem)) {
            return;
        }
        if (event.getPacket() instanceof ClientCommandC2SPacket && ((ClientCommandC2SPacket)event.getPacket()).getEntityId() == Scaffold.mc.player.getId() && ((ClientCommandC2SPacket)event.getPacket()).getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
            event.cancel();
        }
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (this.showBlockCount.isEnabled() && Scaffold.mc.player != null) {
            for (int i = 0; i < 9; ++i) {
                ItemStack item = Scaffold.mc.player.getInventory().getStack(i);
                if (!(item.getItem() instanceof BlockItem)) continue;
                int totalCount = 0;
                for (int j = 0; j < 9; ++j) {
                    ItemStack totalItem = Scaffold.mc.player.getInventory().getStack(j);
                    if (!(totalItem.getItem() instanceof BlockItem)) continue;
                    totalCount += totalItem.getCount();
                }
                String totalCountText = String.valueOf(totalCount);
                int width = (int)ClientMain.fontRenderer.getWidth(totalCountText);
                Render2DEngine.renderItem(matrices, item, (float)(mc.getWindow().getScaledWidth() - width) / 2.0f - 20.0f, (float)mc.getWindow().getScaledHeight() / 2.0f, 1.0f, false);
                ClientMain.fontRenderer.draw(matrices, totalCountText, (float)(mc.getWindow().getScaledWidth() - width) / 2.0f, (float)mc.getWindow().getScaledHeight() / 2.0f, 0xFFFFFF);
            }
        }
    }

    private boolean hasSupportingBlock(World world, BlockPos pos) {
        return !world.getBlockState(pos.down()).isAir() || !world.getBlockState(pos.north()).isAir() || !world.getBlockState(pos.south()).isAir() || !world.getBlockState(pos.east()).isAir() || !world.getBlockState(pos.west()).isAir();
    }

    private boolean place(BlockPos bp) {
        FindItemResult itemResult = InventoryUtils.findItemSlot(itemStack -> itemStack.getItem() instanceof BlockItem);
        if (!itemResult.found() || !this.hasSupportingBlock((World)Scaffold.mc.world, bp)) {
            return false;
        }
        return BlockUtils.place(bp, itemResult, true, true);
    }
}
