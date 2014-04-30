package noppes.turtle.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import noppes.turtle.EntityTurtle;

public class EntityAITurtleAttackOnCollide extends EntityAIAttackOnCollide{

	private EntityTurtle turtle;
    public EntityAITurtleAttackOnCollide(EntityTurtle turtle,
			double par2, boolean par4) {
		super(turtle, par2, par4);
		this.turtle = turtle;
	}

	public boolean shouldExecute(){
		ItemStack item = turtle.getHeldItem();
		if(item == null || turtle.isSitting() || !(item.getItem() instanceof ItemSword))
			return false;
    	return super.shouldExecute();
    }
}
