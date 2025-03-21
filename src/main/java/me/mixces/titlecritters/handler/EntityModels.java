package me.mixces.titlecritters.handler;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.model.Model;
import net.minecraft.client.render.model.entity.*;
import net.minecraft.client.resource.skin.DefaultSkinUtils;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.resource.Identifier;

import java.util.Map;

public class EntityModels {

	private static final Minecraft minecraft = Minecraft.getInstance();

	private static abstract class EntityModel<T extends Model> {
		public final void renderEntityWithPose(T model, float yaw, float pitch) {
			setLimbsPose(model, yaw, pitch);
			Minecraft.getInstance().getTextureManager().bind(getTexture());
			GlStateManager.pushMatrix();
			renderModelParts(model);
			GlStateManager.popMatrix();
		}

		protected abstract void renderModelParts(T model);
		protected abstract void setLimbsPose(T model, float yaw, float pitch);
		protected abstract Identifier getTexture();
	}

	private static abstract class HumanModel<S extends HumanoidModel> extends EntityModel<S> {

		@Override
		protected void renderModelParts(S model) {
			model.head.render(0.0625F);
			model.body.render(0.0625F);
			model.rightArm.render(0.0625F);
			model.leftArm.render(0.0625F);
			model.rightLeg.render(0.0625F);
			model.leftLeg.render(0.0625F);
			model.hat.render(0.0625F);
		}

		@Override
		protected void setLimbsPose(S model, float yaw, float pitch) {
			/* arms up */
			model.head.rotationY = yaw / (180.0F / (float)Math.PI);
			model.head.rotationX = pitch / (180.0F / (float)Math.PI);
			model.head.pivotY = 0.0F;
			model.body.rotationX = 0.0F;
			model.rightLeg.rotationX = 0.0F;
			model.rightLeg.rotationY = 0.0F;
			model.rightLeg.pivotY = 12.0F;
			model.rightLeg.pivotZ = 0.1F;
			model.leftLeg.rotationX = 0.0F;
			model.leftLeg.rotationY = 0.0F;
			model.leftLeg.pivotY = 12.0F;
			model.leftLeg.pivotZ = 0.1F;
			model.rightArm.rotationZ = 0.0F;
			model.leftArm.rotationZ = 0.0F;
			model.rightArm.rotationY = -0.1F;
			model.leftArm.rotationY = 0.1F;
			model.rightArm.rotationX = (float) (-Math.PI / 2);
			model.leftArm.rotationX = (float) (-Math.PI / 2);
			model.rightArm.rotationZ += 0.1F;
			model.leftArm.rotationZ -= 0.1F;
			model.rightArm.rotationX += 0.05F;
			model.leftArm.rotationX -= 0.05F;
		}
	}

	public static class Player<S extends PlayerModel> extends HumanModel<S> {

		@Override
		protected void setLimbsPose(S model, float yaw, float pitch) {
			/* arms by side */
			model.head.rotationY = yaw / (180.0F / (float)Math.PI);
			model.head.rotationX = pitch / (180.0F / (float)Math.PI);
			model.head.pivotY = 0.0F;
			model.body.rotationX = 0.0F;
			model.leftArm.rotationX = 0.0F;
			model.leftArm.rotationY = 0.0F;
			model.leftArm.rotationZ = 0.0F;
			/* holding item pose */
			model.rightArm.rotationX = model.rightArm.rotationX * 0.5F - (float) (Math.PI / 10);
			model.rightArm.rotationY = 0.0F;
			model.rightArm.rotationZ = 0.0F;
			model.rightLeg.rotationX = 0.0F;
			model.rightLeg.rotationY = 0.0F;
			model.rightLeg.pivotY = 12.0F;
			model.rightLeg.pivotZ = 0.1F;
			model.leftLeg.rotationX = 0.0F;
			model.leftLeg.rotationY = 0.0F;
			model.leftLeg.pivotY = 12.0F;
			model.leftLeg.pivotZ = 0.1F;
			model.rightArm.rotationZ = model.rightArm.rotationZ + 0.1F;
			model.leftArm.rotationZ = model.leftArm.rotationZ - 0.1F;
			model.rightArm.rotationX = model.rightArm.rotationX + 0.05F;
			model.leftArm.rotationX = model.leftArm.rotationX - 0.05F;
		}

		@Override
		protected Identifier getTexture() {
			GameProfile profile = minecraft.getSession().getProfile();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getTextures(profile);
			if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
				return minecraft.getSkinManager().register(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
			}
			return DefaultSkinUtils.getDefaultSkin(PlayerEntity.getUuid(profile));
		}
	}

	public static class Zombie<S extends ZombieModel> extends HumanModel<S> {

		@Override
		protected Identifier getTexture() {
			return new Identifier("textures/entity/zombie/zombie.png");
		}
	}

	public static class Skeleton<S extends SkeletonModel> extends HumanModel<S> {

		@Override
		protected Identifier getTexture() {
			return new Identifier("textures/entity/skeleton/skeleton.png");
		}
	}

	public static class Creeper<S extends CreeperModel> extends EntityModel<S> {
		@Override
		protected void renderModelParts(S model) {
			model.head.render(0.0625F);
			model.body.render(0.0625F);
			model.rightBackLeg.render(0.0625F);
			model.leftBackleg.render(0.0625F);
			model.rightFrontLeg.render(0.0625F);
			model.leftFrontLeg.render(0.0625F);
		}

		@Override
		protected void setLimbsPose(S model, float yaw, float pitch) {
			model.head.rotationY = yaw / (180.0F / (float)Math.PI);
			model.head.rotationX = pitch / (180.0F / (float)Math.PI);
			model.rightBackLeg.rotationX = 0.0F;
			model.leftBackleg.rotationX = 0.0F;
			model.rightFrontLeg.rotationX = 0.0F;
			model.leftFrontLeg.rotationX = 0.0F;
		}

		@Override
		protected Identifier getTexture() {
			return new Identifier("textures/entity/creeper/creeper.png");
		}
	}
}
