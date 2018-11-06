package net.snaglobal.trile.wizeye.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity

/**
 * @author lmtri
 * @since Oct 19, 2018 at 1:51 PM
 */
@Dao
interface LoginCredentialDao {
    @Query(value = "SELECT * FROM LoginCredentialEntity")
    fun getAllLoginCredential(): List<LoginCredentialEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginCredential(loginCredentialEntity: LoginCredentialEntity): Long // Inserted RowId
}