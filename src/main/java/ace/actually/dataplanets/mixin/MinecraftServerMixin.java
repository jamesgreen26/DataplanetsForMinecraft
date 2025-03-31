package ace.actually.dataplanets.mixin;

import ace.actually.dataplanets.space.DynamicSystems;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "saveAllChunks", at = @At("TAIL"))
    private void checkKey(boolean p_129886_, boolean p_129887_, boolean p_129888_, CallbackInfoReturnable<Boolean> cir)
    {
        //System.out.println("Unlocking dynamic generation...");
        DynamicSystems.frozeTimes=0;
    }
}
