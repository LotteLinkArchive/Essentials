package com.Da_Technomancer.essentials.blocks;

import com.Da_Technomancer.essentials.EssentialsConfig;
import com.Da_Technomancer.essentials.tileentities.FluidShifterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FluidShifter extends BlockContainer{

	protected FluidShifter(){
		super(Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL));
		String name = "fluid_shifter";
		setRegistryName(name);
		EssentialsBlocks.toRegister.add(this);
		EssentialsBlocks.blockAddQue(this);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world){
		return new FluidShifterTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context){
		return getDefaultState().with(EssentialsProperties.FACING, context.getNearestLookingDirection());
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos pos, IBlockState blockstate){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof FluidShifterTileEntity){
			InventoryHelper.dropInventoryItems(world.getWorld(), pos, (FluidShifterTileEntity) te);
		}
		super.onPlayerDestroy(world, pos, blockstate);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		boolean isWrench = EssentialsConfig.isWrench(playerIn.getHeldItem(hand));
		if(!worldIn.isRemote){
			if(isWrench){
				worldIn.setBlockState(pos, state.cycle(EssentialsProperties.FACING));
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof FluidShifterTileEntity){
					((FluidShifterTileEntity) te).refreshCache();
				}
			}else{
				//TODO playerIn.openGui(Essentials.instance, EssentialsGuiHandler.FLUID_SHIFTER_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
		tooltip.add(new TextComponentString("Ejects contained fluid out the faced side into an attached inventory"));
		tooltip.add(new TextComponentString("Can 'push' items through a line of up to " + EssentialsConfig.itemChuteRange.get() + " Transport Chutes"));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder){
		builder.add(EssentialsProperties.FACING);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof FluidShifterTileEntity){
			((FluidShifterTileEntity) te).refreshCache();
		}
	}
}
