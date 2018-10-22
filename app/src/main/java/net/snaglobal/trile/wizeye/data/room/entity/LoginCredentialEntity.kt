package net.snaglobal.trile.wizeye.data.room.entity

import android.arch.persistence.room.Entity
import java.util.*

/**
 * @author lmtri
 * @since Oct 21, 2018 at 9:38 PM
 */
@Entity(primaryKeys = ["domain", "userId"])
data class LoginCredentialEntity(
        val domain: String,
        val userId: String,
        val password: String,
        val token: String,
        val lastLoggedIn: Date,
        val isLoggedIn: Boolean
)