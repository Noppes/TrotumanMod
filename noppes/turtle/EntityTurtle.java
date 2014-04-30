package noppes.turtle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.turtle.ai.EntityAITurtleAttackOnCollide;
import noppes.turtle.ai.EntityAITurtleWander;

public class EntityTurtle extends EntityTameable {
	
	private final static int MaxMiningTime = 300000;
	
	private int eatingTicks = 20;
	private long mineTimer = 0;
	private boolean wasSitting = false;
	public boolean finishedMining = false;
	private boolean wasMining = false;
	private final static HashMap<Item, Object[]> mined;
	
	static{
		mined = new HashMap<Item, Object[]>();
		mined.put(Items.wooden_pickaxe, new Object[]{Blocks.dirt, Blocks.stone, Blocks.gravel});
		mined.put(Items.stone_pickaxe, new Object[]{Blocks.stone, Items.quartz, Items.iron_ingot, Items.gold_ingot});
		mined.put(Items.golden_pickaxe, new Object[]{new ItemStack(Items.dye, 1, 4), Items.iron_ingot, Items.gold_ingot});
		mined.put(Items.iron_pickaxe, new Object[]{Items.quartz, Items.iron_ingot, Items.gold_ingot, Items.diamond});
		mined.put(Items.diamond_pickaxe, new Object[]{Blocks.quartz_block, new ItemStack(Items.dye, 1, 4), Items.diamond, Items.emerald});
	}

