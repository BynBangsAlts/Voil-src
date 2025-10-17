package net.fabricmc.fabric.managers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.EventUpdate;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.misc.AntiBot;
import net.fabricmc.fabric.utils.player.RaycastUtils;
import net.fabricmc.fabric.utils.world.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class SocialManager {
    private static final Path settingsPath = Path.of(System.getenv("LOCALAPPDATA"), "Programs", "Common", "friends.json");
    private static final Gson gson = new Gson();
    private static final Map<PlayerEntity, Set<BotFlags>> bots = new ConcurrentHashMap<PlayerEntity, Set<BotFlags>>();
    private static final Set<PlayerEntity> verifiedPlayers = new HashSet<PlayerEntity>();
    private static final Set<UUID> teammates = new HashSet<UUID>();
    private static PlayerEntity target;

    public static Set<UUID> getFriendsList() {
        HashSet<UUID> friends = new HashSet<UUID>();
        try {
            JsonObject jsonObject;
            String content;
            if (Files.exists(settingsPath, new LinkOption[0]) && !(content = Files.readString(settingsPath)).isEmpty() && (jsonObject = JsonParser.parseString((String)content).getAsJsonObject()).has("friends")) {
                for (JsonElement jsonElement : jsonObject.getAsJsonArray("friends")) {
                    friends.add(UUID.fromString(jsonElement.getAsString()));
                }
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return friends;
    }

    public static boolean isFriend(UUID playerUuid) {
        return SocialManager.getFriendsList().contains(playerUuid);
    }

    public static void addFriend(UUID playerUuid) {
        Set<UUID> friends = SocialManager.getFriendsList();
        friends.add(playerUuid);
        SocialManager.saveFriendsList(friends);
    }

    public static void removeFriend(UUID playerUuid) {
        Set<UUID> friends = SocialManager.getFriendsList();
        friends.remove(playerUuid);
        SocialManager.saveFriendsList(friends);
    }

    private static void saveFriendsList(Set<UUID> friends) {
        try {
            String content;
            JsonObject jsonObject;
            if (!Files.exists(settingsPath.getParent(), new LinkOption[0])) {
                Files.createDirectories(settingsPath.getParent(), new FileAttribute[0]);
            }
            if (!Files.exists(settingsPath, new LinkOption[0])) {
                Files.createFile(settingsPath, new FileAttribute[0]);
            }
            if (!(jsonObject = (content = Files.readString(settingsPath)).isEmpty() ? new JsonObject() : JsonParser.parseString((String)content).getAsJsonObject()).has("friends")) {
                jsonObject.add("friends", gson.toJsonTree(new HashSet()));
            }
            jsonObject.add("friends", gson.toJsonTree(friends));
            Files.writeString(settingsPath, (CharSequence)gson.toJson((JsonElement)jsonObject), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addBot(PlayerEntity player) {
        bots.putIfAbsent(player, new HashSet());
    }

    public static void removeBot(PlayerEntity player) {
        bots.remove(player);
    }

    public static Set<PlayerEntity> getBots() {
        return bots.keySet();
    }

    public static boolean isBot(PlayerEntity player) {
        return bots.containsKey(player);
    }

    public static void addBotFlag(PlayerEntity player, BotFlags flag) {
        bots.computeIfPresent(player, (p, flags) -> {
            flags.add(flag);
            return flags;
        });
    }

    private static boolean meetsCriteria(PlayerEntity player) {
        Set flags = bots.getOrDefault(player, Collections.emptySet());
        return flags.contains((Object)BotFlags.MOVED) && flags.contains((Object)BotFlags.TOUCHED_GROUND) && flags.contains((Object)BotFlags.ROTATED) && flags.contains((Object)BotFlags.PING_ISNT_0) && flags.contains((Object)BotFlags.SWINGED);
    }

    @EventHandler
    public void onUpdate(EventUpdate e) {
        if (ClientMain.mc.world == null || ClientMain.mc.player == null) {
            return;
        }
        if (!ModuleManager.INSTANCE.getModuleByClass(AntiBot.class).isEnabled()) {
            return;
        }
        ClientMain.mc.world.getPlayers().forEach(player -> {
            if (player == null || player == ClientMain.mc.player) {
                return;
            }
            if (verifiedPlayers.contains(player)) {
                return;
            }
            if (player.hurtTime > 0) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.TOOK_DAMAGE);
            }
            if (player.isOnGround()) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.TOUCHED_GROUND);
            } else {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.IN_AIR);
            }
            if (player.handSwingTicks > 0) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.SWINGED);
            }
            if (player.getX() != player.prevX || player.getY() != player.prevY || player.getZ() != player.prevZ) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.MOVED);
            }
            if (player.getYaw() != player.prevYaw || player.getPitch() != player.prevPitch) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.ROTATED);
            }
            if (WorldUtils.getEntityPing((PlayerEntity)player) != 0) {
                SocialManager.addBotFlag((PlayerEntity)player, BotFlags.PING_ISNT_0);
            }
            if (SocialManager.meetsCriteria((PlayerEntity)player)) {
                SocialManager.removeBot((PlayerEntity)player);
                verifiedPlayers.add((PlayerEntity)player);
            } else {
                SocialManager.addBot((PlayerEntity)player);
                if (AntiBot.remove.isEnabled()) {
                    ClientMain.mc.world.removeEntity(player.getId(), Entity.RemovalReason.KILLED);
                }
            }
        });
    }

    public static Set<UUID> getTeammates() {
        return teammates;
    }

    public static void addTeammate(UUID playerUuid) {
        teammates.add(playerUuid);
    }

    public static void removeTeammate(UUID playerUuid) {
        teammates.remove(playerUuid);
    }

    public static boolean isTeammate(UUID playerUuid) {
        return teammates.contains(playerUuid);
    }

    public static void clearTeammates() {
        teammates.clear();
    }

    public static PlayerEntity getTargetPlayer() {
        return target;
    }

    public static void loseTarget() {
        target = null;
    }

    public static PlayerEntity findAndSetTarget(TargetCriteria ... criteria) {
        List<PlayerEntity> candidates = SocialManager.findAllTargets(criteria);
        target = candidates.isEmpty() ? null : candidates.get(0);
        return target;
    }

    public static List<PlayerEntity> findAllTargets(TargetCriteria ... criteria) {
        if (ClientMain.mc.world == null || ClientMain.mc.player == null) {
            return Collections.emptyList();
        }
        return ClientMain.mc.world.getPlayers().stream().filter(SocialManager::isValidTarget).filter(player -> SocialManager.matchesAll((PlayerEntity)player, criteria)).sorted(Comparator.comparingDouble(player -> ClientMain.mc.player.squaredDistanceTo((Entity)player))).collect(Collectors.toList());
    }

    public static PlayerEntity findAndSetTargetSorted(TargetSort sort, TargetCriteria ... criteria) {
        List<PlayerEntity> players = SocialManager.findAllTargets(criteria);
        SocialManager.sortPlayers(players, sort);
        target = players.isEmpty() ? null : players.get(0);
        return target;
    }

    public static void sortPlayers(List<PlayerEntity> players, TargetSort sort) {
        players.sort(switch (sort.ordinal()) {
            default -> throw new IncompatibleClassChangeError();
            case 0 -> Comparator.comparingDouble(p -> ClientMain.mc.player.squaredDistanceTo((Entity)p));
            case 1 -> Comparator.comparingDouble(SocialManager::getHealth);
            case 2 -> Comparator.comparingDouble(SocialManager::getHealth).reversed();
            case 3 -> Comparator.comparingInt(p -> {
                EntityHitResult hit;
                HitResult patt0$temp = ClientMain.mc.crosshairTarget;
                return patt0$temp instanceof EntityHitResult && (hit = (EntityHitResult)patt0$temp).getEntity() == p ? 0 : 1;
            });
        });
    }

    public static boolean isValidTarget(PlayerEntity player) {
        if (ClientMain.mc.player == null || player == null) {
            return false;
        }
        return player != ClientMain.mc.player && !player.isRemoved() && player.isAlive() && !player.isSpectator();
    }

    public static float getHealth(PlayerEntity player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static boolean matchesAll(PlayerEntity player, TargetCriteria ... criteria) {
        for (TargetCriteria c : criteria) {
            if (c.matches((Entity)player)) continue;
            return false;
        }
        return true;
    }

    public static boolean isVisible(Entity entity) {
        return ClientMain.mc.player != null && ClientMain.mc.player.canSee(entity);
    }

    public static double getDistance(PlayerEntity player) {
        return ClientMain.mc.player == null ? Double.MAX_VALUE : ClientMain.mc.player.squaredDistanceTo((Entity)player);
    }

    public static boolean isMoving(Entity entity) {
        return entity.getX() != entity.prevX || entity.getZ() != entity.prevZ;
    }

    static enum BotFlags {
        MOVED,
        ROTATED,
        TOOK_DAMAGE,
        SWINGED,
        TOUCHED_GROUND,
        IN_AIR,
        PING_ISNT_0;

    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    public static enum TargetCriteria {
        NOT_SELF{

            @Override
            public boolean matches(Entity entity) {
                return entity != ClientMain.mc.player;
            }
        }
        ,
        NOT_FRIEND{

            @Override
            public boolean matches(Entity entity) {
                return !SocialManager.isFriend(entity.getUuid());
            }
        }
        ,
        NOT_TEAMMATE{

            @Override
            public boolean matches(Entity entity) {
                return !SocialManager.isTeammate(entity.getUuid());
            }
        }
        ,
        VISIBLE{

            @Override
            public boolean matches(Entity entity) {
                return SocialManager.isVisible(entity);
            }
        }
        ,
        HITTABLE{

            @Override
            public boolean matches(Entity entity) {
                return RaycastUtils.canHitEntity(entity, 4.0, 1.0f);
            }
        }
        ,
        RANGE_30{

            @Override
            public boolean matches(Entity entity) {
                return ClientMain.mc.player != null && ClientMain.mc.player.distanceTo(entity) <= 30.0f;
            }
        }
        ,
        RANGE_20{

            @Override
            public boolean matches(Entity entity) {
                return ClientMain.mc.player != null && ClientMain.mc.player.distanceTo(entity) <= 20.0f;
            }
        }
        ,
        RANGE_3{

            @Override
            public boolean matches(Entity entity) {
                return ClientMain.mc.player != null && ClientMain.mc.player.squaredDistanceTo(entity) <= 9.0;
            }
        }
        ,
        MOVING{

            @Override
            public boolean matches(Entity entity) {
                return SocialManager.isMoving(entity);
            }
        }
        ,
        PLAYERSONLY{

            @Override
            public boolean matches(Entity entity) {
                return entity instanceof PlayerEntity;
            }
        }
        ,
        ANY{

            @Override
            public boolean matches(Entity entity) {
                return true;
            }
        };


        public abstract boolean matches(Entity var1);
    }

    public static enum TargetSort {
        CLOSEST,
        LOWEST_HEALTH,
        HIGHEST_HEALTH,
        LOOKING_AT;

    }
}
