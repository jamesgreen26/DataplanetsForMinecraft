package ace.actually.dataplanets.items;

import ace.actually.dataplanets.DPPackets;
import ace.actually.dataplanets.compat.gcyr.GCYRPacket;
import ace.actually.dataplanets.space.Planets;
import ace.actually.dataplanets.space.StarSystemCreator;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class TestItem extends Item {
    public TestItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if(player.isCrouching())
        {
            if(!level.isClientSide)
            {
                DPPackets.INSTANCE.send(PacketDistributor.ALL.noArg(),new GCYRPacket(StarSystemCreator.getDynamicDataOrNew()));
            }

        }
        else
        {
            Planets.sphere(level,player.getOnPos(),55);
        }

        return super.use(level,player, p_41434_);
    }
}
