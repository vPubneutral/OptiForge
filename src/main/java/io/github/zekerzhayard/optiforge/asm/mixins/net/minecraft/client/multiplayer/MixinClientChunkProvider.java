package io.github.zekerzhayard.optiforge.asm.mixins.net.minecraft.client.multiplayer;

import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkProvider.class)
public abstract class MixinClientChunkProvider {
    @Redirect(
        method = "Lnet/minecraft/client/multiplayer/ClientChunkProvider;getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/optifine/reflect/ReflectorConstructor;exists()Z",
            remap = false
        ),
        require = 1,
        allow = 1
    )
    private boolean redirect$getChunk$0(@Coerce Object constructor) {
        return false;
    }

    @Inject(
        method = "Lnet/minecraft/client/multiplayer/ClientChunkProvider;updateChunkFromPacket(Lnet/minecraft/world/World;IILnet/minecraft/network/PacketBuffer;Lnet/minecraft/nbt/CompoundNBT;IZ)Lnet/minecraft/world/chunk/Chunk;",
        at = @At("TAIL"),
        require = 1,
        allow = 1
    )
    private void inject$updateChunkFromPacket$0(CallbackInfoReturnable<Chunk> cir) {
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load(cir.getReturnValue()));
    }
}
