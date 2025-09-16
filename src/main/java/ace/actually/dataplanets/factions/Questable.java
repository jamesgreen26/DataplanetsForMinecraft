package ace.actually.dataplanets.factions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A Quest consists of a "task" a "return" and a "reward"
 * - A task is an interpretable string, usually with a keyword like "visit"
 * - A return is also a task, a task and return need to be complete to obtain the reward
 * - A reward is an ItemStack (for now)
 */
public class Questable {

    public static final ResourceLocation QUESTING = ResourceLocation.fromNamespaceAndPath("dataplanets","questing");
    public static String visitLocation(MinecraftServer server)
    {
        List<ResourceKey<Level>> levels = server.levelKeys().stream().toList();
        ResourceKey<Level> levelKey = levels.get(server.overworld().random.nextInt(levels.size()));
        ServerLevel level = server.getLevel(levelKey);

        //TODO: add a locateStructure to some structure that may exist on any planet
        return "visit "+levelKey.location()+" ";
    }
    public static String collectItems(MinecraftServer server)
    {
        //TODO: make an item tag of collectibles that may be found on various planets
        ItemStack stack = new ItemStack(Items.STICK,32);
        return "collect "+BuiltInRegistries.ITEM.getKey(stack.getItem())+" "+stack.getCount();
    }

    //TODO: Should be called from somewhere semi-frequently
    public static void interpret(ServerPlayer spe, String string)
    {
        CompoundTag quests = spe.server.getCommandStorage().get(QUESTING);
        CompoundTag player = quests.getCompound(spe.getStringUUID());

        if(player.getString("task").contains("visit"))
        {
            String[] split = string.split(" ");
            if(spe.serverLevel().dimension()==ResourceKey.create(Registries.DIMENSION,ResourceLocation.parse(split[1])))
            {
                BlockPos pos = new BlockPos(Integer.parseInt(split[2]),Integer.parseInt(split[3]),Integer.parseInt(split[4]));
                if(spe.getOnPos().distSqr(pos)<100)
                {
                    updateQuest(spe);
                }
            }
        }
        if(player.getString("task").contains("collect"))
        {
            String[] split = string.split(" ");
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(split[1]));
            int count = Integer.parseInt(split[2]);
            if(spe.getInventory().countItem(item)>=count)
            {
                updateQuest(spe);
            }
        }
    }

    public static void updateQuest(ServerPlayer spe)
    {
        CompoundTag quests = spe.server.getCommandStorage().get(QUESTING);
        CompoundTag player = quests.getCompound(spe.getStringUUID());
        if(!player.getString("return").equals("finished"))
        {
            player.putString("task",player.getString("return"));
            player.putString("return","finished");
        }
        else
        {
            player.putString("task","not started");
            String[] v = player.getString("reward").split(",");
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(v[0]));

            spe.addItem(new ItemStack(item,Integer.parseInt(v[1])));
        }
        quests.put(spe.getStringUUID(),player);
        spe.server.getCommandStorage().set(QUESTING,quests);
    }
}
