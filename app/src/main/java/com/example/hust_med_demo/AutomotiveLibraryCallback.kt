package com.example.hust_med_demo

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

    private val pkg = "com.example.hust_med_demo"

    // Khởi tạo đường dẫn URI tới file nhạc trong res/raw
    private val uri1 = RawResourceDataSource.buildRawResourceUri(R.raw.kiep_ve_sau)
    private val uri2 = RawResourceDataSource.buildRawResourceUri(R.raw.hoa_hong)
    private val uri3 = RawResourceDataSource.buildRawResourceUri(R.raw.con_mua_tinh_yeu)
    private val uri4 = RawResourceDataSource.buildRawResourceUri(R.raw.mot_minh_mot_som_ban_mai)
    private val uri5 = RawResourceDataSource.buildRawResourceUri(R.raw.thang_tu_la_loi_noi_doi_cua_em)

    // Khởi tạo đường dẫn URI tới ảnh bìa trong res/drawable
    private val art1 = Uri.parse("android.resource://$pkg/drawable/art_kiep_ve_sau")
    private val art2 = Uri.parse("android.resource://$pkg/drawable/art_hoa_hong")
    private val art3 = Uri.parse("android.resource://$pkg/drawable/art_con_mua_tinh_yeu")
    private val art4 = Uri.parse("android.resource://$pkg/drawable/art_mot_minh_mot_som_ban_mai")
    private val art5 = Uri.parse("android.resource://$pkg/drawable/art_thang_tu_la_loi_noi_doi_cua_em")

    // Map mediaId -> URI file nhạc thật
    private val tracks: Map<String, Uri> = mapOf(
        "track_1" to uri1,
        "track_2" to uri2,
        "track_3" to uri3,
        "track_4" to uri4,
        "track_5" to uri5
    )

    // Map mediaId -> URI ảnh bìa
    private val artworks: Map<String, Uri> = mapOf(
        "track_1" to art1,
        "track_2" to art2,
        "track_3" to art3,
        "track_4" to art4,
        "track_5" to art5
    )

    // Danh sách hiển thị lên màn hình
    private val items = listOf(
        createMediaItem("track_1", "Kiếp Ve Sầu", "Đan Trường", uri1, art1),
        createMediaItem("track_2", "Hoa Hồng", "Hà Anh Tuấn", uri2, art2),
        createMediaItem("track_3", "Cơn Mưa Tình Yêu", "Hà Anh Tuấn", uri3, art3),
        createMediaItem("track_4", "Một mình một sớm ban mai", "Hà Anh Tuấn", uri4, art4),
        createMediaItem("track_5", "Tháng tư là lời nói dối của em", "Hà Anh Tuấn", uri5, art5)
    )

    // Hàm helper để tạo MediaItem
    private fun createMediaItem(id: String, title: String, artist: String, mediaUri: Uri, artworkUri: Uri): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(mediaUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setArtworkUri(artworkUri) // Thiết lập ảnh bìa
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )
            .build()
    }

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val root = MediaItem.Builder()
            .setMediaId("root")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("My Media")
                    .setIsBrowsable(true)
                    .setIsPlayable(false)
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

    override fun onAddMediaItems(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val resolved = mediaItems.map { item ->
            val uri = tracks[item.mediaId]
            val art = artworks[item.mediaId]
            if (uri != null) {
                item.buildUpon()
                    .setUri(uri)
                    .setMediaMetadata(
                        item.mediaMetadata.buildUpon()
                            .setArtworkUri(art) // Gắn ảnh bìa khi phát
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

    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val item = items.firstOrNull { it.mediaId == mediaId }
        return Futures.immediateFuture(
            if (item != null) {
                LibraryResult.ofItem(item, null)
            } else {
                LibraryResult.ofError(SessionError(SessionError.ERROR_BAD_VALUE, "Unknown mediaId: $mediaId"))
            }
        )
    }

    override fun onSubscribe(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        return Futures.immediateFuture(LibraryResult.ofVoid(null))
    }
}
