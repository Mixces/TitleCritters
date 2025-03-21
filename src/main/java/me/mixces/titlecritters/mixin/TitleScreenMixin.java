package me.mixces.titlecritters.mixin;

import me.mixces.titlecritters.handler.ModelRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

	private final float playerHeight = 1.8F;
	private final float playerEyeHeight = 1.62F;

	@Inject(
		method = "render",
		at = @At("TAIL")
	)
	private void titleCritters$renderEntity(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
		ModelRenderer model = new ModelRenderer();
		int size = 30;
		float x = ((width / 2.0F - 100.0F)) / 2.0F;
		float y = height / 2.0F + playerHeight * size / 2.0F;
		/* left */
		model.renderLivingEntity(x, y, size, x - mouseX, y - (playerEyeHeight * size) - mouseY, tickDelta, true);

		x = (width + (width / 2.0F + 100.0F)) / 2.0F;
		/* right */
		model.renderLivingEntity(x, y, size, x - mouseX, y - (playerEyeHeight * size) - mouseY, tickDelta, false);
	}
}
