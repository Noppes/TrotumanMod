package noppes.turtle.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import noppes.turtle.EntityTurtle;

public class EntityAITurtleWander extends EntityAIWander{

	private EntityTurtle turtle;
    public EntityAITurtleWander(EntityTurtle turtle,
			double par2) {
		super(turtle, par2);
		this.turtle = turtle;
	}

	public boolean shouldExecute(){
		if(turtle.isSitting())
			return false;
    	return super.shouldExecute();
    }
}
