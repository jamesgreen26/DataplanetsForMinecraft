package ace.actually.dataplanets.mixin;

import ace.actually.dataplanets.Dataplanets;
import ace.actually.dataplanets.DynamicSystems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class ServerPlayerMixin extends Entity {

    @Shadow @Final private AttributeMap attributes;

    @Shadow public abstract RandomSource getRandom();

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute p_21052_);

    public ServerPlayerMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkKey(CallbackInfo ci)
    {
        if(this.level().dimension().location().getNamespace().equals("dataplanets"))
        {
            AttributeInstance gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
            if(gravity!=null)
            {

                AttributeModifier modifier = new AttributeModifier(Dataplanets.LOW_GRAVITY, "Low Gravity", -0.8f, AttributeModifier.Operation.MULTIPLY_TOTAL);
                if(!gravity.hasModifier(modifier))
                {
                    gravity.addTransientModifier(modifier);
                }

            }
        }
    }
}
