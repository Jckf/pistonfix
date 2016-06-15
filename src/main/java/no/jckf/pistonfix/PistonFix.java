package no.jckf.pistonfix;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PistonFix extends JavaPlugin {
    private final HashMap<String, HashMap<String, PistonState>> pistons = new HashMap<>();
    private PistonListener listener;

    @Override
    public void onEnable() {
        this.listener = new PistonListener(this);

        for (World world : this.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                this.loadChunk(chunk);
            }
        }
    }

    void loadChunk(Chunk chunk) {
        this.pistons.put(this.chunkId(chunk), new HashMap<String, PistonState>());
    }

    void unloadChunk(Chunk chunk) {
        this.pistons.remove(this.chunkId(chunk));
    }

    void setPistonState(Block block, PistonState state) {
        this.pistons.get(this.chunkId(block.getChunk())).put(this.blockId(block), state);
    }

    PistonState getPistonState(Block block) {
        if (!this.pistons.get(this.chunkId(block.getChunk())).containsKey(this.blockId(block)))
            return PistonState.NEW;

        return this.pistons.get(this.chunkId(block.getChunk())).get(this.blockId(block));
    }

    private String chunkId(Chunk chunk) {
        return chunk.getWorld().getName() + "x" + chunk.getX() + "x" + chunk.getZ();
    }

    private String blockId(Block block) {
        return block.getX() + "x" + block.getY() + "x" + block.getY();
    }
}
