package noppes.turtle.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import noppes.turtle.ContainerInventory;
import noppes.turtle.EntityTurtle;

import org.lwjgl.opengl.GL11;

public class GuiInventory extends GuiContainer{
	
	private static final ResourceLocation location = new ResourceLocation("turtle","textures/follower.png");
	private EntityTurtle turtle;
    private float xSize_lo;
    private float ySize_lo;
	
	public GuiInventory(ContainerInventory container) {
		super(container);
		this.turtle = container.turtle;
		xSize = 176;
		ySize = 170;
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
    	drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(location);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft + 33, guiTop + 61, 50F);
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glColor4f(1, 1, 1, 1);
        float f2 = turtle.renderYawOffset;
        float f3 = turtle.rotationYaw;
        float f4 = turtle.rotationPitch;
        float f7 = turtle.rotationYawHead;
        float f5 = (float)(guiLeft + 33) - xSize_lo;
        float f6 = (float)((guiTop + 61) - 50) - ySize_lo;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        turtle.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        turtle.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        turtle.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        turtle.rotationYawHead = turtle.rotationYaw;
        GL11.glTranslatef(0.0F, turtle.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(turtle, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        turtle.renderYawOffset = f2;
        turtle.rotationYaw = f3;
        turtle.rotationPitch = f4;
        turtle.rotationYawHead = f7;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        fontRendererObj.drawString("Health: " + (int)turtle.getHealth() + "/" + (int)turtle.getMaxHealth(), guiLeft + 60, guiTop + 30, 0x404040);
    }
    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }
}
