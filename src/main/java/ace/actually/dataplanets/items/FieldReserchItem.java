package ace.actually.dataplanets.items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FieldReserchItem extends Item {
    public FieldReserchItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide && hand==InteractionHand.MAIN_HAND)
        {
            String biome = level.getBiome(player.getOnPos()).unwrapKey().get().location().toString();
            int sectorX = player.getOnPos().getX()/1000;
            int sectorZ = player.getOnPos().getZ()/1000;
            player.sendSystemMessage(Component.empty().append("Biome: ").append(biome)
                    .append("\nSector:"+sectorX+" "+sectorZ).append("\nTime:").append(""+level.getGameTime()));

            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getOrCreateTag();
            tag.putString("biome",biome);
            tag.putInt("sectorX",sectorX);
            tag.putInt("sectorZ",sectorZ);
            tag.putLong("time",level.getGameTime());
            stack.setTag(tag);
        }
        return super.use(level, player, hand);
    }

}
