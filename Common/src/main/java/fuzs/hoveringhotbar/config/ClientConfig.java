package fuzs.hoveringhotbar.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiLayerEvents;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientConfig implements ConfigCore {
    /**
     * We do not need to include {@link RenderGuiLayerEvents#SELECTED_ITEM_NAME} &amp;
     * {@link RenderGuiLayerEvents#OVERLAY_MESSAGE}, as both get their render height from {@code Gui#leftHeight} &amp;
     * {@code Gui#rightHeight}.
     */
    private static final List<ResourceLocation> DEFAULT_HOTBAR_GUI_LAYERS = List.of(RenderGuiLayerEvents.HOTBAR,
            RenderGuiLayerEvents.JUMP_METER,
            RenderGuiLayerEvents.EXPERIENCE_BAR,
            RenderGuiLayerEvents.SPECTATOR_TOOLTIP,
            RenderGuiLayerEvents.EXPERIENCE_LEVEL,
            ResourceLocation.parse("hotbarslotcycling:cycling_slots"),
            ResourceLocation.parse("enchantmentswitch:slot_overlay"),
            ResourceLocation.parse("lockedinslots:slot_overlay"));

    @Config(description = "Move the experience level display above the experience bar.")
    public boolean moveExperienceAboveBar = true;

    private ModConfigSpec.IntValue hotbarOffsetValue;
    private ModConfigSpec.ConfigValue<List<? extends String>> hotbarGuiLayersValue;
    private int configSaveDelay;
    public Set<ResourceLocation> hotbarGuiLayers = Collections.emptySet();

    public int getHotbarOffset() {
        return this.hotbarOffsetValue.getAsInt();
    }

    public void updateHotbarOffset(int screenHeight, boolean moveUp) {
        this.hotbarOffsetValue.set(Math.clamp(this.getHotbarOffset() + (moveUp ? 1 : -1), 0, screenHeight));
        this.configSaveDelay = 20;
    }

    public void onEndClientTick(Minecraft minecraft) {
        if (this.configSaveDelay > 0 && --this.configSaveDelay == 0) {
            this.hotbarOffsetValue.save();
        }
    }

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.hotbarOffsetValue = builder.comment("Height offset for the hotbar from the screen bottom.")
                .defineInRange("hotbar_offset", 2, 0, Integer.MAX_VALUE);
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            this.hotbarGuiLayersValue = builder.comment(
                            "Defines a set of gui layers that should be shifted together with the hotbar.")
                    .defineList("hotbar_gui_layers",
                            DEFAULT_HOTBAR_GUI_LAYERS.stream()
                                    .map(ResourceLocation::toString)
                                    .collect(Collectors.toList()),
                            () -> "",
                            (Object o) -> {
                                return o instanceof String string && ResourceLocation.tryParse(string) != null;
                            });
        }
    }

    @Override
    public void afterConfigReload() {
        if (this.hotbarGuiLayersValue != null) {
            this.hotbarGuiLayers = this.hotbarGuiLayersValue.get()
                    .stream()
                    .map(ResourceLocation::parse)
                    .collect(ImmutableSet.toImmutableSet());
        }
    }
}
