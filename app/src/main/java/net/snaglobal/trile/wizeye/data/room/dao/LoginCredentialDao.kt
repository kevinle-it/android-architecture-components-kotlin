package net.snaglobal.trile.wizeye.data.room.dao

import android.arch.persistence.room.*
import net.snaglobal.trile.wizeye.data.room.entity.LoginCredentialEntity

/**
 * @author lmtri
 * @since Oct 19, 2018 at 1:51 PM
 */
@Dao
interface LoginCredentialDao {
    @Query(value = "SELECT * FROM LoginCredentialEntity")
    fun getAllLoginCredential(): List<LoginCredentialEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLoginCredential(loginCredentialEntity: LoginCredentialEntity): Long // Inserted RowId

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateLoginCredential(loginCredentialEntity: LoginCredentialEntity): Int // NumOfRowsUpdated
}