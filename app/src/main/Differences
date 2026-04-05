package com.example.hust_med_demo
// đã chạy được

import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

@OptIn(UnstableApi::class)
class AutomotiveLibraryCallback : MediaLibraryService.MediaLibrarySession.Callback {

    // Tạo đường dẫn tới fil nhạc trong res
    private val uri1 = RawResourceDataSource.buildRawResourceUri(R.raw.kiep_ve_sau)
    private val uri2 = RawResourceDataSource.buildRawResourceUri(R.raw.hoa_hong)
    private val uri3 = RawResourceDataSource.buildRawResourceUri(R.raw.con_mua_tinh_yeu)

//    mediaId  →  file thật (URI)
    private val tracks: Map<String, Uri> = mapOf(
        "track_1" to uri1,
        "track_2" to uri2,
        "track_3" to uri3
    )
//    Hiển thị items lên màn
    private val items = listOf(
        MediaItem.Builder()
            .setMediaId("track_1") //UI chỉ thấy MediaID, không thấy Uri thật => map mediaID->uri để UI nhận diện và hiển thị trước
            .setUri(uri1)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Kiếp Ve Sầu")
                    .setArtist("Đan Trường")
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )

            .build(),
        MediaItem.Builder()
            .setMediaId("track_2")
            .setUri(uri2)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Hoa Hồng")
                    .setArtist("Hà Anh Tuấn")
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )

            .build(),
        MediaItem.Builder()
            .setMediaId("track_3")
            .setUri(uri3)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Cơn Mưa Tình Yêu")
                    .setArtist("Hà Anh Tuấn")
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )
            .build()
    )

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val root = MediaItem.Builder()
            .setMediaId("root") //id cho gốc
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("My Media")
                    .setIsBrowsable(true) // Đây là 1 folder, có thể truy cập xem nd
                    .setIsPlayable(false) // 1 folder nên không phát trực tiếp được
                    .build()
            )
            .build()
        return Futures.immediateFuture(LibraryResult.ofItem(root, params))
    }

    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        val children = when (parentId) {
            "root" -> listOf(
                MediaItem.Builder()
                    .setMediaId("songs")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Songs")
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .build()
                    ).build()
            )
            "songs" -> items
            else -> emptyList()
        }
        return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(children), params))
    }

    // Quan trọng nhất
    override fun onAddMediaItems(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val resolved = mediaItems.map { item ->
            val uri = tracks[item.mediaId] // Map mediaID -> uri - Item hiển thị -> item để phát
            if (uri != null) {
                item.buildUpon()
                    .setUri(uri)
                    .setMediaMetadata(
                        item.mediaMetadata.buildUpon()
                            .setIsPlayable(true)
                            .setIsBrowsable(false)
                            .build()
                    )
                    .build()
            } else {
                item
            }
        }.toMutableList()
        return Futures.immediateFuture(resolved)
    }

    //thông tin songs
    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val item = items.firstOrNull { it.mediaId == mediaId } // MediaID -> Uri
        return Futures.immediateFuture(
            if (item != null) {
                LibraryResult.ofItem(item, null)
            } else {
                LibraryResult.ofError(SessionError(SessionError.ERROR_BAD_VALUE, "Unknown mediaId: $mediaId"))
            }
        )
    }

    //đki theo dõi dlieu
    override fun onSubscribe(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        return Futures.immediateFuture(LibraryResult.ofVoid(null))
    }
}
