package ace.actually.dataplanets.space;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class SpaceStations {

    public static void spawnStructure(ServerLevel world, BlockPos pos, String name,boolean mirrored)
    {
        StructureTemplateManager manager = world.getStructureManager();

        ResourceLocation load = ResourceLocation.fromNamespaceAndPath("dataplanets",name);
        StructureTemplate structure = manager.get(load).get();
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(Rotation.NONE);
        if(mirrored)
        {
            settings.setMirror(Mirror.LEFT_RIGHT);
        }
        else
        {
            settings.setMirror(Mirror.NONE);
        }
        structure.placeInWorld(world,pos,pos,settings,world.random,2);


    }


    public static void constructSpaceStation(ServerLevel world, BlockPos pos)
    {
        //System.out.println("building space station");
        spawnStructure(world,pos,"hangar",false);
        spawnStructure(world,pos.west(32),"main",false);

        int southCapStone = world.random.nextInt(1,6);

        for (int i = 0; i < southCapStone; i++) {
            spawnStructure(world,pos.west(32).south(32+(32*i)),"main",false);
        }

        int northCapStone = world.random.nextInt(1,6);
        for (int i = 0; i < northCapStone; i++) {
            spawnStructure(world,pos.west(32).north(32+(32*i)),"main",false);
        }

        for (int i = -(southCapStone-1); i < (northCapStone-1); i++) {
            if(world.random.nextInt(3)==0)
            {
                spawnStructure(world,pos.north(32*i),"hangar",false);
            }
        }

        spawnStructure(world,pos.west(32).south(32+(southCapStone*32)),"power",false);
        spawnStructure(world,pos.west(32).north(32+(northCapStone*32)),"control",false);
        //System.out.println("finished space station!");
    }
}
