package io.github.haykam821.vacuole;

import io.github.haykam821.vacuole.game.VacuoleConfig;
import io.github.haykam821.vacuole.game.VacuoleGame;
import io.github.haykam821.vacuole.treasure.TreasureTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;

public class Main implements ModInitializer {
	public static final String MOD_ID = "vacuole";

	private static final Identifier VACUOLE_ID = new Identifier(MOD_ID, "vacuole");
	public static final GameType<VacuoleConfig> VACUOLE_TYPE = GameType.register(VACUOLE_ID, VacuoleGame::open, VacuoleConfig.CODEC);

	@Override
	public void onInitialize() {
		TreasureTypes.initialize();
	}
}
