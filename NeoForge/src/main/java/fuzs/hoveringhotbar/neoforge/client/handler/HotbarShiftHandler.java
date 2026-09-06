package fuzs.hoveringhotbar.neoforge.client.handler;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class HotbarShiftHandler {
    private static boolean isOffsetApplied;

    public static void onBeforeRenderGui(final RenderGuiEvent.Pre event) {
        isOffsetApplied = false;
        event.getGuiGraphics().pose().pushPose();
    }

    public static void onAfterRenderGui(final RenderGuiEvent.Post event) {
        event.getGuiGraphics().pose().popPose();
    }

    public static void onBeforeRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        applyHotbarOffset(event);
        if (event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) {
            renderExperienceLevel(event);
        }
    }

    private static void applyHotbarOffset(RenderGuiLayerEvent.Pre event) {
        if (HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers.contains(event.getName())) {
            if (!isOffsetApplied) {
                isOffsetApplied = true;
                event.getGuiGraphics()
                        .pose()
                        .translate(0.0F, -HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset(), 0.0F);
            }
        } else if (isOffsetApplied) {
            isOffsetApplied = false;
            event.getGuiGraphics()
                    .pose()
                    .translate(0.0F, HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset(), 0.0F);
        }
    }

    private static void renderExperienceLevel(RenderGuiLayerEvent.Pre event) {
        if (!HoveringHotbar.CONFIG.get(ClientConfig.class).moveExperienceAboveBar) {
            return;
        }

        if (Minecraft.getInstance().options.hideGui) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0, -3.0, 0.0);
        // we render the layer manually, to avoid another mod potentially cancelling this,
        // without us having a chance to pop the pose stack after wards
        event.getLayer().render(guiGraphics, event.getPartialTick());
        guiGraphics.pose().popPose();
        event.setCanceled(true);
    }
}
