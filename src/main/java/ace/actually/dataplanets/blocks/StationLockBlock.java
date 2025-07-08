package ace.actually.dataplanets.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StationLockBlock extends Block {
    public static final BooleanProperty isOpen = BooleanProperty.create("is_open");
    public StationLockBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        if(p_60555_.getValue(isOpen))
        {
            return Block.box(0,0,0,1,16,1);
        }
        return Block.box(0,0,0,16,16,16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(isOpen));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return super.getStateForPlacement(p_49820_).setValue(isOpen,false);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(!state.hasProperty(isOpen) && (world.getBlockState(pos.relative(hit.getDirection().getOpposite())).is(Blocks.CAVE_AIR)
                || world.getBlockState(pos.relative(hit.getDirection().getOpposite())).is(this)))
        {
            world.setBlockAndUpdate(pos,state.setValue(isOpen,true));
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    for (int k = -2; k < 3; k++) {
                        if(world.getBlockState(pos.offset(i,j,k)).is(this))
                        {
                            world.setBlockAndUpdate(pos.offset(i,j,k),state.setValue(isOpen,true));
                        }
                    }
                }
            }

            if(world instanceof ServerLevel serverWorld)
            {
                for (int i = -50; i < 50; i++) {
                    for (int j = -5; j < 50; j++) {
                        for (int k = -50; k < 50; k++) {
                            if(world.getBlockState(pos.offset(i,j,k)).getBlock() instanceof HabBuilderBlock habBuilderBlock)
                            {
                                habBuilderBlock.createHab(serverWorld,pos.offset(i,j,k));
                            }
                        }
                    }
                }
            }


        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) {
        super.randomTick(p_222954_, p_222955_, p_222956_, p_222957_);
        p_222955_.setBlockAndUpdate(p_222956_,p_222954_.setValue(isOpen,false));
    }
}
