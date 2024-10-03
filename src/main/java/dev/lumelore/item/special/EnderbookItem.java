package dev.lumelore.item.special;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.lumelore.network.ModPayloads;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class EnderbookItem extends Item {

    public EnderbookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Return early on client
        if (world.isClient()) {
            return super.use(world, user, hand);
        }

        ItemStack itemStack = user.getStackInHand(hand);
        // Open GUI on client
        world.getServer().execute(() -> {
            ServerPlayNetworking.send(world.getServer().getPlayerManager().getPlayer(user.getUuid()), new ModPayloads.OpenEnderbookPayload(true));
        });
        // Increase use stat
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

}
