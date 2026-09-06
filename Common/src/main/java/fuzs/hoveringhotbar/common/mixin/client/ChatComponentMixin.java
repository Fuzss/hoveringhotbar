package fuzs.hoveringhotbar.common.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.hoveringhotbar.common.config.ClientConfig;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin {

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V",
                           at = @At(value = "CONSTANT", args = "intValue=40"),
                           slice = @Slice(to = @At(value = "INVOKE",
                                                   target = "Lnet/minecraft/client/Options;chatOpacity()Lnet/minecraft/client/OptionInstance;")))
    private int extractRenderState(int bottomMargin) {
        return bottomMargin + HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset();

    }
}
