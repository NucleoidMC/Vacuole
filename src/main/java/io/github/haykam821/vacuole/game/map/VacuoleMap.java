package io.github.haykam821.vacuole.game.map;

import java.util.Iterator;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import xyz.nucleoid.map_templates.BlockBounds;
import xyz.nucleoid.map_templates.MapTemplate;
import xyz.nucleoid.map_templates.TemplateRegion;
import xyz.nucleoid.plasmid.game.world.generator.TemplateChunkGenerator;

public class VacuoleMap {
	private final MapTemplate template;
	private final Box box;

	public VacuoleMap(MapTemplate template) {
		this.template = template;
		this.box = this.template.getBounds().asBox().expand(10);
	}

	public MapTemplate getTemplate() {
		return this.template;
	}

	public Box getBox() {
		return this.box;
	}

	public Vec3d getSpawn() {
		TemplateRegion spawn = this.template.getMetadata().getFirstRegion("spawn");
		if (spawn != null) {
			return VacuoleMap.getBottomCenter(spawn.getBounds());
		}

		return VacuoleMap.getBottomCenter(this.template.getBounds());
	}

	public Vec2f getSpawnRotation() {
		TemplateRegion spawn = this.template.getMetadata().getFirstRegion("spawn");
		if (spawn != null) {
			NbtList tag = spawn.getData().getList("Rotation", NbtElement.FLOAT_TYPE);
			return  new Vec2f(tag.getFloat(0), tag.getFloat(1));
		}

		return new Vec2f(0, 0);
	}

	public Iterator<TemplateRegion> getTreasureRegions() {
		return this.template.getMetadata().getRegions("treasure").iterator();
	}

	public Iterator<TemplateRegion> getTreasureSelectorRegions() {
		return this.template.getMetadata().getRegions("treasure_selector").iterator();
	}

	public ChunkGenerator createGenerator(MinecraftServer server) {
		return new TemplateChunkGenerator(server, this.template);
	}

	private static Vec3d getBottomCenter(BlockBounds bounds) {
		Vec3d center = bounds.center();
		return new Vec3d(center.getX(), bounds.min().getY(), center.getZ());
	}
}
