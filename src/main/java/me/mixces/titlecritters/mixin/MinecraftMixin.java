package me.mixces.titlecritters.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	public Screen screen;

	@Shadow
	public GameOptions options;

	@ModifyReturnValue(method = "getMaxFramerate", at = @At("RETURN"))
	private int titleCritters$unCapFramerate(int original) {
		if (screen instanceof TitleScreen) {
			return options.fpsLimit;
		}
		return original;
	}
}
