package com.Da_Technomancer.essentials.blocks;

import com.Da_Technomancer.essentials.EssentialsConfig;
import com.Da_Technomancer.essentials.tileentities.ItemShifterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemShifter extends BlockContainer{

	protected ItemShifter(){
		super(Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL));
		String name = "item_shifter";
		setRegistryName(name);
		EssentialsBlocks.toRegister.add(this);
		EssentialsBlocks.blockAddQue(this);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world){
		return new ItemShifterTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context){
		return getDefaultState().with(EssentialsProperties.FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof ItemShifterTileEntity) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (ItemShifterTileEntity) te);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!worldIn.isRemote){
			TileEntity te = worldIn.getTileEntity(pos);
			if(EssentialsConfig.isWrench(playerIn.getHeldItem(hand))){
				worldIn.setBlockState(pos, state.cycle(EssentialsProperties.FACING));
				if(te instanceof ItemShifterTileEntity){
					((ItemShifterTileEntity) te).refreshCache();
				}
			}else if(te instanceof ItemShifterTileEntity){
				NetworkHooks.openGui((EntityPlayerMP) playerIn, (ItemShifterTileEntity) te, pos);
			}
		}
		return true;
	}

	@Override
	public IBlockState rotate(IBlockState state, Rotation rot){
		return state.with(EssentialsProperties.FACING, rot.rotate(state.get(EssentialsProperties.FACING)));
	}

	@Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn){
		return state.rotate(mirrorIn.toRotation(state.get(EssentialsProperties.FACING)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		tooltip.add(new TextComponentString("Ejects contained items out the faced side, moved into inventories or dropped onto the ground"));
		tooltip.add(new TextComponentString("Can 'push' items through a line of up to " + EssentialsConfig.itemChuteRange.get() + " Transport Chutes"));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder){
		builder.add(EssentialsProperties.FACING);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof ItemShifterTileEntity){
			((ItemShifterTileEntity) te).refreshCache();
		}
	}
}
