package noppes.turtle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ContainerInventory extends Container {
	public EntityTurtle turtle;
	private int x, z;
	private EntityPlayer player;
	
	public ContainerInventory(final EntityTurtle turtle, EntityPlayer player){
		this.turtle = turtle;
		this.player = player;
		x = MathHelper.floor_double(player.posX);
		z = MathHelper.floor_double(player.posZ);

    	addSlotToContainer(new Slot(turtle.inventory, 0, 61, 8));

        for (int k = 0; k < 2; k++){
            for (int j1 = 0; j1 < 6; j1++){
            	addSlotToContainer(new Slot(turtle.inventory, j1 + k * 6 + 1, 61 + j1 * 18, 44 + k * 18));
            }
        	
        }
        
        for(int k = 0 ; k < 4; k++){
            final int i = k;
        	addSlotToContainer(new Slot(turtle.armor, 3 - k, 97 + k * 18, 8){
        		@Override
                public int getSlotStackLimit(){
                    return 1;
                }
                @Override
                public boolean isItemValid(ItemStack stack){
                    if (stack == null) return false;
                    return stack.getItem().isValidArmor(stack, i, turtle);
                }
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex(){
                    return ItemArmor.func_94602_b(i);
                }
            });
        }
		
        for (int k = 0; k < 3; k++){
            for (int j1 = 0; j1 < 9; j1++){
            	addSlotToContainer(new Slot(player.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 88 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++){
        	addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 146));
        }
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return x == MathHelper.floor_double(player.posX) && z == MathHelper.floor_double(player.posZ);
	}
	@Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer){
    	turtle.setHeldItem(turtle.inventory.getStackInSlot(0));
    }
	
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot == null || !slot.getHasStack())
        	return null;
        
        ItemStack itemstack1 = slot.getStack();
        ItemStack itemstack = itemstack1.copy();

        if (par2 < 13)
        {
            if (!this.mergeItemStack(itemstack1, 13, this.inventorySlots.size(), true))
            {
                return null;
            }
        }
        else if (!this.mergeItemStack(itemstack1, 1, 13, false))
        {
            return null;
        }

        if (itemstack1.stackSize == 0){
            slot.putStack((ItemStack)null);
        }
        else{
            slot.onSlotChanged();
        }
        

        return itemstack;
    }
}
