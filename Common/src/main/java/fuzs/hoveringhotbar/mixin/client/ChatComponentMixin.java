package fuzs.hoveringhotbar.mixin.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin {
    @Shadow
    @Final
    private static int BOTTOM_MARGIN;

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 7)
    private int extractRenderState(int chatBottom, GuiGraphics guiGraphics, int ticks, int mouseX, int mouseY, boolean focused) {
        int hotbarOffset = HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarOffset;
        if (hotbarOffset == 0) {
            return chatBottom;
        }

        float scale = (float) this.getScale();
        return Mth.floor((float) (guiGraphics.guiHeight() - BOTTOM_MARGIN - hotbarOffset) / scale);
    }

    @Shadow
    protected abstract double getScale();
}
