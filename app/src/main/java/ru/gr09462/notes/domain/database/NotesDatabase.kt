package ru.gr09462.notes.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Note::class, Category::class],
    version = 2,
    exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        private fun seedCategories(db: SupportSQLiteDatabase){
            db.execSQL(
                "INSERT INTO categories (id, name) VALUES " +
                        "(1, 'Без категории'), (2, 'Учёба'), (3, 'Работа'), (4, 'Личное')"
            )
        }

        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Таблица создаётся ровно таким SQL, какой сгенерировал бы Room
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `categories` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT NOT NULL)"
                )
                // 2. Данные справочника: id=1 обязательно, на него сошлются старые заметки
                seedCategories(db)
                // 3. Новая колонка в старой таблице + внешний ключ
                db.execSQL(
                    "ALTER TABLE `notes` ADD COLUMN `categoryId` INTEGER NOT NULL DEFAULT 1 " +
                            "REFERENCES `categories`(`id`) ON DELETE RESTRICT"
                )
                // 4. Индекс с именем, которое ожидает Room: index_<таблица>_<колонка>
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_notes_categoryId` ON `notes` (`categoryId`)"
                )
            }
        }

        fun getInstance(context: Context): NotesDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : RoomDatabase.Callback() {
                        // ВНИМАНИЕ: при чистой установке миграции НЕ выполняются,
                        // таблицы создаются сразу по entity — справочник заполняем здесь
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            seedCategories(db)
                        }
                    })
                    .build().also { instance = it }
            }
        }
    }
}