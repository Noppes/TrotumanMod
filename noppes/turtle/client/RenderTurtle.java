package noppes.turtle.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.turtle.EntityTurtle;

public class RenderTurtle extends RenderBiped{
	
	private static final ResourceLocation resource = new ResourceLocation("turtle:textures/TROTUMAN.png");

	public RenderTurtle() {
		super(new ModelBiped(), 0);
	}

	@Override
    protected void preRenderCallback(EntityLivingBase entity, float par2){
        GL11.glScalef(0.6f, 0.6f, 0.6f);
    }
    
	@Override
    protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving){
        return resource;
    }

	@Override
    public void doRender(Entity entityliving, double d, double d1, double d2, float f, float f1){
    	EntityTurtle turtle = (EntityTurtle) entityliving;    	
    	if(turtle.isMining())
    		return;
    	if(turtle.isSitting())
    		d1 -= 0.3f;
    	super.doRender(entityliving, d, d1, d2, f, f1);
	}
}
