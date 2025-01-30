package me.hawkutils.utils;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;


public class API {

	public static void sendActionBar(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        p.getHandle().playerConnection.sendPacket(ppoc);
	}
	
	@SuppressWarnings("deprecation")
	public static void sendTitle(Player player, String... messages) {
		if (messages == null) return;
		if (messages.length > 1) {
			player.sendTitle(messages[0].replace("&", "§"), messages[1].replace("&", "§"));
		} else {
			player.sendTitle(messages[0].replace("&", "§"), new String());
		}
	}
	
	public static ItemStack getItemStack(String linha) {
		if (linha.equalsIgnoreCase("player"))
			return new ItemStack(Material.STONE);
		try {
			String[] args = linha.split(">");
			Item item = new Item(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
			return item.build();
		} catch (Exception e) {
			return SkullCreator.itemFromBase64(linha);
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean isEqualsItemStack(ItemStack item, String line) {
		String[] args = line.split(">");
		int ID = Integer.valueOf(args[0]);
		float DATA = Float.valueOf(args[1]);
		if (item != null && item.getTypeId() == ID && item.getData().getData() == DATA) return true;
		return false;
	}
	
	public static void pushPlayerBack(Player player, double force) {
        Vector direction = player.getLocation().getDirection();
        Vector knockback = direction.multiply(-force);
        player.setVelocity(knockback);
    }
	
	public static String serialize(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
	}
	
	public static Location unserialize(String line) {
		String[] args = line.split(":");
		World world = Bukkit.getWorld(args[0]);
		double x = Double.valueOf(args[1]);
		double y = Double.valueOf(args[2]);
		double z = Double.valueOf(args[3]);
		float yaw = Float.valueOf(args[4]);
		float pitch = Float.valueOf(args[5]);
		return new Location(world,x,y,z,yaw,pitch);
	}

	public static boolean isBoolean(String txt) {
		try {
			Boolean.valueOf(txt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInteger(String txt) {
		try {
			Integer.valueOf(txt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String writeItemStack(ItemStack... items) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

			dataOutput.writeInt(items.length);

			for (ItemStack item : items)
				dataOutput.writeObject(item);

			return Base64Coder.encodeLines(outputStream.toByteArray());

		} catch (Exception ignored) {
			return "";
		}
	}

	public static ItemStack[] readItemStack(String source) {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(source));
				BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
			ItemStack[] items = new ItemStack[dataInput.readInt()];
			for (int i = 0; i < items.length; i++)
				items[i] = (ItemStack) dataInput.readObject();
			return items;
		} catch (Exception ignored) {
			return new ItemStack[0];
		}
	}

	public static String formatValueLetters(double numero) {
		if (numero == 0)
			return String.valueOf(numero);
		String[] sufixos = { "", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TD", "QT", "QN",
				"SX", "SP", "O", "N", "V", "U", "D", "T", "QT", "QN", "SX", "SP", "O", "N", "C", "U", "D", "T", "QT",
				"QN", "SX", "SP", "O", "N", "V", "U", "D", "T", "QT", "QN", "SX", "SP", "O", "N", "C", "U", "D", "T",
				"QT", "QN", "SX", "SP", "O", "N", "D", "U", "DD", "TD", "QT", "QN", "SX", "SP", "O", "N", "V", "U", "D",
				"T", "QT", "QN", "SX", "SP", "O", "N", "C", "U", "D", "T", "QT", "QN", "SX", "SP", "O", "N", "V", "U",
				"D", "T", "QT", "QN", "SX", "SP", "O", "N", "C", "U", "D", "T", "QT", "QN", "SX", "SP", "O", "N" };
		int exp = (int) (Math.log10(numero) / 3);
		if (exp >= sufixos.length) {
			exp = sufixos.length - 1;
		}
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		DecimalFormat formatter = new DecimalFormat("#,##0.#", symbols);
		double valorAbreviado = numero / Math.pow(10, exp * 3);
		String valorFormatado = formatter.format(valorAbreviado);
		return valorFormatado + sufixos[exp];
	}

	public static double formatValue(double valor) {
		NumberFormat formatter = new DecimalFormat("0.00");
		return Double.valueOf(formatter.format(valor).replace(",", "."));
	}

	public static String formatTime(int sg) {
		long numero = sg;
		Duration duracao = Duration.ofSeconds(numero);
		long anos = duracao.toDays() / 365;
		long meses = (duracao.toDays() % 365) / 30;
		long dias = (duracao.toDays() % 365) % 30;
		long horas = duracao.toHours() % 24;
		long minutos = duracao.toMinutes() % 60;
		long segundos = duracao.getSeconds() % 60;
		StringBuilder dataFormatadaBuilder = new StringBuilder();
		if (anos > 0) {
			dataFormatadaBuilder.append(anos).append("a ");
		}
		if (meses > 0) {
			dataFormatadaBuilder.append(meses).append("mth ");
		}
		if (dias > 0) {
			dataFormatadaBuilder.append(dias).append("d ");
		}
		if (horas > 0) {
			dataFormatadaBuilder.append(horas).append("h ");
		}
		if (minutos > 0) {
			dataFormatadaBuilder.append(minutos).append("m ");
		}
		if (segundos > 0 || dataFormatadaBuilder.length() == 0) {
			dataFormatadaBuilder.append(segundos).append("s");
		} else {
			dataFormatadaBuilder.deleteCharAt(dataFormatadaBuilder.length() - 1);
		}
		return dataFormatadaBuilder.toString();
	}

	public static String getData() {
		return new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(Calendar.getInstance().getTime());
	}

}
