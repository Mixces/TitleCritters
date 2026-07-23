package me.mixces.titlecritters.mixin;

import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {

	@Invoker
	void invokeRenderItemInHand(ItemStack stack, BakedModel model, ModelTransformations.Type transformationType);
}
