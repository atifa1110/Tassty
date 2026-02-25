package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.source.local.database.entity.MenuEntity

@Dao
interface MenuDao : BaseDao<MenuEntity>{

}
