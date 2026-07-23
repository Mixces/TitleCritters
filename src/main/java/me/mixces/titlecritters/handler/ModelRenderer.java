package me.mixces.titlecritters.handler;

import me.mixces.titlecritters.mixin.ItemRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.model.block.ModelTransformations;
import net.minecraft.client.render.model.entity.CreeperModel;
import net.minecraft.client.render.model.entity.PlayerModel;
import net.minecraft.client.render.model.entity.SkeletonModel;
import net.minecraft.client.render.model.entity.ZombieModel;
import net.minecraft.client.render.platform.GLX;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.client.resource.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModelRenderer {
	private final PlayerModel player = new PlayerModel(0.0F, false);
	private final ZombieModel zombie = new ZombieModel();
	private final SkeletonModel skeleton = new SkeletonModel();
	private final CreeperModel creeper = new CreeperModel();

	private float bodyYaw;
	private float prevBodyYaw;
	private float yaw;
	private float pitch;
	private float prevPitch;
	private float headYaw;
	private float prevHeadYaw;

	public void renderLivingEntity(float x, float y, float size, float mouseX, float mouseY, float tickDelta, boolean onLeft) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();

		GlStateManager.translatef(x, y, 50.0F);
		GlStateManager.scalef(-size, size, size);
		GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);

		float bodyYaw = this.bodyYaw;
		float yaw = this.yaw;
		float pitch = this.pitch;
		float prevHeadYaw = this.prevHeadYaw;
		float headYaw = this.headYaw;
		float prevBodyYaw = this.prevBodyYaw;
		float prevPitch = this.prevPitch;

		Lighting.turnOn();
		GlStateManager.rotatef(-((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.bodyYaw = (float)Math.atan(mouseX / 40.0F) * 20.0F;
		this.yaw = (float)Math.atan(mouseX / 40.0F) * 40.0F;
		this.pitch = -((float)Math.atan(mouseY / 40.0F)) * 20.0F;
		this.headYaw = this.yaw;
		this.prevHeadYaw = this.yaw;
		this.prevBodyYaw = this.bodyYaw;
		this.prevPitch = this.pitch;

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		float lerpBodyYaw = getLerpAngle(this.prevBodyYaw, this.bodyYaw, tickDelta);
		float lerpHeadYaw = getLerpAngle(this.prevHeadYaw, this.headYaw, tickDelta);
		float lerpYaw = lerpHeadYaw - lerpBodyYaw;
		float lerpPitch = this.prevPitch + (this.pitch - this.prevPitch) * tickDelta;

		GlStateManager.rotatef(180.0F - lerpBodyYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
		GlStateManager.scalef(0.9375F, 0.9375F, 0.9375F);
		GlStateManager.translatef(0.0F, -1.5078125F, 0.0F);
		GlStateManager.enableAlphaTest();

		if (onLeft) {
			EntityModels.Zombie<ZombieModel> entityZombie = new EntityModels.Zombie<>();
			entityZombie.renderEntityWithPose(zombie, lerpYaw, lerpPitch);
//			EntityModels.Creeper<CreeperModel> entityCreeper = new EntityModels.Creeper<>();
//			entityCreeper.renderModelParts(creeper, lerpYaw, lerpPitch);
//			EntityModels.Skeleton<SkeletonModel> entitySkeleton = new EntityModels.Skeleton<>();
//			entitySkeleton.renderModelParts(skeleton, lerpYaw, lerpPitch);
		} else {
			EntityModels.Player<PlayerModel> entityPlayer = new EntityModels.Player<>();
			entityPlayer.renderEntityWithPose(player, lerpYaw, lerpPitch);

			/* render item*/
			GlStateManager.pushMatrix();
			player.translateRightArm(0.0625F);
			GlStateManager.translatef(-0.0625F, 0.4375F, 0.0625F);
			ItemStack stack = new ItemStack(Items.DIAMOND_SWORD, 0);
			BakedModel model = Minecraft.getInstance().getItemRenderer().getModelShaper().getModel(stack);
			GlStateManager.pushMatrix();
			((ItemRendererAccessor) Minecraft.getInstance().getItemRenderer()).invokeRenderItemInHand(stack, model, ModelTransformations.Type.THIRD_PERSON);
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}

		GlStateManager.depthMask(true);
		GlStateManager.disableRescaleNormal();
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.enableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		this.bodyYaw = bodyYaw;
		this.yaw = yaw;
		this.pitch = pitch;
		this.prevHeadYaw = prevHeadYaw;
		this.headYaw = headYaw;
		this.prevBodyYaw = prevBodyYaw;
		this.prevPitch = prevPitch;

		GlStateManager.popMatrix();
		Lighting.turnOff();
		GlStateManager.disableRescaleNormal();
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.disableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
	}

	private float getLerpAngle(float prevAngle, float angle, float tickDelta) {
		float f = angle - prevAngle;
		while (f < -180.0F) {
			f += 360.0F;
		}
		while (f >= 180.0F) {
			f -= 360.0F;
		}
		return prevAngle + f * tickDelta;
	}
}
