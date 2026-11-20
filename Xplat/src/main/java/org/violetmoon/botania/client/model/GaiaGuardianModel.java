/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.render.entity.GaiaGuardianRenderer;

// [VanillaCopy] PlayerModel, only the parts relevant to the Gaia Guardian
public class GaiaGuardianModel extends HumanoidModel<GaiaGuardianRenderer.State> {
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart leftPants;
	public final ModelPart rightPants;
	public final ModelPart jacket;
	private final boolean slim;

	public GaiaGuardianModel(ModelPart root, boolean slim) {
		super(root, RenderHelper::getGaiaNoiseDynamicLayer);
		this.leftSleeve = leftArm.getChild("left_sleeve");
		this.rightSleeve = rightArm.getChild("right_sleeve");
		this.leftPants = leftLeg.getChild("left_pants");
		this.rightPants = rightLeg.getChild("right_pants");
		this.jacket = body.getChild("jacket");
		this.slim = slim;
	}

	@Override
	public void setupAnim(GaiaGuardianRenderer.State state) {
		super.setupAnim(state);
		hat.visible = state.showHat;
		jacket.visible = state.showJacket;
		leftPants.visible = state.showLeftPants;
		rightPants.visible = state.showRightPants;
		leftSleeve.visible = state.showLeftSleeve;
		rightSleeve.visible = state.showRightSleeve;
	}

	@Override
	public void translateToHand(GaiaGuardianRenderer.State state, HumanoidArm side, PoseStack poseStack) {
		ModelPart modelpart = this.getArm(side);
		if (this.slim) {
			float f = 0.5F * (float) (side == HumanoidArm.RIGHT ? 1 : -1);
			modelpart.x += f;
			modelpart.translateAndRotate(poseStack);
			modelpart.x -= f;
		} else {
			modelpart.translateAndRotate(poseStack);
		}
	}

}
