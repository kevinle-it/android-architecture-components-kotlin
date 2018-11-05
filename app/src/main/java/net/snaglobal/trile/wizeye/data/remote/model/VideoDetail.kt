package net.snaglobal.trile.wizeye.data.remote.model

/**
 *
 * @author lmtri
 * @since Nov 02, 2018 at 4:24 PM
 */
data class VideoDetail(
        val name: String,
        val description: String,
        val rtspId: String,
        val rtspPassword: String,
        val rtspUrl: String
)