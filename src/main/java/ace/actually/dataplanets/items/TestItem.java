package ace.actually.dataplanets.items;

import ace.actually.dataplanets.space.DynamicSystems;
import ace.actually.dataplanets.space.Planets;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestItem extends Item {
    public TestItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if(player.isCrouching())
        {
            DynamicSystems.onGenSetup(level.getServer());
        }
        else
        {
            Planets.sphere(level,player.getOnPos(),55);
        }

        return super.use(level,player, p_41434_);
    }
}
