package io.github.haykam821.vacuole.treasure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class MinefieldTreasure extends Treasure {
	public static final Codec<MinefieldTreasure> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			BlockStateProvider.TYPE_CODEC.optionalFieldOf("floor_state_provider", BlockStateProvider.of(Blocks.TNT)).forGetter(treasure -> treasure.floorStateProvider),
			BlockStateProvider.TYPE_CODEC.optionalFieldOf("mine_state_provider", BlockStateProvider.of(Blocks.STONE_PRESSURE_PLATE)).forGetter(treasure -> treasure.mineStateProvider),
			Codec.DOUBLE.optionalFieldOf("mine_chance", 0.6).forGetter(treasure -> treasure.mineChance)
		).apply(instance, MinefieldTreasure::new);
	});

	private static final Random RANDOM = Random.create();
	private static final BlockState BARRIER = Blocks.BARRIER.getDefaultState();
	private static final BlockState AIR = Blocks.AIR.getDefaultState();

	private final BlockStateProvider floorStateProvider;
	private final BlockStateProvider mineStateProvider;
	private final double mineChance;

	public MinefieldTreasure(BlockStateProvider floorStateProvider, BlockStateProvider mineStateProvider, double mineChance) {
		super(TreasureTypes.MINEFIELD);

		this.floorStateProvider = floorStateProvider;
		this.mineStateProvider = mineStateProvider;
		this.mineChance = mineChance;
	}

	public MinefieldTreasure() {
		this(BlockStateProvider.of(Blocks.TNT), BlockStateProvider.of(Blocks.STONE_PRESSURE_PLATE), 0.6);
	}

	@Override
	protected BlockState getBase(BlockPos pos) {
		return this.floorStateProvider.get(RANDOM, pos);
	}

	@Override
	public void build() {
		super.build();

		BlockPos.Mutable pos = this.canvas.getMin().mutableCopy().move(Direction.UP);
		for (int x = this.canvas.getMin().getX(); x <= this.canvas.getMax().getX(); x++) {
			pos.setX(x);
			for (int z = this.canvas.getMin().getZ(); z <= this.canvas.getMax().getZ(); z++) {
				if (this.mineChance < RANDOM.nextDouble()) {
					pos.setZ(z);
					this.canvas.setBlockState(pos, this.mineStateProvider.get(RANDOM, pos));
				}
			}
		}
	}

	@Override
	public void tick() {
		for (TntEntity tnt : this.canvas.getEntitiesByType(EntityType.TNT)) {
			if (tnt.getFuse() <= 5) {
				tnt.discard();
			} else if (tnt.getFuse() == 79) {
				this.canvas.setBlockState(tnt.getBlockPos(), BARRIER);
				this.canvas.setBlockState(tnt.getBlockPos().up(), AIR);
			}
		}
	}
}
