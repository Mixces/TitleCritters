package me.mixces.titlecritters.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.model.entity.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

	@Unique
	private float bodyYaw;

	@Unique
	private float prevBodyYaw;

	@Unique
	private float yaw;

	@Unique
	private float pitch;

	@Unique
	private float prevPitch;

	@Unique
	private float headYaw;

	@Unique
	private float prevHeadYaw;

	@Inject(
		method = "render",
		at = @At("TAIL")
	)
	private void titleCritters$renderPlayer(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
		PlayerModel model = new PlayerModel(0.0F, false);

		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(50.0F, 150.0F, 50.0F);
		GlStateManager.scalef(-30, 30, 30);
		GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f = bodyYaw;
		float g = yaw;
		float h = pitch;
		float i = prevHeadYaw;
		float j = headYaw;
		Lighting.turnOn();
		GlStateManager.rotatef(((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		bodyYaw = -(float)Math.atan(mouseX / 40.0F) * 20.0F;
		yaw = -(float)Math.atan(mouseX / 40.0F) * 40.0F;
		pitch = -((float)Math.atan(mouseY / 40.0F)) * 20.0F;
		headYaw = yaw;
		prevHeadYaw = yaw;

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		float rotatedBodyYaw = getRotatedAngle(prevBodyYaw, bodyYaw, h);
		float rotatedHeadYaw = getRotatedAngle(prevHeadYaw, headYaw, h);
		float k = rotatedHeadYaw - rotatedBodyYaw;
		float m = prevPitch + (pitch - prevPitch) * h;
		GlStateManager.rotatef(180.0F - rotatedBodyYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
		GlStateManager.scalef(0.9375F, 0.9375F, 0.9375F);
		GlStateManager.translatef(0.0F, -1.5078125F, 0.0F);
		GlStateManager.enableAlphaTest();
		setAngles(model, k, m);
		renderPlayer(model);
		GlStateManager.depthMask(true);
		GlStateManager.disableRescaleNormal();
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.enableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		bodyYaw = f;
		yaw = g;
		pitch = h;
		prevHeadYaw = i;
		headYaw = j;
		GlStateManager.popMatrix();
		Lighting.turnOff();
		GlStateManager.disableRescaleNormal();
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.disableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
	}

	@Unique
	private void renderPlayer(PlayerModel model) {
		Identifier playerTexture = new Identifier("textures/entity/steve.png");
		Minecraft.getInstance().getTextureManager().bind(playerTexture);
		GlStateManager.pushMatrix();
		model.head.render(0.0625F);
		model.body.render(0.0625F);
		model.rightArm.render(0.0625F);
		model.leftArm.render(0.0625F);
		model.rightLeg.render(0.0625F);
		model.leftLeg.render(0.0625F);
		model.hat.render(0.0625F);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		model.leftPants.render(0.0625F);
		model.rightPants.render(0.0625F);
		model.leftSleeve.render(0.0625F);
		model.rightSleeve.render(0.0625F);
		model.jacket.render(0.0625F);
		GlStateManager.popMatrix();
	}

	@Unique
	private void setAngles(PlayerModel model, float yaw, float pitch) {
		model.head.rotationY = yaw / (180.0F / (float)Math.PI);
		model.head.rotationX = pitch / (180.0F / (float)Math.PI);
		model.rightArm.rotationX = 0.0F;
		model.leftArm.rotationX = 0.0F;
		model.leftArm.rotationZ = 0.0F;
		model.rightLeg.rotationX = 0.0F;
		model.leftLeg.rotationX = 0.0F;
		model.rightLeg.rotationY = 0.0F;
		model.leftLeg.rotationY = 0.0F;
		model.rightArm.rotationY = 0.0F;
		model.rightArm.rotationZ = 0.0F;
		model.leftArm.rotationY = 0.0F;
		model.body.rotationX = 0.0F;
		model.rightLeg.pivotZ = 0.1F;
		model.leftLeg.pivotZ = 0.1F;
		model.rightLeg.pivotY = 12.0F;
		model.leftLeg.pivotY = 12.0F;
		model.head.pivotY = 0.0F;
		model.rightArm.rotationZ = model.rightArm.rotationZ + (0.05F + 0.05F);
		model.leftArm.rotationZ = model.leftArm.rotationZ - (0.05F + 0.05F);
		model.rightArm.rotationX = model.rightArm.rotationX + 0.05F;
		model.leftArm.rotationX = model.leftArm.rotationX - 0.05F;
	}

	@Unique
	private float getRotatedAngle(float prevAng, float ang, float tickDelta) {
		float f = ang - prevAng;
		while (f < -180.0F) {
			f += 360.0F;
		}
		while (f >= 180.0F) {
			f -= 360.0F;
		}
		return prevAng + tickDelta * f;
	}
}
