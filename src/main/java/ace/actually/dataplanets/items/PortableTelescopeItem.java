package ace.actually.dataplanets.items;

import ace.actually.dataplanets.registry.DPItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PortableTelescopeItem extends Item {
    public PortableTelescopeItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide)
        {
            if(player.getItemInHand(InteractionHand.MAIN_HAND).is(this) && player.getItemInHand(InteractionHand.OFF_HAND).is(Items.PAPER))
            {
                String biome = level.getBiome(player.getOnPos()).unwrapKey().get().location().toString();
                int sectorX = player.getOnPos().getX()/1000;
                int sectorZ = player.getOnPos().getZ()/1000;
                player.sendSystemMessage(Component.empty().append("Biome: ").append(biome)
                        .append("\nSector: "+sectorX+" "+sectorZ).append("\nTime: ").append(""+level.getGameTime()));

                ItemStack stack = new ItemStack(DPItems.RESEARCH);
                CompoundTag tag = stack.getOrCreateTag();
                tag.putString("biome",biome);
                tag.putInt("sectorX",sectorX);
                tag.putInt("sectorZ",sectorZ);
                tag.putLong("time",level.getGameTime());
                stack.setHoverName(Component.empty().withStyle(ChatFormatting.AQUA).append("Observation"));

                stack.setTag(tag);
                player.addItem(stack);
                player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
            }

        }
        return super.use(level, player, hand);
    }

}
