package com.example.kashtakala.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kashtakala.data.model.FavouriteDesign
import com.example.kashtakala.data.model.PortfolioItem
import com.example.kashtakala.data.model.SavedQuote

@Database(
    entities = [SavedQuote::class, PortfolioItem::class, FavouriteDesign::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kashta_kala_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}