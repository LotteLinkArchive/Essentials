package com.Da_Technomancer.essentials.blocks;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.EnumFacing;

public class EssentialsProperties{

	public static final DirectionProperty FACING = DirectionProperty.create("facing", EnumFacing.values());
	//0: Empty, 1: water, 2: lava, 3: coal, 4: glowstone, 5: reserved, 6: soul sand, 7: poison potato
	public static final IntegerProperty BRAZIER_CONTENTS = IntegerProperty.create("br_cont", 0, 7);
	public static final BooleanProperty REDSTONE_BOOL = BooleanProperty.create("redstone_bool");//TODO check for use
	public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
	//0: No head, 1: Head in positive direction, 2: Head in negative direction
	public static final IntegerProperty HEAD = IntegerProperty.create("head", 0, 2);
	public static final EnumProperty<EnumFacing.Axis> AXIS = EnumProperty.create("axis", EnumFacing.Axis.class);
}
