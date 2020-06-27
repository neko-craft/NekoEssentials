package cn.apisium.nekoessentials.utils;

import org.bukkit.entity.Player;
import org.iq80.leveldb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class DatabaseSingleton implements DB {
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
        return get(key.getBytes());
    }

    public void set(String key, byte[] value) {
        put(key.getBytes(), value);
    }

    public void set(String key, byte[] value, WriteOptions writeOptions) {
        put(key.getBytes(), value, writeOptions);
    }

    public void delete(String key) {
        delete(key.getBytes());
    }

    public void delete(String key, WriteOptions writeOptions) {
        delete(key.getBytes(), writeOptions);
    }

    @Override
    public byte[] get(byte[] key) throws DBException {
        return _db.get(key);
    }

    @Override
    public byte[] get(byte[] key, ReadOptions options) throws DBException {
        return _db.get(key, options);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public DBIterator iterator() {
        return _db.iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<byte[], byte[]>> action) {
        _db.forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<byte[], byte[]>> spliterator() {
        return _db.spliterator();
    }

    @Override
    public DBIterator iterator(ReadOptions options) {
        return _db.iterator(options);
    }

    @Override
    public void put(byte[] key, byte[] value) throws DBException {
        _db.put(key, value);
    }

    @Override
    public void delete(byte[] key) throws DBException {
        _db.delete(key);
    }

    @Override
    public void write(WriteBatch updates) throws DBException {
        _db.write(updates);
    }

    @Override
    public WriteBatch createWriteBatch() {
        return _db.createWriteBatch();
    }

    @Override
    public Snapshot put(byte[] key, byte[] value, WriteOptions options) throws DBException {
        return _db.put(key, value, options);
    }

    @Override
    public Snapshot delete(byte[] key, WriteOptions options) throws DBException {
        return _db.delete(key, options);
    }

    @Override
    public Snapshot write(WriteBatch updates, WriteOptions options) throws DBException {
        return _db.write(updates, options);
    }

    @Override
    public Snapshot getSnapshot() {
        return _db.getSnapshot();
    }

    @Override
    public long[] getApproximateSizes(Range... ranges) {
        return _db.getApproximateSizes(ranges);
    }

    @Override
    public String getProperty(String name) {
        return _db.getProperty(name);
    }

    @Override
    public void suspendCompactions() throws InterruptedException {
        _db.suspendCompactions();
    }

    @Override
    public void resumeCompactions() {
        _db.resumeCompactions();
    }

    @Override
    public void compactRange(byte[] begin, byte[] end) throws DBException {
        _db.compactRange(begin, end);
    }

    @Override
    public void close() throws IOException {
        _db.close();
    }
}
