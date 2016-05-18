package noppes.turtle.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noppes.turtle.CommonProxy;
import noppes.turtle.ContainerInventory;
import noppes.turtle.EntityTurtle;

public class ClientProxy extends CommonProxy{

	@Override
	public void load(){
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, new RenderTurtle());
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		ContainerInventory container = (ContainerInventory) this.getServerGuiElement(ID, player, world, x, y, z);
		return new GuiInventory(container);
	}
}
