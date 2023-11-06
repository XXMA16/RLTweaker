package com.charles445.rltweaker.asm.patch;

import java.util.NoSuchElementException;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.charles445.rltweaker.asm.util.ASMUtil;
import com.charles445.rltweaker.asm.util.TransformUtil;

public class PatchSRPAI extends PatchManager {

	public PatchSRPAI() {
		this.add(new Patch(this, "com.dhanantry.scapeandrunparasites.entity.ai.EntityAINearestAttackableTargetStatus", ClassWriter.COMPUTE_FRAMES) {

			@Override
			public void patch(ClassNode clazzNode) {
				MethodNode m_getNearest = this.findMethod(clazzNode, "getNearest");

				try {
					MethodInsnNode invoke_isSpectator = ASMUtil.findMethodInsn(m_getNearest, Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "func_175149_v", "isSpectator", "()Z", 0);
					JumpInsnNode target1 = ASMUtil.findInsn(m_getNearest, JumpInsnNode.class, insn -> m_getNearest.instructions.indexOf(insn) > m_getNearest.instructions.indexOf(invoke_isSpectator), 0);
					m_getNearest.instructions.insert(target1, ASMUtil.listOf(
							// if (entityplayer2.getDistanceSq(posX, posY, posZ) < maxXZDistance)
							new VarInsnNode(Opcodes.ALOAD, 16),
							new VarInsnNode(Opcodes.DLOAD, 1),
							new VarInsnNode(Opcodes.DLOAD, 3),
							new VarInsnNode(Opcodes.DLOAD, 5),
							TransformUtil.createObfMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "func_70092_e", "(DDD)D", false),
							new VarInsnNode(Opcodes.DLOAD, 7),
							new InsnNode(Opcodes.DCMPL),
							new JumpInsnNode(Opcodes.IFGE, target1.label)
					));
				} catch (NoSuchElementException e) {
					// ignore
				}

				try {
					VarInsnNode aload_maxYDistance = ASMUtil.findInsn(m_getNearest, VarInsnNode.class,
							insn -> insn.getOpcode() == Opcodes.DLOAD && insn.var == 9, 0);
					aload_maxYDistance.var = 7;
				} catch (NoSuchElementException e) {
					// ignore
				}

				try {
					VarInsnNode aload_maxYDistance = ASMUtil.findInsn(m_getNearest, VarInsnNode.class,
							insn -> insn.getOpcode() == Opcodes.DLOAD && insn.var == 9, 2);
					m_getNearest.instructions.set(aload_maxYDistance, new InsnNode(Opcodes.DCONST_1));
				} catch (NoSuchElementException e) {
					// ignore
				}
			}

		});
		this.add(new Patch(this, "com.dhanantry.scapeandrunparasites.entity.ai.EntityAIInfectedSearch", ClassWriter.COMPUTE_FRAMES) {

			@Override
			public void patch(ClassNode clazzNode) {
				MethodNode m_LeshCount = this.findMethod(clazzNode, "LeshCount");

				try {
					MethodInsnNode invoke_getTimes = ASMUtil.findMethodInsn(m_LeshCount, Opcodes.INVOKEVIRTUAL, "com/dhanantry/scapeandrunparasites/entity/monster/crude/EntityLesh", "getTimes", "()I", 0);
					JumpInsnNode target1 = ASMUtil.findInsn(m_LeshCount, JumpInsnNode.class, insn -> m_LeshCount.instructions.indexOf(insn) > m_LeshCount.instructions.indexOf(invoke_getTimes), 0);
					m_LeshCount.instructions.insert(target1, ASMUtil.listOf(
							// if (mob instanceof EntityCanMelt)
							new VarInsnNode(Opcodes.ALOAD, 7),
							new TypeInsnNode(Opcodes.INSTANCEOF, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanMelt"),
							new JumpInsnNode(Opcodes.IFEQ, target1.label),
							// if (!((EntityCanMelt) mob).isMelting())
							new VarInsnNode(Opcodes.ALOAD, 7),
							new TypeInsnNode(Opcodes.CHECKCAST, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanMelt"),
							new MethodInsnNode(Opcodes.INVOKEINTERFACE, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityCanMelt", "isMelting", "()Z", true),
							new JumpInsnNode(Opcodes.IFNE, target1.label),
							// if (mob.getAttackTarget() == null)
							new VarInsnNode(Opcodes.ALOAD, 7),
							new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase", "getAttackTarget", "()Lnet/minecraft/entity/EntityLivingBase;", false),
							new JumpInsnNode(Opcodes.IFNONNULL, target1.label),
							// if (this.parent.canEntityBeSeen(mob))
							new VarInsnNode(Opcodes.ALOAD, 0),
							new FieldInsnNode(Opcodes.GETFIELD, "com/dhanantry/scapeandrunparasites/entity/ai/EntityAIInfectedSearch", "parent", "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;"),
							new VarInsnNode(Opcodes.ALOAD, 7),
							new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase", "canEntityBeSeen", "(Lnet/minecraft/entity/Entity;)Z", false),
							new JumpInsnNode(Opcodes.IFEQ, target1.label),
							// countI++;
							new IincInsnNode(2, 1),
							// continue;
							new JumpInsnNode(Opcodes.GOTO, target1.label)
					));
				} catch (NoSuchElementException e) {
					// ignore
				}
			}

		});
		this.add(new Patch(this, "com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected", ClassWriter.COMPUTE_FRAMES) {

			@Override
			public void patch(ClassNode clazzNode) {
				MethodNode m_onLivingUpdate = this.findMethod(clazzNode, "func_70636_d", "onLivingUpdate");

				try {
					FieldInsnNode get_primitiveKills1 = ASMUtil.findFieldInsn(m_onLivingUpdate, Opcodes.GETSTATIC, "com/dhanantry/scapeandrunparasites/util/config/SRPConfig", "primitiveKills", "D", 0);
					JumpInsnNode target1 = ASMUtil.findInsn(m_onLivingUpdate, JumpInsnNode.class, insn -> m_onLivingUpdate.instructions.indexOf(insn) > m_onLivingUpdate.instructions.indexOf(get_primitiveKills1), 0);
					LabelNode label1 = new LabelNode();
					m_onLivingUpdate.instructions.insert(target1, ASMUtil.listOf(
							// if (!HookSRP.hasNoInfectedSearchAI(this)) {
							//     this.killcount = 0.0D;
							// } else {
							new VarInsnNode(Opcodes.ALOAD, 0),
							new MethodInsnNode(Opcodes.INVOKESTATIC, "com/charles445/rltweaker/hook/HookSRP", "hasNoInfectedSearchAI", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false),
							new JumpInsnNode(Opcodes.IFNE, label1),
							new VarInsnNode(Opcodes.ALOAD, 0),
							new InsnNode(Opcodes.DCONST_0),
							new FieldInsnNode(Opcodes.PUTFIELD, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPInfected", "killcount", "D"),
							new JumpInsnNode(Opcodes.GOTO, target1.label),
							label1
					));
				} catch (NoSuchElementException e) {
					// ignore
				}

				MethodNode m_onKillEntity = this.findMethod(clazzNode, "func_70074_a", "onKillEntity");

				try {
					FieldInsnNode get_primitiveKills2 = ASMUtil.findFieldInsn(m_onKillEntity, Opcodes.GETSTATIC, "com/dhanantry/scapeandrunparasites/util/config/SRPConfig", "primitiveKills", "D", 0);
					JumpInsnNode target2 = ASMUtil.findInsn(m_onKillEntity, JumpInsnNode.class, insn -> m_onKillEntity.instructions.indexOf(insn) > m_onKillEntity.instructions.indexOf(get_primitiveKills2), 0);
					LabelNode label2 = new LabelNode();
					m_onKillEntity.instructions.insert(target2, ASMUtil.listOf(
							// if (!HookSRP.hasNoInfectedSearchAI(this)) {
							//     this.killcount = 0.0D;
							// } else {
							new VarInsnNode(Opcodes.ALOAD, 0),
							new MethodInsnNode(Opcodes.INVOKESTATIC, "com/charles445/rltweaker/hook/HookSRP", "hasNoInfectedSearchAI", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false),
							new JumpInsnNode(Opcodes.IFNE, label2),
							new VarInsnNode(Opcodes.ALOAD, 0),
							new InsnNode(Opcodes.DCONST_0),
							new FieldInsnNode(Opcodes.PUTFIELD, "com/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPInfected", "killcount", "D"),
							new JumpInsnNode(Opcodes.GOTO, target2.label),
							label2
					));
				} catch (NoSuchElementException e) {
					// ignore
				}
			}

		});
	}

}
