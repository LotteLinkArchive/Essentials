package com.Da_Technomancer.essentials.gui;

import com.Da_Technomancer.essentials.Essentials;
import com.google.common.collect.BiMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class CrudeFluidBar{

	private static BiMap<Fluid, Integer> fluidKeys;

	/**
	 * Encodes a fluid stack into two shorts for syncing via fields
	 * @param stack The fluidstack to be encoded in two shorts
	 * @return A size two array containing the type short and the amount short in that order
	 */
	public static short[] fluidToPacket(@Nullable  FluidStack stack){
		return new short[2];//TODO

		/*

		if(fluidKeys == null || fluidKeys.size() != FluidRegistry.getMaxID()){
			fluidKeys = HashBiMap.create(FluidRegistry.getRegisteredFluidIDs());//Ok, so getRegisteredFluidIDs may possibly be kinda deprecated, but let's be honest here: It's never going to be removed. Also this is more efficient/easier than string ids for syncing
		}

		if(stack == null || stack.amount == 0){
			return new short[2];
		}

		Integer fluidKey = fluidKeys.get(stack.getFluid());

		return fluidKey == null ? new short[2] : new short[] {fluidKey.shortValue(), (short) (stack.amount - Short.MAX_VALUE)};
		*/
	}

	@Nullable
	private static FluidStack packetToFluid(short packetType, short packetAmount){
		return null;//TODO
		/*if(fluidKeys == null || fluidKeys.size() != FluidRegistry.getMaxID()){
			fluidKeys = HashBiMap.create(FluidRegistry.getRegisteredFluidIDs());//Ok, so getRegisteredFluidIDs may possibly be kinda deprecated, but let's be honest here: It's never going to be removed. Also this is more efficient/easier than string ids for syncing
		}

		if(packetType == 0 || packetAmount == -Short.MAX_VALUE){
			return null;
		}

		Fluid f = fluidKeys.inverse().get((int) packetType);
		if(f == null){
			return null;
		}

		return new FluidStack(f, packetAmount + Short.MAX_VALUE);*/
	}

	private static final int MAX_HEIGHT = 48;
	private static final ResourceLocation OVERLAY = new ResourceLocation(Essentials.MODID, "textures/gui/rectangle_fluid_overlay.png");
	private final FluidShifterGuiContainer gui;
	private final int fieldIndex0;
	private final int fieldIndex1;
	private final int capacity;
	private final int x;
	private final int y;
	private final int windowX;
	private final int windowY;


	public CrudeFluidBar(FluidShifterGuiContainer gui, int fieldIndex0, int fieldIndex1, int capacity, int windowX, int windowY, int x, int y){
		this.gui = gui;
		this.fieldIndex0 = fieldIndex0;
		this.fieldIndex1 = fieldIndex1;
		this.capacity = capacity;
		this.x = x;
		this.y = y;
		this.windowX = windowX;
		this.windowY = windowY;
	}

	public boolean drawBack(float partialTicks, int mouseX, int mouseY, FontRenderer fontRenderer){
		Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Gui.drawRect(x + windowX, y + windowY - MAX_HEIGHT, x + windowX + 16, y + windowY, 0xFF959595);
		GlStateManager.color4f(1, 1, 1, 1);

		FluidStack fluid = packetToFluid((short) gui.te.getField(fieldIndex0), (short) gui.te.getField(fieldIndex1));
		if(fluid == null){
			return true;
		}

		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(fluid.getFluid().getStill().toString());
		//Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		int col = fluid.getFluid().getColor(fluid);
		int height = (int) (MAX_HEIGHT * (float) fluid.amount / (float) capacity);
		GlStateManager.color4f((float) ((col >>> 16) & 0xFF) / 255F, ((float) ((col >>> 8) & 0xFF)) / 255F, ((float) (col & 0xFF)) / 255F, ((float) ((col >>> 24) & 0xFF)) / 255F);
		gui.drawTexturedModalRect(x + windowX, y + windowY - height, sprite, 16, height);
		GlStateManager.color3f(1, 1, 1);
		return true;
	}

	public boolean drawFore(int mouseX, int mouseY, FontRenderer fontRenderer){
		Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY);
		Gui.drawModalRectWithCustomSizedTexture(x, y - MAX_HEIGHT, 0, 0, 16, MAX_HEIGHT, 16, MAX_HEIGHT);

		if(mouseX >= x + windowX && mouseX <= x + windowX + 16 && mouseY >= y + windowY - MAX_HEIGHT && mouseY <= y + windowY){
			FluidStack fluid = packetToFluid((short) gui.te.getField(fieldIndex0), (short) gui.te.getField(fieldIndex1));
			if(fluid == null){
				gui.tooltip.add("Empty");
			}else{
				gui.tooltip.add(fluid.getLocalizedName());
				gui.tooltip.add(fluid.amount + "/" + capacity);
			}
		}
		return true;
	}
}
