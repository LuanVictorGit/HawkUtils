package me.hawkutils.utils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkUtil {

    private static final Random RANDOM = new Random();

    public static void launchRandomFirework(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        // Cria o foguete
        Firework firework = world.spawn(location, Firework.class);

        // Configura o meta do foguete
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // Adiciona um efeito aleatório
        fireworkMeta.addEffect(createRandomFireworkEffect());

        // Define a duração aleatória do voo
        fireworkMeta.setPower(1); // 1 ou 2 de potência

        // Aplica o meta ao foguete
        firework.setFireworkMeta(fireworkMeta);
    }

    private static FireworkEffect createRandomFireworkEffect() {
        // Escolhe uma cor aleatória
        Color color1 = getRandomColor();
        Color color2 = getRandomColor();

        // Escolhe um formato aleatório
        FireworkEffect.Type type = FireworkEffect.Type.values()[RANDOM.nextInt(FireworkEffect.Type.values().length)];

        // Cria o efeito do foguete
        return FireworkEffect.builder()
                .withColor(color1)
                .withFade(color2)
                .with(type)
                .flicker(RANDOM.nextBoolean()) // Efeito de cintilação
                .trail(RANDOM.nextBoolean())  // Efeito de rastro
                .build();
    }

    private static Color getRandomColor() {
        return Color.fromRGB(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
    }
}