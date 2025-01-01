package io.github.md5sha256.addictiveexperience.implementation.plant;

import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import com.google.inject.Inject;
import com.google.inject.assistedinject.AssistedInject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugPlantData;
import io.github.md5sha256.addictiveexperience.api.drugs.DrugRegistry;
import jakarta.inject.Named;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SQLResolver implements PlantDataResolver {

    private static final String CREATE_DRUG_PLANT = """
            CREATE TABLE IF NOT EXISTS DRUG_PLANT(
                plant_x INT NOT NULL,
                plant_y INT NOT NULL,
                plant_z INT NOT NULL,
                plant_world BINARY(16) NOT NULL,
                plant_data BLOB NOT NULL
            );
            """;

    private static final String INSERT_DRUG_PLANT = """
            INSERT INTO DRUG_PLANT VALUES(?, ?, ?, ?, ?);
            """;

    private static final String SELECT_DRUG_PLANT_CHUNK = """
            SELECT
                plant_data
            FROM
                DRUG_PLANT
            WHERE
                plant_x >> 4 = ? AND plant_z >> 4 = ? AND plant_world = ?
            """;

    private static final String DELETE_DRUG_PLANT_CHUNK = """
            DELETE FROM DRUG_PLANT WHERE plant_x >> 4 = ? AND plant_z >> 4 = ? AND plant_world = ?
            """;

    private final ConfigurateResolver resolver;
    private final ExecutorService executorService;
    private final File file;
    private final DataSource dataSource;

    @Inject
    SQLResolver(@NotNull Plugin plugin,
                @NotNull DrugRegistry drugRegistry,
                @NotNull @Named("database") ExecutorService databaseExecutor) {
        this.resolver = new ConfigurateResolver(plugin, drugRegistry);
        this.executorService = databaseExecutor;
        this.file = new File(plugin.getDataFolder(), "plants.db");
        initFile();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite://" + file.getAbsolutePath());
        this.dataSource = new HikariDataSource(config);
        initDb();
    }

    private void initFile() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    private void initDb() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_DRUG_PLANT)) {
            statement.execute();
        } catch (SQLException ex) {
            throw new IllegalStateException();
        }
    }

    @NotNull
    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private static byte[] uuidBytes(@NotNull UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        byteBuffer.putLong(uuid.getMostSignificantBits());
        return bytes;
    }

    @Override
    public @NotNull Map<Long, @NotNull DrugPlantData> loadData(@NotNull ChunkPosition chunk) {
        Map<Long, DrugPlantData> datas = new HashMap<>();
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                SELECT_DRUG_PLANT_CHUNK)) {
            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
            statement.setBytes(3, uuidBytes(chunk.getWorld().getUID()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    byte[] plantData = resultSet.getBytes(1);
                    DrugPlantData data = this.resolver.fromBytes(plantData);
                    datas.put(data.position().getPosition(), data);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyMap();
        }
        return datas;
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Map<Long, @NotNull DrugPlantData>> loadDataAsync(@NotNull ChunkPosition chunk) {
        CompletableFuture<@NotNull Map<Long, @NotNull DrugPlantData>> future = new CompletableFuture<>();
        this.executorService.submit(() -> {
            future.complete(loadData(chunk));
        });
        return future;
    }

    @Override
    public void saveData(@NotNull ChunkPosition chunk, @NotNull Collection<DrugPlantData> data) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                INSERT_DRUG_PLANT)) {
            for (DrugPlantData plantData : data) {
                BlockPosition position = plantData.position();
                statement.setInt(1, position.getX());
                statement.setInt(2, position.getY());
                statement.setInt(3, position.getZ());
                statement.setBytes(4, uuidBytes(position.getWorld().getUID()));
                statement.setBytes(5, this.resolver.toBytes(plantData));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> saveDataAsync(@NotNull ChunkPosition chunk,
                                                          @NotNull Collection<DrugPlantData> data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.executorService.submit(() -> {
            saveData(chunk, data);
            future.complete(null);
        });
        return future;
    }

    @Override
    public void clearData(@NotNull ChunkPosition chunk) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                DELETE_DRUG_PLANT_CHUNK)) {
            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> clearDataAsync(@NotNull ChunkPosition chunk) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.executorService.submit(() -> {
            clearData(chunk);
            future.complete(null);
        });
        return future;
    }
}
