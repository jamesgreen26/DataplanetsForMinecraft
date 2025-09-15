package ace.actually.dataplanets.factions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;

public class Questable {

    public static String visitLocation(MinecraftServer server)
    {
        List<ResourceKey<Level>> levels = server.levelKeys().stream().toList();
        ResourceKey<Level> levelKey = levels.get(server.overworld().random.nextInt(levels.size()));
        ServerLevel level = server.getLevel(levelKey);

        //TODO: add a locateStructure to some structure that may exist on any planet
        return "visit "+levelKey.location()+" ";
    }

    //TODO: Should be called from somewhere semi-frequently
    public static void interpret(ServerPlayer spe, String string)
    {
        if(string.contains("visit"))
        {
            String[] split = string.split(" ");
            if(spe.serverLevel().dimension()==ResourceKey.create(Registries.DIMENSION,ResourceLocation.parse(split[0])))
            {
                BlockPos pos = new BlockPos(Integer.parseInt(split[1]),Integer.parseInt(split[2]),Integer.parseInt(split[3]));
                if(spe.getOnPos().distSqr(pos)<100)
                {
                    //TODO: resolve quest
                }
            }
        }
    }
}
