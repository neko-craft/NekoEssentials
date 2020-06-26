package cn.apisium.nekoessentials.utils;

import org.bukkit.entity.Player;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.WriteOptions;

@SuppressWarnings("unused")
public class DatabaseSingleton {
    private static final DatabaseSingleton INSTANCE = new DatabaseSingleton();
    private DB _db;

    private DatabaseSingleton() {
    }

    public void init(DB _db) {
        this._db = _db;
    }

    public static DatabaseSingleton getInstance() {
        return INSTANCE;
    }

    public byte[] getPlayerData(Player player, String key) {
        return get(playerDataToKey(player, key));
    }

    public void setPlayerData(Player player, String key, byte[] data) {
        set(playerDataToKey(player, key), data);
    }

    public void setPlayerData(Player player, String key, byte[] data, WriteOptions writeOptions) {
        set(playerDataToKey(player, key), data, writeOptions);
    }

    public void deletePlayerData(Player player, String key) {
        delete(playerDataToKey(player, key));
    }

    public void deletePlayerData(Player player, String key, WriteOptions writeOptions) {
        delete(playerDataToKey(player, key), writeOptions);
    }

    public String playerDataToKey(Player player, String key) {
        return (player.getUniqueId().toString() + "." + key);
    }

    public byte[] get(String key) {
        return _db.get(key.getBytes());
    }

    public void set(String key, byte[] value) {
        _db.put(key.getBytes(), value);
    }

    public void set(String key, byte[] value, WriteOptions writeOptions) {
        _db.put(key.getBytes(), value, writeOptions);
    }

    public void delete(String key) {
        _db.delete(key.getBytes());
    }

    public void delete(String key, WriteOptions writeOptions) {
        _db.delete(key.getBytes(), writeOptions);
    }
}
