package ace.actually.dataplanets.blocks;

import ace.actually.dataplanets.space.SpaceStations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class HabBuilderBlock extends Block {
    boolean shouldMirror;
    public HabBuilderBlock(Properties p_49795_,boolean shouldMirror) {
        super(p_49795_);
        this.shouldMirror=shouldMirror;
    }
    public void createHab(ServerLevel serverWorld, BlockPos pos)
    {
        String spawnable;
        switch (serverWorld.random.nextInt(5))
        {
            case 0 -> spawnable="shop1";
            default -> spawnable="house1";
        }


        SpaceStations.spawnStructure(serverWorld,pos.above().north(),spawnable,shouldMirror);

        serverWorld.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
    }
}
