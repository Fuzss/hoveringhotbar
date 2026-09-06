package fuzs.hoveringhotbar.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
abstract class GuiMixin {

    @ModifyArg(method = "renderItemHotbar",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"),
               index = 4)
    private int extractItemHotbar(int height) {
        return height == 23 ? 24 : height;
    }
}
