package com.example.finelytics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class],
    version = 1
)
abstract class RoomStockDatabase : RoomDatabase(){
    abstract val dao: StockDao
}


//This is the central class for Room. Entities define schema,
// Dao Interface define methods for insert update and delete
//And this class holds all together.

/*
What is version for?
Tracks database schema changes (tables/columns).
Must increment when modifying @Entity classes.
Without migration, Room deletes old data and recreates DB.

Sample Code
Define DB with version:

kotlin
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase()
After adding a column (e.g., email), update version + migration:

kotlin
@Database(version = 2)  // Incremented!

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE User ADD COLUMN email TEXT")
    }
}

Room.databaseBuilder(...)
    .addMigrations(MIGRATION_1_2)  // Apply migration!
    .build()
Key Rules
✅ Always increment version when changing @Entity.

✅ Write migrations to preserve data.

❌ Avoid fallbackToDestructiveMigration() in production.

🚀 Tip: Test migrations in androidTest!

*/