package net.minecraft.server;

import java.util.Random;

public class BlockObsidian extends Block {

    public BlockObsidian() {
        super(Material.STONE);
        this.a(CreativeModeTab.b);
    }

    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Item.getItemOf(Blocks.OBSIDIAN);
    }

    public MaterialMapColor c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return MaterialMapColor.F;
    }
}
