package net.ultrastudios.simplehardcorerespawn.config.objects;

import net.ultrastudios.lorelink.utils.config.IFallbackEnum;

public enum ParticleType implements IFallbackEnum<ParticleType> {
    STANDARD,
    TOTEM,
    SOUL,
    FANTASY;

    @Override
    public ParticleType getFallback() {
        return STANDARD;
    }
}