	public EntityTurtle(World par1World) {
		super(par1World);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(4, new EntityAITurtleAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 5.0F, 2.0F));
        this.tasks.addTask(7, new EntityAITurtleWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));

        this.setTamed(false);
        
        setSize(0.8f, 1.8f);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if(isMining())
			return false;
		Entity entity = damagesource.getEntity();
		if(entity instanceof EntityArrow)
			entity = ((EntityArrow)entity).shootingEntity;
		if(entity instanceof EntityPlayer)
			return false;
		return super.attackEntityFrom(damagesource, i);
	}

	@Override
    protected void updateAITasks(){
    	if(!isMining() && !finishedMining)
    		super.updateAITasks();
    	else if (finishedMining){
    		super.updateAITasks();
    		EntityPlayer owner = (EntityPlayer)getOwner();
    		double distance = owner.getDistanceToEntity(this);
    		System.out.println(distance);
    		if(distance < 14 && isSitting()){
    			setTurtleSitting(false);
    			getNavigator().tryMoveToEntityLiving(owner, 1);
    		}
    		if(owner != null && distance < 3){
    			finishedMining = false;
    			setTurtleSitting(false);
    			
    			Object[] items = mined.get(this.getHeldItem().getItem());
    			if(items == null)
    				items = mined.get(Items.golden_pickaxe);
    			List<ItemStack> list = new ArrayList<ItemStack>();
    			int looped = 0;
    			int size = items.length;
    			int maxWeight = size * size * 2;
    			while(looped++ < 12 || list.size() < 4){
    				int random = this.rand.nextInt(maxWeight);
	    			for(int i = 1; i <= size; i++){
	    				Object ob = items[size - i];
	    				if(random <= i * i){
	    					if(ob instanceof ItemStack)
	    						list.add((ItemStack) ob);
	    					else if(ob instanceof Block)
	    						list.add(new ItemStack((Block)ob));
	    					else
	    						list.add(new ItemStack((Item)ob));
	    					break;
	    				}
	    				
	    			}
    			}
    			ItemStack held = getHeldItem();
    			held.damageItem(rand.nextInt(held.getMaxDamage() /4) + held.getMaxDamage() / 5, this);
    			list.add(held);

    			setHeldItem(null);
    			for(ItemStack item : list){
    				float f = 0.7F;
    				double d = (double) (worldObj.rand.nextFloat() * f)
    						+ (double) (1.0F - f);
    				double d1 = (double) (worldObj.rand.nextFloat() * f)
    						+ (double) (1.0F - f);
    				double d2 = (double) (worldObj.rand.nextFloat() * f)
    						+ (double) (1.0F - f);
    				EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d1,
    						posZ + d2, item.copy());
    				entityitem.delayBeforeCanPickup = 0;
    				worldObj.spawnEntityInWorld(entityitem);
    				
    				entityitem.onCollideWithPlayer(owner);
    			}
    		}
    	}
    }

	@Override
    protected void entityInit(){
        super.entityInit();
        this.dataWatcher.addObjectByDataType(19, 5);
        this.dataWatcher.addObject(20, (byte)0);
    }
	
	@Override
    public ItemStack getHeldItem(){
		return dataWatcher.getWatchableObjectItemStack(19);
    }
    
    public void setHeldItem(ItemStack item){
    	dataWatcher.updateObject(19, item);
    }
    
    public boolean isMining(){
    	return dataWatcher.getWatchableObjectByte(20) == 1 || System.currentTimeMillis() - mineTimer < MaxMiningTime && !worldObj.isRemote;
    }
    
    public void setMining(boolean bo){
    	dataWatcher.updateObject(20, (byte)(bo?1:0));
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("FinishedMining", finishedMining);
        compound.setLong("MiningTimer", mineTimer);
        if(getHeldItem() != null)
        	compound.setTag("HeldItem",getHeldItem().writeToNBT(new NBTTagCompound()));
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        mineTimer = compound.getLong("MiningTimer");
        finishedMining = compound.getBoolean("FinishedMining");
        setHeldItem(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("HeldItem")));
    }

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);

		if (this.isTamed())
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		else 
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		
	}
	@Override
    public void onLivingUpdate(){
        this.updateArmSwingProgress();
        super.onLivingUpdate();
        
        if(isMining() && !worldObj.isRemote){
        	setMining(System.currentTimeMillis() - mineTimer < MaxMiningTime);
        	if(!isMining()){
        		finishedMining = true;
        	}
        }

        if(worldObj.isRemote && (isMining() && !wasMining || wasMining && !isMining())){
        	for(int i = 0; i < 4; i++)
        		spawnExplosionParticle();
        }
        
        ItemStack par1ItemStack = this.getHeldItem();
        if (par1ItemStack != null && par1ItemStack.getItemUseAction() == EnumAction.eat)
        {
        	if(worldObj.isRemote){
	            for (int j = 0; j < 2; ++j)
	            {
	                Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
	                vec3.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
	                vec3.rotateAroundY(-this.renderYawOffset * (float)Math.PI / 180.0F);
	                Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
	                vec31.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
	                vec31.rotateAroundY(-this.renderYawOffset * (float)Math.PI / 180.0F);
	                vec31 = vec31.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
	                String s = "iconcrack_" + Item.getIdFromItem(par1ItemStack.getItem());
	
	                if (par1ItemStack.getHasSubtypes())
	                {
	                    s = s + "_" + par1ItemStack.getItemDamage();
	                }
	
	                this.worldObj.spawnParticle(s, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.0D, vec3.zCoord);
	            }
        	}
        	else{
        		eatingTicks--;
        		if(!isSitting())
        			setTurtleSitting(true);
        		if(eatingTicks <= 0){
        			playSound("random.burp", 0.5F, this.rand.nextFloat() * 0.1F + 0.9F);
        			setHeldItem(null);
        			setTurtleSitting(wasSitting);
        			setHealth(40);
        		}
        		else if(eatingTicks % 6 == 0)
            		this.playSound("random.eat", 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        	}
        }
        wasMining = isMining();
    }
	@Override
    public void setTamed(boolean par1){
        super.setTamed(par1);

        if (par1)
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        else
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        
    }

    public void swingItem()
    {
    	super.swingItem();
    }
	@Override
    public boolean attackEntityAsMob(Entity par1Entity){
		this.swingItem();
        float i = 2;
		ItemStack item = getHeldItem();
		if(item != null && item.getItem() instanceof ItemSword){
			if(rand.nextBoolean())
				item.damageItem(1, this);
			i += ((ItemSword)item.getItem()).func_150931_i();
		}
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)i);
    }

	@Override
	public boolean isAIEnabled() {
		return true;
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable var1) {
		return null;
	}

	@Override
	public boolean isRiding(){
		return super.isRiding() || super.isSitting();
	}
	
	@Override
    public boolean interact(EntityPlayer player){
		if(isMining())
			return false;
        ItemStack itemstack = player.inventory.getCurrentItem();
        if(!isTamed()){
        	if(itemstack.getItem() == Items.fish){
                if (!player.capabilities.isCreativeMode)
                    --itemstack.stackSize;
                if (itemstack.stackSize <= 0)
                	player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                if (!this.worldObj.isRemote)
                {
                    if (this.rand.nextInt(3) == 0)
                    {
                        this.setTamed(true);
                        this.setPathToEntity((PathEntity)null);
                        this.setAttackTarget((EntityLivingBase)null);
                        this.aiSit.setSitting(true);
                        this.setHealth(20.0F);
                        this.setOwner(player.getCommandSenderName());
                        this.playTameEffect(true);
                        this.worldObj.setEntityState(this, (byte)7);
                    }
                    else
                    {
                        this.playTameEffect(false);
                        this.worldObj.setEntityState(this, (byte)6);
                    }
                }

                return true;
        	}
        }
        else if (player.getCommandSenderName().equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote){
        	if(itemstack != null && (itemstack.getItem() instanceof ItemSword || itemstack.getItem() instanceof ItemPickaxe || itemstack.getItem() == Items.fish)){
        		ItemStack item = getHeldItem();
        		
        		if(itemstack.stackSize > 1)
        			setHeldItem(itemstack.splitStack(1));
        		else{
        			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        			setHeldItem(itemstack);
        		}
        		if(item != null)
        			this.entityDropItem(item, 1);
        		if(itemstack.getItem() == Items.fish){
        			eatingTicks = 80;
        			wasSitting = isSitting();
        			if(!wasSitting)
        				setTurtleSitting(true);
        		}
        		if(itemstack.getItem() instanceof ItemPickaxe){
        			setTurtleSitting(true);
        			mineTimer = System.currentTimeMillis();
        		}
        	}
        	else{
        		setTurtleSitting(!this.isSitting());
        	}
        }

        return super.interact(player);
    }
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlock(i, j - 1, k) == Blocks.grass && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty();
    }
	@Override
    protected boolean canDespawn(){
        return !this.isTamed() && this.ticksExisted > 2400;
    }
    
	public void setTurtleSitting(boolean bo){
        this.aiSit.setSitting(bo);
        this.isJumping = false;
        this.setPathToEntity((PathEntity)null);
        this.setTarget((Entity)null);
        this.setAttackTarget((EntityLivingBase)null);
	}
}
