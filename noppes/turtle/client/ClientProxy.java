package noppes.turtle.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import noppes.turtle.CommonProxy;
import noppes.turtle.EntityTurtle;

public class ClientProxy extends CommonProxy{

	@Override
	public void load(){
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, new RenderTurtle());
	}
	
}
