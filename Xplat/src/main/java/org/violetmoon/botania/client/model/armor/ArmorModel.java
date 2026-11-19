/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.Rotations;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorModel extends HumanoidModel<HumanoidRenderState> {
	private final EquipmentSlot slot;

	public ArmorModel(ModelPart root, EquipmentSlot slot) {
		super(root);
		this.slot = slot;
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		super.setupAnim(state);
		if (state instanceof ArmorStandRenderState stand) {
			rotate(head, stand.headPose);
			head.setPos(0, 1, 0);
			rotate(body, stand.bodyPose);
			rotate(leftArm, stand.leftArmPose);
			rotate(rightArm, stand.rightArmPose);
			rotate(leftLeg, stand.leftLegPose);
			leftLeg.setPos(1.9F, 11, 0);
			rotate(rightLeg, stand.rightLegPose);
			rightLeg.setPos(-1.9F, 11, 0);
		}

		head.visible = slot == EquipmentSlot.HEAD;
		hat.visible = head.visible;
		body.visible = slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS;
		leftArm.visible = rightArm.visible = slot == EquipmentSlot.CHEST;
		leftLeg.visible = rightLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
	}

	private static void rotate(ModelPart part, Rotations rotations) {
		part.setRotation(rotations.x() * Mth.DEG_TO_RAD, rotations.y() * Mth.DEG_TO_RAD, rotations.z() * Mth.DEG_TO_RAD);
	}
}
