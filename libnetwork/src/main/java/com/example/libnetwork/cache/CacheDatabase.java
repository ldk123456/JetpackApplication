package com.example.libnetwork.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.libcommon.AppGlobals;

@Database(entities = {Cache.class}, version = 1, exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        //创建一个内存数据库, 数据只存在于内存中，杀进程之后，数据随之丢失
        //Room.inMemoryDatabaseBuilder()
        database = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "jetpack_databas")
                //数据库创建和打开后的回调
                //.addCallback()
                //数据库升级异常之后的回滚
                //.fallbackToDestructiveMigration()
                //数据库升级异常后根据指定版本进行回滚
                //.fallbackToDestructiveMigrationFrom()
                //.openHelperFactory()
                //room的日志模式
                //.setJournalMode()
                //.addMigrations()
                //设置查询的线程池
                //.setQueryExecutor()
                //是否允许在主线程进行查询
                .allowMainThreadQueries().build();
    }

    public static CacheDatabase get() {
        return database;
    }

    public abstract CacheDao getCache();
}
