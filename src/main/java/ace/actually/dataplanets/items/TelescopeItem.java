package ace.actually.dataplanets.items;

import ace.actually.dataplanets.DynamicSystems;
import ace.actually.dataplanets.StarSystemCreator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TelescopeItem extends Item {
    public TelescopeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide)
        {
            if(pPlayer.isCrouching())
            {
                //TODO: generate data like/with the StarSystemCreator
                DynamicSystems.makeDynamicWorld(pLevel.getServer(), pLevel.getServer().getCommandStorage().get(StarSystemCreator.SYSTEM_DATA).getCompound("sz633").getCompound("sz633d"));
            }
            else
            {
                //StarSystemCreator.makeDynamicBiome(pLevel.getServer());
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.empty().append("WIP: on-the-fly dimension generation"));
    }
}
