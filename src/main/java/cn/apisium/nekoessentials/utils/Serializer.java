package cn.apisium.nekoessentials.utils;

import io.netty.buffer.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
//import org.bukkit.util.io.BukkitObjectOutputStream;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Serializer {
    private Serializer() {}
    public static ByteBuf createByteBuf() { return ByteBufAllocator.DEFAULT.heapBuffer(); }
    public static byte[] byteBufToByteArray(ByteBuf buf) {
        if (buf.hasArray()) return buf.array();
        final byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        return bytes;
    }
//    public static byte[] serializeObject(Serializer obj) throws IOException {
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
//             BukkitObjectOutputStream oo = new BukkitObjectOutputStream(os)) {
//            oo.writeObject(obj);
//            return os.toByteArray();
//        }
//    }
    public static void writeString(ByteBuf buf, CharSequence str) {
        buf.writeInt(ByteBufUtil.utf8Bytes(str));
        ByteBufUtil.writeUtf8(buf, str);
    }
    public static String readString(ByteBuf buf) {
        return buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
    }
    public static byte[] serializeLocation(Location loc) {
        final ByteBuf buf = createByteBuf();
        writeString(buf, loc.getWorld().getName());
        buf.writeDouble(loc.getX());
        buf.writeDouble(loc.getY());
        buf.writeDouble(loc.getZ());
        buf.writeFloat(loc.getYaw());
        buf.writeFloat(loc.getPitch());
        return byteBufToByteArray(buf);
    }
    public static Location deserializeLocation(byte[] bytes) {
        final ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        return new Location(
                Bukkit.getWorld(readString(buf)),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readFloat(),
                buf.readFloat()
        );
    }
}
