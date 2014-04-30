package noppes.turtle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = Turtle.MODID, version = Turtle.VERSION, name="Trotuman")
public class Turtle {
	public static final String MODID = "turtle";
	public static final String VERSION = "1.7.2";

    @SidedProxy(clientSide = "noppes.turtle.client.ClientProxy", serverSide = "noppes.turtle.CommonProxy")
    public static CommonProxy proxy;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		registerEntity(EntityTurtle.class, "TROTUMAN");
        EntityRegistry.addSpawn(EntityTurtle.class, 3, 1, 1, EnumCreatureType.creature, new BiomeGenBase[]{BiomeGenBase.swampland});
        EntityRegistry.addSpawn(EntityTurtle.class, 6, 1, 1, EnumCreatureType.creature, new BiomeGenBase[]{BiomeGenBase.river});
		ModMetadata data;
		proxy.load();
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name){
		int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(cl, name, id, 0x00F80E, 0x009B0E);
        EntityRegistry.registerModEntity(cl, name, id, this, 80, 3, true);
	}
}
