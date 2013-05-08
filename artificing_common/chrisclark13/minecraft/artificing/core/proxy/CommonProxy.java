package chrisclark13.minecraft.artificing.core.proxy;

import chrisclark13.minecraft.artificing.lib.Strings;
import chrisclark13.minecraft.artificing.tileentity.TileArtificingTable;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileArtificingTable.class, Strings.TE_ARTIFICING_TABLE_NAME);
	}
}
